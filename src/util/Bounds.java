package util;

import s2d.math.Vec;

/**
 * Created by Sina Ghaffari (sina.ghaffari321@gmail.com) on 10/12/14.
 *
 * @author Sina Ghaffari
 */
public class Bounds {
    private Vec p1;
    private Vec p2;

    public Bounds( Vec p1, Vec p2 ) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Vec getP2() {
        return p2;
    }

    public void setP2( Vec p2 ) {
        this.p2 = p2;
    }

    public Vec getP1() {
        return p1;
    }

    public void setP1( Vec p1 ) {
        this.p1 = p1;
    }

}
