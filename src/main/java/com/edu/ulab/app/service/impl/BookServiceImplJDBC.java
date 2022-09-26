package com.edu.ulab.app.service.impl;

import java.sql.PreparedStatement;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "jdbc-book-service")
public class BookServiceImplJDBC implements BookService {

	private final JdbcTemplate jdbcTemplate;
	
	private final BookMapper bookMapper;
	
	public BookServiceImplJDBC(JdbcTemplate jdbcTemplate, BookMapper bookMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.bookMapper = bookMapper;
	}

	@Override
	public BookDto createBook(BookDto bookDto) {
		if (bookDto == null) {
    		throw new IllegalArgumentException("Only non null values are allowed");
    	}
		String QUERY = "insert into book(title, author, page_count, user_id) values(?,?,?,?)";
		log.info("Creating new book {}", bookDto);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(QUERY, new String[] {"id"});
					ps.setString(1, bookDto.getTitle());
					ps.setString(2, bookDto.getAuthor());
					ps.setLong(3, bookDto.getPageCount());
					ps.setLong(4, bookDto.getUserId());
					return ps;
				}, keyHolder);
		bookDto.setId(Objects.requireNonNull(keyHolder.getKey().longValue()));
		log.info("{} stored to database", bookDto);
		return bookDto;
	}

	@Override
	public BookDto updateBook(BookDto bookDto) {
		if (bookDto == null || bookDto.getId() == null) {
    		throw new IllegalArgumentException("Can't update book, because source DTO is null or his id is null");
    	}
		String QUERY = """
				update book
				set
					title = ?,
					author = ?,
					page_count = ?,
					user_id = ?
				where id = ?
				""";
		log.info("Updating book with id {}", bookDto.getId());
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(QUERY);
			ps.setString(1, bookDto.getTitle());
			ps.setString(2, bookDto.getAuthor());
			ps.setLong(3, bookDto.getPageCount());
			ps.setLong(4, bookDto.getUserId());
			ps.setLong(5, bookDto.getId());
			return ps;
		});
		log.info("{} updated", bookDto);
		return bookDto;
	}

	@Override
	public BookDto getBookById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Bad book id. It must be non null");
		}
		String QUERY = "select * from book where id = ?";
		Book foundBook = jdbcTemplate.query(connection -> {
			PreparedStatement ps = connection.prepareStatement(QUERY);
			ps.setLong(1, id);
			return ps;
		}, (rs, rn) -> {
			return Book.builder()
					.author(rs.getString("author"))
					.id(rs.getLong("id"))
					.title(rs.getString("title"))
					.pageCount(rs.getInt("page_count"))
					.userId(rs.getLong("user_id"))
					.build();
		}).stream()
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException("Book with id " + id + " not exixts in database!"));
		return bookMapper.bookToBookDto(foundBook);
	}

	@Override
	public void deleteBookById(Long id) {
		if (id == null) {
    		throw new IllegalArgumentException("Bad book id. It must be non null");
    	}
		String QUERY = "delete from book where id = ?";
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(QUERY);
			ps.setLong(1, id);
			return ps;
		});
		log.info("Book with id {} successfully deteted from database", id);
	}

	@Override
	public boolean exists(Long id) {
		if (id == null) {
    		throw new IllegalArgumentException("Bad book id. It must be non null");
    	}
		try {
			getBookById(id);
			log.info("Book with id {} exists in database", id);
			return true;
		} catch (NoSuchElementException exc) {
			log.info(exc.getMessage());
			return false;
		}
	}

}
