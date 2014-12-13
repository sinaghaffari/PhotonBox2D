package util;

/**
 * Created by Sina Ghaffari (sina.ghaffari321@gmail.com) on 10/12/14.
 *
 * @author Sina Ghaffari
 */
public class Color {
    private double red, green, blue, alpha;

    public Color( double red, double green, double blue, double alpha ) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color( double red, double green, double blue ) {
        this( red, green, blue, 1 );
    }

    public double getRed() {
        return red;
    }

    public void setRed( double red ) {
        this.red = red;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen( double green ) {
        this.green = green;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue( double blue ) {
        this.blue = blue;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha( double alpha ) {
        this.alpha = alpha;
    }
}
