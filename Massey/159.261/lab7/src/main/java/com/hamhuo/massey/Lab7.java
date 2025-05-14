package com.hamhuo.massey;

import com.hamhuo.massey.GameEngine;

import java.awt.*;
import java.awt.event.*;

import static com.hamhuo.massey.GameEngine.createGame;

public class Lab7 extends GameEngine {
	// Main Function
	public static void main(String args[]) {
		// Warning: Only call createGame in this function
		// Create a new Lab7
		createGame(new Lab7());
	}

	//-------------------------------------------------------
	// Spaceship
	//-------------------------------------------------------

	// Image of the spaceship
	Image spaceshipImage;
	Image spaceshipEngine;
	Image spaceshipLeft;
	Image spaceshipRight;

	// Spaceship position
	double spaceshipPositionX;
	double spaceshipPositionY;
	
	// Spaceship velocity
	double spaceshipVelocityX;
	double spaceshipVelocityY;

	// Spaceship angle
	double spaceshipAngle;

	// Init Spaceship Function
	public void initSpaceship() {
		// Load the spaceship sprite
		spaceshipImage    = subImage(spritesheet,   0,   0, 240, 240);
		spaceshipEngine   = subImage(spritesheet,   0, 240, 240, 240);
		spaceshipLeft     = subImage(spritesheet, 240, 240, 240, 240);
		spaceshipRight    = subImage(spritesheet, 480, 240, 240, 240);

		// Setup Spaceship variables
		spaceshipPositionX = width()/2;
		spaceshipPositionY = height()/2;
		spaceshipVelocityX = 0;
		spaceshipVelocityY = 0;
		spaceshipAngle     = 0;
	}

	// Function to draw the spaceship
	public void drawSpaceship() {
		// Save the current transform
		saveCurrentTransform();

		// Translate to the position of the asteroid
		translate(spaceshipPositionX, spaceshipPositionY);

		// Rotate the drawing context around the angle of the asteroid
		rotate(spaceshipAngle);

		// Draw the actual spaceship
		drawImage(spaceshipImage, -30, -30, 60, 60);

		// Draw left thruster
		if(left) {
			drawImage(spaceshipLeft,  -30, -30, 60, 60);
		}

		// Draw right thruster
		if(right) {
			drawImage(spaceshipRight, -30, -30, 60, 60);
		}

		// Draw main engine
		if(up) {
			drawImage(spaceshipEngine, -30, -15, 60, 60);
		}

		// Restore last transform to undo the rotate and translate transforms
		restoreLastTransform();
	}

	// Code to update 'move' the spaceship
	public void updateSpaceship(double dt) {
		if(up == true) {
			// Increase the velocity of the spaceship
			// as determined by the angle
			spaceshipVelocityX += sin(spaceshipAngle) * 250 * dt;
			spaceshipVelocityY -= cos(spaceshipAngle) * 250 * dt;
		}

		// If the user is holding down the left arrow key
		if(left == true) {
			// Make the spaceship rotate anti-clockwise
			spaceshipAngle -= 250 * dt;
		}

		// If the user is holding down the right arrow key
		if(right == true) {
			// Make the spaceship rotate clockwise
			spaceshipAngle += 250 * dt;
		}

		// Make the spaceship move forward
		spaceshipPositionX += spaceshipVelocityX * dt;
		spaceshipPositionY += spaceshipVelocityY * dt;

		// If the spaceship reaches the right edge of the screen
		// 'Warp' it back to the left edge
		if(spaceshipPositionX > width())  {spaceshipPositionX -= width();}

		// If the spaceship reaches the left edge of the screen
		// 'Warp' it back to the right edge
		if(spaceshipPositionX < 0)        {spaceshipPositionX += width();}

		// If the spaceship reaches the top edge of the screen
		// 'Warp' it back to the bottom edge
		if(spaceshipPositionY > height()) {spaceshipPositionY -= height();}

		// If the spaceship reaches the bottom edge of the screen
		// 'Warp' it back to the top edge
		if(spaceshipPositionY < 0)        {spaceshipPositionY += height();}
	}


	//-------------------------------------------------------
	// Laser
	//-------------------------------------------------------

	// Image of the laser
	Image laserImage;

	// Maximum number of lasers
	int maxLasers;

	// Laser position
	double[] laserPositionX;
	double[] laserPositionY;

	// Laser velocity
	double[] laserVelocityX;
	double[] laserVelocityY;

	// Laser Angle
	double[] laserAngle;

	// Laser active
	boolean[] laserActive;

	// Initialise Laser
	public void initLaser() {
		// Set max lasers
		maxLasers = 3;

		// Allocate arrays
		laserPositionX = new double[maxLasers];
		laserPositionY = new double[maxLasers];

		laserVelocityX = new double[maxLasers];
		laserVelocityY = new double[maxLasers];

		laserAngle = new double[maxLasers];
		laserActive = new boolean[maxLasers];

		// Make lasers inactive
		for(int i = 0; i < maxLasers; i++) {
			laserActive[i] = false;
		}

		// Load laser image
		laserImage = subImage(spritesheet, 240, 0, 240, 240);
	}

	// Function to shoot a new laser
	public void fireLaser() {
		// For all alsers
		for(int i= 0; i < maxLasers; i++) {
			// Can only fire a laser if there isn't already one active
			if(laserActive[i] == false) {
				// Set the laser position as the current spaceship position
				laserPositionX[i] = spaceshipPositionX;
				laserPositionY[i] = spaceshipPositionY;

				// And make it move in the same direction as the spaceship is facing
				laserVelocityX[i] =  sin(spaceshipAngle) * 250;
				laserVelocityY[i] = -cos(spaceshipAngle) * 250;

				// And face the same direction as the spaceship
				laserAngle[i] = spaceshipAngle;

				// Set it to active
				laserActive[i] = true;

				// Break
				break;
			}
		}
	}

	// Function to draw the laser
	public void drawLaser() {
		for(int i = 0; i < maxLasers; i++) {
			// Only draw the laser if it's active
			if(laserActive[i]) {
				// Save the current transform
				saveCurrentTransform();

				// ranslate to the position of the laser
				translate(laserPositionX[i], laserPositionY[i]);

				// Rotate the drawing context around the angle of the laser
				rotate(laserAngle[i]);

				// Draw the actual laser
				drawImage(laserImage, -30, -30, 60, 60);

				// Restore last transform to undo the rotate and translate transforms
				restoreLastTransform();
			}
		}
	}

	// Function to update 'move' the laser
	public void updateLaser(double dt) {
		for(int i = 0; i < maxLasers; i++) {
			// Move the Laser
			laserPositionX[i] += laserVelocityX[i] * dt;
			laserPositionY[i] += laserVelocityY[i] * dt;

			// If the laser reaches the left edge of the screen
			// Destroy the laser
			if(laserPositionX[i] < 0)         {laserActive[i] = false;}

			// If the laser reaches the right edge of the screen
			// Destroy the laser
			if(laserPositionX[i] >= width())  {laserActive[i] = false;}

			// If the laser reaches the top edge of the screen
			// Destroy the laser
			if(laserPositionY[i] < 0)         {laserActive[i] = false;}

			// If the laser reaches the bottom edge of the screen
			// Destroy the laser
			if(laserPositionY[i] >= height()) {laserActive[i] = false;}
		}
	}

	//-------------------------------------------------------
	// Asteroid
	//-------------------------------------------------------

	// Image of the asteroid
	Image asteroidImage;

	// Asteroid Position
	double asteroidPositionX;
	double asteroidPositionY;

	double asteroidVelocityX;
	double asteroidVelocityY;

	double asteroidAngle;
	double asteroidRadius;

	// Initialise Asteroid
	public void initAsteroid() {
		// Load image
		asteroidImage = subImage(spritesheet, 480, 0, 240, 240);
	}

	// Randomly position Asteroid
	public void randomAsteroid() {
		// Random position
		asteroidPositionX = rand(width());
		asteroidPositionY = rand(height());

		// Check that it's not too close to the spaceship
		while(distance(asteroidPositionX, asteroidPositionY, spaceshipPositionX, spaceshipPositionY) < asteroidRadius*2) {
			// Try a new random position
			asteroidPositionX = rand(width());
			asteroidPositionY = rand(height());
		}

		// Random Velocity
		asteroidVelocityX = -50 + rand(100);
		asteroidVelocityY = -50 + rand(100);

		// Random Angle
		asteroidAngle = rand(360);

		// Fixed Radius
		asteroidRadius = 30;
	}

	// Function to update 'move' the asteroid
	public void updateAsteroid(double dt) {
		// Move the asteroid
		asteroidPositionX += asteroidVelocityX * dt;
		asteroidPositionY += asteroidVelocityY * dt;

		// If the asteroid reaches the left edge of the screen
		// 'Warp' it back to the other side of the screen
		if(asteroidPositionX < 0)         {asteroidPositionX += width();}

		// If the asteroid reaches the right edge of the screen
		// 'Warp' it back to the other side of the screen
		if(asteroidPositionX >= width())  {asteroidPositionX -= width();}

		// If the asteroid reaches the top edge of the screen
		// 'Warp' it back to the other side of the screen
		if(asteroidPositionY < 0)         {asteroidPositionY += height();}

		// If the asteroid reaches the bottom edge of the screen
		// 'Warp' it back to the other side of the screen
		if(asteroidPositionY >= height()) {asteroidPositionY -= height();}
	}

	// Function to draw the asteroid
	public void drawAsteroid() {
		// Save the current transform
		saveCurrentTransform();

		// ranslate to the position of the asteroid
		translate(asteroidPositionX, asteroidPositionY);

		// Rotate the drawing context around the angle of the asteroid
		rotate(asteroidAngle);

		// Draw the actual asteroid
		drawImage(asteroidImage, -30, -30, 60, 60);

		// Restore last transform to undo the rotate and translate transforms
		restoreLastTransform();
	}

	//-------------------------------------------------------
	// Explosion
	//-------------------------------------------------------
	// Images for the explosion
	Image[] explosionImages = new Image[32];

	// Position of the explosion
	double explosionPositionX;
	double explosionPositionY;

	// Timer for the explosion
	double explosionTimer;
	double explosionDuration;

	boolean explosionActive;

	// Initialise Variables for explosion
	public void initExplosion() {
		// Load explosion sprites
		int n = 0;
		for(int y = 960; y < 1920; y += 240) {
			for(int x = 0; x < 1920; x += 240) {
				explosionImages[n] = subImage(spritesheet, x, y, 240, 240);
				n++;
			}
		}
	}

	// Create an explosion at position x,y
	public void createExplosion(double x, double y) {
		// Position the explosion
		explosionPositionX = x;
		explosionPositionY = y;

		// Start new explosion
		explosionTimer = 0;
		explosionDuration = 1.0;
		explosionActive = true;
	}

	// Function to update the explosion
	public void updateExplosion(double dt) {
		// If the explosion is active
		if(explosionActive) {
			// Increment timer
			explosionTimer += dt;

			// Check to see if explosion has finished
			if(explosionTimer >= explosionDuration) {
				explosionActive = false;
			}
		}
	}

	// Function to get frame of animation
	public int getAnimationFrame(double timer, double duration, int numFrames) {
		// Get frame
		int i = (int)floor(((timer % duration) / duration) * numFrames);
		// Check range
		if(i >= numFrames) {
			i = numFrames-1;
		}
		// Return
		return i;
	}

	// Function to draw the explosion
	public void drawExplosion() {
		// Select the right image
		if(explosionActive) {
			// Save the current transform
			saveCurrentTransform();

			// translate to the position of the asteroid
			translate(explosionPositionX, explosionPositionY);

			// Draw the explosion frame
			int i = getAnimationFrame(explosionTimer, explosionDuration, 30);
			drawImage(explosionImages[i], -30, -30, 60, 60);

			// Restore last transform to undo the rotate and translate transforms
			restoreLastTransform();
		}
	}

	//-------------------------------------------------------
	// Alien
	//-------------------------------------------------------
	double alienPositionX, alienPositionY;
	double alienVelocityX, alienVelocityY;
	double alienAngle;
	Image alienImage;

	// Initialise Alien
	public void initAlien() {
		// Load Image
		alienImage = subImage(spritesheet, 0, 480, 240, 240);
	}

	// Randomly place a new alien
	public void randomAlien() {
		// Random Position
		alienPositionX = rand(width());
		alienPositionY = rand(height());

		// Check that it's not too close to the spaceship
		while(distance(alienPositionX, alienPositionY, spaceshipPositionX, spaceshipPositionY) < 100) {
			// Try a new random position
			alienPositionX = rand(width());
			alienPositionY = rand(height());
		}

		// No Velocity
		alienVelocityX = 0;
		alienVelocityY = 0;
	}

	// Update the Alien
	public void updateAlien(double dt) {
		// Three path distances
		double d1, d2, d3;

		// Calculate three paths - X
		d1 = abs(spaceshipPositionX           - alienPositionX);
		d2 = abs((spaceshipPositionX-width()) - alienPositionX);
		d3 = abs((spaceshipPositionX+width()) - alienPositionX);

		// Select shortest path - X
		if(d1 < d2 && d1 < d3) {
			alienVelocityX = spaceshipPositionX           - alienPositionX;
		} else if(d2 < d1 && d2 < d3) {
			alienVelocityX = (spaceshipPositionX-width()) - alienPositionX;
		} else {
			alienVelocityX = (spaceshipPositionX+width()) - alienPositionX;
		}

		// Calculate three paths - Y
		d1 = abs(spaceshipPositionY            - alienPositionY);
		d2 = abs((spaceshipPositionY-height()) - alienPositionY);
		d3 = abs((spaceshipPositionY+height()) - alienPositionY);

		// Select shortest path - Y
		if(d1 < d2 && d1 < d3) {
			alienVelocityY = spaceshipPositionY            - alienPositionY;
		} else if(d2 < d1 && d2 < d3) {
			alienVelocityY = (spaceshipPositionY-height()) - alienPositionY;
		} else {
			alienVelocityY = (spaceshipPositionY+height()) - alienPositionY;
		}

		double l = length(alienVelocityX, alienVelocityY);

		// Rescale
		alienVelocityX *= 50/l;
		alienVelocityY *= 50/l;

		// Calculate Alien Angle
		alienAngle = -atan2(alienVelocityX, alienVelocityY);

		// Move Alien
		alienPositionX += alienVelocityX * dt;
		alienPositionY += alienVelocityY * dt;

		// If the Alien reaches the left edge of the screen
		// 'Warp' it back to the other side of the screen
		if(alienPositionX < 0)         {alienPositionX += width();}

		// If the Alien reaches the right edge of the screen
		// 'Warp' it back to the other side of the screen
		if(alienPositionX >= width())  {alienPositionX -= width();}

		// If the Alien reaches the top edge of the screen
		// 'Warp' it back to the other side of the screen
		if(alienPositionY < 0)         {alienPositionY += height();}

		// If the Alien reaches the bottom edge of the screen
		// 'Warp' it back to the other side of the screen
		if(alienPositionY >= height()) {alienPositionY -= height();}
	}

	// Draw Alien on the screen
	public void drawAlien() {
		// Save the current transform
		saveCurrentTransform();

		// ranslate to the position of the asteroid
		translate(alienPositionX, alienPositionY);

		// Rotate the drawing context around the angle of the asteroid
		rotate(alienAngle);

		// Draw the actual asteroid
		drawImage(alienImage, -30, -30, 60, 60);

		// Restore last transform to undo the rotate and translate transforms
		restoreLastTransform();
	}

	//-------------------------------------------------------
	// Game
	//-------------------------------------------------------

	// Spritesheet
	Image spritesheet;

	// Keep track of keys
	boolean left, right, up, down;
	boolean gameOver;

	// Function to initialise the game
	public void init() {
		// Load sprites
		spritesheet = loadImage("Lab7/src/spritesheet.png");
		
		// Setup booleans
		left  = false;
		right = false;
		up    = false;
		down  = false;

		gameOver = false;

		// Initialise Spaceship
		initSpaceship();

		// Setup Laser
		initLaser();

		// Init Asteroid
		initAsteroid();

		// Randomise Asteroid
		randomAsteroid();

		// Init Explosion
		initExplosion();

		// Init Alien
		initAlien();
	}

	// Updates the display
	public void update(double dt) {
		// If the game is over
		if(gameOver == true) {
			// Don't try to update anything.
			return;
		}

		// Update the spaceship
		updateSpaceship(dt);
		
		// Update the laser
		updateLaser(dt);

		// Update Asteroid
		updateAsteroid(dt);

		// Update Explosion
		updateExplosion(dt);

		// Update Alien
		updateAlien(dt);

		// Detect Collision between Laser and Asteroid
		for(int i = 0; i < maxLasers; i++) {
			if(laserActive[i] == true) {
				if(distance(laserPositionX[i], laserPositionY[i], asteroidPositionX, asteroidPositionY) < asteroidRadius*1.2) {
					// Destroy the laser
					laserActive[i] = false;

					// Create an explosion
					createExplosion(asteroidPositionX, asteroidPositionY);

					// Create a new random Asteroid
					randomAsteroid();
				}
			}
		}

		// Detect Collision between Laser and Alien
		for(int i = 0; i < maxLasers; i++) {
			if(laserActive[i] == true) {
				if(distance(laserPositionX[i], laserPositionY[i], alienPositionX, alienPositionY) < 30*1.2) {
					// Destroy the laser
					laserActive[i] = false;

					// Create an explosion
					createExplosion(alienPositionX, alienPositionY);

					// Create a new random Alien
					randomAlien();
				}
			}
		}

		// Detect Collision between Alien and Asteroid
		if(distance(alienPositionX, alienPositionY, asteroidPositionX, asteroidPositionY) < asteroidRadius+30) {
			// Create an explosion
			createExplosion(alienPositionX, alienPositionY);

			// Create a new random Alien
			randomAlien();

			// Create a new random Asteroid
			randomAsteroid();
		}

		// Detect Collision between Spaceship and Asteroid
		if(distance(spaceshipPositionX, spaceshipPositionY, asteroidPositionX, asteroidPositionY) < asteroidRadius+30) {
			// Collision!
			gameOver = true;
		}

		// Detect Collision between Spaceship and Alien
		if(distance(spaceshipPositionX, spaceshipPositionY, alienPositionX, alienPositionY) < 60) {
			// Collision!
			gameOver = true;
		}
	}

	// This gets called any time the Operating System
	// tells the program to paint itself
	public void paintComponent() {
		// Clear the background to black

		changeBackgroundColor(black);
		clearBackground(width(), height());

		// If the game is not over yet
		if(gameOver == false) {
			// Draw the Asteroid
			changeColor(white);
			drawAsteroid();

			// Draw the laser (if it's active)
			changeColor(white);
			drawLaser();

			// Draw the Spaceship
			drawSpaceship();

			// Draw the Explosion
			drawExplosion();

			// Draw the Alien
			drawAlien();
		} else {
			// If the game is over
			// Display GameOver text
			changeColor(white);
			drawText(width()/2-165, height()/2, "GAME OVER!", "Arial", 50);
		}
	}

	// Called whenever a key is pressed
	public void keyPressed(KeyEvent e) {
		//T he user pressed left arrow
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			// Record it
			left = true;
		}
		// The user pressed right arrow
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// Record it
			right = true;
		}
		// The user pressed up arrow
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			// Record it
			up = true;
		}
		// The user pressed space bar
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			// Fire the laser
			fireLaser();
		}
	}

	// Called whenever a key is released
	public void keyReleased(KeyEvent e) {
		// The user released left arrow
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			// Record it
			left = false;
		}
		// The user released right arrow
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// Record it
			right = false;
		}
		// The user released up arrow
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			// Record it
			up = false;
		}
	}
}
