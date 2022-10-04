package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу и автора. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void saveUserAndBook_thenAssertDmlCount() {
        //Given

        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader1");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.saveAndFlush(person);
        Long generatedId = savedPerson.getId();

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setUserId(generatedId);;

        //When
        Book result = bookRepository.saveAndFlush(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(2);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // update
    @DisplayName("Обновить книгу. Числа select, insert, update должны равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
    	//Given
    	Book book = Book.builder()
    			.author("Test Author")
    			.title("test")
    			.pageCount(1000)
    			.userId(1001L)
    			.build();
    	
    	Book savedBook = bookRepository.saveAndFlush(book);
    	
    	//When
    	savedBook.setPageCount(1001);
    	Book updatedBook = bookRepository.saveAndFlush(savedBook);
    	
    	//Then
    	assertThat(updatedBook.getPageCount()).isEqualTo(1001);
    	assertSelectCount(1);
        assertInsertCount(1);
        assertUpdateCount(1);
        assertDeleteCount(0);
    }
    
    // get
    @DisplayName("Получить книгу. Числа select, insert должны равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBook_thenAssertDmlCount() {
    	//Given
    	Book book = Book.builder()
    			.author("Test Author")
    			.title("test")
    			.pageCount(1000)
    			.userId(1001L)
    			.build();
    	
    	Book savedBook = bookRepository.saveAndFlush(book);
    	Long savedBookId = savedBook.getId();
    	
    	//When
    	Book result = bookRepository.findById(savedBookId).get();
    	
    	//Then
    	assertThat(result.getUserId()).isEqualTo(1001L);
    	assertThat(result.getTitle()).isEqualTo("test");
    	assertSelectCount(1);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    
    // get all
    @DisplayName("Получить все книги. Числа select - 3, insert -2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getAllBooks_thenAssertDmlCount() {
    	
    	//Given
    	Book book1 = Book.builder()
    			.author("Test Author")
    			.title("test1")
    			.pageCount(1000)
    			.userId(1001L)
    			.build();
    	
    	Book book2 = Book.builder()
    			.author("Test Author")
    			.title("test2")
    			.pageCount(1000)
    			.userId(1001L)
    			.build();
    	
    	Long id1 = bookRepository.saveAndFlush(book1).getId();
    	Long id2 = bookRepository.saveAndFlush(book2).getId();
    	
    	Set<Long> expectedBookIds = Set.of(2002L, 3003L, id1, id2);
    	
    	//When
    	Set<Long> actualBookIds = bookRepository.findAll().stream()
    			.map(Book::getId)
    			.collect(Collectors.toSet());
    	
    	//Then
    	assertThat(expectedBookIds.equals(actualBookIds));
    	assertSelectCount(3);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    
    // delete
    @DisplayName("Удалить книгу. В репозитории должна остаться одна заведомо известная книга. Числа select - 3, insert -2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBook_thenAssertDmlCount() {
    	
    	//Given
    	Book book1 = Book.builder()
    			.author("author")
    			.title("default book")
    			.userId(1001L)
    			.id(2002L)
    			.pageCount(5500)
    			.build();
    	
    	Book book2 = Book.builder()
    			.author("on more author")
    			.title("more default book")
    			.userId(1001L)
    			.id(3003L)
    			.pageCount(6655)
    			.build();
    	
    	//When
    	bookRepository.delete(book2);
    	List<Book> found = bookRepository.findAll();
    	
    	//Then
    	assertThat(found.size() == 1);
    	assertThat(found.get(0).getId()).isEqualTo(2002L);
    	assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }
}
