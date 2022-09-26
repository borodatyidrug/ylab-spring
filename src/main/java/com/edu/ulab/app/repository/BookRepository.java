package com.edu.ulab.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.edu.ulab.app.entity.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
	
}
