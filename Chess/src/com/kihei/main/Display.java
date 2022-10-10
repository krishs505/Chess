package com.kihei.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Display {
	
	public static String[][] board = new String[8][8];
	
	public void render(Graphics g, int width, int height) {
		int cX = ((width - 800) / 2);
		int cY = ((height - 800) / 2);
		
		setupBoard(g, cX, cY);
		
		SpriteSheet ss = new SpriteSheet(Game.chesspieces);
		BufferedImage[] imgs = new BufferedImage[] {ss.grabImage(1, 1), ss.grabImage(1, 2), ss.grabImage(1, 3), ss.grabImage(1, 4), ss.grabImage(1, 5), ss.grabImage(1, 6), ss.grabImage(2, 1), ss.grabImage(2, 2), ss.grabImage(2, 3), ss.grabImage(2, 4), ss.grabImage(2, 5), ss.grabImage(2, 6), };
		BufferedImage p = imgs[0];
		
		// update pieces on board based on array
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (board[r][c] != null) {
					if (MouseInput.selR == r && MouseInput.selC == c) {
						g.setColor(new Color(246, 246, 105));
						g.fillRect(cX, cY, 100, 100); // adds highlight if the piece is selected
					}
					switch (board[r][c]) {
						case "WK": p = imgs[0]; break;
						case "WQ": p = imgs[1]; break;
						case "WR": p = imgs[2]; break;
						case "WB": p = imgs[3]; break;
						case "WN": p = imgs[4]; break;
						case "WP": p = imgs[5]; break;
						case "BK": p = imgs[6]; break;
						case "BQ": p = imgs[7]; break;
						case "BR": p = imgs[8]; break;
						case "BB": p = imgs[9]; break;
						case "BN": p = imgs[10]; break;
						case "BP": p = imgs[11]; break;
					}
					
					g.drawImage(p, cX+5, cY+5, null);
				}
				
				for (int i = 0; i < MouseInput.possR.size(); i++) {
					if (i < MouseInput.possC.size() && i < MouseInput.possR.size()) {
						if (r == MouseInput.possR.get(i) && c == MouseInput.possC.get(i)) {
							g.setColor(new Color(217, 217, 208));
							g.fillOval(cX+40, cY+40, 20, 20); // adds grey dot if it's a possible position
						}
					}
				}
				
				cX += 100;
			}
			cY += 100;
			cX -= 800;
		}
		
		if (MouseInput.pawnPromote != -1) {
			int ncX = ((width - 800) / 2) + 100*(MouseInput.pawnPromote);
			int ncY = ((height - 800) / 2);
			
			if (MouseInput.pawnPC.equals("W")) {
				g.setColor(Color.black);
				g.fillRect(ncX, ncY, 100, 400);
				ncX += 5; ncY += 5;
				g.drawImage(imgs[1], ncX, ncY, null); g.drawImage(imgs[4], ncX, ncY+100, null); g.drawImage(imgs[2], ncX, ncY+200, null); g.drawImage(imgs[3], ncX, ncY+300, null);
			} else if (MouseInput.pawnPC.equals("B")) {
				g.setColor(Color.white);
				ncY += 400;
				g.fillRect(ncX, ncY, 100, 400);
				ncX += 5; ncY += 5;
				g.drawImage(imgs[9], ncX, ncY, null); g.drawImage(imgs[8], ncX, ncY+100, null); g.drawImage(imgs[10], ncX, ncY+200, null); g.drawImage(imgs[7], ncX, ncY+300, null);
			}
		}
		
		// there is a mate
		if (MouseInput.mated != null) {
			g.setColor(Color.black);
			String winner = MouseInput.mated;
			switch (winner) {
				case "W": winner = "White won!"; break;
				case "B": winner = "Black won!"; break;
				case "S": winner = "Draw by Stalemate"; break;
			} 
			cStr(g, "GG! " + winner, 0, -440, width, height, 40);
			
			g.setColor(Color.black);
			g.fillRect((width - 200) / 2, (height - 75) / 2 + 440, 200, 70);
			g.setColor(Color.white);
			cStr(g, "Play Again", (width - 200) / 2, (height - 75) / 2 + 435, 200, 75, 35);
		}
		
		g.setColor(new Color(0, 0, 0));
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 40));
		g.drawString("Current turn:", 20, 50);
		if(MouseInput.turn.equals("W")) {
			g.drawImage(imgs[0], 240, -5, null);
		} else {
			g.drawImage(imgs[6], 240, -5, null);
		}
	}
	
	// sets up actual board squares
	private void setupBoard(Graphics g, int cX, int cY) {
		// creates entire back of board as brown
		g.setColor(new Color(212, 190, 159));
		g.fillRect(cX, cY, 800, 800);
				
		// for loop adds white squares every other row
		cX += 100;
		for(int r = 0; r < 4; r++) {
			for(int c = 0; c < 4; c++) {
				g.setColor(new Color(145, 100, 69));
				g.fillRect(cX, cY, 100, 100);
				cX += 200;
			}
			cY += 200;
			cX -= 800;
		}
		// shift it slightly to do the other rows
		cX -= 100;
		cY -= 700;
		// add rest of white squares
		for(int r = 0; r < 4; r++) {
			for(int c = 0; c < 4; c++) {
				g.setColor(new Color(145, 100, 69));
				g.fillRect(cX, cY, 100, 100);
				cX += 200;
			}
			cY += 200;
			cX -= 800;
		}
	}

	
	// center string
	private void cStr(Graphics g, String text, int rectX, int rectY, int rectW, int rectH, int fontSize) {
	    FontMetrics metrics = g.getFontMetrics(new Font(g.getFont().getFontName(), Font.PLAIN, fontSize));
	    // Determine the X coordinate for the text
	    int x = rectX + (rectW - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rectY + ((rectH - metrics.getHeight()) / 2) + metrics.getAscent();
	    g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, fontSize));
	    g.drawString(text, x, y);
	}
}
