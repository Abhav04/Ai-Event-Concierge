package Ai.event.concierge.demo.controller;

import Ai.event.concierge.demo.model.Event;
import Ai.event.concierge.demo.repository.EventRepository;
import Ai.event.concierge.demo.service.GeminiService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EventController {

    private final GeminiService geminiService;
    private final EventRepository repo;

    public EventController(GeminiService geminiService, EventRepository repo) {
        this.geminiService = geminiService;
        this.repo = repo;
    }

    // 🔥 Generate event using Gemini
    @PostMapping("/generate-event")
    public Event generate(@RequestBody String prompt) {

        try {
            JSONObject result = geminiService.generateEvent(prompt);

            Event event = new Event();
            event.setUserPrompt(prompt);
            event.setVenueName(String.valueOf(result.get("venue_name")));
            event.setLocation(String.valueOf(result.get("location")));
            event.setEstimatedCost(String.valueOf(result.get("estimated_cost")));
            event.setWhyItFits(String.valueOf(result.get("why_it_fits")));
            return repo.save(event);

        } catch (Exception e) {
            throw new RuntimeException("Error processing event", e);
        }
    }

    // 📜 Get history
    @GetMapping("/history")
    public List<Event> history() {
        return repo.findAll();
    }
}