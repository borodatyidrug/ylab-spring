package com.edu.ulab.app.storage;

public interface IStorage {
	Long save(Idable<Long> objDto) throws IllegalArgumentException;
	Object get(Long objId) throws IllegalArgumentException;
	void update(Idable<Long> objDto) throws IllegalArgumentException;
	void delete(Long id) throws IllegalArgumentException;
	boolean contains(Long id);
}
