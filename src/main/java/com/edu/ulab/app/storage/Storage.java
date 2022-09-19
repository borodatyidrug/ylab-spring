package com.edu.ulab.app.storage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

// TODO создать хранилище в котором будут содержаться данные
// сделать абстракции через которые можно будет производить операции с хранилищем
// продумать логику поиска и сохранения
// продумать возможные ошибки
// учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
// продумать что у узера может быть много книг и нужно создать эту связь
// так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции

/**
 * Хранилище основано на хэшмапе и принимает любые объекты типа Idable, то есть - те,
 * которые имеют id и реализуют интерфейс Idable. Это значит, что в хранилище могут
 * храниться одновременно как юзеры, так и книги, если они реализуют интерфейс Idable.
 * А это - так, поскольку и у юзеров, и у книг есть id и геттеры для них.
 * Id объектов, удаленных из хранилища, используются повторно для других объектов.
 * @author borodatyidrug
 *
 */
@Slf4j
@Repository
public class Storage implements IStorage {
	
	Long currentId = 0L;
	/*
	 * Хранилище объектов
	 */
	Map<Long, Idable<?>> objStorage;
	/**
	 * Хранилище id, которые высвобождаются после удаления объектов из хранилища объектов
	 */
	Deque<Long> freedIds;
	
	public Storage() {
		objStorage = new HashMap<>();
		freedIds = new ArrayDeque<>();
		freedIds.addLast(currentId);
	}
	
	@Override
	public Long save(Idable<Long> obj) {
		Idable<Long> object = obj;
		if (object != null) {
			log.debug("Got new object for save: {}", object);
			Long generatedId = getNextUnusedId();
			object.setId(generatedId);
			log.debug("New object got id {}", generatedId);
			objStorage.put(generatedId, object);
			log.debug("Saved new object with id {}", generatedId);
			return generatedId;
		} else {
			throw new IllegalArgumentException("Only non null values are allowed");
		}
	}

	@Override
	public Object get(Long objId) {
		if (objId != null) {
			log.debug("Requested object with id {}", objId);
			return Optional.ofNullable(objStorage.get(objId)).orElseThrow();
		} else {
			throw new IllegalArgumentException("Bad object id. It must be non null");
		}
	}

	@Override
	public void update(Idable<Long> obj) {
		Object previous;
		if (obj != null && obj.getId() != null && objStorage.containsKey(obj.getId())) {
			previous = objStorage.replace(obj.getId(), obj);
			log.debug("Object {} updated to object {}", previous, obj);
		} else {
			throw new IllegalArgumentException("Can't update object, because source object is "
					+ "null or his id is null or target object not exist");
		}
	}

	@Override
	public void delete(Long id) {
		if (id != null && objStorage.remove(id) != null) {
			freedIds.addLast(id);
			log.debug("Object with id {} successfully deleted", id);
		} else {
			throw new IllegalArgumentException("Can't delete object, cause object not exist or his id is null");
		}
	}
	
	/**
	 * Возвращает доступный идентификатор для вновь сохраняемого в хранилище объекта
	 * @return Id
	 */
	private Long getNextUnusedId() {
		if (freedIds.isEmpty()) {
			return ++currentId;
		} else {
			return freedIds.pollLast();
		}
	}

	@Override
	public boolean contains(Long id) {
		return id == null ? false : objStorage.containsKey(id);
	}
}
