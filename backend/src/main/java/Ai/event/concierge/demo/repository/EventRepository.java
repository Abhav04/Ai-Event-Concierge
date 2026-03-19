package Ai.event.concierge.demo.repository;

import Ai.event.concierge.demo.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
}