package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

public interface BookService {
	
	/**
	 * Конструирует новую книгу на основании DTO
	 * @param bookDto DTO из контроллера
	 * @return DTO с полученным из хранилища и установленным идентификатором
	 */
    BookDto createBook(BookDto bookDto);
    
    /**
     * Обновляет книгу в соответствии с значениями полей в полученном DTO
     * @param bookDto DTO из контроллера
     * @return DTO c обновленными значениями полей
     */
    BookDto updateBook(BookDto bookDto);
    
    /**
     * Получает из хранилища книгу по идентификатору и возвращает DTO
     * @param id Идентификатор
     * @return DTO
     */
    BookDto getBookById(Long id);
    
    /**
     * Удаляет книгу из хранилища по её идентификатору
     * @param id Идентификатор
     */
    void deleteBookById(Long id);
    
    /**
     * Проверяет наличие книги в хранилище по её дентификатору
     * @param id Идентификатор
     * @return True, если найдена в хранилище
     */
    boolean exists(Long id);
}
