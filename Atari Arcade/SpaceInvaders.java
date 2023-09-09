import java.awt.*;				// NEW A
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/******************************************************************************
 * 
 * Name:	Raunak Agrawal
 * Block:	E			
 * Date:	5/16/19		
 * 
 *  Final Project: Space Invaders
 *  Description: Space Invaders is a classic 80's game that I have decided
 *  to recreate, but in a badish way. It is missing some features because
 *  they were very hard to do and I did not have much time to accomplish
 *  them. What I have is what would be the basic version of Space Invaders.
 *  A spaceship against 55 aliens. The spaceship moves around left and right
 *  while shooting bullets to try and kill all the aliens as they are marching
 *  down the screen. It is very, very intense. It would be better if I got the
 *  monsters to drop bombs and that the spaceship could have lives, but I did
 *  not have time. My only feature is a point system that detects which monster
 *  you hit and how many points you gain and have in total after that. After
 *  you kill all the monsters, you win the game! Then you have the option to
 *  restart so that you can play forever!
 *  	 
 * 
 ******************************************************************************/
public class SpaceInvaders 
extends JFrame
implements ActionListener, KeyListener
{
	private static final int MAX_WIDTH = 1000;		// Window size
	private static final int MAX_HEIGHT = 700;		// Window size
	private static final int TOP_OF_WINDOW = 22;// Top of the visible window

	//properties to make our spaceship
	private static final int SPACESHIP_X = 500;
	private static final int SPACESHIP_Y = 650;
	private static final int SPACESHIP_WIDTH = 70;
	private static final int SPACESHIP_HEIGHT = 50;

	//properties to make 2d array of monster
	private static final int NUM_ROW_MONSTERS = 5;
	private static final int NUM_COL_MONSTERS = 11;

	//each monster width and height
	private static final int MONSTER_WIDTH = 50;
	private static final int MONSTER_HEIGHT = 50;

	//gap between each monster
	private static final int GAP_WIDTH = 5;

	//monster grid margin from top of the screen
	private static final int MONSTER_TOP_MARGIN = 75;

	//width of entire screen
	private static final int MONSTER_GRID_WIDTH = 630;

	//how much the monster moves down
	private static final int MOVE_DOWN = 30;

	//speed for spaceship and monster
	private static final int MOVE_VELOCITY = 23;
	private static final double MONSTER_MOVE_VELOCITY = 10;

	//properties to make bullets and 2d array of bullets
	private static final int NUM_BULLETS = 10000;
	private static final int BULLET_WIDTH = 4;
	private static final int BULLET_HEIGHT = 9;
	private static final int BULLET_VELOCITY = 11;

	//delay for monster moving, for bullet shooting, and delay in general
	private static final int DELAY_IN_MILLISEC = 20;
	private static final int DELAY_FOR_BULLETS = 4;
	private static final int DELAY_FOR_MONSTERS = 45;

	//stages for the game to be at
	private static final int INTRO = 1;
	private static final int LEVEL1 = 2;
	private static final int END = 3;
	private static final int CONTROLS = 4;
	private static final int BYEBYE = 5;

	//points given for each separate rows of monster
	private static final int ROW1SCORE = 40;
	private static final int ROW2SCORE = 20;
	private static final int ROW3SCORE = 10;

	//group x and y position for monster grid
	private static double monsterGroupX;
	private static double monsterGroupY;
	private static double monsterGroupDX = MONSTER_MOVE_VELOCITY;

	//declaration of spaceship, monsters, and bullets
	private static Spaceship spaceboi;
	private static Monster [][] monsters;
	private static Bullet [] bullets = new Bullet[NUM_BULLETS]; 

	//	bullet number in array, bullet delay for each shot, 
	//	monster delay for moving
	private static int numBullet = 0; 
	private static int bulletDelay = 0;
	private static int monsterDelay = 0;

	//declaration of images imported	
	private static Image monsterImage1, monsterImage2, monsterImage3;
	private static Image spaceShipImage;
	private static Image spaceInvadersLogo;

	//state of Game at the time and total number of points the user is at	
	private static int stateOfGame;
	private static int totalScore;



	public static void main(String[] args) 
	{
		//Initialize method initlializes all the stuff I need to create
		Initialize();
		SpaceInvaders mb = new SpaceInvaders();
		mb.addKeyListener(mb);

	}

	public static void Initialize()
	{
		//create monsters
		monsterGroupY = TOP_OF_WINDOW + MONSTER_TOP_MARGIN;
		monsters = new Monster[NUM_ROW_MONSTERS]
				[NUM_COL_MONSTERS];

		//set originial state of Game
		stateOfGame = INTRO;

		//create spaceship
		int spaceX = SPACESHIP_X;
		int spaceY = SPACESHIP_Y;
		int spaceWidth = SPACESHIP_WIDTH;
		int spaceHeight = SPACESHIP_HEIGHT;
		int spaceVelocity = MOVE_VELOCITY;
		spaceboi = new Spaceship(spaceX, spaceY, spaceWidth, 
				spaceHeight, spaceVelocity);

		//create monsters
		int monsterWidth = MONSTER_WIDTH;
		int monsterHeight = MONSTER_HEIGHT;
		for(int row = 0; row < monsters.length; row++)
		{
			int monsterPosY = (row * (MONSTER_HEIGHT));
			for(int col = 0; col <  monsters[0].length; col++)
			{
				int monsterPosX = (col * (MONSTER_WIDTH + GAP_WIDTH));
				monsters[row][col] = new Monster(monsterPosX, 
						monsterPosY, monsterWidth, monsterHeight, true);	
			}
		}

	}

	public SpaceInvaders()
	{
		//create each image that was importede
		monsterImage1 = new ImageIcon("monster.png").getImage();
		monsterImage2 = new ImageIcon("monster2.png").getImage();
		monsterImage3 = new ImageIcon("monster3.png").getImage();
		spaceShipImage = new ImageIcon("Laser_Cannon.png").getImage();
		spaceInvadersLogo = new ImageIcon("spaceinvaders.png").getImage();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(MAX_WIDTH, MAX_HEIGHT);
		setVisible(true);


		createBufferStrategy(2);					// NEW B

		// Sets up a timer but does not start it.  Once started, the timer will go
		//  off every DELAY_IN_MILLISEC milliseconds.  When it goes off all it does
		//  is call this.actionPerformed().  It then goes back to sleep for another
		//  DELAY_IN_MILLISEC.  
		Timer clock= new Timer(DELAY_IN_MILLISEC, this);			
		// NEW #3 !!!!!!!!!!
		// Now actually start the timer.
		clock.start();						
	}

	public void keyTyped(KeyEvent e)
	{
		//
	}

	public void keyReleased(KeyEvent e)
	{
		//
	}

	/**
	 * place where check which key was pressed and take action upon that
	 */
	public void keyPressed(KeyEvent e)
	{

		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_LEFT)
		{
			//moving the spaceship left
			spaceboi.moveLeft();
		}
		else if(keyCode == KeyEvent.VK_RIGHT)
		{
			//moving the spaceship right
			spaceboi.moveRight();
		}
		else if(keyCode == KeyEvent.VK_SPACE)
		{
			//creating the bullets
			double bx = spaceboi.bx();
			double by = spaceboi.by();
			if(bulletDelay == 0)
			{
				bulletDelay = DELAY_FOR_BULLETS;
				bullets[numBullet] = new Bullet(bx, by, BULLET_WIDTH, 
						BULLET_HEIGHT, BULLET_VELOCITY);
				numBullet++;
			}
			else
			{
				bulletDelay--;
			}

		}
		else if(keyCode == KeyEvent.VK_ENTER)
		{
			//changing state of game depending on which key is pressed
			stateOfGame = LEVEL1;
		}
		else if(keyCode == KeyEvent.VK_H)
		{
			//changing state of game depending on which key is pressed
			stateOfGame = CONTROLS;
		}
		else if(keyCode == KeyEvent.VK_UP)
		{
			//changing state of game depending on which key is pressed
			stateOfGame = INTRO;
		}
		else if(keyCode == KeyEvent.VK_Q)
		{
			//changing state of game depending on which key is pressed
			stateOfGame = BYEBYE;
		}
		else if(keyCode == KeyEvent.VK_R)
		{
			//changing state of game depending on which key is pressed
			//reseting game and recalling Initialize and resetting score
			stateOfGame = INTRO;
			Initialize();
			totalScore = 0;
			repaint();
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		//checking to see if we should move the monsters
		if(monsterDelay == 0)
		{
			monsterDelay = DELAY_FOR_MONSTERS;
			//seeing if the grid is greater than the max width of the screen
			if(monsterGroupX + MONSTER_GRID_WIDTH > MAX_WIDTH)
			{
				//moving down and changing direction if true
				monsterGroupY = monsterGroupY + MOVE_DOWN;
				monsterGroupDX = -monsterGroupDX;

			}
			else
			{
				//or else keep going
				monsterGroupX = monsterGroupX + monsterGroupDX;
			}
			//seeing if the grid is less than 0
			if(monsterGroupX < 0)
			{
				//setting it to 0, moving down, changing direction
				monsterGroupX = 0;
				monsterGroupY = monsterGroupY + MOVE_DOWN;
				monsterGroupDX = -monsterGroupDX;

			}
			else
			{
				//or else keep going
				monsterGroupX = monsterGroupX + monsterGroupDX;
			}


		}
		else
		{
			//if none of that other stuff happened, decrease delay until
			//it reaches 0 and makes the big if statement true
			monsterDelay--;
		}

		for(int i = 0; i < numBullet; i++)
		{
			//move the bullets
			bullets[i].move();
			//check if the bullets shot hit a monster
			for(int row = 0; row < monsters.length; row++)
			{
				for(int col = 0; col < monsters[0].length; col++)
				{
					//check if it hit a monster
					boolean hit = bullets[i].hit(monsterGroupX, 
							monsterGroupY, monsters[row][col]);
					//if it did it, check which row
					if(hit)
					{
						if(row == 0)
						{
							//if it hit one monster in the top row, 
							//add 40 points
							totalScore += ROW1SCORE;
						}
						else if(row == 1 || row == 2)
						{
							//if it hit one monster in the middle 
							//two rows, add 20 points
							totalScore += ROW2SCORE;
						}
						else if(row == 3 || row == 4)
						{
							//if it one monster in bottom two rows
							//add 10 points
							totalScore += ROW3SCORE;
						}
					}
				}
			}

		}

		//check to see if all the monsters are alive
		boolean seeIfOneAlive = false;
		for(int row = 0; row < monsters.length; row++)
		{
			for(int col = 0; col < monsters[0].length; col++)
			{
				//do getAlive for each monster, returning
				//alive value and then setting true or keeping false
				if(monsters[row][col].getAlive())
				{
					seeIfOneAlive = true;
				}
			}
		}
		//if the seeIfOneAlive is false, meaning all the monsters are gone
		//than switch to end screen
		if(!seeIfOneAlive)
		{
			stateOfGame = END;
		}

		repaint();
	}

	public void paint(Graphics g)
	{
		BufferStrategy bf = this.getBufferStrategy();
		if (bf == null)
			return;

		Graphics g2 = null;

		try 
		{
			g2 = bf.getDrawGraphics();

			// myPaint does the actual drawing
			myPaint(g2);
		} 
		finally 
		{
			// It is best to dispose() a Graphics object when done with it.
			g2.dispose();
		}

		// Shows the contents of the back buffer on the screen.
		bf.show();

		//Tell the System to do the Drawing now, otherwise it can take a few extra ms until 
		//Drawing is done which looks very jerky
		Toolkit.getDefaultToolkit().sync();	
	}

	public void myPaint(Graphics g)
	{
		//fill up the screen
		g.setColor(Color.black);
		g.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);

		//doing intro screen if stateOfGame equals intro
		if(stateOfGame == INTRO)
		{
			//drawing almost all images and then writing points for monstter
			//and then giving opttion to look at controls.
			g.drawImage(spaceInvadersLogo, 225, 50, 500, 200, null);
			g.drawImage(monsterImage1, 325, 300, 50, 50, null);
			g.drawImage(monsterImage2, 325, 350, 50, 50, null);
			g.drawImage(monsterImage3, 325, 400, 50, 50, null);
			g.setColor(Color.white);
			g.setFont(new Font("Dialog", Font.PLAIN, 30));
			g.drawString(" = 10 points", 375, 330);
			g.drawString(" = 20 points", 375, 385);
			g.drawString(" = 40 points", 375, 440);
			g.drawString("Press Enter to Play", 325, 500);
			g.drawString("Press H to Learn Controls", 325, 550);
		}

		//doing game if stateOfGame equals level1
		if(stateOfGame == LEVEL1)
		{
			//drawing spaceship
			spaceboi.draw(g, spaceShipImage);

			//display user score
			g.setColor(Color.green);
			g.setFont(new Font("Dialog", Font.PLAIN, 30));
			g.drawString("Score: " + totalScore, 0, 50);

			//drawing the different monsters for each row
			for(int row = 0; row < monsters.length; row++)
			{
				for(int col = 0; col < monsters[0].length; col++)
				{
					if(row == 0)
					{
						//drawing certain monster if row is 0
						monsters[row][col].draw(g, monsterImage3, 
								monsterGroupX, monsterGroupY);
					}
					else if(row == 1 || row == 2)
					{
						//drawing different monster if row is 1 or 2
						monsters[row][col].draw(g, monsterImage2, 
								monsterGroupX, monsterGroupY);
					}
					else if(row == 3 || row == 4)
					{
						//drawing other monster if row is 3 o4 4
						monsters[row][col].draw(g, monsterImage1, 
								monsterGroupX, monsterGroupY);
					}
				}
			}

			//drawing the bullets
			for(int i = 0; i < numBullet; i++)
			{
				bullets[i].draw(g);
			}
		}

		//state of game when H is pressed
		if(stateOfGame == CONTROLS)
		{
			//displaying all the controls possible
			g.setColor(Color.white);
			g.setFont(new Font("Dialog", Font.PLAIN, 30));
			g.drawString("Left Arrow to Move Spaceship Left", 225, 250);
			g.drawString("Right Arrow to Move Spaceship Right", 225, 300);
			g.drawString("SpaceBar to Shoot", 225, 350);
			g.drawString("Press Enter to Play", 225, 400);
			g.drawString("Press Up Arrow to Return to Main Screen", 225, 450);
			g.drawString("Press Q to Quit", 225, 500);

		}

		//state of game when the game ends when user wins
		if(stateOfGame == END)
		{
			g.setColor(Color.white);
			g.setFont(new Font("Dialog", Font.PLAIN, 30));
			g.drawString("You Win! Your Score Was: " + totalScore, 325, 300);
			//new control to restart the game
			g.drawString("Press R to Restart", 325, 350);
		}

		//if user clicks Q, ends game
		if(stateOfGame == BYEBYE)
		{
			g.setColor(Color.white);
			g.setFont(new Font("Dialog", Font.PLAIN, 30));
			g.drawString("BYE BYE", 425, 350);
		}

	}


}
//class to construct the spaceship and do certain commands
class Spaceship
{	
	private double spaceX;
	private double spaceY;
	private double spaceWidth;
	private double spaceHeight;
	private double spaceVelocity;
	private static final int MAX_WIDTH = 1000;

	/**
	 * construct spaceship
	 * 
	 * @param spaceXIn 				spaceship position x
	 * @param spaceYIn				spaceship position y
	 * @param spaceWidthIn			spaceship width
	 * @param spaceHeightIn			spaceship height
	 * @param spaceVelocityIn		spaceship velocity
	 */
	public Spaceship(int spaceXIn, int spaceYIn, 
			int spaceWidthIn, int spaceHeightIn, int spaceVelocityIn)
	{
		spaceX = spaceXIn;
		spaceY = spaceYIn;
		spaceWidth = spaceWidthIn;
		spaceHeight = spaceHeightIn;
		spaceVelocity = spaceVelocityIn;

	}

	//move spaceship left
	public void moveLeft()
	{
		spaceX = spaceX - spaceVelocity;
		if(spaceX < 0)
		{
			spaceX = 0;
		}
	}

	//move spaceship right
	public void moveRight()
	{
		spaceX = spaceX + spaceVelocity;
		if(spaceX + spaceWidth > MAX_WIDTH)
		{
			spaceX = MAX_WIDTH - spaceWidth;
		}
	}

	/**
	 * draw spaceship
	 * 
	 * @param g
	 * @param spaceShipImage spaceship image
	 */
	public void draw(Graphics g, Image spaceShipImage)
	{
		g.setColor(Color.green);
		g.drawImage(spaceShipImage, (int)(spaceX), (int)(spaceY), 
				(int)(spaceWidth), (int)(spaceHeight), null);
	}

	//bullet position x
	public double bx()
	{
		return spaceX + spaceWidth/2;
	}

	//bullet position y
	public double by()
	{
		return spaceY;
	}

}

//class to construct monsters and do commands for monster
class Monster
{
	private double monsterX ;
	private double monsterY;
	private double monsterWidth;
	private double monsterHeight;
	private boolean alive;

	/**
	 * construct monster
	 * 
	 * @param monsterXIn			monster position x
	 * @param monsterYIn			monster position y
	 * @param monsterWidthIn		monster width
	 * @param monsterHeightIn		monster height
	 * @param aliveIn				boolean to see if monster alive
	 */
	public Monster(int monsterXIn, int monsterYIn, 
			int monsterWidthIn, int monsterHeightIn, boolean aliveIn)
	{
		monsterX = monsterXIn;
		monsterY = monsterYIn;
		monsterWidth = monsterWidthIn;
		monsterHeight = monsterHeightIn;
		alive = aliveIn;
	}

	/**
	 * draw monster
	 * 
	 * @param g
	 * @param monsterImage			image imported
	 * @param monsterGroupX			group x to draw relative from
	 * @param monsterGroupY			group y to draw relative from
	 */
	public void draw(Graphics g, Image monsterImage, 
			double monsterGroupX, double monsterGroupY)
	{
		//only draw the monster if it is alive
		if(alive)
		{
			g.setColor(Color.white);
			g.drawImage(monsterImage, (int)(monsterX + monsterGroupX), 
					(int)(monsterY + monsterGroupY), (int)(monsterWidth), 
					(int)(monsterHeight), null);
		}
	}

	/**
	 * check if the bullet hit the monster
	 * 
	 * @param monsterGroupX 		monsterGroupX to get monster position x from
	 * @param monsterGroupY			monsterGroupY to get monster position y from
	 * @param bPosX					bullet position X
	 * @param bPosY					bullet position Y
	 * @return
	 */
	public boolean hit(double monsterGroupX, double monsterGroupY, double bPosX, 
			double bPosY)
	{
		//only check if the monster is alive
		if(alive)
		{
			//check if the bullet it
			if(bPosX > monsterGroupX + monsterX && 
					bPosX < monsterGroupX + monsterX + monsterWidth)
			{
				if(bPosY > monsterGroupY + monsterY && 
						bPosY < monsterGroupY + monsterY + monsterHeight)
				{
					//if true, set alive equal to false, return true
					alive = false;
					return true;
				}
			}
			//return false otherwise
			return false;

		}
		else
		{
			//return false if first part does not happen
			return false;
		}	
	}

	//method to return alive to see if monster is alive to see if all the
	//monsters are alive
	public boolean getAlive()
	{
		return alive;
	}
}

//class to create and do commands for bullet
class Bullet
{
	private double bPosX, bPosY;
	private double bWidth, bHeight;
	private double by;
	private boolean alive = true;

	/**
	 * construct bullet
	 * 
	 * @param bPosXIn				bullet position X
	 * @param bPosYIn				bullet position Y 
	 * @param bWidthIn				bullet width
	 * @param bHeightIn				bullet height
	 * @param byIn					bullet velocity
	 */
	public Bullet(double bPosXIn, double bPosYIn, double bWidthIn, 
			double bHeightIn, double byIn)
	{
		//constructing bullet
		bPosX = bPosXIn;
		bPosY = bPosYIn;
		bWidth = bWidthIn;
		bHeight = bHeightIn;
		by = byIn;
	}

	//move the bullet
	public void move()
	{
		//move the bullet
		bPosY = bPosY - by;
	}

	//draw the bullet
	public void draw(Graphics g)
	{
		//only draw if bullet alive meaning has not hit a monster
		if(alive)
		{
			g.setColor(Color.white);
			g.fillRect((int)(bPosX), (int)(bPosY), (int)(bWidth),(int)(bHeight));
		}

	}

	/**
	 * hit method that calls hit method in monster class
	 * 
	 * @param monsterGroupX			monster group x for position
	 * @param monsterGroupY			monster group y for position
	 * @param monster				individual monster to test
	 * @return
	 */
	public boolean hit(double monsterGroupX, double monsterGroupY, 
			Monster monster)
	{
		//check to see if the bullet hit the monster
		if(alive)
		{
			boolean hit = monster.hit(monsterGroupX, monsterGroupY, 
					bPosX, bPosY);
			//return false if the bullet hit
			if(hit)
			{
				alive = false;
			}
			return hit;
		}
		else
		{
			return false;
		}

	}
}


