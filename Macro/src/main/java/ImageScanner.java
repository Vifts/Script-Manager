import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static java.lang.System.*;

/**
 * @author BOLLEJEF, modified by Bret
 * Image scanner takes an image and checks if it exists on screen.
 */
public class ImageScanner {

    private int foundX = 0;
    private int foundY = 0;

    /**
     *
     * @param bi image to check for on screen
     * @return is the image on the screen currently?
     */
    public boolean isOnScreen(BufferedImage image, BufferedImage bi) {
        for(int x = 0; x< image.getWidth();x++) { //Base image x iteration...
            for(int y = 0; y< image.getHeight();y++) { //base image y iteration...

                boolean invalid = false; //set match flag to found...
                int x3 = x, y3 = y; //set place holders to current base image coords

                for(int x2 = 0;x2 <bi.getWidth(); x2++) { //comparison image x iteration...
                    y3 = y; //set place holder y to current base image coords
                    for(int y2 = 0; y2 <bi.getHeight(); y2++) { //comparison image y iteration...
                        //if comparison image pixel does not equal base image placeholders...
                        if(bi.getRGB(x2, y2) != image.getRGB(x3, y3)) {
                            invalid = true;
                            break;
                        } else {
                            y3++;
                        }
                    }
                    if(invalid) {
                        break;
                    } else {
                        x3++;
                    }

                }

                if(!invalid) { //If there is a match...
                    foundX = x3 - (bi.getWidth() / 2);
                    foundY = y3 - (bi.getHeight() / 2);
                    //out.println(x3 + ":" + y3);
                    return true;
                }
            }
        }
        return false; //If no image match is found...

    }

    public int getFoundX() {
        return foundX;
    }

    public int getFoundY() {
        return foundY;
    }
}