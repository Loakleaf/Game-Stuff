package Game;

import java.awt.Color;
import java.awt.event.*;
import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;

public class Test2 extends GraphicsProgram {

	private static final long serialVersionUID = 3980044442939103621L;

	public static final double PLATFORM_WIDTH = 120;
	public static final double PLATFORM_HEIGHT = 20;

	public static final int PLAYER_SIZE = 50;

	public static final int TIMER_LENGTH = 10;

	boolean left = false;
	boolean right = false;
	boolean up = false;
	boolean down = false;
	boolean r = false;
	boolean space = false;

	GLabel score = new GLabel ("");

	//INFINITE TIME CHEAT
	boolean infTime = true;


	public void init() {
		setSize(1920, 900);
	}

	@Override public void keyPressed(KeyEvent e) {
		e.getKeyCode();
		int x = e.getKeyCode();
		switch(x){
		case KeyEvent.VK_RIGHT: right = true;
		break;
		case KeyEvent.VK_UP: up = true;
		break;
		case KeyEvent.VK_LEFT: left = true;
		break;
		case KeyEvent.VK_DOWN: down = true;
		break;
		case KeyEvent.VK_R: r = true;
		break;
		case KeyEvent.VK_SPACE: space = true;
		break;
		}
	}
	@Override public void keyReleased(KeyEvent e) {
		e.getKeyCode();
		//println(e.getKeyCode());
		//left
		int x = e.getKeyCode();
		switch(x){
		case KeyEvent.VK_RIGHT: right = false;
		break;
		case KeyEvent.VK_UP: up = false;
		break;
		case KeyEvent.VK_LEFT: left = false;
		break;
		case KeyEvent.VK_DOWN: down = false;
		break;
		case KeyEvent.VK_R: r = false;
		break;
		case KeyEvent.VK_SPACE: space = false;
		break;
		}
	}
	public void run() {

		boolean winning = true;

		int levelTimer = TIMER_LENGTH*144;

		boolean floater = false;

		int playerWinCount = 0;

		//GImage wallpaper = new GImage ("Test.png", 1920, 1080);
		//add(wallpaper);

		GOval playerCharacter = new GOval (getWidth()/2, getHeight()-PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE);
		playerCharacter.setFilled(true);

		double playerSpeedX = 0;
		double playerSpeedY = 0;

		boolean extraJump = false;

		int floaterCounter = 0;

		//boolean pole = false;

		addMouseListeners();
		addKeyListeners();

		init();

		while(winning || infTime) {
			removeAll();
			boolean notBroken = true;

			//GLabel score = new GLabel ("Win Count: " + playerWinCount, 0, 0);
			//double scoreHeight = score.getHeight();
			//score.setLocation(0, scoreHeight);
			//add(score);

			playerCharacter.setLocation(getWidth()/2-PLAYER_SIZE, getHeight()-PLAYER_SIZE);
			add(playerCharacter);


			addRectangles();
			while(notBroken && (winning || infTime)) {
				//movement
				if(right && playerSpeedX <5) {
					playerSpeedX = playerSpeedX+.3;
				}
				if(left && playerSpeedX>-5) {
					playerSpeedX = playerSpeedX-.3;
				}
				if(up && playerSpeedY == 0) {
					if(extraJump) {
						playerSpeedY = -8;
						extraJump = false;
					}else {
						playerSpeedY = -6;
					}
				}
				if(down) {

				}
				//bonus
				if(space && floaterCounter>0) {
					playerSpeedY = 0;
					floaterCounter -= 1;
					floater = true;

				}else if(floaterCounter<=0 && floater){
					playerCharacter.setColor(Color.BLACK);
					floater = false;
				}
				//	if(space && pole) {
				//		GPolygon poly = thickLine(playerCharacter.getX()/getWidth()*80, 0, playerCharacter.getX()/getWidth()*80, 100, 10, null);
				//		add(poly);
				//		println(poly.getY());
				//		pole = false;
				//		playerCharacter.setColor(Color.BLACK);
				//	}
				//Gravity
				if(playerCharacter.getY()+PLAYER_SIZE<getHeight()) {
					playerSpeedY=playerSpeedY+.1;
				}
				//Stop on floor
				else if (playerCharacter.getY()+PLAYER_SIZE>getHeight()-15 && playerSpeedY>0) {
					playerCharacter.setColor(Color.BLACK);
					playerSpeedY = 0;
				}
				//Stop on platform
				if(getCollider(playerCharacter.getX(), playerCharacter.getY()) != null) {
					playerCharacter.setColor(Color.BLACK);
					floaterCounter = 0;
					//Extra Jump
					if(getCollider(playerCharacter.getX(), playerCharacter.getY()).getColor() == Color.GREEN) {
						playerCharacter.setColor(Color.GREEN);
						extraJump = true;
					}
					//Floater
					else if(getCollider(playerCharacter.getX(), playerCharacter.getY()).getColor() == Color.BLUE) {
						playerCharacter.setColor(Color.BLUE);
						floaterCounter = 250;
					}
					//Mover
					else if(getCollider(playerCharacter.getX(), playerCharacter.getY()).getColor() == Color.GRAY) {
						playerCharacter.setColor(Color.GRAY);
						if(getCollider(playerCharacter.getX(), playerCharacter.getY()).getWidth()<getWidth()*.8) {
							getCollider(playerCharacter.getX(), playerCharacter.getY()).setSize(getCollider(playerCharacter.getX(), playerCharacter.getY()).getWidth()+.6, getCollider(playerCharacter.getX(), playerCharacter.getY()).getHeight());
							getCollider(playerCharacter.getX(), playerCharacter.getY()).setLocation(getCollider(playerCharacter.getX(), playerCharacter.getY()).getX()-.3, getCollider(playerCharacter.getX(), playerCharacter.getY()).getY());
						}
					}
					//Pole
					//	else if(getCollider(playerCharacter.getX(), playerCharacter.getY()).getColor() == Color.RED) {
					//		playerCharacter.setColor(Color.RED);
					//		pole = true;
					//	}
					getCollider(playerCharacter.getX(), playerCharacter.getY()).setFilled(true);
					getCollider(playerCharacter.getX(), playerCharacter.getY()).setColor(playerCharacter.getColor());
					getCollider(playerCharacter.getX(), playerCharacter.getY()).setFillColor(playerCharacter.getColor());
					playerSpeedY = 0;
				}
				//x slow down
				if(playerSpeedX>0) {
					playerSpeedX -= .07;
				}
				else if(playerSpeedX<0){
					playerSpeedX += .07;
				}

				//wrap around stuff
				if(playerCharacter.getX()+PLAYER_SIZE <0) {
					playerCharacter.setLocation(getWidth(), playerCharacter.getY());
				}

				if(playerCharacter.getX()>getWidth()) {
					playerCharacter.setLocation(0-PLAYER_SIZE, playerCharacter.getY());
				}
				//Lose Level
				if(levelTimer < 0) {
					winning = false;
				}
				//Win level
				if(playerCharacter.getY() <0) {
					notBroken = false;
					playerWinCount += 1;
					levelTimer = TIMER_LENGTH*144;
				}

				if (r) {
					notBroken = false;
					r = true;
					pause(1000/144);
				}

				levelTimer -= 1;
				playerCharacter.move(playerSpeedX, playerSpeedY);
				pause(1000/144);
			}
		}
	}//ends run
	public void addRectangles() {
		RandomGenerator rand = RandomGenerator.getInstance();
		for(int i = 0; i<10; i++) {
			GRect platform = new GRect(rand.nextDouble(0, getWidth()-PLATFORM_WIDTH), i*getHeight()/10, PLATFORM_WIDTH, PLATFORM_HEIGHT);
			if (rand.nextInt(0, 5) == 0) {
				platform.setColor(Color.GREEN);
				platform.setFillColor(Color.WHITE);
			}
			if (rand.nextInt(0, 5) == 0) {
				platform.setColor(Color.BLUE);
				platform.setFillColor(Color.WHITE);
			}
			//	if (rand.nextInt(0, 1) == 1) {
			//		platform.setColor(Color.RED);
			//		platform.setFillColor(Color.WHITE);
			//	}
			if (rand.nextInt(0, 100) == 0) {
				platform.setColor(Color.GRAY);
				platform.setFillColor(Color.WHITE);
			}
			add(platform);
		}
	}
	public GRect getCollider(double x, double y) {
		if(getElementAt(x, y) != null) {
			return((GRect)getElementAt(x, y));
		}
		if(getElementAt(x+PLAYER_SIZE, y) != null) {
			return((GRect)getElementAt(x+PLAYER_SIZE, y));
		}
		if(getElementAt(x, y+PLAYER_SIZE) != null) {
			return((GRect)getElementAt(x, y+PLAYER_SIZE));
		}
		if(getElementAt(x+PLAYER_SIZE, y+PLAYER_SIZE) != null) {
			return((GRect)getElementAt(x+PLAYER_SIZE, y+PLAYER_SIZE));
		}
		//if(getElementAt(x, y+PLAYER_SIZE/2) != null) {
		//	return((GRect)getElementAt(x, y+PLAYER_SIZE));
		//}
		//if(getElementAt(x+PLAYER_SIZE, y+PLAYER_SIZE/2) != null) {
		//	return((GRect)getElementAt(x+PLAYER_SIZE, y+PLAYER_SIZE));
		//}
		return(null);
	}
}//ends class

