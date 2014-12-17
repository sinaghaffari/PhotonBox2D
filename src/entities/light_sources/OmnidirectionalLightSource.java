package entities.light_sources;

import main.PhotonWorld;
import s2d.math.Vec;
import util.Color;

import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by Sina Ghaffari (sina.ghaffari321@gmail.com) on 10/12/14.
 *
 * @author Sina Ghaffari
 */
public class OmnidirectionalLightSource extends LightSource {
    public OmnidirectionalLightSource( Vec position, Color color, PhotonWorld world ) {
        super( position, color, world );
    }

    @Override
    public void tick( long rays ) {
        for ( int i = 0; i < rays; i++ ) {
            world.resolveRay( position, Vec.createVectorGeometrically( ThreadLocalRandom.current().nextDouble() * 2 * Math.PI, 1 ), color );
        }
        super.tick( rays );
    }
}