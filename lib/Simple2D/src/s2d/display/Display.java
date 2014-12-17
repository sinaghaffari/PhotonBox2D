/*
 * Copyright (C) 2014  Sina Ghaffari (sina.ghaffari@mail.utoronto.ca) & Tristan Homsi (thomsi@uwaterloo.ca)
 * 
 * This file is part of Simple2D.

 * Simple2D is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Simple2D is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Simple2D.  If not, see <http://www.gnu.org/licenses/>.
 */
package s2d.display;

import net.beadsproject.beads.core.AudioContext;
import s2d.input.Keyboard;
import s2d.input.Mouse;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A window which handles mouse and keyboard input as well as rendering tasks.
 *
 * @author Sina Ghaffari
 * @version 1.0
 * @since 1.0
 */
public class Display {
    private static AudioContext ac;
    private static Robot rb;
    private static DisplayContainer mainFrame;
    private static InnerDisplay mainComponent;
    private static Image backBuffer;
    private static Graphics2D g;
    private static boolean isDisplayCreated;
    private static int width;
    private static int height;
    private static double syncDelta = 0;
    private static long lastSyncFrame = 0;
    private static long deltaTime;
    private static double relativePerformance;
    private static long frameCount = 0;
    private static File outputDirectory = null;
    private static boolean shouldRenderScreen = true;

    /**
     * Creates and opens a display.
     *
     * @param w Width of the display.
     * @param h Height of the display.
     */
    public static void createDisplay( int w, int h ) {
        if ( !isDisplayCreated ) {
            width = w;
            height = h;
            mainFrame = new DisplayContainer();
            mainComponent = new InnerDisplay();
            mainFrame.getContentPane().setPreferredSize( new Dimension( width, height ) );
            mainComponent.setSize( width, height );
            mainFrame.add( mainComponent );
            mainFrame.pack();
            mainFrame.setLocationRelativeTo( null );
            mainFrame.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
            mainFrame.setVisible( true );
            mainComponent.initInnerDisplay();
            Mouse.attachMouse( mainComponent );
            backBuffer = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage( Display.getWidth(), Display.getHeight(), Transparency.OPAQUE );
            g = (Graphics2D) backBuffer.getGraphics();
            ac = new AudioContext();
            ac.start();
            try {
                rb = new Robot();
            } catch ( AWTException e ) {
            }
            lastSyncFrame = System.nanoTime();
            deltaTime = 0;
            relativePerformance = 0;
            isDisplayCreated = true;
        }
    }

    /**
     * The graphics object that gets drawn to the front buffer. <br>
     * Use this for rendering.
     *
     * @return The {@link java.awt.Graphics2D} object used to render to the screen.
     */
    public static Graphics2D getGraphics() {
        return (Graphics2D) g.create();
    }

    /**
     * The main audio context used to process all sounds. <br>
     * Use this to play and process sounds.
     *
     * @return the {@link AudioContext} object used to process and play sounds.
     */
    public static AudioContext getAudioContext() {
        return ac;
    }

    /**
     * @return The robot that is attributed to the current screen and display.
     */
    public static Robot getRobot() {
        return rb;
    }

    /**
     * @return Whether or not the native window can be resized.
     */
    public static boolean isResizable() {
        return mainFrame.isResizable();
    }

    /**
     * Sets whether or not the native window can be resized or not.
     *
     * @param b
     */
    public static void setResizable( boolean b ) {
        mainFrame.setResizable( b );
    }

    /**
     * @return Whether or not the native window is visible.
     */
    public static boolean isVisible() {
        return mainFrame.isVisible();
    }

    /**
     * Sets whether or not the native window is visible.
     *
     * @param b
     */
    public static void setVisible( boolean b ) {
        mainFrame.setVisible( b );
    }

    /**
     * Outputs frame pixels to JPG files in the desired location.
     *
     * @param outputLocation Location to place frame images.
     * @param renderToScreen Whether or not the screen should render
     */
    public static void outputToFile( File outputLocation, boolean renderToScreen ) {
        outputDirectory = outputLocation;
        shouldRenderScreen = renderToScreen;
    }

    public static void outputToFile( boolean renderToScreen ) {
        int counter = 0;
        File f = null;
        do
            f = new File( "S2D Output " + counter++ );
        while ( f.exists() );
        f.mkdir();
        outputToFile( f, renderToScreen );
    }

    /**
     * Whether or not the user tried to close the display.
     *
     * @return true when the user attempts to close the display.
     */
    public static boolean isCloseRequested() {

        return mainFrame.isCloseRequested;
    }

    /**
     * Whether or not the display window was resized since the last update.
     *
     * @return true if the native window was resized since the last update.
     */
    public static boolean wasResized() {
        return mainFrame.wasResized();
    }

    /**
     * Sets the title of the display. This will appear on the top strip.
     *
     * @param s The title that the display will show.
     */
    public static void setTitle( String s ) {
        if ( isDisplayCreated )
            mainFrame.setTitle( s );
    }

    /**
     * Sets the display as the active window and sends all input to it.
     * <p/>
     * Note: It is not recommended to call this method often as it will pull focus and disallow keyboard input to any other window.
     */
    public static void grabFocus() {
        mainComponent.grabFocus();
    }

    /**
     * Update the window. Calls swapBuffers() and finally polls the input
     * devices.
     *
     * @see s2d.display.Display#processDisplayEvents() pollInput()
     * @see s2d.display.Display#swapBuffers() swapBuffers()
     */
    public static void update() {
        if ( isDisplayCreated ) {
            processDisplayEvents();
            swapBuffers();
        }
    }

    /**
     * An accurate sync method that will attempt to run at a constant frame
     * rate. It should be called once every frame.
     *
     * @param fps The desired frame rate, in frames per second.
     */
    public static void sync( double fps ) {
        if ( isDisplayCreated ) {
            if ( fps <= 0 ) {
                return;
            }
            double delta1 = getSyncDelta();
            int delay = (int) ((1000 / fps) - delta1);
            if ( delay >= 0 ) {
                try {
                    Thread.sleep( delay );
                } catch ( InterruptedException e ) {
                }
            }
            syncDelta = getSyncDelta();
        }
    }

    private static double getSyncDelta() {
        long time = System.nanoTime();
        int delta = (int) (time - lastSyncFrame);

        lastSyncFrame = time;

        return delta / 1000000.0;
    }

    /**
     * Receives and polls new input events. This method is called from update(),
     * so it is not necessary to call this method if update() is called
     * periodically.
     *
     * @see s2d.display.Display#update() update()
     */
    public static void processDisplayEvents() {
        if ( isDisplayCreated ) {
            Mouse.updateMouse();
            Keyboard.updateKeyboard();
        }
    }

    /**
     * Swap the display buffers. This method is called from update(), so it is
     * not necessary to call this method if update() is called periodically.
     *
     * @see s2d.display.Display#update() update()
     */
    public static void swapBuffers() {
        if ( isDisplayCreated ) {
            frameCount++;
            if ( shouldRenderScreen ) {
                mainComponent.swapBuffer( backBuffer );
                mainComponent.repaint();
            }
            if ( outputDirectory != null ) {
                File f = new File( outputDirectory, "Frame " + frameCount + ".jpg" );
                BufferedImage bit = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
                bit.getGraphics().drawImage( backBuffer, 0, 0, null );
                try {
                    ImageIO.write( bit, "JPG", f );
                } catch ( IOException e ) {
                }
            }
        }
    }

    /**
     * Closes the window.
     */
    public static void destroy() {
        if ( isDisplayCreated ) {
            mainFrame.dispose();
            ac.stop();
        }
    }

    /**
     * @return The width of the drawing area of the window.
     */
    public static int getWidth() {
        return width;
    }

    /**
     * @return The height of the drawing area of the window.
     */
    public static int getHeight() {
        return height;
    }

    /**
     * @return The number of frames that have been drawn.
     */
    public static long getFrameCount() {
        return frameCount;
    }
}

class InnerDisplay extends JComponent implements MouseListener,
        MouseMotionListener, KeyListener, MouseWheelListener {
    Image frontBuffer;

    public InnerDisplay() {
        this.addMouseListener( this );
        this.addMouseMotionListener( this );
        this.addKeyListener( this );
        this.addMouseWheelListener( this );
    }

    public void initInnerDisplay() {
        frontBuffer = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage( Display.getWidth(), Display.getHeight(), Transparency.OPAQUE );
        //this.setIgnoreRepaint(true);
    }

    @Override
    public void paintComponent( Graphics g ) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.drawImage( frontBuffer, 0, 0, null );

        g2.dispose();
    }

    public void swapBuffer( Image backBuffer ) {
        if ( frontBuffer != null ) {
            Graphics2D g2 = (Graphics2D) frontBuffer.getGraphics();
            g2.drawImage( backBuffer, 0, 0, null );
        }
    }

    @Override
    public void keyTyped( KeyEvent e ) {
    }

    @Override
    public void keyPressed( KeyEvent e ) {
        Keyboard.addKey( e.getKeyCode() );
    }

    @Override
    public void keyReleased( KeyEvent e ) {
        Keyboard.removeKey( e.getKeyCode() );
    }

    @Override
    public void mouseDragged( MouseEvent e ) {
        Mouse.setMouse( e );
    }

    @Override
    public void mouseMoved( MouseEvent e ) {
        Mouse.setMouse( e );
    }

    @Override
    public void mouseClicked( MouseEvent e ) {
        Mouse.setMouse( e );
    }

    @Override
    public void mousePressed( MouseEvent e ) {
        Mouse.setMouse( e );
    }

    @Override
    public void mouseReleased( MouseEvent e ) {
        Mouse.setMouse( e );
    }

    @Override
    public void mouseEntered( MouseEvent e ) {
        Mouse.setMouse( e );
    }

    @Override
    public void mouseExited( MouseEvent e ) {
        Mouse.setMouse( e );
    }

    @Override
    public void mouseWheelMoved( MouseWheelEvent e ) {
        Mouse.setMouse( e );
    }
}

class DisplayContainer extends JFrame implements WindowListener {
    public boolean isCloseRequested = false;
    private int w, h;

    public DisplayContainer() {
        w = this.getWidth();
        h = this.getHeight();
        addWindowListener( this );
    }

    public boolean wasResized() {
        boolean returnb = (w != this.getWidth()) || (h != this.getHeight());
        if ( returnb ) {
            w = this.getWidth();
            h = this.getHeight();
            return true;
        }
        return false;
    }

    @Override
    public void windowOpened( WindowEvent e ) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing( WindowEvent e ) {
        isCloseRequested = true;
    }

    @Override
    public void windowClosed( WindowEvent e ) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified( WindowEvent e ) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified( WindowEvent e ) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowActivated( WindowEvent e ) {
        Display.grabFocus();

    }

    @Override
    public void windowDeactivated( WindowEvent e ) {
        // TODO Auto-generated method stub

    }

}
