package com.guesswhat.server.persistence.jpa.entity;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Blob;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Image extends Entity {

	@Persistent
	private Blob image;

	public Image() {

	}

	public Image(Blob image) {
		super();
		this.image = image;
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}
}
