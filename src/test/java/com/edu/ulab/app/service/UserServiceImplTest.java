package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImplJPA;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImplJPA userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {
    	
        //given

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson  = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");


        //when

        when(userMapper.userDtoToUser(userDto)).thenReturn(person);
        when(userRepository.saveAndFlush(person)).thenReturn(savedPerson);
        when(userMapper.userToUserDto(savedPerson)).thenReturn(result);


        //then

        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }

    // update
    @Test
    @DisplayName("Обновление пользователя")
    void updateUser_Test() {
    	
    	//Given
    	UserDto userDto = new UserDto();
    	userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson  = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");
        
        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");
        
        
        
        //when
        when(userMapper.userDtoToUser(userDto)).thenReturn(person);
        when(userRepository.saveAndFlush(person)).thenReturn(savedPerson);
        when(userMapper.userToUserDto(savedPerson)).thenReturn(result);
        
    }
    
    // get
    @Test
    @DisplayName("Получение пользователя")
    void getUser_Test() {
    	//Given
    	UserDto userDto = new UserDto();
    	userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson  = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");
        
        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");
        
        //when
        when(userMapper.userDtoToUser(userDto)).thenReturn(person);
        when(userRepository.saveAndFlush(person)).thenReturn(savedPerson);
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedPerson));
        when(userMapper.userToUserDto(savedPerson)).thenReturn(result);
        
        //Then
        UserDto userDtoResult = userService.createUser(userDto);
        UserDto found = userService.getUserById(1L);
        assertThat(userDtoResult.getTitle()).isEqualTo(found.getTitle());
    }
    
    // get all
    // delete

    // * failed
    //         doThrow(dataInvalidException).when(testRepository)
    //                .save(same(test));
    // example failed
    //  assertThatThrownBy(() -> testeService.createTest(testRequest))
    //                .isInstanceOf(DataInvalidException.class)
    //                .hasMessage("Invalid data set");
}
