import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;

public class Lab4 extends GameEngine {
	// Main Function
	public static void main(String args[]) {
		// Warning: Only call createGame in this function
		// Create a new Lab4
		createGame(new Lab4());
	}

	//-------------------------------------------------------
	// Game
	//-------------------------------------------------------
	
	// Booleans to keep track of whether a key is pressed or not
	boolean left, right, up;
	boolean gameOver;

	//-------------------------------------------------------
	// Spaceship
	//-------------------------------------------------------

	// Spaceship position & Velocity
	double spaceshipPositionX, spaceshipPositionY;
	double spaceshipVelocityX, spaceshipVelocityY;

	// Spaceship angle
	double spaceshipAngle;

	// Code to update 'move' the spaceship
	public void updateSpaceship(double dt) {
		if(up == true) {
			// Increase the velocity of the spaceship
			// as determined by the angle
			spaceshipVelocityX +=  sin(spaceshipAngle) * 250 * dt;
			spaceshipVelocityY += -cos(spaceshipAngle) * 250 * dt;
		}

		// If the user is holding down the left arrow key
		// Make the spaceship rotate anti-clockwise
		if(left == true) {
			spaceshipAngle -= 180 * dt;
		}

		// If the user is holding down the right arrow key
		// Make the spaceship rotate clockwise
		if(right == true) {
			spaceshipAngle += 180 * dt;
		}

		// Make the spaceship move forward
		spaceshipPositionX += spaceshipVelocityX * dt;
		spaceshipPositionY += spaceshipVelocityY * dt;
		
	}

	// Function to draw the spaceship
	public void drawSpaceship() {
		// Set the color to white
		changeColor(white);

		// Save the current transform
		saveCurrentTransform();

		// ranslate to the position of the asteroid
		translate(spaceshipPositionX, spaceshipPositionY);

		// Rotate the drawing context around the angle of the asteroid
		rotate(spaceshipAngle);

		// Draw the actual spaceship
		drawLine(-10,  10,  10,  10);
		drawLine( 10,  10,   0, -20);
		drawLine(  0, -20, -10,  10);

		// Restore last transform to undo the rotate and translate transforms
		restoreLastTransform();
	}

	//-------------------------------------------------------
	// Laser
	//-------------------------------------------------------

	// Laser position & Velocity
	double laserPositionX, laserPositionY;
	double laserVelocityX, laserVelocityY;

	// Laser Angle
	double laserAngle;

	// Laser active
	boolean laserActive = false;

	// Function to shoot a new laser
	public void fireLaser() {
		// Set the laser position as the current spaceship position
		laserPositionX = spaceshipPositionX;
		laserPositionY = spaceshipPositionY;

		// And make it move in the same direction as the spaceship is facing
		laserVelocityX =  sin(spaceshipAngle) * 250;
		laserVelocityY = -cos(spaceshipAngle) * 250;

		// And face the same direction as the spaceship
		laserAngle = spaceshipAngle;

		// Set it to active
		laserActive = true;
	}

	// Function to update 'move' the laser
	public void updateLaser(double dt) {
		// Update the laser
	}

	// Function to draw the laser
	public void drawLaser() {
		// Draw the laser
	}

	//-------------------------------------------------------
	// Asteroid
	//-------------------------------------------------------

	// Asteroid Position & Velocity
	double asteroidPositionX, asteroidPositionY;
	double asteroidVelocityX, asteroidVelocityY;

	// Asteroid Radius
	double asteroidRadius;

	public void randomAsteroid() {
		// Generate a random asteroid
		asteroidPositionX = rand(500);
		asteroidPositionY = rand(500);

		asteroidVelocityX = -50 + rand(100);
		asteroidVelocityY = -50 + rand(100);
	}

	// Function to update 'move' the asteroid
	public void updateAsteroid(double dt) {
		// Update the asteroid
	}

	// Function to draw the asteroid
	public void drawAsteroid() {
		// Draw the asteroid
	}

	//-------------------------------------------------------
	// Game
	//-------------------------------------------------------

	public void init() {
		// Initialise game boolean
		gameOver = false;

		// Initialise key booleans
		left  = false;
		right = false;
		up    = false;

		// Initialise Spaceship
		spaceshipPositionX = 250; spaceshipPositionY = 250;
		spaceshipVelocityX = 0;   spaceshipVelocityY = 0;
		spaceshipAngle = 0;

		// Initialise Laser
		laserPositionX = 0; laserPositionY = 0;
		laserVelocityX = 0; laserVelocityY = 0;
		laserAngle = 0;
		laserActive = false;
		
		// Initialise Asteroid
		asteroidPositionX = 0; asteroidPositionY = 0;
		asteroidVelocityX = 0; asteroidVelocityY = 0;
		asteroidRadius = 20;
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

		//-------------------------------------------------------
		// Add code to check for collisions
		//-------------------------------------------------------
	}

	// This gets called any time the Operating System
	// tells the program to paint itself
	public void paintComponent() {
		// Clear the background to black
		changeBackgroundColor(black);
		clearBackground(500, 500);

		// If the game is not over yet
		if(gameOver == false) {
			// Paint the Spaceship
			drawSpaceship();

			// Paint the Asteroid
			drawAsteroid();

			// Paint the laser (if it's active)
			drawLaser();
		} else {
			// If the game is over
			// Display GameOver text
			changeColor(white);
			drawText(85, 250, "GAME OVER!", "Arial", 50);
		}
	}

	// Called whenever a key is pressed
	public void keyPressed(KeyEvent e) {
		// The user pressed left arrow
		if(e.getKeyCode() == KeyEvent.VK_LEFT)  {
			left  = true;
		}
		// The user pressed right arrow
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = true;
		}
		// The user pressed up arrow
		if(e.getKeyCode() == KeyEvent.VK_UP)    {
			up    = true;
		}
	}

	// Called whenever a key is released
	public void keyReleased(KeyEvent e) {
		// The user released left arrow
		if(e.getKeyCode() == KeyEvent.VK_LEFT)  {
			left  = false;
		}
		// The user released right arrow
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = false;
		}
		// The user released up arrow
		if(e.getKeyCode() == KeyEvent.VK_UP)    {
			up    = false;
		}
	}
}