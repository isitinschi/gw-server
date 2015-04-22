package com.guesswhat.server.persistence.jpa.dao.impl;

import com.guesswhat.server.persistence.jpa.dao.EntityDAO;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;

public class ImageHolderDAOImpl extends EntityDAO<ImageHolder> {

	@Override
	public Class<ImageHolder> getEntityClass() {
		return ImageHolder.class;
	}
	
}
