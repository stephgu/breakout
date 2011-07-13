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
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 60;

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
		setUpBricks();
		setUpPaddle();
	}
	
	private void setUpBricks() {
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
	
	private void brickRow(int y, Color color) {
		int x = BRICK_SEP;
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			addBrick(x, y, color);
			x += (BRICK_SEP + BRICK_WIDTH);
		}
	}
	
	private void addBrick(int x, int y, Color color) {
		GRect brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true);
		brick.setColor(color);
		brick.setFillColor(color);
		add(brick);
	}
	
	private void setUpPaddle() {
		print(HEIGHT - PADDLE_Y_OFFSET);
		paddle.setFilled(true);
		paddle.setColor(Color.BLACK);
		paddle.setFillColor(Color.BLACK);
		add(paddle);
	}
	
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		if (x > (WIDTH - PADDLE_WIDTH)) {
			x = (WIDTH - PADDLE_WIDTH - BRICK_SEP);
		} else if (x == 0) {
			x = BRICK_SEP;
		}
		paddle.setLocation(x, HEIGHT - (PADDLE_Y_OFFSET + PADDLE_HEIGHT));
	}
	
	/* Private instance variables */
	
	GRect paddle = new GRect((WIDTH - PADDLE_WIDTH)/2.0, HEIGHT - (PADDLE_Y_OFFSET + PADDLE_HEIGHT), PADDLE_WIDTH, PADDLE_HEIGHT);
}
