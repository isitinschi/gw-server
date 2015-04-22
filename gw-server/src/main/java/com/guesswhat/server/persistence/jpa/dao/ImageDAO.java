package com.guesswhat.server.persistence.jpa.dao;

import com.guesswhat.server.persistence.jpa.entity.Image;

public abstract class ImageDAO extends EntityDAO<Image> {
	
	@Override
	public Class<Image> getEntityClass() {
		return Image.class;
	}
}
