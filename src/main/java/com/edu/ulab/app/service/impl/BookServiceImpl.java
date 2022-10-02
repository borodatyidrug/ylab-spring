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
@Service(value = "storage-book-service")
public class BookServiceImpl implements BookService {
	
	private IStorage storage;
	private BookMapper bookMapper;
	
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
    	if (bookDto == null) {
    		throw new IllegalArgumentException("Only non null values are allowed");
    	}
    	Book book = bookMapper.bookDtoToBook(bookDto);
    	log.debug("Created book: {}", book);
    	Long generatedId = storage.save(book);
        bookDto.setId(generatedId);
        log.debug("Created book got generated id from storage. Id is {}", generatedId);
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
    	if (bookDto == null || bookDto.getId() == null || !storage.contains(bookDto.getId())) {
    		throw new IllegalArgumentException("Can't update book, because source DTO is "
					+ "null or his id is null or target book not exists");
    	}
    	Book book = bookMapper.bookDtoToBook(bookDto);
    	Long id = book.getId();
    	storage.update(book);
    	log.debug("Book with id {} successfully updated to {}", id, book);
        return null;
    }

    @Override
    public BookDto getBookById(Long id) {
    	if (id == null) {
    		throw new IllegalArgumentException("Bad book id. It must be non null");
    	}
    	Book book = (Book) storage.get(id);
    	BookDto bookDto = bookMapper.bookToBookDto(book);
    	log.debug("Got book {} with id {} from storage", book, id);
        return bookDto;
    }

    @Override
    public void deleteBookById(Long id) {
    	if (id == null || !storage.delete(id)) {
    		log.debug("Book with id {} not deleted", id);
    	}
    	log.debug("Book with id {} successfully deleted", id);
    }

	@Override
	public boolean exists(Long id) {
		Boolean exists = id == null ? false : storage.contains(id);
		log.debug("Book with id {} " + (exists ? "exists" : "not exists") + " in storage", id);
		return exists;
	}
}
