package com.cachingdemo.simplepersonproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cachingdemo.simplepersonproject.entity.Person;
import com.cachingdemo.simplepersonproject.repository.PersonRepository;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Cacheable(value = "persons", key = "#id")
    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    @CachePut(value = "persons", key = "#person.id")
    public Person updatePerson(Person person) {
        return personRepository.save(person);
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
