package entities.light_sources;

import main.PhotonWorld;
import s2d.math.Vec;
import util.Color;


/**
 * Created by Sina Ghaffari (sina.ghaffari321@gmail.com) on 10/12/14.
 *
 * @author Sina Ghaffari
 */
public class AbsoluteDirectionalLightSource extends DirectionalLightSource {
    public AbsoluteDirectionalLightSource( Vec position, Color color, PhotonWorld world, double direction ) {
        super( position, color, world, direction );
    }

    @Override
    public void tick( long rays ) {

    }
}
