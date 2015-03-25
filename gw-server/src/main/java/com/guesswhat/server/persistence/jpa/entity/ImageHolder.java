package com.guesswhat.server.persistence.jpa.entity;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ImageHolder extends Entity {

	@Persistent
	private Image xxhdpiImage;
	@Persistent
	private Image xhdpiImage;
	@Persistent
	private Image hdpiImage;
	@Persistent
	private Image mdpiImage;
	@Persistent
	private Image ldpiImage;

	public ImageHolder() {
		
	}

	public Image getXxhdpiImage() {
		return xxhdpiImage;
	}

	public void setXxhdpiImage(Image xxhdpiImage) {
		this.xxhdpiImage = xxhdpiImage;
	}

	public Image getXhdpiImage() {
		return xhdpiImage;
	}

	public void setXhdpiImage(Image xhdpiImage) {
		this.xhdpiImage = xhdpiImage;
	}

	public Image getHdpiImage() {
		return hdpiImage;
	}

	public void setHdpiImage(Image hdpiImage) {
		this.hdpiImage = hdpiImage;
	}

	public Image getMdpiImage() {
		return mdpiImage;
	}

	public void setMdpiImage(Image mdpiImage) {
		this.mdpiImage = mdpiImage;
	}

	public Image getLdpiImage() {
		return ldpiImage;
	}

	public void setLdpiImage(Image ldpiImage) {
		this.ldpiImage = ldpiImage;
	}

	public boolean isFull() {
		if (xxhdpiImage != null && xhdpiImage != null && hdpiImage != null && mdpiImage != null && ldpiImage != null) {
			return true;
		}
		
		return false;
	}

	

}
