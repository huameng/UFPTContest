import java.util.*;
class ANTICHESS {
	
	static char[][] board = new char[8][8];
	static char[][] state;
	static String fileName = "game729";
	static int turn;
	static ArrayList<Point> best_static;
	static int MAX_PLAYER;
	static boolean DEBUG_FLAG = true;
	static int EVAL_VERSION = 1;
	

	public static void main(String[] args){
		if (DEBUG_FLAG && args.length >= 1) EVAL_VERSION = Integer.parseInt(args[0]);

		Scanner in = new Scanner(System.in);
		int player = Integer.parseInt(in.nextLine());

		for(int i = 0;i<8;++i){
			String line = in.nextLine();
			for(int j = 0;j<8;++j){
				board[i][j] = line.charAt(j);
			}
		}
		state = board;
		String ans = makeMove(player);
		if (!DEBUG_FLAG) System.out.println(ans);
		
	}

	public static String makeMove(int player){
		MAX_PLAYER = player&1;
		alphabeta(6, Integer.MIN_VALUE, Integer.MAX_VALUE, player&1);


		if (best_static == null) System.exit(0);


		//make the move		
		Point FROM = best_static.get(0);
		Point TO = best_static.get(1);
		char tmp = state[FROM.x][FROM.y];
		state[FROM.x][FROM.y] = '.';
		state[TO.x][TO.y] = tmp;

		String ans = FROM.x + " " + FROM.y + " " + TO.x + " " + TO.y;
		if (DEBUG_FLAG) {
			System.out.println(1-player);
			printState(player);
		}
		
		//System.err.println("ANS = " + ans);	
		return ans;
	}
	public static void printState(int player) {
		//sSystem.out.println("Player " + player);
		for (int i = 0; i<state.length; ++i) {
			System.out.println(state[i]);
			System.err.println(state[i]);
		}

	}


	public static int alphabeta( int depth, int alpha, int beta, int player) {
		//printState(player);
 		
		//initialization
		if (depth == 0) return evalBoard(state,MAX_PLAYER);
		ArrayList<ArrayList<Point>> M = getMoves(player,state);
		if (M.size() == 0) return evalBoard(state,MAX_PLAYER);

		ArrayList<Point> best = null;	
		if (player == MAX_PLAYER) {	

			for (ArrayList<Point> A : M) {		

				
				Point FROM = A.get(0);
				Point TO = A.get(1);

				char tmpFROM = state[FROM.x][FROM.y];
				char tmpTO = state[TO.x][TO.y];
				state[FROM.x][FROM.y] = '.';
				state[TO.x][TO.y] = tmpFROM;

				int tmpalpha = alphabeta( depth-1, alpha,beta, 1-player);
				if (tmpalpha > alpha) {
					alpha = tmpalpha;
					best = A;
				}
				if (tmpalpha == alpha && best == null) best = A;

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
				//if (TO.x < 0 || TO.x > 7 || FROM.x < 0 || FROM.x > 7) continue;

				char tmpFROM = state[FROM.x][FROM.y];
				char tmpTO = state[TO.x][TO.y];
				state[FROM.x][FROM.y] = '.';
				state[TO.x][TO.y] = tmpFROM;

				int tmpbeta = alphabeta( depth-1, alpha, beta, 1-player);
				if (tmpbeta < beta) {
					beta = tmpbeta;
					best = A;
				}
				if (tmpbeta == beta && best == null) best = A;

				state[FROM.x][FROM.y] = tmpFROM;
				state[TO.x][TO.y] = tmpTO;
				if (beta <= alpha) break;
			}
			best_static = best;
			return beta;
		}
	}
	public static ArrayList<ArrayList<Point>> getMoves(int player, char [][] state) {
		return FindMoves.getMoves(player,state);
	} 

	public static int evalBoard(char [][] state, int player) {
		int count = (EVAL_VERSION == 2) 
			? 
			ChessUtility.eval2(state,player) 
			: 
			(EVAL_VERSION == 1) 
			? 
			ChessUtility.eval1(state, player) 
			: 
			ChessUtility.eval3(state,player);
		/*
		int count = 0;
		for (int i = 0; i<state.length; ++i) {
			for (int j = 0; j<state[0].length; ++j) {
				if (state[i][j] == '.') continue;
				if (state[i][j] >= 'a' && player != 1) count++;
				if (state[i][j] <= 'Z' && player == 1) count++;
			}
		}
		///*
		for (int i = 0; i<state.length; ++i) {
			for (int j = 0; j<state[0].length; ++j) {
				if (state[i][j] == '.') continue;
				if (state[i][j] >= 'a' && player == 1) count--;
				if (state[i][j] <= 'Z' && player != 1) count--;
			}
		}
		//*/
		return count;
	}


}

class FindMoves {
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
					/*if (x == 1 && inBounds(x+2, y)) {
						if (!hasPiece(x+2, y) && !foundCapture) {
							moves.add(makePair(x,y,x+2,y));
						}
					}*/
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
					/*if (x == 6 && inBounds(x-2, y)) {
						if (!hasPiece(x-2, y) && !foundCapture) {
							moves.add(makePair(x,y,x-2,y));
						}
					}*/
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
					int[] xsq = {-1,-1,-1,0,0,1,1,1};
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
									// System.out.println(x + " " + y + " --> " + newX + " " + newY);
									moves.add(makePair(x,y,newX,newY));
								}
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
			/*for(int i=0;i<8;++i) {
				for(int j=0;j<8;++j) {
					System.out.print(board[i][j]);
				}
				System.out.println();
			}
			System.out.println(realMoves);*/
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

class Point {
	int x, y; 
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return this.x + " " + this.y;
	}
	
	public boolean equals(Object o) {
		Point p = (Point)o;
		return (p.x == this.x && p.y == this.y);
	}
}

 class ChessUtility {

	public static int eval1(char[][] state, int player)
	{
		int myPiecesNum = findPieces(player,state).size();
		int enemyPiecesNum = findPieces(player,state).size();
		
		int diff = enemyPiecesNum - myPiecesNum;
		
		return diff;
	}


	public static int eval2(char[][] state, int player)
	{
		double diffCheckFactor = 2.0;
		double diffCountFactor = 1.0;
		
		Set<Point> myInCheck = findPiecesInCheck(player, state);
		List<Point> myPieces = findPieces(player, state);
		int myInCheckNum = myInCheck.size();
		int myNumPieces = myPieces.size();	// Lower = better
		
		int enemyPlayer = (player == 1) ? 2 : 1;
		Set<Point> enemyInCheck = findPiecesInCheck(enemyPlayer, state);
		List<Point> enemyPieces = findPieces(enemyPlayer, state);
		int enemyInCheckNum = enemyInCheck.size();
		int enemyNumPieces = enemyPieces.size();	// More = better
		
		int diffCount = enemyNumPieces - myNumPieces;	// higher = better
		int diffCheck = myInCheckNum - enemyInCheckNum;	// higher = better
		
		return (int)(diffCount*diffCountFactor) + (int)(diffCheck*diffCheckFactor);
	}
	
	public static int eval3(char[][] state, int player)
	{
		double diffCheckFactor = 2.0;
		double diffCountFactor = 1.0;
		
		Set<Point> myInCheck = findPiecesInCheck(player, state);
		List<Point> myPieces = findPieces(player, state);
		int myInCheckNum = myInCheck.size();
		int myNumPieces = myPieces.size();	// Lower = better
		
		int enemyPlayer = (player == 1) ? 2 : 1;
		Set<Point> enemyInCheck = findPiecesInCheck(enemyPlayer, state);
		List<Point> enemyPieces = findPieces(enemyPlayer, state);
		int enemyInCheckNum = enemyInCheck.size();
		int enemyNumPieces = enemyPieces.size();	// More = better
		
		int diffCount = enemyNumPieces - myNumPieces;	// higher = better
		int diffCheck = myInCheckNum - enemyInCheckNum;	// higher = better
		
		int score = 0;
		
		score += (int)(diffCount*diffCountFactor);
		score += (int)(diffCheck*diffCheckFactor);
		
		// Modify score based on types of pieces on the board
		int pieceTypeVal = 0;
		double pieceTypeFactor = 1.0;
		
		double goodVal = 1.0;
		double badVal = 2.0;
		
		for(Point p : myPieces)
		{
			char piece = state[p.x][p.y];
			switch(piece)
			{
				case 'p': case 'P': case 'k': case 'K': case 'n': case 'N':
					pieceTypeVal -= goodVal;
					break;
				case 'q': case 'Q': case 'r': case 'R': case 'b': case 'B':
					pieceTypeVal -= badVal;
					break;
			}
		}
		
		for(Point p : enemyPieces)
		{
			char piece = state[p.x][p.y];
			switch(piece)
			{
				case 'p': case 'P': case 'k': case 'K': case 'n': case 'N':
					pieceTypeVal += goodVal;
					break;
				case 'q': case 'Q': case 'r': case 'R': case 'b': case 'B':
					pieceTypeVal += badVal;
					break;
			}
		}
		
		score += (int)(pieceTypeVal*pieceTypeFactor);
		
		return score;
	}
	
	public static HashSet<Point> findPiecesInCheck(int player, char[][] board) {
		// IF YOU ARE COMBINING THIS WITH ANOTHER CLASS, REMEMBER TO REMOVE "FindMoves." BELOW
		ArrayList<ArrayList<Point>> theirMoves = FindMoves.getMoves(player == 1 ? 2 : 1, board);
		HashSet<Point> points = new HashSet<Point>();
		for(ArrayList<Point> a : theirMoves) {
			Point to = a.get(1);
			if (board[to.x][to.y] == '.') {
				return points;
			}
			else {
				points.add(to);
			}
		}
		return points;
	}
	
	public static ArrayList<Point> findPieces(int player, char[][] board) {
		ArrayList<Point> toRet = new ArrayList<Point>();
		for(int i=0;i<8;++i) {
			for(int j=0;j<8;++j) {
				if (board[i][j] == '.') {
					continue;
				}
				else if (player == 1 && board[i][j] >= 'a') {
					toRet.add(new Point(i,j));
				}
				else if (player == 2 && board[i][j] <= 'Z') {
					toRet.add(new Point(i,j));
				}
			}
		}
		return toRet;
	}
	
	public static int numPieces(char[][] state, int player)
	{
		int count1 = 0;
		int count2 = 0;
		for (int i = 0; i<state.length; ++i) {
			for (int j = 0; j<state[0].length; ++j) {
				if (state[i][j] == '.') continue;
				if (state[i][j] >= 'a' && state[i][j] <= 'z' && player== 1) count1++;
				if (state[i][j] >= 'A' && state[i][j] <= 'Z' && player == 2) count2++;
			}
		}
		
		if(player==1)
			return count1;
		else // player==2
			return count2;
	}
}


