import java.util.*;

public class FindMoves {
	static char[][] board;
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		char[][] board = new char[8][8];
		int player = in.nextInt();
		for(int i=0;i<8;++i) {
			String row = in.next();
			for(int j=0;j<8;++j) {
				board[i][j] = row.charAt(j);
			}
		}
		System.out.println(getMoves(player, board));
		
	}
	
	public static ArrayList<ArrayList<Point>> getMoves(int player, char[][] b) {
		board = b;
		int len = board.length;
		ArrayList<Point> ourPieces = new ArrayList<Point>();
		for(int i=0;i<len;++i) {
			for(int j=0;j<len;++j) {
				char c = board[i][j];
				if (player == 1) {
					if ((int)c >= 97) {
						ourPieces.add(new Point(i,j));
					}
				}
				else {
					if ((int)c < 97 && (int)c >= 65) {
						ourPieces.add(new Point(i,j));
					}
				}
			}
		}
		
		boolean foundCapture = false;
		ArrayList<ArrayList<Point>> moves = new ArrayList<ArrayList<Point>>();
		
		for(Point p : ourPieces) {
			int x = p.x;
			int y = p.y;
			switch (board[x][y]) {
				case 'p':
					if (inBounds(x+1, y)) {
						if (!hasPiece(x+1, y) && !foundCapture) {
							ArrayList<Point> toAdd = new ArrayList<Point>();
							toAdd.add(new Point(x,y));
							toAdd.add(new Point(x+1,y));
							moves.add(toAdd);
						}
					}
					if (x == 1 && inBounds(x+2, y)) {
						if (!hasPiece(x+2, y) && !foundCapture) {
							moves.add(makePair(x,y,x+2,y));
						}
					}
					if (inBounds(x+1, y+1)) {
						if (hasEnemyPiece(x+1, y+1, player)) {
							foundCapture = true;
							ArrayList<Point> toAdd = new ArrayList<Point>();
							toAdd.add(new Point(x,y));
							toAdd.add(new Point(x+1,y+1));
							moves.add(toAdd);
						}
					}
					if (inBounds(x+1, y-1)) {
						if (hasEnemyPiece(x+1, y-1, player)) {
							foundCapture = true;
							ArrayList<Point> toAdd = new ArrayList<Point>();
							toAdd.add(new Point(x,y));
							toAdd.add(new Point(x+1,y-1));
							moves.add(toAdd);
						}
					}
					break;
				case 'P':
					if (inBounds(x-1, y)) {
						if (!hasPiece(x-1, y) && !foundCapture) {
							ArrayList<Point> toAdd = new ArrayList<Point>();
							toAdd.add(new Point(x,y));
							toAdd.add(new Point(x-1,y));
							moves.add(toAdd);
						}
					}
					if (x == 6 && inBounds(x-2, y)) {
						if (!hasPiece(x-2, y) && !foundCapture) {
							moves.add(makePair(x,y,x-2,y));
						}
					}
					if (inBounds(x-1, y+1)) {
						if (hasEnemyPiece(x-1, y+1, player)) {
							foundCapture = true;
							ArrayList<Point> toAdd = new ArrayList<Point>();
							toAdd.add(new Point(x,y));
							toAdd.add(new Point(x-1,y+1));
							moves.add(toAdd);
						}
					}
					if (inBounds(x-1, y-1)) {
						if (hasEnemyPiece(x-1, y-1, player)) {
							foundCapture = true;
							ArrayList<Point> toAdd = new ArrayList<Point>();
							toAdd.add(new Point(x,y));
							toAdd.add(new Point(x-1,y-1));
							moves.add(toAdd);
						}
					}
					break;
				case 'n':
				case 'N':
					int[] xsn = {-2,-2,-1,-1,1,1,2,2};
					int[] ysn = {-1,1,-2,2,-2,2,-1,1};
					for(int i=0;i<xsn.length;++i) {
						int newX = x + xsn[i];
						int newY = y + ysn[i];
						if (inBounds(newX,newY)) {
							if (!hasPiece(newX, newY) || hasEnemyPiece(newX, newY, player)) {
								if (!foundCapture || hasEnemyPiece(newX, newY, player)){
									moves.add(makePair(x,y,newX,newY));
								}
								if (hasEnemyPiece(newX, newY, player)) {
									foundCapture = true;
								} 
							}
						}
					}
					break;
				case 'b':
				case 'B':
					int[] xsb = {-1,-1,1,1};
					int[] ysb = {-1,1,-1,1};
					for(int i=0;i<xsb.length;++i) {
						int curDist = 1;
						while(inBounds(x+xsb[i]*curDist, y+ysb[i]*curDist)) {
							int newX = x + xsb[i]*curDist;
							int newY = y + ysb[i]*curDist;
							++curDist;
							if (!hasPiece(newX, newY)) {
								if (!foundCapture) {
									moves.add(makePair(x,y,newX,newY));
								}
							}
							else {
								if (hasEnemyPiece(newX, newY, player)) {
									foundCapture = true;
									moves.add(makePair(x,y,newX,newY));
								}
								break;
							}
						}
					}
					break;
				case 'r':
				case 'R':
					int[] xsr = {-1,0,0,1};
					int[] ysr = {0,-1,1,0};
					for(int i=0;i<xsr.length;++i) {
						int curDist = 1;
						while(inBounds(x+xsr[i]*curDist, y+ysr[i]*curDist)) {
							int newX = x + xsr[i]*curDist;
							int newY = y + ysr[i]*curDist;
							++curDist;
							if (!hasPiece(newX, newY)) {
								if (!foundCapture) {
									moves.add(makePair(x,y,newX,newY));
								}
							}
							else {
								if (hasEnemyPiece(newX, newY, player)) {
									foundCapture = true;
									moves.add(makePair(x,y,newX,newY));
								}
								break;
							}
						}
					}
					break;
				case 'q':
				case 'Q':
					int[] xsq = {-1,-1,1,0,0,1,1,1};
					int[] ysq = {-1,0,1,-1,1,-1,0,1};
					for(int i=0;i<xsq.length;++i) {
						int curDist = 1;
						while(inBounds(x+xsq[i]*curDist, y+ysq[i]*curDist)) {
							int newX = x + xsq[i]*curDist;
							int newY = y + ysq[i]*curDist;
							++curDist;
							if (!hasPiece(newX, newY)) {
								if (!foundCapture) {
									moves.add(makePair(x,y,newX,newY));
								}
							}
							else {
								if (hasEnemyPiece(newX, newY, player)) {
									foundCapture = true;
									moves.add(makePair(x,y,newX,newY));
								}
								break;
							}
						}
					}
					break;
				case 'k':
				case 'K':
					int[] xsk = {-1,-1,-1,0,0,1,1,1};
					int[] ysk = {-1,0,1,-1,1,-1,0,1};
					for(int i=0;i<xsk.length;++i) {
						int curDist = 1;
						int newX = x + xsk[i];
						int newY = y + ysk[i];
						if (inBounds(newX, newY)) {
							if (!hasPiece(newX, newY)) {
								if (!foundCapture) {
									moves.add(makePair(x,y,newX,newY));
								}
							}
							else {
								if (hasEnemyPiece(newX, newY, player)) {
									foundCapture = true;
									moves.add(makePair(x,y,newX,newY));
								}
								break;
							}
						}
					}
					break;
				
			}
		}
		
		if (foundCapture) {
			ArrayList<ArrayList<Point>> realMoves = new ArrayList<ArrayList<Point>>();
			for(ArrayList<Point> a : moves) {
				Point from = a.get(0);
				Point to = a.get(1);
				if (hasEnemyPiece(to.x, to.y, player)) {
					realMoves.add(a);
				}
			}
			return realMoves;
		}
		else {
			return moves;
		}
		
	}
	
	
	public static ArrayList<Point> makePair(int x, int y, int newX, int newY) {
		ArrayList<Point> toRet = new ArrayList<Point>();
		toRet.add(new Point(x,y));
		toRet.add(new Point(newX, newY));
		return toRet;
	}
	
	public static boolean hasEnemyPiece(int x, int y, int player) {
		if (player == 1) {
			return ((int)board[x][y] >= (int)'A' && (int)board[x][y] <= (int)'Z');
		}
		else {
			return ((int)board[x][y] >= (int)'a' && (int)board[x][y] <= (int)'z');
		}
	}
	
	public static boolean hasPiece(int x, int y) {
		return board[x][y] != '.';
	}
	
	public static boolean inBounds(int x, int y) {
		return (x >= 0 && y >= 0 && x < 8 && y < 8);
	}
}
