import java.util.HashMap;
import java.util.Stack;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;



public class Game extends JPanel {
	private Dimension _screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private double _windowWidth = _screenSize.getWidth();
    private double _windowHeight = _screenSize.getHeight();
    private WindowFrame windowFrame;
    private Graphics g;
   

	
	public Game() {
		createWindow();
		g = windowFrame.getGraphics();
	}
	//with the create window method within the game constructor,
	//we will be able to simplify the setup in the main method.
	private void createWindow() {
		windowFrame = WindowFrame.getInstance();
		
		
	}
	
	
	public static void main(String[] args) {
		
        //loop while until game stops running
		
	
		Game g = new Game();
      
       
        //put all states into statemap
		HashMap<String, State> stateMap = new HashMap<String, State>();
		stateMap.put("menu", new MainMenuState());
		stateMap.put("battle", new BattleState());
		stateMap.put("inventory", new InventoryMenuState());
		
		
		StateStack stateStack = new StateStack(stateMap);
		//the main menu is set as the initial state
		stateStack.push("menu");
		
		
		
		//At the end of the main method, there should be the game loop.
		//The update and render methods are called on the stateStack instance, 
		//so only the top element (the current state) should be updated and rendered.
		/*
		 while(true) {
			float elapsedTime = System.nanoTime();
			stateStack.update();
			stateStack.render();
		}
		*/
	}
}