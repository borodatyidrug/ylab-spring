package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UpdateUserBookRequest;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.DetailedBookResponse;
import com.edu.ulab.app.web.response.DetailedUserBookResponse;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {
	
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
    	
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        
        createdUser.setBooksId(bookIdList);
        userService.updateUser(userDto);
        
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    /**
     * Обновляет информацию о самом пользователе и списке книг, ассоциированных с ним, если пользователь
     * с соответствующим id существует в хранилище.
     * @param updateUserBookRequest
     * @return 
     */
    public UserBookResponse updateUserWithBooks(UpdateUserBookRequest updateUserBookRequest) {
    	
    	log.info("Got user books update request: {}", updateUserBookRequest);
    	Long userId = updateUserBookRequest.getUserId();
    	UserDto userDto = userService.getUserById(userId);
    	log.info("Mapped user request: {}", userDto);
        
    	List<Long> bookIdList = updateUserBookRequest.getBookIdList().stream()
    			// отбрасываю несуществующие в хранилище книги, т.к. для добавления книг в хранилище или их обновления
    			// должен применяться свой запрос. Возможно, через BookController (еще не существует :))
    			.filter(bookService::exists)
    			.peek(bookId -> {
    				BookDto updatingBook = bookService.getBookById(bookId);
    				Long prevOwnerId = updatingBook.getUserId();
    				if (userId != prevOwnerId) { // если у книги был иной предыдущий владелец
    					UserDto prevOwner = userService.getUserById(prevOwnerId); // то получаю предыдущего владельца книги
        				List<Long> prevOwnerBookIdList = prevOwner.getBooksId(); // и список его книг
        				// удаляю из него id книги, которая теперь становится книгой обновляемого пользователя
        				prevOwnerBookIdList.remove(bookId);
        				prevOwner.setBooksId(prevOwnerBookIdList); // возвращаю ему обновленный список его книг
        				userService.updateUser(prevOwner); // обновляю самого предыдущего владельца в хранилище
        				updatingBook.setUserId(userId); // Меняю принадлежность книги на нового пользователя, к которому она теперь относится
        				bookService.updateBook(updatingBook);
    				}})
    			.toList();
    	
		userDto.setBooksId(bookIdList);
    	userService.updateUser(userDto);
    	log.info("User with id {} successfully updated to {}", userDto.getId(), userDto);
    	
    	return UserBookResponse.builder()
    			.userId(userDto.getId())
    			.booksIdList(bookIdList)
    			.build();
    }

    public DetailedUserBookResponse getUserWithBooks(Long userId) {
    	
    	log.info("Got user with books get request by id {}", userId);
    	UserDto userDto = userService.getUserById(userId);
    	List<Long> booksId = userDto.getBooksId();
    	log.info("Got books ids list {} from user {}", booksId, userDto);
    	
    	List<DetailedBookResponse> booksResponse = booksId.stream()
    			.map(bookService::getBookById)
    			.map(bookDto -> {
    				DetailedBookResponse mapped = bookMapper.bookDtoToDetailedBookResponse(bookDto);
    				log.info("{} mapped to {}", bookDto, mapped);
    				return mapped;})
    			.toList();
    	
    	DetailedUserBookResponse detailedUserBookResponse = 
    			userMapper.userDtoToDetailedUserBookResponse(userDto);
    	detailedUserBookResponse.setBookResponceList(booksResponse);
    	
        return detailedUserBookResponse;
    }

    public void deleteUserWithBooks(Long userId) {
    	userService.getUserById(userId).getBooksId().stream().forEach(bookService::deleteBookById);
    	userService.deleteUserById(userId);
    }
}
