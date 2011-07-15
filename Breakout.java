/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 404;
	public static final int APPLICATION_HEIGHT = 625;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = 400;
	private static final int HEIGHT = 600;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		addMouseListeners();
		bgMusic.loop();
		setUpBricks();
		setUpPaddle();
		setUpBall();
		setLabels();
		waitForClick();
		removeLabels();
		makeBallMove();
		endGame();
		waitForClick();
		restart();
	}
	
	/**
	 * Sets up NBRICK_ROWS rows with NBRICKS_PER_ROW bricks in each row, changing color every two rows starting
	 * with red, orange, yellow, blue. Has a spacing of BRICK_SEP between all sides of brick and the very first
	 * row is offset by BRICK_Y_OFFSET 
	 */
	private void setUpBricks() {
		numBricks = NBRICKS_PER_ROW*NBRICK_ROWS;
		int y = BRICK_Y_OFFSET;
		int cdex = 0;
		Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE};
		for (int i = 0; i < NBRICK_ROWS; i++) {
			if ((i % 2 == 0) && i != 0) {
				cdex++;
			}
			brickRow(y, colors[cdex]);
			y += (BRICK_SEP+BRICK_HEIGHT);
		}
	}
	
	/**
	 * Creates one row of bricks, with NBRICKS_PER_ROW # of bricks in the row 
	 */
	private void brickRow(int y, Color color) {
		int x = BRICK_SEP;
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			addBrick(x, y, color);
			x += (BRICK_SEP + BRICK_WIDTH);
		}
	}
	
	/**
	 * Adds a brick-shaped rectangle to the canvas as the specified spot and color
	 * @param x The x-coor of the top left corner of the brick
	 * @param y The y-coor of the top left corner of the brick
	 */
	private void addBrick(int x, int y, Color color) {
		GRect brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true);
		brick.setColor(color);
		brick.setFillColor(color);
		add(brick);
	}
	
	/**
	 * Sets a black paddle in the center of the canvas
	 */
	private void setUpPaddle() {
		paddle.setFilled(true);
		paddle.setColor(Color.BLACK);
		paddle.setFillColor(Color.BLACK);
		add(paddle);
	}
	
	/**
	 * Sets "CLICK TO START" label in the center and "Score" label in the bottom left corner
	 */
	private void setLabels() {
		clickForStart.setFont(new Font("Serif", Font.BOLD, 18));
		clickForStart.setLocation((WIDTH - clickForStart.getWidth())/2.0, (HEIGHT - clickForStart.getAscent())/2.0);
		add(clickForStart);
		score.setLabel("Score: " + yourScore);
		score.setFont(new Font("Serif", Font.BOLD, 18));
		score.setLocation(4, HEIGHT - 4);
		add(score);
	}
	
	/**
	 * Removes "CLICK TO START" label after user clicks screen 
	 */
	private void removeLabels() {
		remove(clickForStart);
	}
	
	/**
	 * Controls the paddle by setting the location of the paddle at the x-coor location of your mouse 
	 */
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		if (x > (WIDTH - PADDLE_WIDTH)) {
			x = (WIDTH - PADDLE_WIDTH - BRICK_SEP);
		} else if (x == 0) {
			x = BRICK_SEP;
		}
		paddle.setLocation(x, HEIGHT - (PADDLE_Y_OFFSET + PADDLE_HEIGHT));
	}
	
	/**
	 * Sets the a black ball with radius BALL_RADIUS in the middle of the canvas
	 */
	private void setUpBall() {
		ball.setLocation((WIDTH - BALL_RADIUS*2)/2.0, (HEIGHT - BALL_RADIUS*2)/2.0);
		ballx = ball.getX();
		bally = ball.getY();
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
		add(ball);
	}
	
	/**
	 * Sets the ball into motion by giving it an initial downward momentum. While the ball is not at the bottom
	 * of the screen, makes the ball bounce of the borders of the canvas, checks for collisions with bricks
	 * and the paddle, then pauses for 10 milliseconds before repeating. 
	 */
	private void makeBallMove() {
		initialMovement();
		while (ballNotAtBottom()) {
			if (numBricks == 0) break;
			if(((ballx+BALL_RADIUS*2) > WIDTH) || ballx < 0) {
				vx = -vx;
			}
			if(((bally + BALL_RADIUS*2) > HEIGHT) || bally < 0) {
				vy = -vy;
			}
			ballx += vx;
			bally += vy;
			ball.setLocation(ballx, bally);
			checkForCollisions();
			pause(10);
		}
	}
	
	/**
	 * Gives the ball an initial downward vertical velocity of three, and a random horizontal velocity
	 * from 1 to 3, with a 50% chance of going left or right 
	 */
	private void initialMovement() {
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean()) vx = -vx;
		vy = 3.0; 
	}
	
	/**
	 * Checks for collisions with the paddle or brick. If collided with brick, bounce off and remove the brick, 
	 * and change the score accordingly.  
	 * If collided with paddle, make sure it doesn't have the "glued to paddle" bug and bounce of the paddle. 
	 * Change speed after colliding with paddle certain number of times. Ball's trajectory depends on where on 
	 * the paddle the ball lands. 
	 */
	private void checkForCollisions() {
		GObject collider = getCollidingObject(); 
		if (collider == score) {
			
		} else if (collider == paddle) {
			fixGluing();
			vy = -vy; 
			edgeConditions();
			speedGame();
		} else if ((collider != null)) {
			bounceClip.play();
			vy = -vy; 
			remove(collider);
			keepScore();
		}
	}
	
	/** 
	 * Gets the object at the four corners of the ball. If an object was found at the top two corners
	 * of the ball, set boolean glued to be true. Else, set to be false. 
	 * @return The object at that point or null if there is no object. 
	 */
	private GObject getCollidingObject() {
		double[][] fourCorners = {{ballx, bally}, {ballx + 2*BALL_RADIUS, bally}, 
				{ballx + 2*BALL_RADIUS, bally + 2*BALL_RADIUS}, {ballx, bally + 2*BALL_RADIUS}};
		for (int i = 0; i < 4; i++) {
			GObject obj = getElementAt(fourCorners[i][0], fourCorners[i][1]);
			if (obj != null) {
				if (i == 1 || i == 2) {
					glued = true;
				} else {
					glued = false;
				}
				return obj;
			}
		}
		glued = false;
		return null; 
	}
	
	/**
	 * If the top two corners of the ball come in contact with the paddle, change the ball's y coor to be
	 * a little over the diameter of the ball away from it's original position. This is to ensure the ball
	 * does not get stuck on the paddle. 
	 */
	private void fixGluing() {
		if(glued == true) {
			bally -= BALL_RADIUS*2.03;
		}
	}
	
	/**
	 * Tests whether the ball is on the edge of the paddle or not. If it is, make it bounce in the direction from
	 * where it came. Otherwise, ball bounces off the paddle at appropriate angle. 
	 */
	private void edgeConditions() {
		if (((ballx < paddle.getX()+BALL_RADIUS*1.1) && (ballx > paddle.getX()-BALL_RADIUS*1.1)) || 
				((ballx < paddle.getX()+PADDLE_WIDTH) && (ballx > paddle.getX()+PADDLE_WIDTH-BALL_RADIUS*2))) {
			vx = -vx;
		}
	}
	
	/**
	 * Doubles the horizontal velocity of the ball after it has hit the paddle 10 times. 
	 */
	private void speedGame() {
		hitPaddleCount++;
		if (hitPaddleCount == 10) {
			vx *= 2;
		}
	}
	
	/**
	 * Adds 10*absolute value of horizontal velocity of ball for each brick the ball hits. Displays this in the 
	 * "Score" label. Keeps track of the number of bricks remaining in the game.  
	 */
	private void keepScore() {
		numBricks--;
		yourScore += 10*(Math.abs(vx));
		score.setLabel("Score: " + yourScore);
		add(score);
	}
	
	/**
	 * Checks to see whether the ball has hit the bottom edge of the canvas. 
	 * @return true if it is not at the bottom, false if it is at the bottom 
	 */
	private boolean ballNotAtBottom() {
		if ((bally + BALL_RADIUS*2) > HEIGHT) {
			println(bally + BALL_RADIUS*2);
			return false;
		}
		return true;
	}
	
	
	private void endGame() {
		GLabel end;
		if (numBricks == 0) {
			win.setFont(new Font("Serif", Font.BOLD, 18));
			win.setLocation((WIDTH - gameover.getWidth())/2.0, (HEIGHT - gameover.getAscent())/2.0);
			add(win);
			end = win;
		} else {
			gameover.setFont(new Font("Serif", Font.BOLD, 18));
			gameover.setLocation((WIDTH - gameover.getWidth())/2.0, (HEIGHT - gameover.getAscent())/2.0);
			add(gameover);
			end = gameover;
		}
		setScores();
		clickToRestart.setFont(new Font("Serif", Font.BOLD, 16));
		clickToRestart.setLocation((WIDTH - clickToRestart.getWidth())/2.0, 
									end.getY() - clickToRestart.getHeight());
		add(clickToRestart);
	}
	
	private void setScores() {
		if (yourScore > highScore) {
			highScore = yourScore;
		}
		highscore.setLabel("Highscore: " + highScore);
		highscore.setFont(new Font("Serif", Font.BOLD, 16));
		highscore.setLocation((WIDTH - highscore.getWidth())/2.0, HEIGHT/2.0 + gameover.getHeight());
		add(highscore);
		score.setLabel("Your Score: " + yourScore);
		score.setFont(new Font("Serif", Font.BOLD, 16));
		score.setLocation((WIDTH - score.getWidth())/2.0, highscore.getY() + highscore.getHeight());
		add(score);
		
	}
	
	private void restart() {
		yourScore = 0;
		removeAll(); 
		run();
	}
	
	/* Private instance variables */
	
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	AudioClip bgMusic = MediaTools.loadAudioClip("gameboy.au");
	GRect paddle = new GRect((WIDTH - PADDLE_WIDTH)/2.0, HEIGHT - (PADDLE_Y_OFFSET + PADDLE_HEIGHT), PADDLE_WIDTH, PADDLE_HEIGHT);
	GOval ball = new GOval((WIDTH - BALL_RADIUS*2)/2.0, (HEIGHT - BALL_RADIUS*2)/2.0, BALL_RADIUS*2, BALL_RADIUS*2);
	GLabel clickForStart = new GLabel("CLICK TO START");
	GLabel gameover = new GLabel("GAME OVER");
	GLabel win = new GLabel("YOU WIN");
	GLabel clickToRestart = new GLabel("CLICK TO RESTART");
	
	private RandomGenerator rgen = RandomGenerator.getInstance(); 
	private boolean glued;
	private double vx, vy; 
	double ballx = ball.getX();
	double bally = ball.getY();
	private int numBricks;
	private int yourScore = 0; 
	private int highScore = 0;
	private int hitPaddleCount = 0;
	GLabel score = new GLabel("Score: " + yourScore);
	GLabel highscore = new GLabel("Highscore: " + highScore);
}
