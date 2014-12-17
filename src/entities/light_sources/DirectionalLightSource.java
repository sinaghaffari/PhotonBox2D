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
    protected double direction;

    public DirectionalLightSource( Vec position, Color color, PhotonWorld world, double direction ) {
        super( position, color, world );
        this.direction = direction;
    }

    public void tick( long rays ) {
        super.tick( rays );
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection( double direction ) {
        this.direction = direction;
    }
}
