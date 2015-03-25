package com.guesswhat.server.persistence.jpa.dao.impl;

import javax.jdo.PersistenceManager;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;

public class ImageHolderDAOImpl extends EntityDAO<ImageHolder> {

	@Override
	public void update(ImageHolder imageHolder) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		
		Image xxhdpiImage = imageHolder.getXxhdpiImage();
		Image xhdpiImage = imageHolder.getXhdpiImage();
		Image hdpiImage = imageHolder.getHdpiImage();
		Image mdpiImage = imageHolder.getMdpiImage();
		Image ldpiImage = imageHolder.getLdpiImage();

		try {
			pm.currentTransaction().begin();
			imageHolder = pm.getObjectById(ImageHolder.class, imageHolder.getKey());
			imageHolder.setXxhdpiImage(xxhdpiImage);
			imageHolder.setXhdpiImage(xhdpiImage);
			imageHolder.setHdpiImage(hdpiImage);
			imageHolder.setMdpiImage(mdpiImage);
			imageHolder.setLdpiImage(ldpiImage);
			pm.makePersistent(imageHolder);
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
		return ImageHolder.class;
	}
	
}
