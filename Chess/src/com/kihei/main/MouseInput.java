package com.kihei.main;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MouseInput extends MouseAdapter {
	
	public static int selR = -1;
	public static int selC = -1;
	List<List<Integer>> poss = new ArrayList<List<Integer>>();
	//poss.add(new ArrayList<Integer>(Arrays.asList(1, 2)));
	public static ArrayList<Integer> possR = new ArrayList<Integer>();
	public static ArrayList<Integer> possC = new ArrayList<Integer>();
	public static String mated = null;
	public static String turn = "W";
	public static boolean inCheck = false;
	public static boolean whiteCanKCastle = true;
	public static boolean whiteCanQCastle = true;
	public static boolean blackCanKCastle = true;
	public static boolean blackCanQCastle = true;
	public static boolean whiteCastled = false;
	public static boolean blackCastled = false;
	public static int pawnPromote = -1;
	public static String pawnPC = null;
	
	public void mousePressed(MouseEvent e) {
		// get the moose position
		int mx = e.getX();
		int my = e.getY();
		
		// the misery begins.. only if someone isn't mated yet-
		if (MouseInput.mated == null) {
			// starting x and y positions for checking each square
			int cX = ((984 - 800) / 2);
			int cY = ((961 - 800) / 2);
			
			// all the shit happens in this loop lmaoo
			for(int r = 0; r < 8; r++) {
				for(int c = 0; c < 8; c++) {
					if (mouseOver(mx, my, cX, cY, 100, 100)) { // it found the piece based on mouse position
						// System.out.println("(" + r + ", " + c + "): " + Display.board[r][c]);
						// System.out.println(Display.board[r][c] + " at " + position(r, c));
						
						if (pawnPromote == -1) { // ensuring there isn't a pawn gui to be handled
							if (selR == -1) { // there is nothing selected
								if (Display.board[r][c] == null) return; // not a piece -> return
								if (!Display.board[r][c].substring(0,1).equals(turn)) return; // not their turn -> return
								
								selR = r;
								selC = c;
								
								// finds the possible moves for the selected piece (includes checking for "walk into check")
								possibleMoves(Display.board[selR][selC], selR, selC, Display.board, true, oppTurn(turn));
								
							} else { // there is a piece selected and the user selected a position to go to
								// modify board array for new position IF it is a possible position for that piece
								for (int i = 0; i < possR.size(); i++) {
									if (r == possR.get(i) && c == possC.get(i)) {
										//System.out.println("Calculating..");
										
										String temp = Display.board[selR][selC];
										Display.board[selR][selC] = null;
										Display.board[r][c] = temp;
										
										// check for pawn promotion
										if (temp.equals("WP") && r == 0) {
											pawnPromote = c;
											pawnPC = "W";
										} else if (temp.equals("BP") && r == 7) {
											pawnPromote = c;
											pawnPC = "B";
										}
										
										// check for castle
										if (!inCheck) {
											if (!whiteCastled && temp.equals("WK")) {
												if (whiteCanQCastle && c == selC - 2) {
													Display.board[r][selC - 4] = null;
													Display.board[r][selC - 1] = "WR";
													whiteCanQCastle = false;
													whiteCastled = true;
												} else if (whiteCanKCastle && c == selC + 2) {
													Display.board[r][selC + 3] = null;
													Display.board[r][selC + 1] = "WR";
													whiteCanKCastle = false;
													whiteCastled = true;
												}
											}
											if (!blackCastled && temp.equals("BK")) {
												if (blackCanQCastle && c == selC - 2) {
													Display.board[r][selC - 4] = null;
													Display.board[r][selC - 1] = "BR";
													blackCanQCastle = false;
													blackCastled = true;
												} else if (blackCanKCastle && c == selC + 2) {
													Display.board[r][selC + 3] = null;
													Display.board[r][selC + 1] = "BR";
													blackCanKCastle = false;
													blackCastled = true;
												}
											}
										}
										
										// if a rook moves, disallow that side's castling
										if (temp.equals("WR")) {
											if (whiteCanQCastle && selC == 0) {
												whiteCanQCastle = false;
											} else if (whiteCanKCastle && selC == 7) {
												whiteCanKCastle = false;
											}
										} else if (temp.equals("BR")) {
											if (blackCanQCastle && selC == 0) {
												blackCanQCastle = false;
											} else if (blackCanKCastle && selC == 7) {
												blackCanKCastle = false;
											}
										}
										
										// System.out.println(move(temp.substring(1), position(r, c)));
										
										inCheck = checkForCheck(Display.board, turn);
										mated = checkForMate(inCheck, Display.board, turn);
										
										swapTurn();
										
										System.out.println("Done");
										
										//System.out.println((end - begin) / 1000.0 + " ms");
									}
								}
								
								// unselect piece and reset possible moves arrays
								selR = -1; selC = -1;
								possR = new ArrayList<Integer>();
								possC = new ArrayList<Integer>();
							}
						} else {
							// pawn is being promoted (gui is shown)
							if (c == pawnPromote) {
								if (pawnPC.equals("W") && r >= 0 && r <= 3) {
									switch (r) {
										case 0: Display.board[0][c] = "WQ"; break;
										case 1: Display.board[0][c] = "WN"; break;
										case 2: Display.board[0][c] = "WR"; break;
										case 3: Display.board[0][c] = "WB"; break;
									}
									
									pawnPromote = -1;
									pawnPC = null;
								} else if (pawnPC.equals("B") && r >= 4 && r <= 7) {
									switch (r) {
										case 7: Display.board[7][c] = "BQ"; break;
										case 6: Display.board[7][c] = "BN"; break;
										case 5: Display.board[7][c] = "BR"; break;
										case 4: Display.board[7][c] = "BB"; break;
									}
									
									pawnPromote = -1;
									pawnPC = null;
								}
							}
						}
					}
					cX += 100;
				}
				
				cY += 100;
				cX -= 800;
			}
			
			
			
		} else {
			// check for play again button if game is over (there was a mate)
			if (mouseOver(mx, my, (984 - 200) / 2, (961 - 75) / 2 + 413, 200, 75)) {
				Display.board = new String[8][8];
				//Game.setup();
				
				turn = "W";
			}
		}
	}
	
	public static boolean isE(String p) {
		if (p == null)
			return true;
		else
			return false;
	}
	
	public static boolean isW(String p) {
		if (p != null && p.substring(0, 1).equals("W"))
			return true;
		else
			return false;
	}
	
	public static boolean isB(String p) {
		if (p != null && p.substring(0, 1).equals("B"))
			return true;
		else
			return false;
	}
	
	private boolean inBoard(int r, int c) {
		if (r >= 0 && r <= 7 && c >= 0 && c <= 7) {
			return true;
		} else {
			return false;
		}
	}
	
	private String position(int r, int c) {
		int R = 8 - r;
		int C = c;
		String rC = "";
		switch (C) {
			case 0: rC = "a"; break;
			case 1: rC = "b"; break;
			case 2: rC = "c"; break;
			case 3: rC = "d"; break;
			case 4: rC = "e"; break;
			case 5: rC = "f"; break;
			case 6: rC = "g"; break;
			case 7: rC = "h"; break;
		}
		
		return rC + R;
	}
	
	private String move(String piece, String position) {
		if (piece.equals("P")) {
			return position;
		} else {
			return piece + position;
		}
	}
	
	private String[][] cloneArray(String[][] arr) {
		String [][] newArr = new String[8][8];
		for(int i = 0; i < arr.length; i++)
		    newArr[i] = arr[i].clone();
		
		return newArr;
	}
	
	private void possibleMoves(String piece, int sR, int sC, String[][] bd, boolean cfc, String pTurn) {
		int j = 1;
		
		switch (piece) {
			case "WP":
				if (inBoard(sR - 1, sC) && isE(bd[sR - 1][sC])) {
					possR.add(sR - 1);
					possC.add(sC);
				}
				if (sR == 6 && isE(bd[sR - 1][sC]) && isE(bd[sR - 2][sC])) { // first row of pawns -> double move
					possR.add(sR - 2);
					possC.add(sC);
				}
				// checking for diagonal capture
				if (inBoard(sR - 1, sC - 1) && isB(bd[sR - 1][sC - 1])) {
					possR.add(sR - 1);
					possC.add(sC - 1);
				}
				if (inBoard(sR - 1, sC + 1) && isB(bd[sR - 1][sC + 1])) {
					possR.add(sR - 1);
					possC.add(sC + 1);
				}
				break;
			case "BP":
				if (inBoard(sR + 1, sC) && isE(bd[sR + 1][sC])) {
					possR.add(sR + 1);
					possC.add(sC);
				}
				if (sR == 1 && isE(bd[sR + 1][sC]) && isE(bd[sR + 2][sC])) { // first row of pawns -> double move
					possR.add(sR + 2);
					possC.add(sC);
				}
				// checking for diagonal capture
				if (inBoard(sR + 1, sC - 1) && isW(bd[sR + 1][sC - 1])) {
					possR.add(sR + 1);
					possC.add(sC - 1);
				}
				if (inBoard(sR + 1, sC + 1) && isW(bd[sR + 1][sC + 1])) {
					possR.add(sR + 1);
					possC.add(sC + 1);
				}
				break;
			case "WR":
				for (int i = sR - 1; i >= 0; i--) {
					if (!isW(bd[i][sC])) {
						possR.add(i);
						possC.add(sC);
						if (isB(bd[i][sC])) break;
					} else {
						break;
					}
				}
				for (int i = sR + 1; i < 8; i++) {
					if (!isW(bd[i][sC])) {
						possR.add(i);
						possC.add(sC);
						if (isB(bd[i][sC])) break;
					} else {
						break;
					}
				}
				for (int i = sC - 1; i >= 0; i--) {
					if (!isW(bd[sR][i])) {
						possR.add(sR);
						possC.add(i);
						if (isB(bd[sR][i])) break;
					} else {
						break;
					}
				}
				for (int i = sC + 1; i < 8; i++) {
					if (!isW(bd[sR][i])) {
						possR.add(sR);
						possC.add(i);
						if (isB(bd[sR][i])) break;
					} else {
						break;
					}
				}
				break;
			case "BR":
				for (int i = sR - 1; i >= 0; i--) {
					if (!isB(bd[i][sC])) {
						possR.add(i);
						possC.add(sC);
						if (isW(bd[i][sC])) break;
					} else {
						break;
					}
				}
				for (int i = sR + 1; i < 8; i++) {
					if (!isB(bd[i][sC])) {
						possR.add(i);
						possC.add(sC);
						if (isW(bd[i][sC])) break;
					} else {
						break;
					}
				}
				for (int i = sC - 1; i >= 0; i--) {
					if (!isB(bd[sR][i])) {
						possR.add(sR);
						possC.add(i);
						if (isW(bd[sR][i])) break;
					} else {
						break;
					}
				}
				for (int i = sC + 1; i < 8; i++) {
					if (!isB(bd[sR][i])) {
						possR.add(sR);
						possC.add(i);
						if (isW(bd[sR][i])) break;
					} else {
						break;
					}
				}
				break;
			case "WN":
				possR.add(sR - 2); possC.add(sC - 1);
				possR.add(sR - 2); possC.add(sC + 1);
				
				possR.add(sR + 2); possC.add(sC - 1);
				possR.add(sR + 2); possC.add(sC + 1);
				
				possR.add(sR - 1); possC.add(sC - 2);
				possR.add(sR + 1); possC.add(sC - 2);
				
				possR.add(sR - 1); possC.add(sC + 2);
				possR.add(sR + 1); possC.add(sC + 2);
				
				break;
			case "BN":
				possR.add(sR - 2); possC.add(sC - 1);
				possR.add(sR - 2); possC.add(sC + 1);
				
				possR.add(sR + 2); possC.add(sC - 1);
				possR.add(sR + 2); possC.add(sC + 1);
				
				possR.add(sR - 1); possC.add(sC - 2);
				possR.add(sR + 1); possC.add(sC - 2);
				
				possR.add(sR - 1); possC.add(sC + 2);
				possR.add(sR + 1); possC.add(sC + 2);
				
				break;
			case "WB":
				j = 1;
				while(sR + j < 8 && sC + j < 8) {
					if (!isW(bd[sR + j][sC + j])) {
						possR.add(sR + j);
						possC.add(sC + j);
						if (isB(bd[sR + j][sC + j])) break;
					} else {
						break;
					}
					j++;
				}
				j = 1;
				while(sR - j >= 0 && sC - j >= 0) {
					if (!isW(bd[sR - j][sC - j])) {
						possR.add(sR - j);
						possC.add(sC - j);
						if (isB(bd[sR - j][sC - j])) break;
					} else {
						break;
					}
					j++;
				}
				j = 1;
				while(sR + j < 8 && sC - j >= 0) {
					if (!isW(bd[sR + j][sC - j])) {
						possR.add(sR + j);
						possC.add(sC - j);
						if (isB(bd[sR + j][sC - j])) break;
					} else {
						break;
					}
					j++;
				}
				j = 1;
				while(sR - j >= 0 && sC + j < 8) {
					if (!isW(bd[sR - j][sC + j])) {
						possR.add(sR - j);
						possC.add(sC + j);
						if (isB(bd[sR - j][sC + j])) break;
					} else {
						break;
					}
					j++;
				}
				break;
			case "BB":
				j = 1;
				while(sR + j < 8 && sC + j < 8) {
					if (!isB(bd[sR + j][sC + j])) {
						possR.add(sR + j);
						possC.add(sC + j);
						if (isW(bd[sR + j][sC + j])) break;
					} else {
						break;
					}
					j++;
				}
				j = 1;
				while(sR - j >= 0 && sC - j >= 0) {
					if (!isB(bd[sR - j][sC - j])) {
						possR.add(sR - j);
						possC.add(sC - j);
						if (isW(bd[sR - j][sC - j])) break;
					} else {
						break;
					}
					j++;
				}
				j = 1;
				while(sR + j < 8 && sC - j >= 0) {
					if (!isB(bd[sR + j][sC - j])) {
						possR.add(sR + j);
						possC.add(sC - j);
						if (isW(bd[sR + j][sC - j])) break;
					} else {
						break;
					}
					j++;
				}
				j = 1;
				while(sR - j >= 0 && sC + j < 8) {
					if (!isB(bd[sR - j][sC + j])) {
						possR.add(sR - j);
						possC.add(sC + j);
						if (isW(bd[sR - j][sC + j])) break;
					} else {
						break;
					}
					j++;
				}
				break;
			case "WQ":
				possibleMoves("WR", sR, sC, bd, cfc, pTurn);
				possibleMoves("WB", sR, sC, bd, cfc, pTurn);
				break;
			case "BQ":
				possibleMoves("BR", sR, sC, bd, cfc, pTurn);
				possibleMoves("BB", sR, sC, bd, cfc, pTurn);
				break;
			case "WK":
				possR.add(sR - 1); possC.add(sC - 1);
				possR.add(sR - 1); possC.add(sC);
				possR.add(sR - 1); possC.add(sC + 1);
		
				possR.add(sR + 1); possC.add(sC - 1);
				possR.add(sR + 1); possC.add(sC);
				possR.add(sR + 1); possC.add(sC + 1);
					
				possR.add(sR); possC.add(sC - 1);
				possR.add(sR); possC.add(sC + 1);
				
				if (!whiteCastled && !inCheck) {
					if (whiteCanKCastle && sC + 3 < 8 && bd[sR][sC + 3] != null && bd[sR][sC + 3].equals("WR") && isE(bd[sR][sC + 1]) && isE(bd[sR][sC + 2])) {
						possR.add(sR); possC.add(sC + 2);
					}
				
					if (whiteCanQCastle && sC - 4 >= 0 && bd[sR][sC - 4] != null && bd[sR][sC - 4].equals("WR") && isE(bd[sR][sC - 1]) && isE(bd[sR][sC - 2]) && isE(bd[sR][sC - 3])) {
						possR.add(sR); possC.add(sC - 2);
					}
				}
				
				break;
			case "BK":
				possR.add(sR - 1); possC.add(sC - 1);
				possR.add(sR - 1); possC.add(sC);
				possR.add(sR - 1); possC.add(sC + 1);
		
				possR.add(sR + 1); possC.add(sC - 1);
				possR.add(sR + 1); possC.add(sC);
				possR.add(sR + 1); possC.add(sC + 1);
					
				possR.add(sR); possC.add(sC - 1);
				possR.add(sR); possC.add(sC + 1);
				
				if (!blackCastled && !inCheck) {
					if (blackCanKCastle && sC + 3 < 8 && bd[sR][sC + 3] != null && bd[sR][sC + 3].equals("BR") && isE(bd[sR][sC + 1]) && isE(bd[sR][sC + 2])) {
						possR.add(sR); possC.add(sC + 2);
					}
				
					if (blackCanQCastle && sC - 4 >= 0 && bd[sR][sC - 4] != null && bd[sR][sC - 4].equals("BR") && isE(bd[sR][sC - 1]) && isE(bd[sR][sC - 2]) && isE(bd[sR][sC - 3])) {
						possR.add(sR); possC.add(sC - 2);
					}
				}
				
				break;
		}
		
		String scol = piece.substring(0, 1); // color of selected piece
		for (int i = 0; i < possR.size(); i++) {
			// is it not in the board? then remove it from the possible moves
			// if it is on the board, check if it's the same color and remove
			if (!inBoard(possR.get(i), possC.get(i))) {
				possR.remove(i); possC.remove(i); i--;
			} else if ((scol.equals("W") && isW(bd[possR.get(i)][possC.get(i)])) || (scol.equals("B") && isB(bd[possR.get(i)][possC.get(i)]))) {
				possR.remove(i); possC.remove(i); i--;
			}
		}
		
		if (cfc) {
			// check if you are "walking into check" - creates a temp board of each possible move of the selected piece and checks for check
			// if you are, remove that possible move
			for(int i = 0; i < possR.size(); i++) {
				String[][] tempBoard = cloneArray(bd);
				
				String temp = tempBoard[sR][sC];
				tempBoard[sR][sC] = null;
				tempBoard[possR.get(i)][possC.get(i)] = temp;
			
				// System.out.println("If " + temp + " moves to " + position(possR.get(i), possC.get(i)));
				boolean checkFound = checkForCheck(tempBoard, pTurn);
				if (checkFound) {
					possR.remove(i); possC.remove(i);
					i--;
				}
			}
		}
	}
	
	public boolean checkForCheck(String[][] bd, String color) {
		int[] king = new int[2];
		String kC = oppTurn(color);
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (bd[r][c] != null && bd[r][c].equals(kC + "K")) {
					king[0] = r;
					king[1] = c;
				}
			}
		}
		
		ArrayList<Integer> possRT = new ArrayList<Integer>();
		ArrayList<Integer> possCT = new ArrayList<Integer>();
		for (int i = 0; i < possR.size(); i++) {
			possRT.add(possR.get(i)); possCT.add(possC.get(i));
		}
		possR = new ArrayList<Integer>();
		possC = new ArrayList<Integer>();
		
		boolean checkFound = false;
		
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (bd[r][c] != null && bd[r][c].substring(0, 1).equals(color)) {
					possibleMoves(bd[r][c], r, c, bd, false, null);
					
					for (int i = 0; i < possR.size(); i++) {
						if (possR.get(i) == king[0] && possC.get(i) == king[1]) {
							checkFound = true;
						}
					}
					
					possR = new ArrayList<Integer>();
					possC = new ArrayList<Integer>();
				}
			}
		}
		
		for (int i = 0; i < possRT.size(); i++) {
			possR.add(possRT.get(i));
			possC.add(possCT.get(i));
		}
		
		return checkFound;
	}
	
	public String checkForMate(boolean inCheck, String[][] bd, String color) {
		if (inCheck) {
			// System.out.println("Check found");
			
			String opp = oppTurn(color);
			boolean mate = true;
			
			ArrayList<Integer> possRT = new ArrayList<Integer>();
			ArrayList<Integer> possCT = new ArrayList<Integer>();
			for (int i = 0; i < possR.size(); i++) {
				possRT.add(possR.get(i)); possCT.add(possC.get(i));
			}
			possR = new ArrayList<Integer>();
			possC = new ArrayList<Integer>();
			
			for (int r = 0; r < 8; r++) {
				if (mate == false) { break; }
				
				for (int c = 0; c < 8; c++) {
					if (mate == false) { break; }
					
					if (bd[r][c] != null && bd[r][c].substring(0, 1).equals(opp)) {
						possibleMoves(bd[r][c], r, c, bd, true, color);
						
						// for (int i = 0; i < possR.size(); i++) {
							// System.out.println(bd[r][c] + "" + position(possR.get(i), possC.get(i)));
						// }
						
						if (possR.size() > 0) {
							mate = false;
							// System.out.println(opp + " king can get out of check with " + bd[r][c] + position(possR.get(0), possC.get(0)));
						}
						
						possR = new ArrayList<Integer>();
						possC = new ArrayList<Integer>();
					}
				}
			}
			
			for (int i = 0; i < possRT.size(); i++) {
				possR.add(possRT.get(i));
				possC.add(possCT.get(i));
			}
			
			if (mate) {
				return color;
			} else {
				return null;
			}
			
		} else {
			// System.out.println("No check found.");
			return null;
		}
	}
	
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if (mx > x && mx < x + width && my > y && my < y + height)
			return true;
		else
			return false;
	}
	
	private void swapTurn() {
		if (turn.equals("W"))
			turn = "B";
		else
			turn = "W";
	}
	
	private String oppTurn(String t) {
		if (t.equals("W"))
			return "B";
		else
			return "W";
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		
	}
	
}
