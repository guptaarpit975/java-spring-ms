package com.cachingdemo.simplepersonproject.service.bulkoperations;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cachingdemo.simplepersonproject.entity.Person;
import com.cachingdemo.simplepersonproject.exceptions.ResourceNotFoundException;
import com.cachingdemo.simplepersonproject.repository.PersonRepository;

@Service
public class MultiThreadedPersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    @Qualifier("personThreadPool")
    private Executor executor;
    
    public void updatePersonsInParallel(List<Long> personIds, String newSuffix) {
        List<CompletableFuture<Void>> futures = personIds.stream()
            .map(id -> CompletableFuture.runAsync(() -> updatePersonName(id, newSuffix), executor))
            .collect(Collectors.toList());

        // Wait for all tasks to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
    
    private void updatePersonName(Long personId, String newSuffix) {
        try {
            Person person = personRepository.findById(personId)
                    .orElseThrow(() -> new ResourceNotFoundException("Person not found: " + personId));
            
            // Simulated processing (e.g., append suffix to name)
            person.setName(person.getName() + newSuffix);
            personRepository.save(person);

            System.out.println("✅ Updated person: " + personId + " by " + Thread.currentThread().getName());

        } catch (Exception e) {
            System.err.println("❌ Error updating person " + personId + ": " + e.getMessage());
        }
    }
	
}
