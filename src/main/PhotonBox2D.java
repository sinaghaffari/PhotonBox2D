package main;

import entities.Line;
import entities.light_sources.LightSource;
import entities.light_sources.OmnidirectionalLightSource;
import s2d.display.Display;
import s2d.input.Keyboard;
import s2d.input.Mouse;
import s2d.math.Vec;
import util.Color;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;


/**
 * Created by Sina Ghaffari (sina.ghaffari321@gmail.com) on 10/12/14.
 *
 * @author Sina Ghaffari
 */
public class PhotonBox2D {
    public static void main( String[] args ) throws InterruptedException {
        System.setProperty( "sun.java2d.nodraw", "true" );
        System.setProperty( "sun.java2d.translaccel", "true" );
        System.setProperty( "sun.java2d.opengl", "true" );
        System.setProperty( "sun.java2d.accthreshold", "0" );
        Display.createDisplay( 1000, 600 );
        long lastFrame = System.nanoTime();
        PhotonWorld world = new PhotonWorld( 1000, 600 );
        LightSource ls1 = new OmnidirectionalLightSource( Vec.createVectorAlgebraically( 400, 386 ), new Color( 1, 0, 0 ), world );
        LightSource ls2 = new OmnidirectionalLightSource( Vec.createVectorAlgebraically( 600, 386 ), new Color( 0, 1, 0 ), world );
        LightSource ls3 = new OmnidirectionalLightSource( Vec.createVectorAlgebraically( 500, 214 ), new Color( 0, 0, 1 ), world );
        world.addLightSource( ls1 );
        world.addLightSource( ls2 );
        world.addLightSource( ls3 );
        world.addLine( new Line( Vec.createVectorAlgebraically( 400, 200 ), Vec.createVectorAlgebraically( 500, 100 ), 1, 0, 0 ) );
        world.addLine( new Line( Vec.createVectorAlgebraically( 700, 200 ), Vec.createVectorAlgebraically( 900, 300 ), 1, 0, 0 ) );
        world.addLine( new Line( Vec.createVectorAlgebraically( 100, 500 ), Vec.createVectorAlgebraically( 200, 300 ), 1, 0, 0 ) );
        world.addLine( new Line( Vec.createVectorAlgebraically( 430, 500 ), Vec.createVectorAlgebraically( 570, 500 ), 1, 0, 0 ) );
        long countPhotons = System.nanoTime();
        long photons = 0;
        long photonsPerSecond = 0;
        while ( !Display.isCloseRequested() ) {
            if ( System.nanoTime() - countPhotons >= 1000000000 ) {
                countPhotons = System.nanoTime();
                photonsPerSecond = world.getGlobalRayCount() - photons;
                photons = world.getGlobalRayCount();
            }
            if ( System.nanoTime() - lastFrame >= 1000000000 / 60 ) {
                lastFrame = System.nanoTime();
                Graphics2D g2 = Display.getGraphics();
                if ( Keyboard.isKeyTyped( KeyEvent.VK_SPACE ) ) {
                    Calendar time = Calendar.getInstance();
                    File outputFile = new File( "PhotonBox Screenshots/ScreenShot-" + time.getTimeInMillis() + ".png" );
                    outputFile.getParentFile().mkdirs();
                    BufferedImage bi = new BufferedImage( world.getWidth(), world.getHeight(), BufferedImage.TYPE_INT_ARGB );
                    Graphics2D bg = (Graphics2D) bi.getGraphics();
                    bg.setPaint( java.awt.Color.black );
                    bg.fillRect( 0, 0, Display.getWidth(), Display.getHeight() );
                    world.render( bg );
                    bg.dispose();
                    try {
                        ImageIO.write( bi, "png", outputFile );
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    }
                }
                world.setExposure( world.getExposure() + Mouse.getDWheel() );
                g2.setPaint( java.awt.Color.black );
                g2.fillRect( 0, 0, Display.getWidth(), Display.getHeight() );
                world.render( g2 );
                Display.setTitle( "#Photons: " + world.getGlobalRayCount() + " Photons/s: " + photonsPerSecond + " Press Space to take a screenshot!" );
                Display.swapBuffers();

            }
            Display.processDisplayEvents();
            Display.sync( 60 );
        }
        Display.destroy();
        world.es.shutdown();
        System.exit( 0 );
    }
}