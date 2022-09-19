package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.IStorage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
	
	IStorage storage;
	BookMapper bookMapper;
	
	@Autowired
	public void setStorage(IStorage storage) {
		this.storage = storage;
	}
	
	@Autowired
	public void setBookMapper(BookMapper bookMapper) {
		this.bookMapper = bookMapper;
	}
	
    @Override
    public BookDto createBook(BookDto bookDto) {
    	Book book = bookMapper.bookDtoToBook(bookDto);
    	log.debug("Created book: {}", book);
    	Long generatedId = storage.save(book);
        bookDto.setId(generatedId);
        log.debug("Created book got generated id from storage. Id is {}", generatedId);
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
    	Book book = bookMapper.bookDtoToBook(bookDto);
    	Long id = book.getId();
    	storage.update(book);
    	log.debug("Book with id {} successfully updated to {}", id, book);
        return null;
    }

    @Override
    public BookDto getBookById(Long id) {
    	Book book = (Book) storage.get(id);
    	BookDto bookDto = bookMapper.bookToBookDto(book);
    	log.debug("Got book {} with id {} from storage", book, id);
        return bookDto;
    }

    @Override
    public void deleteBookById(Long id) {
    	storage.delete(id);
    	log.debug("Book with id {} successfully deleted", id);
    }

	@Override
	public boolean exists(Long id) {
		Boolean exists = storage.contains(id);
		log.debug("Book with id {} " + (exists ? "exists" : "not exists") + " in storage", id);
		return exists;
	}
}
