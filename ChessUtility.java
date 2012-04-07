import java.util.*;

public class ChessUtility {
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
}
