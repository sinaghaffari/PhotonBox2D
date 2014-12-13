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

import s2d.raycasting.RayCaster;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * An object meant to store any shape that can be made in java. The main purpose of this object is for raycasting, but other uses will be added soon.<br>
 * In the raycaster is saves it from doing a lot of calculations on the spot.
 *
 * @author Sina Ghaffari
 * @version 1.0
 * @see RayCaster
 * @since 1.0
 */
public class ShapeData {
    /**
     * An {@link ArrayList} containing {@link Ellipse2D}s.
     */
    public ArrayList<Ellipse2D> ellipseList = new ArrayList<Ellipse2D>();
    /**
     * An {@link ArrayList} containing {@link Rectangle2D}s.
     */
    public ArrayList<Rectangle2D> rectangleList = new ArrayList<Rectangle2D>();
    /**
     * An {@link ArrayList} containing {@link Line2D}s.
     */
    public ArrayList<Line2D> lineList = new ArrayList<Line2D>();
    /**
     * An {@link ArrayList} containing {@link QuadCurve2D}s.
     */
    public ArrayList<QuadCurve2D> quadList = new ArrayList<QuadCurve2D>();
    /**
     * An {@link ArrayList} containing {@link CubicCurve2D}s.
     */
    public ArrayList<CubicCurve2D> cubicList = new ArrayList<CubicCurve2D>();
    /**
     * An {@link ArrayList} the properties of the  {@link CubicCurve2D}s.
     */
    public ArrayList<double[]> cubicListInfo = new ArrayList<double[]>();

    /**
     * Adds an {@link Ellipse2D}
     *
     * @param s The {@link Ellipse2D} to be added.
     */
    public void add( Ellipse2D s ) {
        ellipseList.add( s );
    }

    /**
     * Adds a {@link Rectangle2D}
     *
     * @param s The {@link Rectangle2D} to be added.
     */
    public void add( Rectangle2D s ) {
        rectangleList.add( s );
    }

    /**
     * Adds a {@link Line2D}
     *
     * @param s The {@link Line2D} to be added.
     */
    public void add( Line2D s ) {
        lineList.add( s );
    }

    /**
     * Adds a {@link QuadCurve2D}
     *
     * @param s The {@link QuadCurve2D} to be added.
     */
    public void add( QuadCurve2D s ) {
        quadList.add( s );
    }

    /**
     * Adds a {@link CubicCurve2D}<br>
     * <b> Note: </b> Problems exist, currently changes all cubic curves to line segments.
     *
     * @param s The {@link CubicCurve2D} to be added.
     */
    public void add( CubicCurve2D s ) {
        /*
		cubicList.add(s);
		double[] temp = new double[8];
		double x0 = s.getX1();
		double y0 = s.getY1();

		double x1 = s.getCtrlX1();
		double y1 = s.getCtrlY1();

		double x2 = s.getCtrlX2();
		double y2 = s.getCtrlY2();

		double x3 = s.getX2();
		double y3 = s.getY2();
		
		temp[0] = (-x0+3*x1-3*x2+x3);
		temp[1] = 3*(x0-2*x1+x2);
		temp[2] = 3*(-x0+x1);
		temp[3] = (x0);

		temp[4] = (-y0+3*y1-3*y2+y3);
		temp[5] = 3*(y0-2*y1+y2);
		temp[6] = 3*(-y0+y1);
		temp[7] = (y0);
		cubicListInfo.add(temp);
		*/
        this.add( (Shape) s );
    }

    /**
     * Adds any other arbitrary shape and saves it as a combination of {@link Line2D}s, {@link QuadCurve2D}s, and {@link CubicCurve2D}s.
     *
     * @param s The {@link Shape} to be added.
     */
    public void add( Shape s ) {
        PathIterator path = s.getPathIterator( null );
        double[] p = new double[6];
        int i = path.currentSegment( p );
        path.next();
        double x = p[0];
        double y = p[1];
        while ( !path.isDone() ) {
            i = path.currentSegment( p );
            path.next();
            if ( i == PathIterator.SEG_LINETO ) {
                add( new Line2D.Double( x, y, p[0], p[1] ) );
            } else if ( i == PathIterator.SEG_QUADTO ) {
                add( new QuadCurve2D.Double( x, y, p[0], p[1], p[2], p[3] ) );
            } else if ( i == PathIterator.SEG_CUBICTO ) {
                PathIterator pi = new CubicCurve2D.Double( x, y, p[0], p[1], p[2], p[3], p[4], p[5] ).getPathIterator( null, 0.5f );
                double[] tp = new double[2];
                pi.currentSegment( tp );
                pi.next();
                double tx = tp[0];
                double ty = tp[1];
                while ( !pi.isDone() ) {
                    pi.currentSegment( tp );
                    pi.next();
                    add( new Line2D.Double( tx, ty, tp[0], tp[1] ) );
                    tx = tp[0];
                    ty = tp[1];
                }
                //add(new CubicCurve2D.Double(x, y, p[0], p[1], p[2], p[3], p[4], p[5]));
            }
            x = p[0];
            y = p[1];
        }
    }
}
