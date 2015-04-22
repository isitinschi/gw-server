package com.guesswhat.server.persistence.jpa.dao;

import static com.guesswhat.server.persistence.jpa.cfg.OfyService.ofy;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityDAO<T> {
	
	public abstract Class<T> getEntityClass();
	
	public void update(T entity) {
		save(entity);
	}
	
	public void save(T entity) {
		ofy().save().entity(entity).now();
	}
	
	public void removeAll() {
		ofy().delete().entities(findAll()).now(); 
	}
	
	public void removeAll(List<T> entities) {		
		ofy().delete().entities(entities).now(); 
	}
	
	public void remove(Long id) {
		ofy().delete().entity(find(id)).now();
	}
	
	public T find(Long id) {
		T entity = ofy().load().type(getEntityClass()).id(id).now();		
		return entity;			
	}
	
	public List<T> findAll() {
		List<T> results = ofy().load().type(getEntityClass()).list();
        
        if (results == null) {
        	results = new ArrayList<T>();
        }
        
        return results;
	}
	
	public T findFirstByFilter(String field, Object value) {
		return ofy().load().type(getEntityClass()).filter(field, value).first().now();
	}	
		
}
