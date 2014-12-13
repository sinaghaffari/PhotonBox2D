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
package s2d.util;

import java.util.ArrayList;

/**
 * Various methods that do simple but necessary tasks.
 *
 * @author Sina Ghaffari
 */
public class Util {

    /**
     * Determines the angle at which a line must be drawn to get from the first
     * point to the second in radians.
     *
     * @param x1 The X coordinate of the first point.
     * @param y1 The Y coordinate of the first point.
     * @param x2 The X coordinate of the second point.
     * @param y2 The Y coordinate of the second point.
     * @return The angle of a line from the first point to the second in
     * radians.
     */
    public static double angleOfLineRad( double x1, double y1, double x2,
                                         double y2 ) {
        double t = Math.atan2( (y2 - y1), (x2 - x1) );
        return (t < 0) ? t + (Math.PI * 2) : t;
    }

    /**
     * Determines the angle at which a line must be drawn to get from the first
     * point to the second in degrees.
     *
     * @param x1 The X coordinate of the first point.
     * @param y1 The Y coordinate of the first point.
     * @param x2 The X coordinate of the second point.
     * @param y2 The Y coordinate of the second point.
     * @return The angle of a line from the first point to the second in
     * degrees.
     */
    public static double angleOfLineDeg( double x1, double y1, double x2,
                                         double y2 ) {
        double t = Math.toDegrees( Math.atan2( (y2 - y1), (x2 - x1) ) );
        return (t < 0) ? t + 360 : t;
    }

    public static int sgn( double i ) {
        return (i >= 0) ? 1 : -1;
    }

    /**
     * Determines the distance between two hypothetical points.
     *
     * @param x1 The X coordinate of the first point.
     * @param y1 The Y coordinate of the first point.
     * @param x2 The X coordinate of the second point.
     * @param y2 The Y coordinate of the second point.
     * @return The distance between the first and second points.
     */
    public static double distance( double x1, double y1, double x2, double y2 ) {
        return Math.sqrt( (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) );
    }

    /**
     * Determines the non-squared distance between two hypothetical points.
     *
     * @param x1 The X coordinate of the first point.
     * @param y1 The Y coordinate of the first point.
     * @param x2 The X coordinate of the second point.
     * @param y2 The Y coordinate of the second point.
     * @return The non-squared distance between the first and second points.
     */
    public static double distanceSquared( double x1, double y1, double x2,
                                          double y2 ) {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
    }

    /**
     * Determines the largest number that can be divided, without remainder,
     * into both numbers.
     *
     * @param p The first number.
     * @param q The second number.
     * @return The greatest common divisor of the two numbers.
     */
    public static int greatestCommonDivisor( int p, int q ) {
        if ( q == 0 ) {
            return p;
        }
        return greatestCommonDivisor( q, p % q );
    }

    /**
     * Determines every number divisible, without remainder, with a number and
     * returns it as an ArrayList.
     *
     * @param num The number whose factors are to be returned.
     * @return An ArrayList containing ever factor of num.
     */
    public static ArrayList<Integer> findFactors( int num ) {
        int incrementer = 1;
        if ( (num % 2) != 0 ) {
            incrementer = 2; // only test the odd ones
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        for ( int i = 1; i <= (num / 2); i = i + incrementer ) {
            if ( (num % i) == 0 ) {
                list.add( i );
            }
        }
        list.add( num );
        return list;
    }

    /**
     * Finds the two factors of a number that are closest together. For instance
     * the middle factors of 100 are 10 and 10. The two closest factors of 18
     * are 6 and 3.
     *
     * @param num The number whose middle factors are to be returned
     * @return The middle factors of num.
     */
    public static int[] findMiddleFactors( int num ) {
        ArrayList<Integer> list = findFactors( num );
        if ( (list.size() % 2) == 1 ) {
            return new int[]{
                    list.get( (int) Math.ceil( list.size() / 2.0 ) - 1 ),
                    list.get( (int) Math.ceil( list.size() / 2.0 ) - 1 ) };
        } else {
            return new int[]{ list.get( (int) (list.size() / 2.0) - 1 ),
                    list.get( (int) (list.size() / 2.0) ) };
        }
    }
}
