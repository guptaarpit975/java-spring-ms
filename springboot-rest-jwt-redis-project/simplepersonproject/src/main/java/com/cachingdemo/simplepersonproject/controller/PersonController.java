package com.cachingdemo.simplepersonproject.controller;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cachingdemo.simplepersonproject.entity.Person;
import com.cachingdemo.simplepersonproject.service.PersonService;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService service;

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable Long id) {
    	long startTime = System.nanoTime();
    	
        // Code of the method to be measured
    	Person prsn = service.getPersonById(id);
    	
    	long endTime = System.nanoTime();
    	long duration = (endTime - startTime); // Time in nanoseconds
        double durationInMilliseconds = (double) duration / 1_000_000.0;
        System.out.println("Time to fetch Person : " + durationInMilliseconds + " ms");
        
        return ResponseEntity.ok(prsn);
    }

    @PostMapping
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return ResponseEntity.ok(service.createPerson(person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    /*
    @GetMapping
    public ResponseEntity<List<Person>> all() {
        return ResponseEntity.ok(service.getAll());
    }*/
}
