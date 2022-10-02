package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.web.request.UpdateUserRequest;
import com.edu.ulab.app.web.request.UserRequest;
import com.edu.ulab.app.web.response.DetailedUserBookResponse;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
    UserDto userRequestToUserDto(UserRequest userRequest);
    
    UserRequest userDtoToUserRequest(UserDto userDto);
    
    Person userDtoToUser(UserDto userDto);
    
    UserDto userToUserDto(Person user);
    
    DetailedUserBookResponse userDtoToDetailedUserBookResponse(UserDto userDto);
    
    UserDto updateUserRequestToUserDto(UpdateUserRequest updateUserRequest);
}
