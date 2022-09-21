package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.mapper.UserMapperImpl;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.IStorage;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
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
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id
    	if (userDto != null) {
    		User user = userMapper.userDtoToUser(userDto);
        	log.debug("Created user: {}", user);
        	Long generatedId = storage.save(user);
        	log.debug("Created user got generated id from storage. Id is {}", generatedId);
            userDto.setId(generatedId);
            return userDto;
    	} else {
    		throw new IllegalArgumentException("Only non null values are allowed");
    	}
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
    	if (userDto != null && userDto.getId() != null && storage.contains(userDto.getId())) {
    		User user = userMapper.userDtoToUser(userDto);
        	Long id = user.getId();
        	storage.update(user);
        	log.debug("User with id {} successfully updated to {}", id, user);
            return userDto;
    	} else {
    		throw new IllegalArgumentException("Can't update user, because source DTO is "
					+ "null or his id is null or target user not exists");
    	}
    }

    @Override
    public UserDto getUserById(Long id) {
    	if (id != null) {
    		User user = (User) storage.get(id);
        	UserDto userDto = userMapper.userToUserDto(user);
        	log.debug("Got user {} with id {} from storage", user, id);
            return userDto;
    	} else {
    		throw new IllegalArgumentException("Bad user id. It must be non null");
    	}
    }

    @Override
    public void deleteUserById(Long id) {
    	if (id != null && storage.delete(id)) {
    		log.debug("User with id {} successfully deleted", id);
    	} else {
    		log.debug("User with id {} not deleted", id);
    	}
    }

	@Override
	public boolean exists(Long id) {
		Boolean exists = id == null ? false : storage.contains(id);
		log.debug("User with id {} " + (exists ? "exists" : "not exists") + " in storage", id);
		return exists;
	}
}
