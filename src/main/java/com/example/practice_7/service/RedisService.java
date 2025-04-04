package com.example.practice_7.service;

import com.example.practice_7.entity.Event;
import com.example.practice_7.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveTicketWithExpiration(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null.");
        }
        if (ticket.getEvent() == null) {
            throw new IllegalArgumentException("Event associated with the ticket cannot be null.");
        }
        LocalDateTime eventEndTime = LocalDateTime.of(ticket.getEvent().getDate(), ticket.getEvent().getEndTime());
        long expirationTimeInSeconds = Duration.between(LocalDateTime.now(), eventEndTime).getSeconds() + 10;
        if (expirationTimeInSeconds > 0) {
            redisTemplate.opsForValue().set("ticket:" + ticket.getId(), ticket, expirationTimeInSeconds, TimeUnit.SECONDS);
        } else {
            throw new IllegalStateException("The event has already ended, so the ticket cannot be saved.");
        }
    }

    public void addEventToList(Event event, String listName) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }
        redisTemplate.opsForList().rightPush(listName, event);
    }

    public void removeEventsList(String listName) {
        if (!redisTemplate.hasKey(listName)) {
            throw new IllegalArgumentException("List with provided name does not exist.");
        }
        redisTemplate.delete(listName);
    }

}
