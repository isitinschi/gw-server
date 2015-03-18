package com.guesswhat.server.persistence.jpa.dao;

public interface EntityDAO<T> {
	void save(T t);
	void update(T t);
	void remove(T t);
	T find(T t);
}
