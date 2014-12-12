package entities;

import s2d.math.Vec;

/**
 * Created by Sina Ghaffari (sina.ghaffari321@gmail.com) on 10/12/14.
 *
 * @author Sina Ghaffari
 */
public class Line {
    private Vec p1, p2;
    private Vec normal;
    private double diffuse = 1;
    private double reflect = 0;
    private double transmit = 0;

    public Line( Vec p1, Vec p2, double diffuse, double reflect, double transmit ) {
        this.p1 = p1;
        this.p2 = p2;
        if ( diffuse + reflect + transmit > 1 ) {
            System.out.println( this + ": Fields not valid" );
            this.diffuse = 1;
            this.reflect = 0;
            this.transmit = 0;
        } else {
            this.diffuse = (diffuse < 0) ? 0 : (diffuse > 1) ? 1 : diffuse;
            this.reflect = (reflect < 0) ? 0 : (reflect > 1) ? 1 : reflect;
            this.transmit = (transmit < 0) ? 0 : (transmit > 1) ? 1 : transmit;
        }
        normal = Vec.createVectorAlgebraically( p2.getY() - p1.getY(), -p2.getX() + p1.getX() );
    }

    public Vec getP1() {
        return p1;
    }

    public void setP1( Vec p1 ) {
        this.p1 = p1;
    }

    public Vec getP2() {
        return p2;
    }

    public void setP2( Vec p2 ) {
        this.p2 = p2;
    }

    public Vec getNormal() {
        return normal;
    }

    public void setNormal( Vec normal ) {
        this.normal = normal;
    }

    public double getDiffuse() {
        return diffuse;
    }

    public void setDiffuse( double diffuse ) {
        this.diffuse = diffuse;
    }

    public double getReflect() {
        return reflect;
    }

    public void setReflect( double reflect ) {
        this.reflect = reflect;
    }

    public double getTransmit() {
        return transmit;
    }

    public void setTransmit( double transmit ) {
        this.transmit = transmit;
    }
}
