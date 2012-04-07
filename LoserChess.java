import java.util.*;
import java.io.*;

public class LoserChess{
	static char[][] board = new char[8][8];
	static char[][] state;
	static String fileName = "blahblahZachIsAwesome729.txt";
	static int turn;
	static ArrayList<Point> best_static;
	static int MAX_PLAYER;


	public static void main(String[] args) throws Exception{
		Scanner in = new Scanner(System.in);
		int player = Integer.parseInt(in.nextLine());

		for(int i = 0;i<8;++i){
			String line = in.nextLine();
			for(int j = 0;j<8;++j){
				board[i][j] = line.charAt(j);
			}
		}

		File file = new File(fileName);

		if(file.exists()){
			//file.close();
			turn = readNumber();
			writeNumber(turn++);
		}
		else{
			//file.close();
			writeNumber(2);
			turn = 1;
		}

		System.out.println(makeMove(player));
	}

	public static String makeMove(int player){
		alphabeta(6, 0, Integer.MAX_VALUE, player&1);
		MAX_PLAYER = player;

		//make the move		
		Point FROM = best_static.get(0);
		Point TO = best_static.get(1);
		char tmp = state[FROM.x][FROM.y];
		state[FROM.x][FROM.y] = '.';
		state[TO.x][TO.y] = tmp;

		String ans = FROM.x + " " + FROM.y + " " + TO.x + " " + TO.y;
		
		
		System.err.println("ANS = " + ans);	
		return ans;
	}

	public static int readNumber() throws Exception{
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String line = br.readLine();
            int x = Integer.parseInt(line);
            br.close();
            return x;
    }
   
    public static void writeNumber(int x) throws Exception{
            FileWriter fw = new FileWriter(new File(fileName));
            fw.write(x);
            fw.flush();
            fw.close();
    }

    public static int alphabeta( int depth, int alpha, int beta, int player) {
		//printState(player);
		if (depth == 5) System.err.println("SCORE = " + evalBoard(state,player));

		if (depth == 0) return evalBoard(state,player);
		
		ArrayList<ArrayList<Point>> M = getMoves(player,state);
		ArrayList<Point> best = null;	
		if (player == MAX_PLAYER) {		

			for (ArrayList<Point> A : M) {		
				Point FROM = A.get(0);
				Point TO = A.get(1);
				if (TO.x < 0 || TO.x > 7 || FROM.x < 0 || FROM.x > 7) continue;

				char tmpFROM = state[FROM.x][FROM.y];
				char tmpTO = state[TO.x][TO.y];
				state[FROM.x][FROM.y] = '.';
				state[TO.x][TO.y] = tmpFROM;

				int tmpalpha = alphabeta( depth-1, alpha,beta, 1-player);
				if (tmpalpha >= alpha) {
					alpha = tmpalpha;
					best = A;
				}

				state[FROM.x][FROM.y] = tmpFROM;
				state[TO.x][TO.y] = tmpTO;
				if (beta <= alpha) break;
			}
			best_static = best;
			return alpha;
		} else {
			for (ArrayList<Point> A : M) {	
				Point FROM = A.get(0);
				Point TO = A.get(1);
				if (TO.x < 0 || TO.x > 7 || FROM.x < 0 || FROM.x > 7) continue;

				char tmpFROM = state[FROM.x][FROM.y];
				char tmpTO = state[TO.x][TO.y];
				state[FROM.x][FROM.y] = '.';
				state[TO.x][TO.y] = tmpFROM;

				int tmpbeta = alphabeta( depth-1, alpha, beta, 1-player);
				if (tmpbeta <= beta) {
					beta = tmpbeta;
					best = A;
				}

				state[FROM.x][FROM.y] = tmpFROM;
				state[TO.x][TO.y] = tmpTO;
				if (beta <= alpha) break;
			}
			best_static = best;
			return beta;
		}
	}

	public static int evalBoard(char [][] state, int player) {
		int count = 0;
		for (int i = 0; i<state.length; ++i) {
			for (int j = 0; j<state[0].length; ++j) {
				if (state[i][j] == '.') continue;
				if (state[i][j] >= 'a' && player == 1) count++;
				if (state[i][j] <= 'Z' && player != 1) count++;
			}
		}

		for (int i = 0; i<state.length; ++i) {
			for (int j = 0; j<state[0].length; ++j) {
				if (state[i][j] == '.') continue;
				if (state[i][j] >= 'a' && player != 1) count--;
				if (state[i][j] <= 'Z' && player == 1) count--;
			}
		}



		return count;
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
					if (inBounds(x, y+1)) {
						if (!hasPiece(x, y+1) && !foundCapture) {
							ArrayList<Point> toAdd = new ArrayList<Point>();
							toAdd.add(new Point(x,y));
							toAdd.add(new Point(x,y+1));
							moves.add(toAdd);
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
					if (inBounds(x-1, y+1)) {
						if (hasEnemyPiece(x-1, y+1, player)) {
							foundCapture = true;
							ArrayList<Point> toAdd = new ArrayList<Point>();
							toAdd.add(new Point(x,y));
							toAdd.add(new Point(x-1,y+1));
							moves.add(toAdd);
						}
					}
					break;
				case 'P':
					if (inBounds(x, y-1)) {
						if (!hasPiece(x, y-1) && !foundCapture) {
							ArrayList<Point> toAdd = new ArrayList<Point>();
							toAdd.add(new Point(x,y));
							toAdd.add(new Point(x,y-1));
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
					int[] xsk = {-1,-1,1,0,0,1,1,1};
					int[] ysk = {-1,0,1,-1,1,-1,0,1};
					for(int i=0;i<xsk.length;++i) {
						int curDist = 1;
						int newX = x + xsk[i];
						int newY = y + ysk[i];
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
		if (x < 0 || y < 0 || x > 7 || y > 7) return false;
		if (player == 1) {
			return ((int)board[x][y] >= (int)'A' && (int)board[x][y] <= (int)'Z');
		}
		else {
			return ((int)board[x][y] >= (int)'a' && (int)board[x][y] <= (int)'z');
		}
	}
	
	public static boolean hasPiece(int x, int y) {
		if (x < 0 || y < 0 || x > 7 || y > 7) return false;
		return board[x][y] != '.';
	}
	
	public static boolean inBounds(int x, int y) {
		return (x >= 0 && y >= 0 && x < 8 && y < 8);
	}
}
