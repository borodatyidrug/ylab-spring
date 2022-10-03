package com.edu.ulab.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edu.ulab.app.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	
}
