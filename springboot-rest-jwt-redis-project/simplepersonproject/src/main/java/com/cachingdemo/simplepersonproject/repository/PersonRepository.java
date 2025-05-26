package com.cachingdemo.simplepersonproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cachingdemo.simplepersonproject.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}

