package com.cachingdemo.simplepersonproject.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cachingdemo.simplepersonproject.entity.Person;
import com.cachingdemo.simplepersonproject.repository.PersonRepository;

import java.util.concurrent.CompletableFuture;

@Service
public class PersonAsyncService {

	private static Logger logger = LoggerFactory.getLogger(PersonAsyncService.class);
	
    @Autowired
    private PersonRepository personRepository;

    @Async
    public CompletableFuture<Person> updatePersonNameAsync(Long id, String newName) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));

        person.setName(newName);
        Person saved = personRepository.save(person);

        return CompletableFuture.completedFuture(saved);
    }

    @Async
    public void sendUpdateNotificationAsync(Long personId) {
        
    	logger.info("Successfully updated person with id=" + personId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("📢 Notification sent for person update: " + personId);
    }
}