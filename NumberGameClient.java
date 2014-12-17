/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package numbergameclient;

import GameApp.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

/**
 *
 * @author Administrator
 */
public class NumberGameClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
   		NumberGameClient aGame = new NumberGameClient( args );
	}
	
	// DATA
	//............................................................

	static NumberGame numberGameImp;
	private Scanner someInput;

	// CONSTRUCTOR
	//............................................................
	public NumberGameClient( String args[] )
	{
		this.someInput = new Scanner(System.in);
		BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));
		//this.tryAgain = false;

		try
		{

			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef =
				orb.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This is
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "NumberGame";
			numberGameImp = NumberGameHelper.narrow(ncRef.resolve_str(name));

			//System.out.println("Obtained a handle on server object: " + numberGameImp);
			
			boolean playGame = false;
			
			
			try
			{
				String play;
				boolean goodAnswer = false;
				int numOfLives;
				boolean inGame = true;
				
			do
			{
				
				do
				{
				// Prompt user if they want to play the game
					System.out.println("Would you like to play a game y/n?");
					play = userEntry.readLine();
							
						
					if (play.equals("y") || play.equals("Y")) {
						//if user answers yes then step out of loop
						goodAnswer = true;		
						playGame = true;
								
					}else if(play.equals("n") || play.equals("N")){
									
						//exit system
						System.out.println("Ok, suit yourself! BYE");
						System.exit(0);
					}else{
						System.out.println();
						System.out.println("Incorrect Input");
						System.out.println();
					}
								
				} while(goodAnswer == false);
		
			
				
						boolean firstTime = true;
						String guess;
						int numGuess;
						
						
						//set number of lives
						numOfLives = 3;
						
					do
					{
					
						String result;
						
						if (firstTime == true){
							// firstTime call
							firstTime = false;
							System.out.println(numberGameImp.playGame());
							guess = userEntry.readLine();
							numGuess = Integer.parseInt(guess);
							result = (numberGameImp.guessANumber(numGuess));
							System.out.println(result);
							numOfLives--;
							
							// check to see if game is over
							if (result.contains("Game Over")){
								//game over exit loop
								firstTime = true;
								numOfLives = 0;
							}
						}else{
							//not first time round
							guess = userEntry.readLine();
							numGuess = Integer.parseInt(guess);
							result = (numberGameImp.guessANumber(numGuess));
							System.out.println(result);
							//check to see if game is over
							
								numOfLives--;
							// check to see if game is over
							if (result.contains("Game Over")){
								//game over, exit loop
								firstTime = true;
								numOfLives = 0;
							}
						}
						
					}while(numOfLives > 0);
			}while(true);		
				}
				catch (Exception e)
				{
					System.out.println("ERROR");
					e.printStackTrace();
				}
		}
		catch (Exception e)
		{
			System.out.println("ERROR: " + e);
			e.printStackTrace();
		}

  } //EOM
 
} //EOC