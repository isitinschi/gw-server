package com.guesswhat.server.persistence.jpa.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.Filter;

public abstract class EntityDAO<T> {
	public static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");
	
	public abstract void update(T t);
	public abstract Class<T> getEntityClass();

	public static PersistenceManagerFactory getPersistenceManagerFactory() {
		return pmfInstance;
	}
	
	public void save(T entity) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		try {
			pm.makePersistent(entity);
		} finally {
			pm.close();
		}
	}
	
	public void remove(Key key) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		try {
			pm.currentTransaction().begin();
			
			T entity = (T) pm.getObjectById(getEntityClass(), key);
			pm.deletePersistent(entity);

			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}
	
	public void remove(Long id) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		try {
			pm.currentTransaction().begin();
			
			T entity = (T) pm.getObjectById(getEntityClass(), id);
			pm.deletePersistent(entity);

			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}
	
	public T find(Key key) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		T entity = null;
		try {
			entity = (T) pm.getObjectById(getEntityClass(), key);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
		
		return entity;
	}
	
	public T find(Long id) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		T entity = null;
		try {
			entity = (T) pm.getObjectById(getEntityClass(), id);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
		
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
        List<T> results = null;
        Query q = pm.newQuery(getEntityClass());
        try {
            results = (List<T>) q.execute();
        } finally {
            q.closeAll();
        }
        
        if (results == null) {
        	results = new ArrayList<T>();
        }
        
        return results;
	}
	
	public T findByFilter(String filter) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
        List<T> results = null;
        
        Query q = pm.newQuery(getEntityClass());
        q.setFilter(filter);
        try {
            results = (List<T>) q.execute();
        } finally {
            q.closeAll();
        }
        
        if (results == null || results.isEmpty()) {
        	return null;
        }

        return results.get(0);
	}
	
		
}
