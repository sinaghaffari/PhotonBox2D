package main;

import entities.Line;
import entities.light_sources.OmnidirectionalLightSource;
import s2d.Display.Display;
import s2d.math.Vec;
import util.Color;

import java.awt.*;


/**
 * Created by Sina Ghaffari (sina.ghaffari321@gmail.com) on 10/12/14.
 *
 * @author Sina Ghaffari
 */
public class PhotonBox2D {
    public static void main( String[] args ) {
        System.setProperty( "sun.java2d.nodraw", "true" );
        System.setProperty( "sun.java2d.translaccel", "true" );
        System.setProperty( "sun.java2d.opengl", "true" );
        System.setProperty( "sun.java2d.accthreshold", "0" );
        Display.createDisplay( 1000, 600 );
        long lastFrame = System.nanoTime();
        PhotonWorld world = new PhotonWorld( 1000, 600 );
        world.addLightSource( new OmnidirectionalLightSource( Vec.createVectorAlgebraically( 400, 300 ), new Color( 1, 0, 0 ), world ) );
        world.addLightSource( new OmnidirectionalLightSource( Vec.createVectorAlgebraically( 600, 300 ), new Color( 0, 1, 0 ), world ) );
        world.addLine( new Line( Vec.createVectorAlgebraically( 400, 200 ), Vec.createVectorAlgebraically( 500, 100 ), 1, 0, 0 ) );
        while ( !Display.isCloseRequested() ) {
            world.smartTick( 1000000000 / 60 );
            if ( System.nanoTime() - lastFrame >= 1000000000 / 60 ) {
                lastFrame = System.nanoTime();
                Graphics2D g2 = Display.getGraphics();
                g2.setPaint( java.awt.Color.blue );
                g2.fillRect( 0, 0, Display.getWidth(), Display.getHeight() );
                world.render( g2 );
                Display.setTitle( "" + world.getGlobalRayCount() );
                Display.update();
            }
            Display.processDisplayEvents();
        }
        Display.destroy();
    }
}
