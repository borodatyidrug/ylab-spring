package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.IStorage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "storage-user-service")
public class UserServiceImpl implements UserService {
	
	private IStorage storage;
	private UserMapper userMapper;
	
	@Autowired
	public void setStorage(IStorage storage) {
		this.storage = storage;
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
    	Person user = userMapper.userDtoToUser(userDto);
    	log.debug("Created user: {}", user);
    	Long generatedId = storage.save(user);
    	log.debug("Created user got generated id from storage. Id is {}", generatedId);
        userDto.setId(generatedId);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
    	if (userDto == null || userDto.getId() == null || !storage.contains(userDto.getId())) {
    		throw new IllegalArgumentException("Can't update user, because source DTO is "
					+ "null or his id is null or target user not exists");
    	}
    	Person user = userMapper.userDtoToUser(userDto);
    	Long id = user.getId();
    	storage.update(user);
    	log.debug("User with id {} successfully updated to {}", id, user);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
    	if (id == null) {
    		throw new IllegalArgumentException("Bad user id. It must be non null");
    	}
    	Person user = (Person) storage.get(id);
    	UserDto userDto = userMapper.userToUserDto(user);
    	log.debug("Got user {} with id {} from storage", user, id);
        return userDto;
    }

    @Override
    public void deleteUserById(Long id) {
    	if (id == null || !storage.delete(id)) {
    		log.debug("User with id {} not deleted", id);
    	}
    	log.debug("User with id {} successfully deleted", id);
    }

	@Override
	public boolean exists(Long id) {
		Boolean exists = id == null ? false : storage.contains(id);
		log.debug("User with id {} " + (exists ? "exists" : "not exists") + " in storage", id);
		return exists;
	}
}
