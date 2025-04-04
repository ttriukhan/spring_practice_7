package com.example.practice_7.repository;

import com.example.practice_7.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {

    List<Event> findByName(String springBootWorkshop);
    List<Event> findByOrganiserName(String organiser);
    List<Event> findByDate(LocalDate date);
}
