package io.current_affairs.Precise;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HuggingFaceService {
	
	@Value("${huggingface.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate;

    public HuggingFaceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    //For text summarization
    
    private List<String> splitIntoChunks(String text, int maxLength) {
        List<String> chunks = new ArrayList<>();
        int length = text.length();
        for (int i = 0; i < length; i += maxLength) {
            chunks.add(text.substring(i, Math.min(length, i + maxLength)));
        }
        return chunks;
    }

    public String summarizeText(String text) {
        String url = "https://api-inference.huggingface.co/models/facebook/bart-large-cnn";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Content-Type", "application/json");

        Map<String, Object> body = new HashMap<>();
        body.put("inputs", text);
        body.put("parameters", Map.of("max_length", 150));
        

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();

}
   //For summarization based on url 
    public String summarizeUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String text = doc.body().text();
        return summarizeText(text);
    }
    
    
    //Summarize using pdf
    public String summarizePdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
     // Split the text into chunks
        List<String> chunks = splitIntoChunks(text, 1024); // Adjust chunk size as needed

        // Summarize each chunk
        StringBuilder summaryBuilder = new StringBuilder();
        for (String chunk : chunks) {
            summaryBuilder.append(summarizeText(chunk)).append(" ");
        }

        text= summaryBuilder.toString().trim();
        return summarizeText(text);
    }
    
    
    
}
