package com.edu.ulab.app.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "jpa-book-service")
public class BookServiceImplJPA implements BookService {

	private BookRepository bookRepository;
	private BookMapper bookMapper;
	
	@Autowired
	public void setBookRepository(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	@Autowired
	public void setBookMapper(BookMapper bookMapper) {
		this.bookMapper = bookMapper;
	}
	
	@Override
	public BookDto createBook(BookDto bookDto) {
		if (bookDto == null) {
    		throw new IllegalArgumentException("Only non null values are allowed");
    	}
		Book book = bookMapper.bookDtoToBook(bookDto);
		log.info("{} mapped to {}", bookDto, book);
		Book savedBook = bookRepository.saveAndFlush(book);
		log.info("{} saved to repository", savedBook);
		return bookMapper.bookToBookDto(savedBook);
	}

	@Override
	public BookDto updateBook(BookDto bookDto) {
		if (bookDto == null || bookDto.getId() == null || !bookRepository.existsById(bookDto.getId())) {
    		throw new IllegalArgumentException("Can't update book, because source DTO is "
					+ "null or her id is null or target book not exists");
    	}
		Book book = bookMapper.bookDtoToBook(bookDto);
		log.info("{} mapped to {}", bookDto, book);
		Book updatedBook = bookRepository.saveAndFlush(book);
		log.info("{} updated by {} and saved to repository", updatedBook);
		return bookMapper.bookToBookDto(updatedBook);
	}

	@Override
	public BookDto getBookById(Long id) {
		if (id == null) {
    		throw new IllegalArgumentException("Bad book id. It must be non null");
    	}
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Book with id " + id + " not exixts in repository!"));
		return bookMapper.bookToBookDto(book);
	}

	@Override
	public void deleteBookById(Long id) {
		if (id == null) {
    		throw new IllegalArgumentException("Bad book id. It must be non null");
    	}
		bookRepository.deleteById(id);
		log.info("Book with id {} successfully deleted", id);
	}

	@Override
	public boolean exists(Long id) {
		Boolean exists = id == null ? false : bookRepository.existsById(id);
		log.debug("Book with id {} " + (exists ? "exists" : "not exists") + " in repository", id);
		return exists;
	}

}
