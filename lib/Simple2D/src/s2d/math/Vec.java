/*
 * Copyright (C) 2014  Sina Ghaffari (sina.ghaffari@mail.utoronto.ca) & Tristan Homsi (thomsi@uwaterloo.ca)
 * 
 * This file is part of Simple2D.

 * Simple2D is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Simple2D is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Simple2D.  If not, see <http://www.gnu.org/licenses/>.
 */
package s2d.math;


import s2d.util.Util;

/**
 * A two-dimensional vector class that can do most simple vector calculations. <br>
 * Useful for velocities, positions, and accelerations.
 *
 * @author Sina Ghaffari
 * @version 1.0
 * @since 1.0
 */
public class Vec {
    private double angle = 0;
    private double x = 0, y = 0;
    private double magnitude = 0;
    private boolean wasCalculatedAngle = false;
    private boolean wasCalculatedMagnitude = false;

    private Vec() {

    }

    private Vec( double angle, double magnitude ) {
        wasCalculatedAngle = true;
        wasCalculatedMagnitude = true;
        this.angle = angle;
        this.magnitude = magnitude;
        calculatePosition();

    }

    private Vec( double x, double y, double zero ) {
        this.x = x;
        this.y = y;
    }

    private Vec( Vec vec ) {
        this.angle = vec.angle;
        this.magnitude = vec.magnitude;
        this.x = vec.x;
        this.y = vec.y;
    }

    /**
     * Creates an algebraic vector from the origin to the given point. <br>
     * The magnitude and angle are determined automatically.
     *
     * @param x The X length of the vector. Can also be called the X
     *          coordinate of the head of the vector.
     * @param y The Y length of the vector. Can also be called the Y
     *          coordinate of the head of the vector.
     * @return A Vec with the given properties.
     */
    public static Vec createVectorAlgebraically( double x, double y ) {
        return new Vec( x, y, 0 );

    }

    /**
     * Creates a geometric vector with the given angle and magnitude (length). <br>
     * The x and y coordinates are determined automatically.
     *
     * @param angle     The standard angle, in radians, of the vector.
     * @param magnitude The length of the vector.
     * @return A Vec with the given properties.
     */
    public static Vec createVectorGeometrically( double angle, double magnitude ) {
        return new Vec( angle, magnitude );
    }

    /**
     * Creates a vector with zero length.
     *
     * @return A zero vector.
     */
    public static Vec createZeroVector() {
        return new Vec();
    }

    /**
     * Creates a vector identical to the given Vec.
     *
     * @param vec The Vec to be cloned.
     * @return The cloned Vec.
     */
    public static Vec copyVector( Vec vec ) {
        return new Vec( vec );
    }

    private void calculateMagnitude() {
        if ( !wasCalculatedMagnitude ) {
            magnitude = Math.sqrt( x * x + y * y );
            wasCalculatedMagnitude = true;
        }
    }

    private void calculateAngle() {
        if ( !wasCalculatedAngle ) {
            angle = Util.angleOfLineRad( 0, 0, x, y );
            if ( Double.isNaN( angle ) ) {
                angle = 0;
            }
            wasCalculatedAngle = true;
        }
    }

    private void calculatePosition() {
        x = magnitude * Math.cos( angle );
        y = magnitude * Math.sin( angle );
    }

    /**
     * Adds the given Vec to the current Vec
     *
     * @param vec The Vec to be added to the current Vec.
     * @return A new Vec, which is a sum of the two.
     */
    public Vec add( Vec vec ) {
        double x_p_n = this.x + vec.x;
        double y_p_n = this.y + vec.y;

        return new Vec( x_p_n, y_p_n, 0 );
    }

    /**
     * Subtracts the current Vec by the given Vec.
     *
     * @param vec The Vec to subtract from the current Vec.
     * @return A new Vec, which is the sum of the current Vec and the negative
     * of the given Vec.
     */
    public Vec subtract( Vec vec ) {
        double x_p_n = this.x - vec.x;
        double y_p_n = this.y - vec.y;

        return new Vec( x_p_n, y_p_n, 0 );
    }

    /**
     * Determines the two dimensional cross product of the two vectors.
     *
     * @param vec The Vec to be cross producted with the current.
     * @return The cross product of the two vectors.
     */
    public double cross( Vec vec ) {
        double temp = (x * vec.y) - (vec.x * y);
        return temp;
    }

    /**
     * Determines the dot product of the two vectors.
     *
     * @param vec The Vec to be dot producted with the current.
     * @return The dot product of the two vectors.
     */
    public double dot( Vec vec ) {
        return ((this.x * vec.x) + (this.y * vec.y));
    }

    /**
     * Determines the angle between two vectors in radians.
     *
     * @param vec The Vec that should be checked with the current.
     * @return The angle between two vectors in radians.
     */
    public double angleBetween( Vec vec ) {
        this.calculateMagnitude();
        vec.calculateMagnitude();
        double inside = vec.dot( this ) / (this.magnitude * vec.magnitude);
        if ( inside > 1 ) inside = 1;
        else if ( inside < -1 ) inside = -1;
        return Math.acos( inside );
    }

    /**
     * Determines a vector directly opposite to the current Vec.
     *
     * @return A Vec opposite to the current Vec.
     */
    public Vec opposite() {
        return new Vec( this.x * -1, this.y * -1, 0 );
    }

    /**
     * Projects the current Vec onto the given Vec.
     *
     * @param vec The Vec to be projected onto.
     * @return A Vec that is the projection of the current Vec onto the given
     * Vec.
     */
    public Vec projectOnto( Vec vec ) {
        vec.calculateMagnitude();
        return vec.multiply( this.dot( vec ) / (vec.magnitude * vec.magnitude) );
    }

    /**
     * Multiplies the x and y components of the vector by a scalar amount. <br>
     * This scales the magnitude by that amount as well.
     *
     * @param i The scalar that should be multiplied into the vector.
     * @return A new Vec after the multiplication.
     */
    public Vec multiply( double i ) {
        return Vec.createVectorAlgebraically( x * i, y * i );
    }

    /**
     * Divides the x and y components of the vector by a scalar amount. <br>
     * This scales the magnitude by the inverse of that amount as well.
     *
     * @param i The scalar that should be divided into the vector.
     * @return A new Vec after the division.
     */
    public Vec divide( double i ) {
        return Vec.createVectorAlgebraically( x / i, y / i );
    }

    /**
     * Shortens the vector if its magnitude is greater than the limit.
     *
     * @param limit The maximum magnitude of the vector.
     * @return A new Vec with a truncated magnitude. Will return <b>this</b> if
     * the magnitude of the vector is less than the limit.
     */
    public Vec truncate( double limit ) {
        calculateAngle();
        calculateMagnitude();
        if ( magnitude > limit ) {
            return Vec.createVectorGeometrically( angle, limit );
        } else {
            return this;
        }
    }

    /**
     * Normalizes the vector by returning a vector with the same angle, but a
     * magnitude of 1.
     *
     * @return Returns the normal of the Vec.
     */
    public Vec normalize() {
        calculateAngle();
        return Vec.createVectorGeometrically( this.angle, 1 );
    }

    /**
     * @return The angle of the vector in radians.
     */
    public double getAngle() {
        calculateAngle();
        return angle;
    }

    /**
     * Sets the angle of the vector
     *
     * @param angle The new angle in radians.
     */
    public void setAngle( double angle ) {
        this.angle = angle;
        wasCalculatedMagnitude = false;
        calculateMagnitude();
        calculatePosition();
    }

    /**
     * @return The magnitude of the vector.
     */
    public double getMagnitude() {
        calculateMagnitude();
        return magnitude;
    }

    /**
     * Sets the magnitude of the vector.
     *
     * @param magnitude The new magnitude.
     */
    public void setMagnitude( double magnitude ) {
        this.magnitude = magnitude;
        wasCalculatedAngle = false;
        calculateAngle();
        calculatePosition();
    }

    /**
     * @return The X component of the vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the X component of the vector.
     *
     * @param x The new X component.
     */
    public void setX( double x ) {
        this.x = x;
        this.wasCalculatedAngle = false;
        this.wasCalculatedMagnitude = false;
        calculateMagnitude();
        calculateAngle();
    }

    /**
     * @return The Y component of the vector.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the Y component of the vector.
     *
     * @param y The new Y component.
     */
    public void setY( double y ) {
        this.y = y;
        this.wasCalculatedAngle = false;
        this.wasCalculatedMagnitude = false;
        calculateMagnitude();
        calculateAngle();
    }

    /**
     * @return A string containing the information of this vector.
     */
    public String toString() {
        calculateAngle();
        calculateMagnitude();
        return "[X:" + x + "|Y:" + y + "|Angle:" + angle + "|Magnitude:" + magnitude + "]";
    }

    public Vec rotateCW( double d ) {
        calculateAngle();
        calculateMagnitude();
        return Vec.createVectorGeometrically( this.angle + d, this.magnitude );
    }

    public void rotateInstanceCW( double d ) {
        calculateAngle();
        calculateMagnitude();
        this.setAngle( this.angle + d );
        calculatePosition();
    }

    public boolean equals( Object obj ) {
        if ( obj instanceof Vec ) {
            Vec v = (Vec) obj;
            return (getX() == v.getX()) && (getY() == v.getY());
        }
        return super.equals( obj );
    }
}
