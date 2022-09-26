package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;

public interface UserService {
	
	/**
	 * Конструирует нового юзера на основании DTO
	 * @param userDto DTO из контроллера
	 * @return DTO с полученным из хранилища и установленным идентификатором
	 */
    UserDto createUser(UserDto userDto);
    
    /**
     * Обновляет юзера в соответствии с значениями полей в полученном DTO
     * @param userDto DTO из контроллера
     * @return DTO c обновленными значениями полей
     */
    UserDto updateUser(UserDto userDto);
    
    /**
     * Получает из хранилища юзера по идентификатору и возвращает DTO
     * @param id Идентификатор
     * @return DTO
     */
    UserDto getUserById(Long id);
    
    /**
     * Удаляет юзера из хранилища по его идентификатору
     * @param id Идентификатор
     */
    void deleteUserById(Long id);
    
    /**
     * Проверяет наличие юзера в хранилище по его дентификатору
     * @param id Идентификатор
     * @return True, если найден в хранилище
     */
    boolean exists(Long id);
}
