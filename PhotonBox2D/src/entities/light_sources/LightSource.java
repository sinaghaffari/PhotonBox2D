package entities.light_sources;

import main.PhotonWorld;
import s2d.math.Vec;
import util.Color;

/**
 * Created by Sina Ghaffari (sina.ghaffari321@gmail.com) on 10/12/14.
 *
 * @author Sina Ghaffari
 */
public abstract class LightSource {

    public PhotonWorld world;
    protected Vec position;
    protected Color color;
    private long rayCount = 0;

    public LightSource( Vec position, Color color, PhotonWorld world ) {
        this.position = position;
        this.color = color;
        this.world = world;
    }

    public void tick( long rays ) {
        rayCount += rays;
    }
}
