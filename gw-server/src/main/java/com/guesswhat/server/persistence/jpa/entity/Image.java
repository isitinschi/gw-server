package com.guesswhat.server.persistence.jpa.entity;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Image {

	@Id
	private Long id;
	private Blob image;
	private Long secondPart;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSecondPart() {
		return secondPart;
	}

	public void setSecondPart(Long secondPart) {
		this.secondPart = secondPart;
	}
}
