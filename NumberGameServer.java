/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package numbergameserver;

import GameApp.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

/**
 *
 * @author Administrator
 */
class NumberGameImpl extends NumberGamePOA
{
  private ORB orb;

  private int aNumber;
  private int lives;

  public void setORB(ORB orb_val)
  {
    orb = orb_val;
  }

  // implement playGame() method
  public String playGame()
  {
	aNumber = ( 1 + (int) (Math.random() * 10) );
	lives = 3;
	return ("New Game. You have " + lives + "lives. Guess a number between 1-10...");
  }

  // implement getLives() method
  public int getLives()
  {
	return this.lives;
  }

  // implement getLives() method
  public int getNumber()
  {
	return this.aNumber;
  }

    // implement guessANumber() method
  public String guessANumber( int aGuess)
  {
	if (lives == 0){
		return ("Game Over! The number was " + aNumber);
	}else if(guessANumberReturnTrueOrFalse(aGuess) == false){
		lives--;
		return (aGuess + " is Incorrect! You have " + lives + " remaining..");
	}else {
		return (aGuess + " is Correct! Game Over, you Win!!");
	}
  }//EOM

  // implement guessANumberReturnLives() method
  public int guessANumberReturnLives( int aGuess)
  {
	if (guessANumberReturnTrueOrFalse(aGuess) == false){
		lives--;
		
	}
	return lives;
  }

  // implement guessANumberReturnTrueOrFalse() method
  public boolean guessANumberReturnTrueOrFalse( int aGuess)
  {
	if (aGuess == aNumber){
		return true;
	}else{
		return false;
	}
  }

}//EOC


public class NumberGameServer {

  public static void main(String args[]) {
		try
		{
			//create and initialize the ORB
			ORB orb = ORB.init(args, null);
			
			//get reference to rootpoa & activate the POAManager
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			
			//create servant and register it with the ORB
			NumberGameImpl numberGameImp = new NumberGameImpl();
			numberGameImp.setORB(orb);
			
			//get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(numberGameImp);
			NumberGame href = NumberGameHelper.narrow(ref);
			
			//get the root naming context
			org.omg.CORBA.Object objRef =
				orb.resolve_initial_references("NameService");
			//Use NamingContextExt which is part of the Interoperable
			// Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			//bind the Object Reference in Naming
			String name = "NumberGame";
			NameComponent path[] = ncRef.to_name( name );
			ncRef.rebind(path, href);
			
			System.out.println("NumberGameServer ready and waiting....");
			
			//wait for invocations from clients
			orb.run();
		
		}
		catch (Exception e)
		{
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}
		System.out.println("NumberGameServer Exiting.....");
  }
}
