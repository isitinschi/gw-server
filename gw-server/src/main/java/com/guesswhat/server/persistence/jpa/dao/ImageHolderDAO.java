package com.guesswhat.server.persistence.jpa.dao;

import com.guesswhat.server.persistence.jpa.entity.ImageHolder;

public abstract class ImageHolderDAO extends EntityDAO<ImageHolder> {
	
	@Override
	public Class<ImageHolder> getEntityClass() {
		return ImageHolder.class;
	}
}
