package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.List;

/**
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader1");
        person.setFullName("Test Test");

        //When
        Person result = userRepository.saveAndFlush(person);

        //Then
        assertThat(result.getAge()).isEqualTo(111);
        assertSelectCount(1);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // update
    @DisplayName("Обновить юзера. Числа update, select, insert должны равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
        "classpath:sql/2_insert_person_data.sql",
        "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount() {
    	//Given
    	Person person = Person.builder()
    			.age(111)
    			.title("reader1")
    			.fullName("Test Test")
    			.build();
    	
    	Person savedPerson = userRepository.saveAndFlush(person);
    	
    	//When
    	savedPerson.setAge(112);
    	Person result = userRepository.saveAndFlush(savedPerson);
    	
    	//Then
    	assertThat(result.getAge()).isEqualTo(112);
    	assertSelectCount(1);
    	assertInsertCount(1);
    	assertUpdateCount(1);
    	assertDeleteCount(0);
    }
    
    // get
    @DisplayName("Получить юзера. Числа insert, select должны равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
        "classpath:sql/2_insert_person_data.sql",
        "classpath:sql/3_insert_book_data.sql"
    })
    void getPerson_thenAssertDmlCount() {
    	//Given
    	Person person = Person.builder()
    			.age(111)
    			.title("reader1")
    			.fullName("Test Test")
    			.build();
    	
    	Person savedPerson = userRepository.saveAndFlush(person);
    	Long savedPersonId = savedPerson.getId();
    	
    	//When
    	Person result = userRepository.findById(savedPersonId).get();
    	
    	//Then
    	assertThat(result.getAge()).isEqualTo(111);
    	assertThat(result.getTitle()).isEqualTo(result.getTitle());
    	assertSelectCount(1);
    	assertInsertCount(1);
    	assertUpdateCount(0);
    	assertDeleteCount(0);
    }
    
    
    // get all
    @DisplayName("Получить всех добавленных юзеров. Число insert - 2, select - 3")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
        "classpath:sql/2_insert_person_data.sql",
        "classpath:sql/3_insert_book_data.sql"
    })
    void getAllPersons_thenAssertDmlCount() {
    	//Given
    	Person person1 = Person.builder()
    			.age(111)
    			.title("reader1")
    			.fullName("Test Test")
    			.build();
    	Person person2 = Person.builder()
    			.age(112)
    			.title("reader2")
    			.fullName("Test1 Test1")
    			.build();
    	
    	userRepository.saveAndFlush(person1);
    	userRepository.saveAndFlush(person2);
    	
    	List<Integer> agesExpected = List.of(55, 111, 112);
    	List<String> titlesExpected = List.of("reader", "reader1", "reader2");
    	
    	//When
    	List<Person> result = userRepository.findAll();
    	List<Integer> ages = result.stream().map(Person::getAge).toList();
    	List<String> titles = result.stream().map(Person::getTitle).toList();
    	
    	//Then
    	assertThat(result.size()).isEqualTo(3);
    	assertThat(agesExpected.equals(ages));
    	assertThat(titlesExpected.equals(titles));
    	assertSelectCount(3);
    	assertInsertCount(2);
    	assertUpdateCount(0);
    	assertDeleteCount(0);
    }
    
    // delete
    @DisplayName("Удалить ранее добавленного юзера. Число insert - 1, select - 2, delete - 1. В репозитории должен"
    		+ "остаться один заранее известный юзер")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
        "classpath:sql/2_insert_person_data.sql",
        "classpath:sql/3_insert_book_data.sql"
    })
    void deletePerson_thenAssertDmlCount() {
    	//Given
    	Person person = Person.builder()
    			.age(111)
    			.title("reader1")
    			.fullName("Test Test")
    			.build();
    	
    	Person savedPerson = userRepository.saveAndFlush(person);
    	
    	//When
    	userRepository.delete(savedPerson);
    	List<Person> found = userRepository.findAll();
    	List<Person> deleted = found.stream().filter(p -> p.getTitle().equals("reader1")).toList();
    	
    	//Then
    	assertThat(deleted.isEmpty());
    	assertThat(found.size() == 1);
    	assertSelectCount(2);
    	assertInsertCount(1);
    	assertUpdateCount(0);
    	assertDeleteCount(1);
    }

    // * failed
    @DisplayName("Добавить в репозиторий двух юзеров с одинаковыми title. Ожидается исключение"
    		+ " нарушения целостности данных из-за ограничений уникальности на поле title,"
    		+ " участвующего в формировании индекса")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
        "classpath:sql/2_insert_person_data.sql",
        "classpath:sql/3_insert_book_data.sql"
    })
    void addTwoUsersWithEqualsTitles_thenAssertThrowException() {
    	//Given
    	Person person1 = Person.builder()
    			.age(111)
    			.title("reader1")
    			.fullName("Test Test")
    			.build();
    	Person person2 = Person.builder()
    			.age(112)
    			.title("reader1")
    			.fullName("Test1 Test1")
    			.build();
    	
    	//When
    	userRepository.saveAndFlush(person1);
    	
    	//Then
    	assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(person2));
    }
}
