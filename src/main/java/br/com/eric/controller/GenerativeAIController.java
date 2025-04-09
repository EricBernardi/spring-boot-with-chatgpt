package br.com.eric.controller;

import br.com.eric.service.ChatService;
import br.com.eric.service.ImageService;
import br.com.eric.service.RecipeService;
import br.com.eric.service.TranscriptionService;
import org.springframework.ai.image.ImageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class GenerativeAIController {

    private final ChatService chatService;
    private final RecipeService recipeService;
    private final ImageService imageService;
    private final TranscriptionService transcriptionService;

    public GenerativeAIController(ChatService chatService, RecipeService recipeService,
                                  ImageService imageService,
                                  TranscriptionService transcriptionService) {
        this.chatService = chatService;
        this.recipeService = recipeService;
        this.imageService = imageService;
        this.transcriptionService = transcriptionService;
    }

    @GetMapping("ask-ai")
    public String getResponse(@RequestParam String prompt){
        return chatService.getResponse(prompt);
    }

    @GetMapping("ask-ai-options")
    public String getResponseWithOptions(@RequestParam String prompt){
        return chatService.getResponseWithOptions(prompt);
    }

    @GetMapping("recipe-creator")
    public String createRecipe(@RequestParam String ingredients,
                               @RequestParam(defaultValue = "any") String cuisine,
                               @RequestParam(defaultValue = "none") String dietaryRestrictions){

        return recipeService.createRecipe(ingredients, cuisine, dietaryRestrictions);

    }

    @GetMapping("image-generator")
    public List<String> createImage(@RequestParam String prompt,
                                    @RequestParam(defaultValue = "hd") String quality,
                                    @RequestParam(defaultValue = "1") Integer n,
                                    @RequestParam(defaultValue = "1024") Integer height,
                                    @RequestParam(defaultValue = "1024") Integer width){

        ImageResponse response = imageService.generateImage(prompt, quality, n, height, width);
        List<String> imageUrls = response.getResults().stream()
                .map(result -> result.getOutput().getUrl())
                .toList();
        return imageUrls;
    }

    @PostMapping("transcribe")
    public ResponseEntity<String> transcribeAudio(@RequestParam("file") MultipartFile file){
        try {
            String transcription = transcriptionService.transcribeAudio(file);
            return ResponseEntity.ok(transcription);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the audio file: " + e.getMessage());
        }
    }
}
