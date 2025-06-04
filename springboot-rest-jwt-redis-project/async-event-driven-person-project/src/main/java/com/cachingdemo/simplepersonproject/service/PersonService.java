package com.cachingdemo.simplepersonproject.service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cachingdemo.simplepersonproject.entity.Person;
import com.cachingdemo.simplepersonproject.exceptions.ResourceNotFoundException;
import com.cachingdemo.simplepersonproject.repository.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private RedissonClient redissonClient;

    @Cacheable(value = "persons", key = "#id")
    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    /*
    @CachePut(value = "persons", key = "#person.id")
    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }*/

    @CachePut(value = "persons", key = "#person.id")
    @Transactional
    public Person updatePersonWithDistributedLock(Person person) {
    	
    	long personId = person.getId();
    	String lockKey = "lock:person:" + personId;
        RLock lock = redissonClient.getLock(lockKey);
        Person updatedPerson;
        try {
            boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS); // wait max 5s, lease 10s
            if (isLocked) {
                Person p = personRepository.findById(personId)
                    .orElseThrow(() -> new ResourceNotFoundException("Person not found"));

                updatedPerson = personRepository.save(person);
                //Thread.sleep(30000);
                return updatedPerson;
            } else {
                throw new RuntimeException("Could not acquire lock");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Lock acquisition interrupted", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    
    @CacheEvict(value = "persons", key = "#id")
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    @CachePut(value = "persons", key = "#person.id")
    public Person createPerson(Person person) {
        return personRepository.save(person); // Insert new
    }
}
