package com.example.practice_7;


import com.example.practice_7.entity.Event;
import com.example.practice_7.entity.Ticket;
import com.example.practice_7.service.RedisService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class RedisServiceTest {

    @Container
    @ServiceConnection
    static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7"));

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private List<Event> events;

    @Test
    public void testSaveTicketWithExpiration() {
        LocalTime endTime = LocalTime.of(20, 0);
        Event event = new Event();
        event.setId("ev1");
        event.setDate(LocalDate.now());
        event.setEndTime(endTime);

        Ticket ticket = new Ticket();
        ticket.setId("t1");
        ticket.setEvent(event);

        redisService.saveTicketWithExpiration(ticket);

        Ticket savedTicket = (Ticket) redisTemplate.opsForValue().get("ticket:t1");
        assertNotNull(savedTicket);
        LocalTime expTime = LocalTime.now().plusSeconds(redisTemplate.getExpire("ticket:t1"));
        assertFalse(expTime.isAfter(endTime));
        assertFalse(expTime.isBefore(endTime.plusHours(1)));
    }

    @Test
    public void testAddEventToList() {
        String listName = "springEvents";
        Event event = new Event("sEv0", "Spring Event 1", "Organiser",
                LocalDate.of(2025, 4, 4), LocalTime.of(12, 0),
                LocalTime.of(13, 0), "Address");
        boolean existsList = redisTemplate.hasKey(listName);
        assertFalse(existsList);

        redisService.addEventToList(event, listName);
        existsList = redisTemplate.hasKey(listName);
        assertTrue(existsList);
        boolean existsEvent = redisTemplate.hasKey(listName + ":event:sEv0");
        assertTrue(existsEvent);
        Long listSize = redisTemplate.opsForList().size(listName);
        assertEquals(listSize, 1);

        Event retrievedEvent = (Event) redisTemplate.opsForList().index(listName, 0);
        assertNotNull(retrievedEvent);
        assertEquals(event, retrievedEvent);
    }

    @Test
    public void testRemoveEventsList() {
        String listName = "delEvents";
        createEventsList(listName);
        boolean exists = redisTemplate.hasKey(listName);
        assertTrue(exists);

        redisService.removeEventsList(listName);
        exists = redisTemplate.hasKey(listName);
        assertFalse(exists);
    }

    private void createEventsList(String listName) {
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
        events.forEach(event -> redisService.addEventToList(event, listName));
    }
}
