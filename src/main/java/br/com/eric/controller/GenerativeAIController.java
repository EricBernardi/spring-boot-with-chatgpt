package br.com.eric.controller;

import br.com.eric.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenerativeAIController {

    private final ChatService chatService;

    public GenerativeAIController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("ask-ai")
    public String getResponse(@RequestParam String prompt){
        return chatService.getResponse(prompt);
    }
}
