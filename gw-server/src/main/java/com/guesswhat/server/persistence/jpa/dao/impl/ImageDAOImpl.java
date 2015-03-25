package com.guesswhat.server.persistence.jpa.dao.impl;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Blob;
import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.Image;

public class ImageDAOImpl extends EntityDAO<Image> {

	@Override
	public void update(Image image) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		Blob imageData = image.getImage();

		try {
			pm.currentTransaction().begin();
			image = pm.getObjectById(Image.class, image.getKey());
			image.setImage(imageData);
			pm.makePersistent(image);
			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}

	@Override
	public Class getEntityClass() {
		return Image.class;
	}
	
}
