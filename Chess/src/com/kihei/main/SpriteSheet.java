package com.kihei.main;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	private BufferedImage sprite;
	
	public SpriteSheet(BufferedImage ss) {
		this.sprite = ss;
	}
	
	public BufferedImage grabImage(int row, int col) {
		return sprite.getSubimage((col * 90) - 90, (row * 90) - 90, 90, 90);
	}
	
}
