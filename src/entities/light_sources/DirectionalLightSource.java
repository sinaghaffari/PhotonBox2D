package entities.light_sources;

import main.PhotonWorld;
import s2d.math.Vec;
import util.Color;


/**
 * Created by Sina Ghaffari (sina.ghaffari321@gmail.com) on 10/12/14.
 *
 * @author Sina Ghaffari
 */
public abstract class DirectionalLightSource extends LightSource {
    public DirectionalLightSource( Vec position, Color color, PhotonWorld world ) {
        super( position, color, world );
    }

    public abstract void tick( long rays );
}
