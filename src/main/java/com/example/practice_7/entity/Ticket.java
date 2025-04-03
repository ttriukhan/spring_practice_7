package com.example.practice_7.entity;

import com.example.practice_7.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "event", "guest" })
@RedisHash("ticket")
public class Ticket {
    @Id
    private String id;

    @Reference
    private Event event;

    @Reference
    private Guest guest;

    private TicketType type;
}
