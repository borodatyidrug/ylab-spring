package com.edu.ulab.app.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "jpa-user-service")
public class UserServiceImplJPA implements UserService {

	private UserRepository userRepository;
	private UserMapper userMapper;
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		if (userDto == null) {
    		throw new IllegalArgumentException("Only non null values are allowed");
    	}
		Person person = userMapper.userDtoToUser(userDto);
		log.info("{} mapped to {}", userDto, person);
		Person savedPerson = userRepository.save(person);
		log.info("{} saved to repository", savedPerson);
		return userMapper.userToUserDto(savedPerson);
	}

	@Override
	public UserDto updateUser(UserDto userDto) {
		if (userDto == null || userDto.getId() == null || !userRepository.existsById(userDto.getId())) {
    		throw new IllegalArgumentException("Can't update user, because source DTO is "
					+ "null or his id is null or target user not exists");
    	}
		Person person = userMapper.userDtoToUser(userDto);
		log.info("{} mapped to {}", userDto, person);
		Person updatedPerson = userRepository.save(person);
		log.info("{} updated by {} and saved to repository", updatedPerson);
		return userMapper.userToUserDto(updatedPerson);
	}

	@Override
	public UserDto getUserById(Long id) {
		if (id == null) {
    		throw new IllegalArgumentException("Bad user id. It must be non null");
    	}
		Person person = userRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("User with id " + id + " not exixts in repository!"));
		log.info("Got {} by id {} from repository", person, id);
		return userMapper.userToUserDto(person);
	}

	@Override
	public void deleteUserById(Long id) {
		if (id == null) {
    		throw new IllegalArgumentException("Bad user id. It must be non null");
    	}
		userRepository.deleteById(id);
		log.info("User with id {} successfully deleted", id);
	}

	@Override
	public boolean exists(Long id) {
		Boolean exists = id == null ? false : userRepository.existsById(id);
		log.debug("User with id {} " + (exists ? "exists" : "not exists") + " in repository", id);
		return exists;
	}

}
