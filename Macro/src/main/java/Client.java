import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import static java.lang.System.*;

/**
 * @author Bret & Frank
 *
 * Abstract Parent that defines a client that will have the basic structure to
 * read its portion of the screen(known as area of responsibility) and click
 * the mouse within that area.
 */

public abstract class Client implements Runnable {


    public Robot rob = new Robot(); //Robot instance used for screen scanning
    private static volatile Robot mouse;

    static {
        try {
            mouse = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public ImageScanner scan = new ImageScanner();
    

    private static Hashtable<String, BufferedImage>
            hmRefImages = new Hashtable<String, BufferedImage>();

    private Rectangle clientArea; // The area of responsibility for this client.

    // The offset values used to find actual coordinates
    private int xOffset;
    private int yOffset;

    BufferedImage clientAreaImage; // The images to be checked for

    public static final int TICK_TIME = 500; // Time in miliseconds for each tick iteration
    public static final int TICK_MULTI = 1000 / TICK_TIME; // Used to scale ticks to seconds
    public static int mouseSpeedRandom;//Total steps used in mouseGlide from start to destination
	public static final int MOUSE_TIME = 10;//Time used in mouseGlide

    /**
     * Client is a class meant to be parent to script classes
     * @param x1 Top left x coordinate of client window
     * @param y1 Top left y coordinate of client window
     * @param x2 Bottom right x coordinate of client window
     * @param y2 bottom right y coordinate of client windows
     * @throws AWTException, IOException
     */

    /**
     * mouseGlide is used to randomize mouse speed to destination
     * @param x1 Starting X coordinate of mouse position
     * @param y1 Starting Y coordinate of mouse position
     * @param x2 Final X coordinate of mouse destination
     * @param y2 Final Y coordinate of mouse destination
     * @throws AWTException, InterruptedException
     */

  	public static void mouseGlide(double x1, double y1, double x2, double y2, int t, int s) {
          try {
              Robot r = new Robot();
              double dx = (x2 - x1) / ((double) s);
              double dy = (y2 - y1) / ((double) s);
              double dt = t / ((double) s);
              for (int step = 1; step <= s; step++) {
                  Thread.sleep((int) dt);
                  r.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
              }
          } catch (AWTException e) {
              e.printStackTrace();
          } catch (InterruptedException e) {
              e.printStackTrace();
          } 
  	}
  	//Get current X position of mouse
  	public static double mX(){
			return (MouseInfo.getPointerInfo().getLocation().getX());
	}
  //Get current Y position of mouse
  	public static double mY(){
		return (MouseInfo.getPointerInfo().getLocation().getY());
}
    
    public Client(int x1, int y1, int x2, int y2) throws AWTException, IOException {
    	x1 = 0;
    	y1 = 0;
    	x2 = 1920;
    	y2 = 1080;
        int width = x2 - x1;
        int height = y2 - y1;
        clientArea = new Rectangle(x1, y1, width, height);
        xOffset = x1;
        yOffset = y1;
        loadResourceHash();
    }

    /**
     * Uses class Robot instance to click on screen using Operating System mouse.
     * @param x horizontal coordinate of the screen
     * @param y vertical coordinate of the screen
     */
    public static synchronized void leftClickOnLocation(int x, int y) {
        mouseGlide(mX(),mY(),x,y,MOUSE_TIME,mouseSpeedRandom);
        mouse.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        mouse.delay(50);
        mouse.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }public static synchronized void rightClickOnLocation(int x, int y) {
        mouseGlide(mX(),mY(),x,y,MOUSE_TIME,mouseSpeedRandom);
        mouse.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        mouse.delay(50);
        mouse.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uses ImageScanner to check if the reference image is in the Area of Responsibility
     * @param key key for desired reference image
     * @return if reference is on screen or not
     */
    public boolean isInAOR(String key) {
        return scan.isOnScreen(getClientAreaImage(), getReferenceImage(key));
    }

    /**
     * Adds reference images and text keys to hash
     */
    private synchronized void loadResourceHash() throws IOException {
        if(hmRefImages.isEmpty()) {
            out.println("Loading Resources...");
            hmRefImages.put("autoOff", ImageIO.read(new File("res/autoOff.png")));

        }
    }

    /**
     * Accessor method for the hash containing the reference images
     * @param key for the desired hash value
     * @return image for the entered key
     */
    public BufferedImage getReferenceImage(String key) {
        return hmRefImages.get(key);
    }

    /**
     * Used to get a screenshot of the area of responsibility of the client
     * @return screen shot of the area of responsibility
     */
    public BufferedImage getClientAreaImage() {
        return rob.createScreenCapture(clientArea);
    }

    /**
     * Used to get the x coordinate relative to entire screen
     * @param x x coordinate of relative to entire screen
     * @return screen x coordinate
     */
    public int getRelativeX(int x) {
        return x + xOffset;
    }

    /**
     * Used to get the y coordinate relative to entire screen
     * @param y y coordinate of relative to entire screen
     * @return screen y coordinate
     */
    public int getRelativeY(int y) {
        return y + yOffset;
    }

}