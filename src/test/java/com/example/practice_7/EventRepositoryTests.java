package com.example.practice_7;

import com.example.practice_7.entity.Event;
import com.example.practice_7.repository.EventRepository;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@DataRedisTest
public class EventRepositoryTests {

    @Container
    @ServiceConnection
    static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7"));


    @Autowired
    private EventRepository eventRepository;

    private List<Event> events;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }

    @Test
    void testSave() {
        String id = "newEv";
        Event event = new Event(id, "New Event", "Organiser",
                LocalDate.of(2025, 4, 4), LocalTime.of(12, 0),
                LocalTime.of(13, 0), "Address");
        eventRepository.save(event);
        Event foundEvent = eventRepository.findById(id).orElse(null);
        assertNotNull(foundEvent);
        assertEquals(foundEvent, event);
    }

    @Test
    void testUpdate() {
        String id = "newEv";
        Event event = new Event(id, "New Event", "Organiser",
                LocalDate.of(2025, 4, 4), LocalTime.of(12, 0),
                LocalTime.of(13, 0), "Address");
        eventRepository.save(event);
        Event foundEvent = eventRepository.findById(id).orElse(null);
        assertNotNull(foundEvent);
        assertEquals(foundEvent, event);
        String newName = "Updated name";
        event.setName(newName);
        eventRepository.save(event);
        foundEvent = eventRepository.findById(id).orElse(null);
        assertNotNull(foundEvent);
        assertEquals(foundEvent.getName(), newName);
    }

    @Test
    void testDeleteById() {
        String id = "newEv";
        Event event = new Event(id, "New Event", "Organiser",
                LocalDate.of(2025, 4, 4), LocalTime.of(12, 0),
                LocalTime.of(13, 0), "Address");
        eventRepository.save(event);
        Event foundEvent = eventRepository.findById(id).orElse(null);
        assertNotNull(foundEvent);
        assertEquals(foundEvent, event);
        eventRepository.deleteById(id);
        foundEvent = eventRepository.findById(id).orElse(null);
        assertNull(foundEvent);
    }

    @Test
    void testFindByName() {
        createTestData();
        String name = "Spring Boot Workshop";
        List<Event> foundEvents = eventRepository.findByName(name);
        List<Event> filteredEvents = events.stream().filter(event -> event.getName().equals(name)).toList();
        assertEquals(foundEvents, filteredEvents);
    }

    @Test
    void testFindByDate() {
        createTestData();
        LocalDate date = LocalDate.of(2025, 4, 15);
        List<Event> foundEvents = eventRepository.findByDate(date);
        List<Event> filteredEvents = events.stream().filter(event -> event.getDate().equals(date)).toList();
        assertEquals(foundEvents, filteredEvents);
    }

    @Test
    void testFindByOrganiser() {
        createTestData();
        String organiser = "SBDB";
        List<Event> foundEvents = eventRepository.findByOrganiserName(organiser);
        List<Event> filteredEvents = events.stream().filter(event -> event.getOrganiserName().equals(organiser)).toList();
        assertEquals(foundEvents, filteredEvents);
    }



    private void createTestData() {
        Event event1 = new Event("ev1", "Spring Boot Workshop", "KMA",
                LocalDate.of(2025, 4, 10), LocalTime.of(10, 0),
                LocalTime.of(12, 0), "New York");

        Event event2 = new Event("ev2", "Redis Deep Dive", "SBDB",
                LocalDate.of(2025, 4, 15), LocalTime.of(14, 0),
                LocalTime.of(16, 0), "San Francisco");

        Event event3 = new Event("ev3", "Spring Boot Workshop", "Spring",
                LocalDate.of(2025, 5, 10), LocalTime.of(10, 0),
                LocalTime.of(12, 0), "New York");

        Event event4 = new Event("ev4", "MongoDB", "SBDB",
                LocalDate.of(2025, 4, 15), LocalTime.of(10, 0),
                LocalTime.of(12, 0), "San Francisco");

        events = List.of(event1, event2, event3, event4);
        eventRepository.saveAll(events);
    }

}
