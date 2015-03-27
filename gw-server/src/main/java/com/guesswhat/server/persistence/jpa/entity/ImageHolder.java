package com.guesswhat.server.persistence.jpa.entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ImageHolder {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private Key xxhdpiImage;
	@Persistent
	private Key xhdpiImage;
	@Persistent
	private Key hdpiImage;
	@Persistent
	private Key mdpiImage;
	@Persistent
	private Key ldpiImage;

	public ImageHolder() {
		
	}

	public boolean isFull() {
		if (xxhdpiImage != null && xhdpiImage != null && hdpiImage != null && mdpiImage != null && ldpiImage != null) {
			return true;
		}
		
		return false;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getXxhdpiImage() {
		return xxhdpiImage;
	}

	public void setXxhdpiImage(Key xxhdpiImage) {
		this.xxhdpiImage = xxhdpiImage;
	}

	public Key getXhdpiImage() {
		return xhdpiImage;
	}

	public void setXhdpiImage(Key xhdpiImage) {
		this.xhdpiImage = xhdpiImage;
	}

	public Key getHdpiImage() {
		return hdpiImage;
	}

	public void setHdpiImage(Key hdpiImage) {
		this.hdpiImage = hdpiImage;
	}

	public Key getMdpiImage() {
		return mdpiImage;
	}

	public void setMdpiImage(Key mdpiImage) {
		this.mdpiImage = mdpiImage;
	}

	public Key getLdpiImage() {
		return ldpiImage;
	}

	public void setLdpiImage(Key ldpiImage) {
		this.ldpiImage = ldpiImage;
	}

}
