package com.edu.ulab.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edu.ulab.app.entity.Person;

public interface UserRepository extends JpaRepository<Person, Long> {

}
