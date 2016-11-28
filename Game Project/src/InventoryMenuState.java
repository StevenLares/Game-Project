import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class InventoryMenuState extends JPanel implements State {

	Player player;
	PartyMember pm1, pm2, pm3;
	Character[] party;
	int characterIndex = 0;
	int partySize;
	Inventory inv;

	int invIndex = 0;

	BufferedImage arrow, smallArrow, biggerArrow;
	int cursorX;
	int cursorY;
	boolean yesSelected = true;
	boolean infoSelected;
	boolean itemSelected;
	boolean itemUsed;
	boolean characterSelected;
	WindowFrame frame = WindowFrame.getInstance();
	int windowWidth = frame.getWidth();
	int windowHeight = frame.getHeight();
	StateMapSingleton stateMap = StateMapSingleton.getInstance();
	StateStackSingleton stateStack = StateStackSingleton.getInstance();
	String currentMenu;
	String[] menus = {"Items", "Equip", "Status", "Save", "Settings"};
	Color textColor = Color.WHITE;
	Color backgroundColor = Color.BLACK;
	int settingOption;
	int colorOption;

	public InventoryMenuState(Player p, PartyMember pm1, PartyMember pm2, PartyMember pm3, Inventory i) {
		player = p;
		this.pm1 = pm1;
		this.pm2 = pm2;
		this.pm3 = pm3;
		party = new Character[] {player, pm1, pm2, null};
		partySize = 3;
		inv = i;

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT 
						|| e.getKeyCode() == KeyEvent.VK_D) {
					System.out.println("Right key pressed");
					infoSelected = false;
					rightPressed();
					System.out.println("char: " + characterIndex);
					System.out.println("inv: " + invIndex);
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT
						|| e.getKeyCode() == KeyEvent.VK_A) {
					System.out.println("Left key pressed");
					infoSelected = false;
					leftPressed();
					System.out.println("char: " + characterIndex);
					System.out.println("inv: " + invIndex);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_W) {
					System.out.println("Up key pressed");
					infoSelected = false;
					upPressed();
					System.out.println("char: " + characterIndex);
					System.out.println("inv: " + invIndex);
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN
						|| e.getKeyCode() == KeyEvent.VK_S) {
					System.out.println("Down key pressed");
					infoSelected = false;
					downPressed();
					System.out.println("char: " + characterIndex);
					System.out.println("inv: " + invIndex);
				}
				if	(e.getKeyCode() == KeyEvent.VK_ENTER
						|| e.getKeyCode() == KeyEvent.VK_SPACE){
					System.out.println("Enter key pressed");
					infoSelected = false;
					select();
					System.out.println("char: " + characterIndex);
					System.out.println("inv: " + invIndex);
				}
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE
						|| e.getKeyCode() == KeyEvent.VK_CAPS_LOCK){
					System.out.println("Backspace key pressed");
					infoSelected = false;
					back();
					System.out.println("char: " + characterIndex);
					System.out.println("inv: " + invIndex);
				}
				if (e.getKeyCode() == KeyEvent.VK_I) {
					System.out.println("'i' pressed");
					info();
				}
				render();
			}
		});

		this.setFocusable(true);

		try {
			arrow = ImageIO.read(new File("images/small-arrow.png"));
			smallArrow = ImageIO.read(new File("images/smallest-arrow.png"));
			biggerArrow = ImageIO.read(new File("images/arrow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.addNotify();
	}

	public void addNotify() {
		super.addNotify();
		requestFocus();
	}

	@Override
	public void update() {

	}

	@Override
	public void render() {
		repaint();
	}

	public void paintComponent(Graphics g) {
		Font large = new Font("Comic sans MS", Font.PLAIN, 32);
		Font medium = new Font("Comic sans MS", Font.PLAIN, 24);
		Font small = new Font("Comic sans MS", Font.PLAIN, 20);

		g.setColor(backgroundColor);
		g.fillRect(0, 0, windowWidth, windowHeight);	//background
		g.setColor(textColor);
		g.drawRect(5, 75, 250, 344);					//menu selection
		g.drawRect(5, 5, 1013, 65); 					//location
		g.drawRect(5, 423, 250, 147);					//money and game time
		g.drawRect(260, 75, 758, 495);					//party members or items

		//draws current location
		g.setFont(large);
		g.drawString("Strawberry Fields", 385, 50);

		//draws submenu selections
		g.setFont(small);
		g.drawString("Items", 74, 150);
		g.drawString("Equip", 74, 200);
		g.drawString("Status", 74, 250);
		g.drawString("Save", 74, 300);
		g.drawString("Settings", 74, 350);

		//draws money and in-game time
		g.drawString("Money: ", 30, 475);
		g.drawString("$" + inv.getMoney(), 100, 475);
		//g.drawString("Time: ", 30, 525);

		//draws party
		int count = 0;
		while(count < partySize) {
			Character c = party[count];
			g.drawImage(c.getMenuSprite(), 360, 95 + 120*count, null);
			g.drawRect(360, 95 + 120*count, 100, 100);
			g.setFont(large);
			g.drawString(c.getName(), 475, 125 + 120*count);
			g.setFont(medium);
			g.drawString("HP: " + c.getHealth() + "/" + c.getTotalHealth() , 475, 160 + 120*count);
			g.drawString("MP: " + c.getMagic() + "/" + c.getTotalMagic() , 475, 190 + 120*count);
			g.drawString("Level: " + c.getLevel(), 745, 125 + 120*count);
			g.drawString("EXP: " + c.getExp(), 745, 160 + 120*count);
			g.drawString("Next Level: ", 745, 190 + 120*count);
			count++;
		}

		if(currentMenu.equals("Main")) {
			g.drawImage(arrow, 25, (128 + cursorY), null);
		}
		else if(currentMenu.equals("Items")) {
			g.setColor(backgroundColor);
			g.fillRect(260, 75, 758, 495);
			g.setColor(textColor);
			g.drawRect(260, 75, 758, 495);

			int x = 330;
			int y = 125;
			for(int j = 0; j < inv.getNumItems(); j++) {
				if(j%2 == 0) {
					g.drawString(inv.getItem(j).getName(), x, y);
					g.drawString("x" + inv.getItemAmount(j), x + 280, y);
					x = 700;
					continue;
				}
				g.drawString(inv.getItem(j).getName(), x, y);
				g.drawString("x" + inv.getItemAmount(j), x + 270, y);
				x = 330;
				y += 50;
			}
			g.drawImage(arrow, 25, 128, null);	

			if(infoSelected && invIndex < inv.getNumItems()) {
				g.setColor(backgroundColor);
				g.fillRect(415, 200, 450, 250);
				g.setColor(textColor);
				g.drawRect(415, 200, 450, 250);
				
				g.drawString(inv.getItem(invIndex).getName(), 600, 280);
				//draw item desription	
			}
			else if(itemSelected) {
				g.setColor(backgroundColor);
				g.fillRect(260, 75, 758, 495);
				g.setColor(textColor);
				g.drawRect(260, 75, 758, 495);

				g.setFont(large);
				g.drawString("Give to...", 300, 135);

				//draws party
				count = 0;
				while(count < partySize) {
					Character c = party[count];
					g.drawImage(c.getSmallMenuSprite(), 360, 170 + 100*count, null);
					g.drawRect(360, 170 + 100*count, 80, 80);
					g.setFont(medium);
					g.drawString(c.getName(), 475, 190 + 100*count);
					g.setFont(small);
					g.drawString("HP: " + c.getHealth() + "/" + c.getTotalHealth() , 475, 220 + 100*count);
					g.drawString("MP: " + c.getMagic() + "/" + c.getTotalMagic() , 475, 245 + 100*count);
					count++;
				}
				g.setFont(medium);
				g.drawImage(biggerArrow, (280 + cursorX), (187 + cursorY), null);
			}
			else if(itemUsed) {
				g.setColor(backgroundColor);
				g.fillRect(260, 75, 758, 495);
				g.setColor(textColor);
				g.drawRect(260, 75, 758, 495);
				g.setColor(backgroundColor);
				g.fillRect(415, 200, 450, 250);
				g.setColor(textColor);
				g.drawRect(415, 200, 450, 250);

				Consumable cons = inv.use(invIndex, party[characterIndex]);
				g.setFont(medium);
				g.drawString(cons.getName(), 500, 300);
				g.drawString("Restored " + cons.getRestoreAmt() + " HP", 500, 360);
				g.setFont(small);
			}
			else 
				g.drawImage(arrow, (280 + cursorX), (103 + cursorY), null);

		}
		else if(currentMenu.equals("Equip")) {
			g.setColor(backgroundColor);
			g.fillRect(260, 75, 758, 495);
			g.setColor(textColor);
			g.drawRect(260, 75, 758, 495);
			g.drawImage(arrow, 25, 178, null);
			g.drawImage(biggerArrow, (280 + cursorX), (120 + cursorY), null);

			//draws party
			count = 0;
			while(count < partySize) {
				Character c = party[count];
				g.drawImage(c.getMenuSprite(), 360, 95 + 120*count, null);
				g.drawRect(360, 95 + 120*count, 100, 100);
				g.setFont(large);
				g.drawString(c.getName(), 475, 125 + 120*count);
				g.setFont(medium);
				if(c.getWeapon() != null) 
					g.drawString("Weapon: " + c.getWeapon().getName(), 475, 160 + 120*count);
				else 
					g.drawString("Weapon:", 475, 160 + 120*count);
				if(c.getArmor() != null) 
					g.drawString("Armor: " + c.getArmor().getName(), 475, 190 + 120*count);
				else
					g.drawString("Armor:", 475, 190 + 120*count);
				count++;
			}

			if(characterSelected) {
				g.setColor(backgroundColor);
				g.fillRect(260, 75, 758, 495);
				g.setColor(textColor);
				g.drawRect(260, 75, 758, 495);

				int x = 330;
				int y = 125;

				for(int j = 0; j < inv.getNumEquip(); j++) {
					if(j%2 == 0) {
						g.drawString(inv.getEquip(j).getName(), x, y);
						g.drawString("x" + inv.getEquipAmount(j), x + 280, y);
						x = 700;
						continue;
					}
					g.drawString(inv.getEquip(j).getName(), x, y);
					g.drawString("x" + inv.getEquipAmount(j), x + 270, y);
					x = 330;
					y += 50;
				}
				g.drawImage(arrow, (280 + cursorX), (103 + cursorY), null);
				if(infoSelected && invIndex < inv.getNumEquip()) {
					g.setColor(backgroundColor);
					g.fillRect(415, 200, 450, 250);
					g.setColor(textColor);
					g.drawRect(415, 200, 450, 250);

					g.drawString(inv.getEquip(invIndex).getName(), 600, 280);
					//draw item desription
				}
			}
			else if(itemSelected) {
				g.setColor(backgroundColor);
				g.fillRect(260, 75, 758, 495);
				g.setColor(textColor);
				g.drawRect(260, 75, 758, 495);
				g.setColor(backgroundColor);
				g.fillRect(415, 200, 450, 250);
				g.setColor(textColor);
				g.drawRect(415, 200, 450, 250);

				g.setFont(large);
				g.drawString("Equip " + inv.getEquip(invIndex).getName() + "?", 545, 290);
				g.setFont(medium);
				g.drawString("Yes", 540, 400);
				g.drawString("No", 700, 400);
				g.setFont(small);
				g.drawImage(arrow, (500 + cursorX), 377, null);
			}
			else if(itemUsed) {
				g.setColor(backgroundColor);
				g.fillRect(260, 75, 758, 495);
				g.setColor(textColor);
				g.drawRect(260, 75, 758, 495);
				g.setColor(backgroundColor);
				g.fillRect(415, 200, 450, 250);
				g.setColor(textColor);
				g.drawRect(415, 200, 450, 250);

				g.setFont(medium);
				g.drawString("Equipped " + inv.getEquip(invIndex).getName(), 550, 300);
				inv.equip(invIndex, party[characterIndex]);
				g.setFont(small);
			}
		}
		else if(currentMenu.equals("Status")) {
			g.drawImage(arrow, 25, 228, null);
			g.drawImage(biggerArrow, (280 + cursorX), (120 + cursorY), null);
		
			if(characterSelected) {
				g.setColor(backgroundColor);
				g.fillRect(260, 75, 758, 495);
				g.setColor(textColor);
				g.drawRect(260, 75, 758, 495);

				Character c = party[characterIndex];
				g.drawImage(c.getMenuSprite(), 310, 120, null);
				g.drawRect(310, 120, 100, 100);
				g.setFont(large);
				g.drawString(c.getName(), 430, 150);
				g.setFont(medium);
				g.drawString("HP: " + c.getHealth() + "/" + c.getTotalHealth() , 430, 185);
				g.drawString("MP: " + c.getMagic() + "/" + c.getTotalMagic() , 430, 215);
				g.drawString("Level: " + c.getLevel(), 700, 150);
				g.drawString("EXP: " + c.getExp(), 700, 185);
				g.drawString("Next Level: ", 700, 215);

				g.drawString("Strength:", 310, 310);
				g.drawString(c.getStr() + "", 560, 310);
				g.drawString("Defense:", 310, 370);
				g.drawString(c.getDef() + "", 560, 370);
				g.drawString("Magic Strength:", 310, 430);
				g.drawString(c.getMagicStr() + "", 560, 430);
				g.drawString("Magic Defense:", 310, 490);
				g.drawString(c.getMagicDef() + "", 560, 490);

				g.drawString("Weapon:", 700, 370);
				g.drawString("Armor:", 700, 430);
				if(c.getWeapon() != null) 
					g.drawString(c.getWeapon().getName() + "", 820, 370);				
				if(c.getArmor() != null) 
					g.drawString(c.getArmor().getName() + "", 820, 430);
			}
		}
		else if(currentMenu.equals("Save")) {
			g.setColor(backgroundColor);
			g.fillRect(260, 75, 758, 495);
			g.setColor(textColor);
			g.drawRect(260, 75, 758, 495);
			g.setColor(backgroundColor);
			g.fillRect(415, 200, 450, 250);
			g.setColor(textColor);
			g.drawRect(415, 200, 450, 250);

			g.setFont(large);
			g.drawString("Would You Like To Save?", 450, 255);
			g.drawString("Yes", 550, 330);
			g.drawString("No", 550, 395);

			g.drawImage(arrow, 25, 278, null);
			g.drawImage(biggerArrow, 450, (295 + cursorY), null);
		}
		else if(currentMenu.equals("Settings")) {
			g.setColor(backgroundColor);
			g.fillRect(260, 75, 758, 495);
			g.setColor(textColor);
			g.drawRect(260, 75, 758, 495);

			g.drawImage(arrow, 25, 328, null);
			g.drawImage(arrow, 280, (110 + cursorY), null);
			
			g.drawString("Text color:", 325, 133);
				g.drawString("Red", 400, 183);
				g.drawString("Blue", 500, 183);
				g.drawString("Green", 600, 183);
				g.drawString("Yellow", 700, 183);
				g.drawString("Black", 800, 183);
				g.drawString("White", 900, 183);
			g.drawString("Background color:", 325, 233);
				g.drawString("Red", 400, 283);
				g.drawString("Blue", 500, 283);
				g.drawString("Green", 600, 283);
				g.drawString("Yellow", 700, 283);
				g.drawString("Black", 800, 283);
				g.drawString("White", 900, 283);
			if(settingOption != 0) 
				g.drawImage(smallArrow, (378 + cursorX), (168 + cursorY), null);
		}
	}

	@Override
	public void onEnter() { 
		currentMenu = new String("Main");
		update();
		render();
	}

	@Override
	public void onExit() {

	}

	private void info() {
		if(infoSelected)
			infoSelected = false;
		else
			infoSelected = true;	
	}
	private void select() {
		if(currentMenu.equals("Main")) {
			currentMenu = menus[cursorY/50];
			cursorX = 0;
			cursorY = 0;
			invIndex = 0;
			characterIndex = 0;
		}
		else if(currentMenu.equals("Items")) {
			if(itemSelected && party[characterIndex] != null) {
				itemSelected = false;
				itemUsed = true;
			}
			else if(itemUsed) {
				cursorX = 0;
				cursorY = 0;
				invIndex = 0;
				characterIndex = 0;
				itemUsed = false;
			}
			else if(inv.getItem(invIndex) != null && party[characterIndex] != null) {
				itemSelected = true;
				cursorX = 0;
				cursorY = 0;
			}
		}
		else if(currentMenu.equals("Equip")) {
			if(characterSelected) {
				if(inv.getEquip(invIndex) == null) {}
				else {
					characterSelected = false;
					itemSelected = true;
					cursorX = 0;
					cursorY = 0;
				}
			}
			else if(itemSelected) {
				if(yesSelected) {
					itemSelected = false;
					itemUsed = true;
					cursorX = 0;
				}
				else {
					cursorX = 0;
					invIndex = 0;
					itemSelected = false;
					characterSelected = true;
					yesSelected = true;
				}
			}
			else if(itemUsed) {
				cursorX = 0;
				cursorY = 0;
				invIndex = 0;
				characterIndex = 0;
				itemUsed = false;
			}
			else if(party[characterIndex] != null) {
				characterSelected = true;
				cursorY = 0;
			}
		}
		else if(currentMenu.equals("Status")) {
			if(!characterSelected && party[characterIndex] != null) {
				characterSelected = true;
				cursorX = 0;
				cursorY = 0;
			}
		}
		else if(currentMenu.equals("Save")) {
			if(!yesSelected) {
				currentMenu = "Main";
				cursorX = 0;
				cursorY = 150;
				yesSelected = true;
			}
		}
		else if(currentMenu.equals("Settings")) {
			if(settingOption == 1) {
				switch(colorOption) {
				case 1: if(backgroundColor != Color.RED)
							textColor = Color.RED;
						break;
				case 2: if(backgroundColor != Color.BLUE)
							textColor = Color.BLUE;
						break;
				case 3: if(backgroundColor != Color.GREEN)
							textColor = Color.GREEN;
						break;
				case 4: if(backgroundColor != Color.YELLOW)
							textColor = Color.YELLOW;
						break;
				case 5: if(backgroundColor != Color.BLACK)
							textColor = Color.BLACK;
						break;
				case 6: if(backgroundColor != Color.WHITE)
							textColor = Color.WHITE;
						break;
				}
			}
			else if(settingOption == 2) {
				switch(colorOption) {
				case 1: if(textColor != Color.RED)
							backgroundColor = Color.RED;
						break;
				case 2: if(textColor != Color.BLUE)
							backgroundColor = Color.BLUE;
						break;
				case 3: if(textColor != Color.GREEN)
							backgroundColor = Color.GREEN;
						break;
				case 4: if(textColor != Color.YELLOW)
							backgroundColor = Color.YELLOW;
						break;
				case 5: if(textColor != Color.BLACK)
							backgroundColor = Color.BLACK;
						break;
				case 6: if(textColor != Color.WHITE)
							backgroundColor = Color.WHITE;
						break;
				}
			}
			else {
				settingOption = cursorY/100 + 1;
				colorOption = 1;
			}
		}
	}

	private void back() {
		if(currentMenu.equals("Main")) {
			stateStack.pop();
		}
		else if(currentMenu.equals("Items")) {
			if(itemSelected) {
				characterIndex = 0;
				invIndex = 0;
				if(itemSelected) {
					cursorX = 0;
					cursorY = 0;
				}
				itemSelected = false;
			}
			else if(itemUsed) {
				cursorX = 0;
				cursorY = 0;
				invIndex = 0;
				characterIndex = 0;
				itemUsed = false;
			}
			else {
				currentMenu = "Main";
				cursorY = 0;
				characterIndex = 0;
				invIndex = 0;
			}
		}
		else if(currentMenu.equals("Equip")) {
			if(characterSelected) {
				invIndex = 0;
				characterIndex = 0;
				cursorX = 0;
				cursorY = 0;
				characterSelected = false;
			}
			else if(itemSelected) {
				invIndex = 0;
				cursorX = 0;
				cursorY = 0;
				itemSelected = false;
				characterSelected = true;
			}
			else if(itemUsed) {
				cursorX = 0;
				cursorY = 0;
				invIndex = 0;
				characterIndex = 0;
				itemUsed = false;
			}
			else {
				currentMenu = "Main";
				cursorY = 50;
				characterIndex = 0;
				invIndex = 0;
			}
		}
		else if(currentMenu.equals("Status")) {
			if(characterSelected) {
				characterSelected = false;
				cursorX = 0;
				cursorY = 0;
				characterIndex = 0;
			}
			else {
				characterIndex = 0;
				currentMenu = "Main";
				cursorY = 100;
			}
		}
		else if(currentMenu.equals("Save")) {
			currentMenu = "Main";
			cursorX = 0;
			cursorY = 150;
			yesSelected = true;
		}
		else if(currentMenu.equals("Settings")) {
			if(settingOption != 0) {
				settingOption = 0;
				colorOption = 1;
				cursorX = 0;
			}
			else {
				currentMenu = "Main";
				cursorX = 0;
				cursorY = 200;
			}
		}
	}

	private void upPressed() {
		if(currentMenu.equals("Main")) {
			if(cursorY == 0) 
				cursorY = 200;
			else 
				cursorY -= 50;
		}
		else if(currentMenu.equals("Items")) {
			if(itemSelected) {
				if(cursorY == 0) {
					cursorY = 300;
					characterIndex = 3;
				}
				else {
					cursorY -= 100;
					characterIndex--;
				}
			}
			else if (!itemUsed){
				if(cursorY == 0) {
					cursorY = 400;
					invIndex = invIndex + 16;
				}
				else {
					cursorY -= 50;
					invIndex = invIndex - 2;
				}
			}
		}
		else if(currentMenu.equals("Equip")) {
			if(characterSelected) {
				if(cursorY == 0) {
					cursorY = 400;
					invIndex = invIndex + 16;
				}
				else {
					cursorY -= 50;
					invIndex = invIndex - 2;
				}
			}
			else if(itemSelected) {}
			else if(itemUsed) {

			}
			else {
				if(cursorY == 0) {
					cursorY = 360;
					characterIndex = 3;
				}
				else {
					cursorY -= 120;
					characterIndex--;
				}
			}
		}
		else if(currentMenu.equals("Status")) {
			if(!characterSelected) {
				if(cursorY == 0) {
					cursorY = 360;
					characterIndex = 3;
				}
				else {
					cursorY -= 120;
					characterIndex--;
				}
			}
		}
		else if(currentMenu.equals("Save")) {
			if(cursorY == 0) {
				cursorY = 65;
				yesSelected = false;
			}	
			else {
				cursorY -= 65;
				yesSelected = true;
			}
		}
		else if(currentMenu.equals("Settings")) {
			if(settingOption == 0) {
				if(cursorY == 0) 
					cursorY = 100;	
				else 
					cursorY -= 100;
			}
		}
	}

	private void downPressed() {
		if(currentMenu.equals("Main")) {
			if(cursorY == 200) 
				cursorY = 0;
			else 
				cursorY += 50;
		}
		else if(currentMenu.equals("Items")) {
			if(itemSelected) {
				if(cursorY == 300) {
					cursorY = 0;
					characterIndex = 0;
				}
				else {
					cursorY += 100;
					characterIndex++;
				}
			}
			else if(!itemUsed){
				if(cursorY == 400) {
					cursorY = 0;
					invIndex = invIndex - 16;
				}
				else {
					cursorY += 50;
					invIndex = invIndex + 2;
				}
			}
		}
		else if(currentMenu.equals("Equip")) {
			if(characterSelected) {
				if(cursorY == 400) {
					cursorY = 0;
					invIndex = invIndex - 16;
				}
				else {
					cursorY += 50;
					invIndex = invIndex + 2;
				}
			}
			else if(itemSelected) {}
			else if(itemUsed) {

			}
			else {
				if(cursorY == 360) {
					cursorY = 0;
					characterIndex = 0;
				}
				else {
					cursorY += 120;
					characterIndex++;
				}
			}
		}
		else if(currentMenu.equals("Status")) {
			if(!characterSelected) {
				if(cursorY == 360) {
					cursorY = 0;
					characterIndex = 0;
				}
				else {
					cursorY += 120;
					characterIndex++;
				}
			}
		}
		else if(currentMenu.equals("Save")) {
			if(cursorY == 65) {
				cursorY = 0;
				yesSelected = true;
			}
			else {
				cursorY += 65;
				yesSelected = false;
			}
		}
		else if(currentMenu.equals("Settings")) {
			if(settingOption == 0) {
				if(cursorY == 100) 
					cursorY = 0;	
				else 
					cursorY += 100;
			}
		}
	}

	private void rightPressed() {
		if(currentMenu.equals("Main")) {}
		else if(currentMenu.equals("Items")) {
			if(!itemSelected && !itemUsed) {
				if(cursorX == 0) {
					cursorX += 380;
					invIndex++;
				}
				else if(!itemUsed){
					cursorX -= 380;
					invIndex--;
				}
			}
		}
		else if(currentMenu.equals("Equip")) {
			if(characterSelected) {
				if(cursorX == 380) {
					cursorX -= 380;
					invIndex--;
				}
				else {
					cursorX += 380;
					invIndex++;
				}
			}
			else if(itemSelected) {
				if(cursorX == 0) {
					cursorX += 158;
					yesSelected = false;
				}
				else {
					cursorX -= 158;
					yesSelected = true;
				}
			}
			else if(itemUsed) {

			}
		}
		else if(currentMenu.equals("Status")) {}
		else if(currentMenu.equals("Save")) {}
		else if(currentMenu.equals("Settings")) {
			if(settingOption != 0) {
				if(cursorX == 500) {
					cursorX = 0;	
					colorOption = 1;
				}
				else {
					cursorX += 100;
					colorOption++;
				}
			}
		}
	}
	
	private void leftPressed() {
		if(currentMenu.equals("Main")) {}
		else if(currentMenu.equals("Items")) {
			if(!itemSelected && !itemUsed) {
				if(cursorX == 0) {
					cursorX += 380;
					invIndex++;
				}
				else if (!itemUsed){
					cursorX -= 380;
					invIndex--;
				}
			}
		}
		else if(currentMenu.equals("Equip")) {
			if(characterSelected) {
				if(cursorX == 0) {
					cursorX += 380;
					invIndex++;
				}
				else {
					cursorX -= 380;
					invIndex--;
				}
			}
			else if(itemSelected) {
				if(cursorX == 0) {
					cursorX += 158;
					yesSelected = false;
				}
				else {
					cursorX -= 158;
					yesSelected = true;
				}
			}
			else if(itemUsed) {

			}
		}
		else if(currentMenu.equals("Status")) {}
		else if(currentMenu.equals("Save")) {}
		else if(currentMenu.equals("Settings")) {
			if(settingOption != 0) {
				if(cursorX == 0) {
					cursorX = 500;	
					colorOption = 6;
				}
				else {
					cursorX -= 100;
					colorOption--;
				}
			}
		}
	}
}
