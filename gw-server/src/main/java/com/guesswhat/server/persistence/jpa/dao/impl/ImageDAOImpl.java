package com.guesswhat.server.persistence.jpa.dao.impl;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.Image;

public class ImageDAOImpl extends EntityDAO<Image> {

	@Override
	public void update(Image image) {
		// Nothing to do
	}

	@Override
	public Class<Image> getEntityClass() {
		return Image.class;
	}
	
}
