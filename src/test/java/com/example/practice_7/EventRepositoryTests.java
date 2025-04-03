package com.example.practice_7;

import com.example.practice_7.entity.Event;
import com.example.practice_7.repository.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.RedisOperations;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Testcontainers
@DataRedisTest
public class EventRepositoryTests {

    @Autowired
    private RedisOperations<Object, Object> redisOperations;
    @Autowired
    private EventRepository eventRepository;

    private Event event1 = new Event("1", "Spring Conference", LocalDate.of(2025, 4, 25), LocalTime.of(9, 0), LocalTime.of(17, 0), "New York");
    private Event event2 = new Event("2", "Summer Conference", LocalDate.of(2025, 4, 28), LocalTime.of(9, 0), LocalTime.of(17, 0), "Kyiv, 123 str");
    private Event event3 = new Event("3", "AnimeCon", LocalDate.of(2025, 5, 10), LocalTime.of(9, 0), LocalTime.of(17, 0), "Kyiv, 23b");
    private Event event4 = new Event("4", "GiveFest", LocalDate.of(2025, 6, 20), LocalTime.of(9, 0), LocalTime.of(17, 0), "New York");
    private Event event5 = new Event("5", "Cake Picnic", LocalDate.of(2025, 7, 7), LocalTime.of(9, 0), LocalTime.of(17, 0), "Odesa");

    private List<Event> events = new ArrayList<>();

    @BeforeEach
    @AfterEach
    void setUp() {
//        redisOperations.execute((RedisConnection connection) -> {
//            connection.();
//            return "Ok";
//        });
    }

    private void flushTestEvents() {
        events = Arrays.asList(event1, event2, event3, event4, event5);
        eventRepository.saveAll(events);
    }


}
