package com.edu.ulab.app.service.impl;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "jdbc-user-service")
public class UserServiceImplJDBC implements UserService {

	private final JdbcTemplate jdbcTemplate;
	
	private final UserMapper userMapper;
	
	public UserServiceImplJDBC(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.userMapper = userMapper;
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		if (userDto == null) {
    		throw new IllegalArgumentException("Only non null values are allowed");
    	}
		String QUERY = "insert into person(full_name, title, age, resume) values(?, ?, ?, ?)";
		log.info("Creating new user {}", userDto);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				connection -> {
					PreparedStatement ps = connection.prepareStatement(QUERY, new String[] {"id"});
					ps.setString(1, userDto.getFullName());
					ps.setString(2, userDto.getTitle());
					ps.setString(4, userDto.getResume());
					ps.setLong(3, userDto.getAge());
					return ps;
				}, keyHolder);
		userDto.setId(Objects.requireNonNull(keyHolder.getKey().longValue()));
		log.info("{} stored to database", userDto);
		return userDto;
	}

	@Override
	public UserDto updateUser(UserDto userDto) {
		if (userDto == null || userDto.getId() == null) {
    		throw new IllegalArgumentException("Can't update user, because source DTO is null or his id is null");
    	}
		String QUERY = """
				update person
				set
					full_name = ?,
					title = ?,
					age = ?,
					books_id = ?,
					resume = ?,
				where id = ?
				""";
		log.info("Updating user with id {}", userDto.getId());
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(QUERY);
			ps.setString(1, userDto.getFullName());
			ps.setString(2, userDto.getTitle());
			ps.setLong(3, userDto.getAge());
			ps.setArray(4, connection.createArrayOf("bigint", userDto.getBooksId().toArray()));
			ps.setLong(6, userDto.getId());
			ps.setString(5, userDto.getResume());
			return ps;
		});
		log.info("{} updated", userDto);
		return userDto;
	}

	@Override
	public UserDto getUserById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Bad user id. It must be non null");
		}
		String QUERY = "select * from person where id = ?";
		Person foundUser = jdbcTemplate.query(connection -> {
			PreparedStatement ps = connection.prepareStatement(QUERY);
			ps.setLong(1, id);
			return ps;
		}, (rs, rn) -> { // RowMapper
			return Person.builder()
				.fullName(rs.getString("full_name"))
				.title(rs.getString("title"))
				.age(rs.getInt("age"))
				.id(id)
				.resume(rs.getString("resume"))
				.booksId(Arrays.asList((Object[]) rs.getArray("books_id").getArray()).stream()
						.map(s -> (Long) s)
						.toList())
				.build();
		}).stream()
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("User with id " + id + " not exists in database!"));
		log.info("Found {} in database by id {}", foundUser);
		return userMapper.userToUserDto(foundUser);
	}

	@Override
	public void deleteUserById(Long id) {
		if (id == null) {
    		throw new IllegalArgumentException("Bad user id. It must be non null");
    	}
		String QUERY = "delete from person where id = ?";
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(QUERY);
			ps.setLong(1, id);
			return ps;
		});
		log.info("User with id {} successfully deteted from database", id);
	}

	@Override
	public boolean exists(Long id) {
		if (id == null) {
    		throw new IllegalArgumentException("Bad user id. It must be non null");
    	}
		try {
			getUserById(id);
			log.info("User with id {} exists in database", id);
			return true;
		} catch (NoSuchElementException exc) {
			log.info(exc.getMessage());
			return false;
		}
	}

}
