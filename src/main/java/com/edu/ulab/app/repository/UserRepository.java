package com.edu.ulab.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.edu.ulab.app.entity.Person;

public interface UserRepository extends CrudRepository<Person, Long> {

}
