import java.util.*;

public class OpeningBook {
	static ArrayList<Opening> openings;
	
	public static boolean sameBoard(char[][] a, char[][] b) {
		for(int i=0;i<8;++i) {
			for(int j=0;j<8;++j) {
				if (a[i][j] != b[i][j]) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	
	
	public static ArrayList<Point> checkOpeningBookForMove(char[][] board, int player) {
		for(Opening o : openings) {
			if (player == o.player && sameBoard(board, o.board)) {
				return o.move;
			}
		}
		return null;
	}
	
	public static void makeOpeningBook() {
		// hardcode lots of openings
		openings = new ArrayList<Opening>();
		char[][] board = 
		{
			{'r','n','b','q','k','b','n','r'}, 
			{'p','p','p','p','p','p','p','p'}, 
			{'.','.','.','.','.','.','.','.'}, 
			{'.','.','.','.','.','.','.','.'}, 
			{'.','.','.','.','.','.','.','.'}, 
			{'.','.','.','.','.','.','.','.'}, 
			{'P','P','P','P','P','P','P','P'}, 
			{'R','N','B','Q','K','B','N','R'}
		};
		ArrayList<Point> move = new ArrayList<Point>();
		move.add(new Point(1,4));
		move.add(new Point(2,4));
		int player = 1;
		Opening o = new Opening(board, move, player);
		openings.add(o);
	}
	
}

class Opening {
	char[][] board;
	ArrayList<Point> move;
	int player;
	public Opening(char[][] b, ArrayList<Point> m, int p) {
		this.board = b;
		this.move = m;
		this.player = p;
	}
}
