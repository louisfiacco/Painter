/*----------------------------------------------------------------
 *  Author: Louis Fiacc   
 *  Email: lwfiac22@g.holycross.edu
 *  Written: 11/06/2018
 *  
 *  A graphical, mouse-based painting program. User can use
 *  left-click to draw, and right-click to bucket-fill. A toolbar
 *  with buttons allows the user to select pen colors or quit.
 *
 *  Example:  java Painter
 *----------------------------------------------------------------*/


import GUI.*;
import java.awt.Color;

public class Painter extends AsyncEventAdapter {

	// ZOOM controls how zoomed-in the drawing canvas is. It's easiest
	// to see what is happening if we zoom by a factor of 8 or 16 or so.
	static final int ZOOM = 8;

	// WIDTH and HEIGHT control how large the drawing canvas is.
	static final int WIDTH = 60;
	static final int HEIGHT = 40;

	// WINDOW_WIDTH and WINDOW_HEIGHT are how large the entire window is,
	// including toolbar. We multiply by ZOOM because each dot drawn is actually
	// multiple pixels. To make room for the toolbar, we add 100 pixels to the
	// drawing canvas width.
	static final int WINDOW_WIDTH = WIDTH * ZOOM + 100;
	static final int WINDOW_HEIGHT = HEIGHT * ZOOM;

	// Windows, toolbar buttons, and the drawing widget.
	static Window window;
	static Button quit, color, small, medium, large;
	static CanvasWidget drawing;

	// The canvas variable refers to the large drawing canvas on the left side.
	static Canvas canvas;

	// The main function creates the Painter window, shows it on the
	// screen, waits for the user to close it, then prints a goodbye message.
	public static void main(String args[]) {
		String username = System.getenv("USER");
		StdOut.println("~~=== Welcome to Painter, " + username + "! ===~~");

		window = new Window("Painter!", WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setBackgroundColor(Canvas.DARK_GRAY);

		// Add the toolbar buttons.
		quit = new Button(WINDOW_WIDTH-90, 10, 80, 30, "Quit");
		color = new Button(WINDOW_WIDTH-90, 50, 80, 30, "Color");
		small = new Button(WINDOW_WIDTH-90, 90, 80, 30, "Small");
		medium = new Button(WINDOW_WIDTH-90, 130, 80, 30, "Medium");
		large = new Button(WINDOW_WIDTH-90, 170, 80, 30, "Large");
		window.add(quit);
		window.add(color);
		window.add(small);
		window.add(medium);
		window.add(large);

		// Add the drawing area on the left side.
		drawing = new CanvasWidget(0, 0, WIDTH*ZOOM, HEIGHT*ZOOM, WIDTH, HEIGHT);
		window.add(drawing);
		canvas = drawing.canvas;

		// Register a call-back to receive mouse events.
		window.addListener(new Painter());

		window.showAndAnimate(30);
		StdOut.println("All done, goodbye!");
		System.exit(0);
	}

	// This function is called whenever the user does a mouse single-click.
	public void mouseWasClicked(double x, double y, String button) {
		if (button.equals("left") && quit.containsPoint(x, y)) {
			window.hide();
		} else if (button.equals("left") && small.containsPoint(x, y)) {
			drawing.canvas.setPenRadius(1);
		} else if (button.equals("left") && medium.containsPoint(x, y)) {
			drawing.canvas.setPenRadius(3);
		} else if (button.equals("left") && large.containsPoint(x, y)) {
			drawing.canvas.setPenRadius(6);
		} else if (button.equals("left") && color.containsPoint(x, y)) {
			drawing.showColorPicker();
		} else if (button.equals("left") && drawing.containsPoint(x, y)) {
			// Each drawing dot is multiple pixels, so we divide the x and y
			// values by ZOOM to figure out which drawing coordinate was
			// clicked.
			int xx = (int)(x/ZOOM);
			int yy = (int)(y/ZOOM);
			respondToLeftClick(xx, yy);
		} else if (button.equals("right") && drawing.containsPoint(x, y)) {
			// Each drawing dot is multiple pixels, so we divide the x and y
			// values by ZOOM to figure out which drawing coordinate was
			// clicked.
			int xx = (int)(x/ZOOM);
			int yy = (int)(y/ZOOM);
			respondToRightClick(xx, yy);
		}
	}

	// This function is called when the user starts pressing a mouse button.
	public void mouseWasPressed(double x, double y, String button) {
		if (drawing.containsPoint(x, y))
			mouseWasClicked(x, y, button);
	}

	// This function is called repeatedly while the user drags the mouse while
	// holding a button.
	public void mouseWasDragged(double x, double y) {
		if (drawing.containsPoint(x, y))
			mouseWasClicked(x, y, "left");
	}

	// This function is called whenever the user left-clicks within the drawing canvas.
	public static void respondToLeftClick(int x, int y) {
		// The user left-clicked the mouse at coordinates (x, y),
		// so paint that pixel using the current pen color.
		// But only do this if the pixel is not already painted that color.
		if (!canvas.colorAt(x, y).equals(canvas.getPenColor())) {
			canvas.point(x, y);
		}
	}
	
	// This function is called whenever the user right-clicks within the drawing canvas.
	public static void respondToRightClick(int x, int y) {
		// The user right-clicked the mouse at coordinates (x, y),
		// so fill that entire area using the current pen color.
		// But only do this if the pixel is not already painted that color.
	
        Color prevC = canvas.colorAt(x,y);
        Color newC = canvas.getPenColor();
        bucketFill (x, y, prevC, newC);
	}

  
    public static void bucketFill(int x, int y, Color prevC, Color newC) {
        if ((x >= 60 || x < 0) || (y >= 40 || y < 0)){
            return;
        }
        if (canvas.colorAt(x,y).equals(prevC)){
            canvas.point(x,y);
        
            bucketFill(x - 1, y, prevC, newC);
            bucketFill(x + 1, y, prevC, newC);
            bucketFill(x, y - 1, prevC, newC);
            bucketFill(x, y + 1, prevC, newC);
        }

    }

}
