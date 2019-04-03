////////
import java.util.*;
import java.io.*;

////////
public class Main {
	// ####
	
	public static void main(String [] args) throws IOException, InterruptedException {

			int sum = 0 ;
			for ( int i = 0 ; i < 100 ; ++i ) 
			{
				int seed = new Random().nextInt(128);
				Random rand = new Random(seed);
				HW1_17000XXXXX tetris = new HW1_17000XXXXX(); 
				
				int score = tetris.run(rand);
				System.out.println(tetris.id+": score="+score);
				sum += score ;
			}
			System.out.println("average score:"+sum/100) ;
		
	}
	
}


