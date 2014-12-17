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
public class NaturalDirectionalLightSource extends DirectionalLightSource {
    int spread = 100;

    public NaturalDirectionalLightSource( Vec position, Color color, PhotonWorld world, double direction ) {
        super( position, color, world, direction );
    }

    @Override
    public void tick( long rays ) {
        for ( int i = 0; i < rays; i++ ) {
            double x = ThreadLocalRandom.current().nextDouble();
            boolean pn = ThreadLocalRandom.current().nextBoolean();
            double angleModifier = Math.pow( x, spread ) / ((x - 1));
            angleModifier *= (pn) ? -1 : 1;
            world.resolveRay( position, Vec.createVectorGeometrically( direction + angleModifier, 1 ), color );
        }
        super.tick( rays );
    }
}
