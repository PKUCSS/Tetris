////////
import java.util.*;
import java.io.*;
import java.math.*;
////////
//  HW_1,Tetris AI , ³ÂË¼Ë¶ £¬17000XXXXX
//2019.3
public class HW1_17000XXXXX extends Tetris {
	// enter your student id here
	public String id = new String("17000XXXXX");
	
	public boolean debug = false ;
	public boolean has_decided = false ;   // Has the path decided?
	public int start_x = 0 ;               // start position
	public int rotate_times = 0 ;		 // rotate times
	
	public double eval(boolean m[][]) {   // evaluation function
		if (debug) {                      // print the board for debugging
			for ( int y = 19 ; y >= 0 ; y--) {
				for ( int x = 0 ; x < 10 ; ++x ) {
					if(m[y][x] == true)
						System.out.printf("H") ;
					else System.out.printf(" ") ;
				}
				System.out.println("") ;
			}
			
			System.out.println("==========\n") ;
			System.out.println("") ;
		}
		
		
		double score = 0 ; 
		double w1 = -0.510066 ,w2 = 0.760666,w3 =-0.35663,w4 = -0.184483 ; // weights
		double ah = 0 ,cl = 0 ,holes = 0 ,bumps = 0 ;
		// aggregate heights,cleared lines,holes,bumps
		int tmp = 0 ;
		int height[] =new int[10] ;
		
		for ( int x = 0 ; x < 10 ; x++) {
			height[x] = 0 ;
			for ( int y = 19 ; y >= 0 ; y--) {
				if(m[y][x] == true ) {
					ah += y+1 ;
					height[x] = y + 1 ;
					tmp = y + 1 ;
					break ;
				}
			}
		}
		for (int x = 1 ; x < 10 ; ++x ) {
			bumps += Math.abs(height[x]-height[x-1]) ;
		}
		for ( int y =  0 ; y < 20 ; ++y ) {
			int c = 0 ;
			for (int x = 0 ; x < 10 ; ++x ) {
				if(m[y][x]) c++ ;
			}
			if(c == 10) cl++ ;
		}
		for (int x = 0 ; x < 10 ; x++ ) {
			for ( int y = 0 ; y < 20 ; ++y ) {
				boolean flag = true ;
				if(m[y][x] == false)
				for (int p = y + 1 ; p < 20 ; ++p ) {
					if (m[p][x] == true) {
						flag = false ;
					}
				}
				if (flag == false) holes++ ;
			}
		}
		if (debug) {
			System.out.printf("%f %f %f %f\n",ah,cl,holes,bumps) ;
			score = w1*ah + w2*cl + w3*holes + w4*bumps ;
			System.out.println(score) ;
		}
		return w1*ah + w2*cl + w3*holes + w4*bumps ;
	}
	
	public boolean[][] rotate(boolean[][] m) {
		boolean new_piece[][] = new boolean[4][4];
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				new_piece[y][x] = m[x][3-y];
			}
		}
		return new_piece ;
	}
	
	public void Choose_pos() {        // Choose aim position and generate the route
		//System.out.println("Choose pos function\n") ;
		boolean tmpboard[][] = getBoard();
		boolean tmppiece[][] = getPiece();
		for ( int y = 0 ; y < 4 ; ++y ) {
			for ( int x = 0 ; x < 4 ; ++x ) {
				if(tmppiece[y][x] == true ) tmpboard[piece_y-y][piece_x+x] = false ;
			}
		}
		double max_score = -1000000 ;
					// enumerate rotate_times and start_x and choose the best one
		for (int r = 0 ; r < 4 ; ++r ) {
			tmppiece = getPiece();
			for ( int i = 0 ; i < r ; ++i ) {
				tmppiece = rotate(tmppiece) ;
			}
			for ( int sx = -5 ; sx < 10 ; ++sx ) {
				tmpboard = getBoard() ;
				boolean valid = true ;
				for ( int y = 0 ; y < 4; ++y ) {
					for (int x = 0 ; x < 4 ; ++x ) {
						if (tmppiece[y][x] == true && (sx+x < 0 || sx + x > 9)) valid = false ;
					}
				}
				if (valid == false) continue ;
				int py = 19 ;
				while(true) {
					boolean able = true ;
					for (int y = 0 ; y < 4 ; ++y ) {
						for ( int x = 0 ; x < 4 ; ++x ) {
							if ((tmppiece[y][x] == true &&(py-y < 0 || sx +x < 0 ||  sx + x > 9)) || (tmppiece[y][x] == true && tmpboard[py-y][sx+x] == true)) 
								able = false ;
						}
					}
					if(able) {
						if( py == 0) break ;
						else {
							py-- ;
							continue ;
						}
					}
					else {
						py++ ;
						break ;
					}
			   }
			   for (int y = 0 ; y < 4 ; ++y) {
				   for ( int x = 0 ; x < 4 ; ++x) {
					   if(tmppiece[y][x] == true) tmpboard[py-y][sx+x] = true ;
				   }
			   }
			   double score = eval(tmpboard);
			   //System.out.printf("%d %d %f\n",sx,r,score) ;
			   if(score > max_score) {
				  if (debug) {
					  System.out.println("Choose!") ;
					  System.out.printf("%d %d",sx,r) ;
				  }
				   max_score = score ;
				   start_x = sx ;
				   rotate_times = r ;
			   }
 			}
		}
	}
	
	// #### My AI Robot Playing the Tetris Game
	public PieceOperator robotPlay() {
		// this is a random player ...
		
		if (piece_y == 23) {
			if (has_decided == false) {
				has_decided = true ;
				Choose_pos() ;
			} 
			if (rotate_times > 0 ) {
				rotate_times -= 1  ;
				return  PieceOperator.Rotate;
			}
			else return PieceOperator.Keep; 	

		}
		if (piece_y == 22 || piece_y == 21) {
			if(piece_x < start_x) return PieceOperator.ShiftRight;
			else if (piece_x >  start_x) return PieceOperator.ShiftLeft;
			else return PieceOperator.Keep;
		}
		if (piece_y < 21 ) {
			has_decided  = false ;
			start_x = 0 ;
			rotate_times = 0 ;
			return PieceOperator.Drop;
		}
		
		switch (new Random().nextInt(5)) {
			case 0: return PieceOperator.ShiftLeft;
			case 1: return PieceOperator.ShiftRight;
			case 2: return PieceOperator.Rotate;
			case 3: return PieceOperator.Drop;
		}
		return PieceOperator.Keep;
	}
}


