package com.guesswhat.server.persistence.jpa.jdo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public abstract class EntityJDO {
	protected static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");
	
	protected static PersistenceManagerFactory getPersistenceManagerFactory() {
		return pmfInstance;
	}
}
