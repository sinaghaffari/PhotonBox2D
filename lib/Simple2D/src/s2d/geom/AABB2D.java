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
package s2d.geom;

import s2d.math.Vec;

import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * An axis-aligned bounding block that can handle collisions.
 *
 * @author Sina Ghaffari
 * @version 1.0
 * @since 1.0
 */
public class AABB2D {
    private double x, y, w, h;
    private Vec position;
    private Rectangle2D bounds;

    /**
     * @param x The x-coordinate of the top left corner of the box.
     * @param y The y-coordinate of the top left corner of the box.
     * @param w The width of the box
     * @param h The height of the box.
     */
    public AABB2D( double x, double y, double w, double h ) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        position = Vec.createVectorAlgebraically( x, y );
        bounds = new Rectangle2D.Double( x, y, w, h );
    }

    /**
     * Returns the new position of a an AABB2D after having interacted with a list of other AABB2D.
     *
     * @param box       The moving AABB2D
     * @param staticBox A {@link java.util.List} of all of the static walls that are to be collided with.
     * @param velocity  The velocity of the moving box
     * @return the new position of the AABB2D (box).
     */
    public static Vec sweptCollision( AABB2D box, List<AABB2D> staticBox, Vec velocity ) {
        Vec nPosition = Vec.copyVector( box.position );
        Vec nVelocity = Vec.copyVector( velocity );
        Set<Collision> closestEntryTime = new TreeSet<Collision>( new CollisionComparator() );
        Rectangle2D rec = new Rectangle2D.Double( velocity.getX() > 0 ? box.x : box.x + velocity.getX(), velocity.getY() > 0 ? box.y : box.y + velocity.getY(), velocity.getX() > 0 ? velocity.getX() + box.w : box.w - velocity.getX(), velocity.getY() > 0 ? velocity.getY() + box.h : box.h - velocity.getY() );
        for ( AABB2D w : staticBox ) {
            double entryTime = 1;
            if ( rec.intersects( w.bounds )/*(rec.getX() + rec.getWidth() > w.bounds.getX() && rec.getX() < w.bounds.getX() + w.bounds.getWidth() && rec.getY() + rec.getHeight() > w.bounds.getY() && rec.getY() < w.bounds.getY() + w.bounds.getHeight())*/ ) {
                double xEntry, xExit, yEntry, yExit;
                double xTEntry, xTExit, yTEntry, yTExit;
                Vec normal;
                if ( velocity.getX() > 0 ) {
                    xEntry = w.x - (box.x + box.w);
                    xExit = (w.x + w.w) - box.x;
                } else {
                    xEntry = (w.x + w.w) - box.x;
                    xExit = w.x - (box.x + box.w);
                }
                if ( velocity.getY() > 0 ) {
                    yEntry = w.y - (box.y + box.h);
                    yExit = (w.y + w.h) - box.y;
                } else {
                    yEntry = (w.y + w.h) - box.y;
                    yExit = w.y - (box.y + box.h);
                }
                if ( velocity.getX() == 0 ) {
                    xTEntry = Double.NEGATIVE_INFINITY;
                    xTExit = Double.POSITIVE_INFINITY;
                } else {
                    xTEntry = xEntry / velocity.getX();
                    xTExit = xExit / velocity.getX();
                }
                if ( velocity.getY() == 0 ) {
                    yTEntry = Double.NEGATIVE_INFINITY;
                    yTExit = Double.POSITIVE_INFINITY;
                } else {
                    yTEntry = yEntry / velocity.getY();
                    yTExit = yExit / velocity.getY();
                }
                entryTime = Math.max( xTEntry, yTEntry );
                double exitTime = Math.min( xTExit, yTExit );

                if ( entryTime > exitTime || xTEntry < 0 && yTEntry < 0 || xTEntry > 1 || yTEntry > 1 ) {
                    entryTime = 1;
                } else {
                    if ( xTEntry > yTEntry ) {
                        if ( xEntry < 0 ) {
                            normal = Vec.createVectorAlgebraically( 1, 0 );
                        } else {
                            normal = Vec.createVectorAlgebraically( -1, 0 );
                        }

                    } else {
                        if ( yEntry < 0 ) {
                            normal = Vec.createVectorAlgebraically( 0, 1 );
                        } else {
                            normal = Vec.createVectorAlgebraically( 0, -1 );
                        }

                    }
                    closestEntryTime.add( new Collision( entryTime, normal, w ) );
                }
            }
        }
        if ( closestEntryTime.isEmpty() )
            return nPosition.add( velocity );
        Collision best = closestEntryTime.iterator().next();
        double dotProduct = (velocity.getX() * best.normal.getY() + velocity.getY() * best.normal.getX()) * (1 - best.collisionTime);
        nVelocity.setX( dotProduct * best.normal.getY() );
        nVelocity.setY( dotProduct * best.normal.getX() );
        nPosition = nPosition.add( velocity.multiply( best.collisionTime ) );
        if ( closestEntryTime.size() > 1 ) {
            return AABB2D.sweptCollision( new AABB2D( nPosition.getX(), nPosition.getY(), box.w, box.h ), staticBox, nVelocity );
        }
        return nPosition.add( nVelocity );
    }

    /**
     * @return The bounds of the box as a {@link java.awt.geom.Rectangle2D}
     */
    public Rectangle2D getBounds() {
        return bounds;
    }

    /**
     * @return The position {@link Vec} of the top left corner of the box.
     */
    public Vec getPosition() {
        return position;
    }

    /**
     * Sets the location of the box.
     *
     * @param x The new x-coordinate of the top left corner of the box.
     * @param y The new y-coordinate of the top left corner of the box.
     */
    public void setPosition( double x, double y ) {
        this.x = x;
        this.y = y;
        bounds.setRect( x, y, w, h );
        position = Vec.createVectorAlgebraically( x, y );
    }
}

class Collision {
    double collisionTime;
    Vec normal;
    AABB2D wall;

    public Collision( double collisionTime, Vec normal, AABB2D wall ) {
        this.collisionTime = collisionTime;
        this.normal = normal;
        this.wall = wall;
    }
}

class CollisionComparator implements Comparator<Collision> {

    @Override
    public int compare( Collision o1, Collision o2 ) {
        if ( o1.collisionTime == o2.collisionTime )
            return 0;
        else if ( o1.collisionTime < o2.collisionTime )
            return -1;
        return 1;
    }
}
