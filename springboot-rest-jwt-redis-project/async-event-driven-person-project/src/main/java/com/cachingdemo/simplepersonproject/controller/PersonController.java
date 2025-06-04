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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cachingdemo.simplepersonproject.entity.Person;
import com.cachingdemo.simplepersonproject.service.PersonAsyncService;
import com.cachingdemo.simplepersonproject.service.PersonService;
import com.cachingdemo.simplepersonproject.service.bulkoperations.MultiThreadedPersonService;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService service;

    @Autowired
    private MultiThreadedPersonService multiThreadService;
    
    @Autowired
    private PersonAsyncService personAsyncService;
    
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
    
    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        return ResponseEntity.ok(service.updatePersonWithDistributedLock(person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
    
    // This method returns very fast,releasing the thread very soon to its Threadpool
    // Reason - it uses a method which has @Async and thus the processing is handed over to a separate thread.
    @PostMapping("/async-update")
    public ResponseEntity<String> asyncUpdate(@RequestParam Long id, @RequestParam String name) {
        // Kick off the async update
        personAsyncService.updatePersonNameAsync(id, name)
            .thenAccept(person -> {
                // Fire-and-forget notification
                personAsyncService.sendUpdateNotificationAsync(person.getId());
            });

        return ResponseEntity.ok("Update started asynchronously.");
    }
    
    @PostMapping("/bulk-update")
    public ResponseEntity<String> bulkUpdate(@RequestBody List<Long> ids,
                                             @RequestParam String suffix) {
        multiThreadService.updatePersonsInParallel(ids, suffix);
        return ResponseEntity.ok("Bulk update triggered in parallel.");
    }
    
    
}
