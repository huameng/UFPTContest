import java.util.*;

public class ChessUtility {

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
		
		List<Point> myInCheck = findPiecesInCheck(player, state);
		List<Point> myPieces = findPieces(player, state);
		int myInCheckNum = myInCheck.size();
		int myNumPieces = myPieces.size();	// Lower = better
		
		int enemyPlayer = (player == 1) ? 2 : 1;
		List<Point> enemyInCheck = findPiecesInCheck(enemyPlayer, state);
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
		
		List<Point> myInCheck = findPiecesInCheck(player, state);
		List<Point> myPieces = findPieces(player, state);
		int myInCheckNum = myInCheck.size();
		int myNumPieces = myPieces.size();	// Lower = better
		
		int enemyPlayer = (player == 1) ? 2 : 1;
		List<Point> enemyInCheck = findPiecesInCheck(enemyPlayer, state);
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
		double badVal = 2.5;
		
		for(Point p : myPieces)
		{
			char piece = state[p.x][p.y];
			switch(piece)
			{
				case 'p': case 'P': case 'k': case 'K': case 'n': case 'N':
					pieceTypeVal += goodVal;
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
					pieceTypeVal -= goodVal;
					break;
				case 'q': case 'Q': case 'r': case 'R': case 'b': case 'B':
					pieceTypeVal += badVal;
					break;
			}
		}
		
		score += (int)(pieceTypeVal*pieceTypeFactor);
		
		return score;
	}
	
	public static ArrayList<Point> findPiecesInCheck(int player, char[][] board) {
		// IF YOU ARE COMBINING THIS WITH ANOTHER CLASS, REMEMBER TO REMOVE "FindMoves." BELOW
		ArrayList<ArrayList<Point>> theirMoves = FindMoves.getMoves(player == 1 ? 2 : 1, board);
		ArrayList<Point> points = new ArrayList<Point>();
		for(ArrayList<Point> a : theirMoves) {
			Point to = a.get(1);
			if (board[to.x][to.y] == '.') {
				return points;
			}
			else {
				boolean alreadyThere = false;
				for(Point p : points) {
					if (p.x == to.x && p.y == to.y) {
						alreadyThere = true;
						break;
					}
				}
				if (!alreadyThere) {
					points.add(to);
				}
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
