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
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        
        UserDto userDtoForUpdate = new UserDto();
        userDtoForUpdate.setId(1L);
        userDtoForUpdate.setAge(11);
        userDtoForUpdate.setFullName("test name");
        userDtoForUpdate.setTitle("test title");
        userDtoForUpdate.setBooksId(List.of(2002L, 3003L));
        
        Person mappedUserForUpdate = Person.builder()
        		.id(1L)
        		.age(11)
        		.fullName("test name")
        		.title("test title")
        		.booksId(List.of(2002L, 3003L))
        		.build();
        
        Person updatedPerson = Person.builder()
        		.id(1L)
        		.age(11)
        		.fullName("test name")
        		.title("test title")
        		.booksId(List.of(2002L, 3003L))
        		.build();
        
        UserDto mappedUpdatedUser = new UserDto();
        mappedUpdatedUser.setId(1L);
        mappedUpdatedUser.setAge(11);
        mappedUpdatedUser.setFullName("test name");
        mappedUpdatedUser.setTitle("test title");
        mappedUpdatedUser.setBooksId(List.of(2002L, 3003L));
           
        //when
        //когда сохраняем пользователя впервые
        when(userMapper.userDtoToUser(userDto)).thenReturn(person);
        when(userRepository.saveAndFlush(person)).thenReturn(savedPerson);
        when(userMapper.userToUserDto(savedPerson)).thenReturn(result);
        
        //когда обновляем пользователя
        when(userMapper.userDtoToUser(userDtoForUpdate)).thenReturn(mappedUserForUpdate);
        when(userRepository.saveAndFlush(mappedUserForUpdate)).thenReturn(updatedPerson);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userMapper.userToUserDto(updatedPerson)).thenReturn(mappedUpdatedUser);
        
        //Then
        UserDto createdUserDto = userService.createUser(userDto); //создаем пользователя 
        UserDto updatedUserDto = userService.updateUser(userDtoForUpdate); //обновляем пользователя
        assertThat(updatedUserDto.getBooksId().equals(userDtoForUpdate.getBooksId()));
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
    // К сожалению, интерфейсы моих сервисов, почему-то, изначально не предусматривали
    // получение всех пользователей или книг. До текущего момента никто не обратил мое
    // внимание на это. Если такой метод все же обязательно нужно реализовать в сервисах
    // и протестировать, то я, конечно, сделаю это в рамках доработки
    
    // delete
    @Test
    @DisplayName("Удаление пользователя. Метод userRepository.deleteById() должен быть вызван")
    void deleteUser_Test() {
    	// Given
    	Long userId = 1L;
    	
    	//When
    	userService.deleteUserById(userId);
    	
    	//Then
    	verify(userRepository).deleteById(userId);
    }
    
    // * failed
    @Test
    @DisplayName("Попытка обновления юзера посредством невалидного DTO")
    void updateUserByNotValidDto_Test() {
    	
    	//Given
    	UserDto notValidUserDto = new UserDto();
    	notValidUserDto.setTitle("test title");
    	
    	String message = "Can't update user, because source DTO is "
				+ "null or his id is null or target user not exists";
    	
    	//When
    	//Then
    	assertThrows(message, IllegalArgumentException.class, () -> userService.updateUser(notValidUserDto));
    }
}
