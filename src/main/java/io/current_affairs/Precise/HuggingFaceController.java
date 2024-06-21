package io.current_affairs.Precise;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
public class HuggingFaceController {
	
	
	
	@Autowired
	private final HuggingFaceService huggingFaceService;

    
    public HuggingFaceController(HuggingFaceService huggingFaceService) {
        this.huggingFaceService = huggingFaceService;
    }

    @PostMapping("/summarize")
    public String summarize(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        return huggingFaceService.summarizeText(text);

}
    
    @PostMapping("/summarize-url")
    public String summarizeUrl(@RequestBody Map<String, String> request) throws IOException {
        String url = request.get("url");
        return huggingFaceService.summarizeUrl(url);
             
        
}
    
    @PostMapping("/summarize-pdf")
    public String summarizePdf(@RequestParam("file") MultipartFile file) throws IOException {
        return huggingFaceService.summarizePdf(file);
    }
    
    

    
}
    
