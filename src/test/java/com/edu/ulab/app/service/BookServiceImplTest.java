package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImplJPA;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImplJPA bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given
        Person person  = new Person();
        person.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setUserId(1L);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");

        //when

        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.saveAndFlush(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
    }


    // update
    @Test
    @DisplayName("Обновление книги. У первоначально сохраненной книги кол-во страниц должно быть 1000, а у "
    		+ "обновленной - такое же, какое задано в bookDtoForUpdate")
    void bookUpdate_Test() {
    	//Given
    	BookDto bookDto = new BookDto();
    	bookDto.setAuthor("test author");
    	bookDto.setTitle("test title");
    	bookDto.setPageCount(1000);
    	bookDto.setUserId(1L);
    	
    	Book mappedFromDtoBook = Book.builder()
    			.author("test author")
    			.title("test title")
    			.pageCount(1000)
    			.userId(1L)
    			.build();
    	
    	Book savedBook = Book.builder()
    			.author("test author")
    			.title("test title")
    			.pageCount(1000)
    			.userId(1L)
    			.id(2002L)
    			.build();
    	
    	BookDto mappedSavedBook = new BookDto();
    	mappedSavedBook.setAuthor("test author");
    	mappedSavedBook.setTitle("test title");
    	mappedSavedBook.setPageCount(1000);
    	mappedSavedBook.setUserId(1L);
    	mappedSavedBook.setId(2002L);
    	
    	BookDto bookDtoForUpdate = new BookDto();
    	bookDtoForUpdate.setAuthor("test author");
    	bookDtoForUpdate.setTitle("test title");
    	bookDtoForUpdate.setPageCount(1001);
    	bookDtoForUpdate.setUserId(1L);
    	bookDtoForUpdate.setId(2002L);
    	
    	Book mappedBookForUpdate = Book.builder()
    			.author("test author")
    			.title("test title")
    			.pageCount(1001)
    			.userId(1L)
    			.id(2002L)
    			.build();
    	
    	Book updatedBook = Book.builder()
    			.author("test author")
    			.title("test title")
    			.pageCount(1001)
    			.userId(1L)
    			.id(2002L)
    			.build();
    	
    	BookDto mappedUpdatedBook = new BookDto();
    	mappedUpdatedBook.setAuthor("test author");
    	mappedUpdatedBook.setTitle("test title");
    	mappedUpdatedBook.setPageCount(1001);
    	mappedUpdatedBook.setUserId(1L);
    	mappedUpdatedBook.setId(2002L);
    	
    	//when
    	//когда сохраняем книгу впервые
    	when(bookMapper.bookDtoToBook(bookDto)).thenReturn(mappedFromDtoBook);
    	when(bookRepository.saveAndFlush(mappedFromDtoBook)).thenReturn(savedBook);
    	when(bookMapper.bookToBookDto(savedBook)).thenReturn(mappedSavedBook);
    	
    	//когда обновляем ранее сохраненную книгу
    	when(bookMapper.bookDtoToBook(bookDtoForUpdate)).thenReturn(mappedBookForUpdate);
    	when(bookRepository.saveAndFlush(mappedBookForUpdate)).thenReturn(updatedBook);
    	when(bookRepository.existsById(2002L)).thenReturn(true);
    	when(bookMapper.bookToBookDto(updatedBook)).thenReturn(mappedUpdatedBook);
    	
    	//Then
    	BookDto savedBookDto = bookService.createBook(bookDto); // создаем книгу
    	BookDto updatedBookDto = bookService.updateBook(bookDtoForUpdate); // обновляем книгу
    	assertThat(savedBookDto.getPageCount() == 1000);
    	assertThat(bookDtoForUpdate.getPageCount() == updatedBookDto.getPageCount());
    }
    
    // get
    @Test
    @DisplayName("Получение ранее созданной книги. Название и id юзера полученной книги должны совпадать "
    		+ "с теми, которые были заданы в bookDto")
    void getBook_Test() {
    	//Given
    	BookDto bookDto = new BookDto();
    	bookDto.setAuthor("test author");
    	bookDto.setTitle("test title");
    	bookDto.setPageCount(1000);
    	bookDto.setUserId(1L);
    	
    	Book mappedFromDtoBook = Book.builder()
    			.author("test author")
    			.title("test title")
    			.pageCount(1000)
    			.userId(1L)
    			.build();
    	
    	Book savedBook = Book.builder()
    			.author("test author")
    			.title("test title")
    			.pageCount(1000)
    			.userId(1L)
    			.id(2002L)
    			.build();
    	
    	BookDto mappedSavedBook = new BookDto();
    	mappedSavedBook.setAuthor("test author");
    	mappedSavedBook.setTitle("test title");
    	mappedSavedBook.setPageCount(1000);
    	mappedSavedBook.setUserId(1L);
    	mappedSavedBook.setId(2002L);
    	
    	//When
    	when(bookMapper.bookDtoToBook(bookDto)).thenReturn(mappedFromDtoBook);
    	when(bookRepository.saveAndFlush(mappedFromDtoBook)).thenReturn(savedBook);
    	when(bookMapper.bookToBookDto(savedBook)).thenReturn(mappedSavedBook);
    	when(bookRepository.findById(2002L)).thenReturn(Optional.of(savedBook));
    	
    	//Then
    	BookDto savedBookDto = bookService.createBook(bookDto);
    	BookDto foundBookDto = bookService.getBookById(2002L);
    	assertThat(savedBookDto.getTitle()).isEqualTo(foundBookDto.getTitle());
    	assertThat(savedBookDto.getUserId()).isEqualTo(foundBookDto.getUserId());
    }
    
    // get all
    // К сожалению, интерфейсы моих сервисов, почему-то, изначально не предусматривали
    // получение всех пользователей или книг. До текущего момента никто не обратил мое
    // внимание на это. Если такой метод все же обязательно нужно реализовать в сервисах
    // и протестировать, то я, конечно, сделаю это в рамках доработки
    
    // delete
    @Test
    @DisplayName("Удаление книги. Метод bookRepository.deleteById() должен быть вызван")
    void deleteBook_Test() {
    	//Given
    	Long bookId = 2002L;
    	
    	//When
    	bookService.deleteBookById(bookId);
    	
    	//Then
    	verify(bookRepository).deleteById(bookId);
    }
    
    // * failed
    @Test
    @DisplayName("Попытка получения несуществующей в репозитории книги. Должен быть вброс NoSuchElementException")
    void getNotExistingBook_Test() {
    	//Given
    	Long bookId = 4004L;
    	String message = "Book with id " + bookId + " not exixts in repository!";
    	
    	//When
    	when(bookRepository.findById(bookId)).thenThrow(NoSuchElementException.class);
    	
    	//Then
    	assertThrows(message, NoSuchElementException.class, () -> bookService.getBookById(bookId));
    }
}
