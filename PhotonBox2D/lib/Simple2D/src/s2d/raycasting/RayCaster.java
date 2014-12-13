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
package s2d.raycasting;

import s2d.geom.ShapeData;
import s2d.math.ComplexNumber;
import s2d.math.Vec;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 * A powerful and efficient ray tracer that can cast a ray to any {@link Shape}.
 *
 * @author Sina Ghaffari
 * @version 1.0
 * @since 1.0
 */
public class RayCaster {
    private static final ComplexNumber u[] = { new ComplexNumber( 1, 0 ), new ComplexNumber( -0.5, Math.sqrt( 3 ) / 2 ), new ComplexNumber( -0.5, -Math.sqrt( 3 ) / 2 ) };
    ;

    /**
     * Casts a ray from the position vector <code>start</code> in the direction of <code>direction</code> at a collection of shapes stored as a {@link ShapeData}.
     *
     * @param sd        A collection of shapes designed to separate shapes from one another and speed up raycasting.
     * @param start     A {@link Vec} representing the initial coordinates of the ray.
     * @param direction A {@link Vec} representing the direction of the ray.
     * @return The closest point from <code>start</code> that the ray intersects the shapes stored in a {@link Point2D}.
     */
    public static Point2D castRayClosestPoint( ShapeData sd, Vec start, Vec direction ) {
        double sx = start.getX();
        double sy = start.getY();
        double dx = direction.getX();
        double dy = direction.getY();
        double sdx = sx + dx;
        double sdy = sy + dy;

        double rA = sdy - sy;
        double rB = sx - sdx;
        double rC = rA * sx + rB * sy;
        double closestT = Double.POSITIVE_INFINITY;
        for ( Rectangle2D rec : sd.rectangleList ) {
            int outcode = rec.outcode( start.getX(), start.getY() );

            if ( outcode == 1 || outcode == 3 || outcode == 9 || outcode == 0 ) {
                double lx1 = rec.getMinX();
                double ly1 = rec.getMinY();

                double lx2 = rec.getMinX();
                double ly2 = rec.getMaxY();

                double A = ly2 - ly1;
                double B = lx1 - lx2;
                double C = A * lx1 + B * ly1;

                boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
                if ( firstCheck ) {
                    double t = (-A * sx - B * sy + C) / (A * dx + B * dy);
                    if ( t >= 0 ) {
                        if ( t < closestT ) {
                            closestT = t;
                        }
                    }
                }
            }
            if ( outcode == 2 || outcode == 3 || outcode == 6 || outcode == 0 ) {
                double lx1 = rec.getMinX();
                double ly1 = rec.getMinY();

                double lx2 = rec.getMaxX();
                double ly2 = rec.getMinY();

                double A = ly2 - ly1;
                double B = lx1 - lx2;
                double C = A * lx1 + B * ly1;

                boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
                if ( firstCheck ) {
                    double t = (-A * sx - B * sy + C) / (A * dx + B * dy);
                    if ( t >= 0 ) {
                        if ( t < closestT ) {
                            closestT = t;
                        }
                    }
                }
            }
            if ( outcode == 4 || outcode == 6 || outcode == 12 || outcode == 0 ) {
                double lx1 = rec.getMaxX();
                double ly1 = rec.getMinY();

                double lx2 = rec.getMaxX();
                double ly2 = rec.getMaxY();

                double A = ly2 - ly1;
                double B = lx1 - lx2;
                double C = A * lx1 + B * ly1;

                boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
                if ( firstCheck ) {
                    double t = (-A * sx - B * sy + C) / (A * dx + B * dy);
                    if ( t >= 0 ) {
                        if ( t < closestT ) {
                            closestT = t;
                        }
                    }
                }
            }
            if ( outcode == 8 || outcode == 12 || outcode == 9 || outcode == 0 ) {
                double lx1 = rec.getMinX();
                double ly1 = rec.getMaxY();

                double lx2 = rec.getMaxX();
                double ly2 = rec.getMaxY();

                double A = ly2 - ly1;
                double B = lx1 - lx2;
                double C = A * lx1 + B * ly1;

                boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
                if ( firstCheck ) {
                    double t = (-A * sx - B * sy + C) / (A * dx + B * dy);
                    if ( t >= 0 ) {
                        if ( t < closestT ) {
                            closestT = t;
                        }
                    }
                }
            }
        }

        for ( Line2D lin : sd.lineList ) {
            double lx1 = lin.getX1();
            double ly1 = lin.getY1();
            double lx2 = lin.getX2();
            double ly2 = lin.getY2();

            double A = ly2 - ly1;
            double B = lx1 - lx2;
            double C = A * lx1 + B * ly1;
            boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
            if ( firstCheck ) { // intersects, determine at which point.
                double t = (C - A * sx - B * sy) / (A * dx + B * dy);
                if ( t >= 0 ) {
                    if ( t < closestT ) {
                        closestT = t;
                    }
                }
            }
        }
        for ( Ellipse2D ell : sd.ellipseList ) {
            double xe = ell.getCenterX();
            double ye = ell.getCenterY();
            // a and b value of the ellipse.
            double a = ell.getWidth() / 2.0;
            double b = ell.getHeight() / 2.0;

            double stx = start.getX() - xe;
            double sty = start.getY() - ye;
            double discriminant = a * a * b * b * (a * a * dy * dy + b * b * dx * dx + 2 * dy * dx * stx * sty - dy * dy * stx * stx - dx * dx * sty * sty);
            double rootDiscriminant = Math.sqrt( discriminant );
            if ( discriminant == 0 ) {
                double t = (rootDiscriminant + a * a * (-dy) * sy - b * b * dx * sx) / (a * a * dy * dy + b * b * dx * dx);
                if ( t >= 0 ) {
                    if ( t < closestT ) {
                        closestT = t;
                    }
                }
            } else if ( discriminant > 0 ) {
                double v1 = a * a * (-dy) * sty - b * b * dx * stx;
                double v2 = (a * a * dy * dy + b * b * dx * dx);
                double t1 = (-rootDiscriminant + v1) / v2;
                double t2 = (rootDiscriminant + v1) / v2;
                if ( t1 >= 0 && t2 >= 0 ) {
                    if ( t1 <= t2 ) {
                        if ( t1 < closestT ) {
                            closestT = t1;
                        }
                    } else if ( t2 < t1 ) {
                        if ( t2 < closestT ) {
                            closestT = t2;
                        }
                    }
                } else if ( t1 >= 0 && t2 < 0 ) {
                    if ( t1 < closestT ) {
                        closestT = t1;
                    }
                } else if ( t2 >= 0 && t1 < 0 ) {
                    if ( t2 < closestT ) {
                        closestT = t2;
                    }
                }

            }
        }
        for ( QuadCurve2D quad : sd.quadList ) {
            double x0 = quad.getX1();
            double y0 = quad.getY1();

            double x1 = quad.getCtrlX();
            double y1 = quad.getCtrlY();

            double x2 = quad.getX2();
            double y2 = quad.getY2();
            double discriminant = rA * rA * x1 * x1 - rA * rA * x0 * x2 - rA * rB * x2 * y0 + 2 * rA * rB * x1 * y1 - rA * rB * x0 * y2 + rA * rC * x0 - 2 * rA * rC * x1 + rA * rC * x2 + rB * rB * y1 * y1 - rB * rB * y0 * y2 + rB * rC * y0 - 2 * rB * rC * y1 + rB * rC * y2;
            double sqrtDiscriminant = Math.sqrt( discriminant );
            if ( discriminant >= 0 ) {
                double v1 = -rA * x0 + rA * x1 - rB * y0 + rB * y1;
                double v2 = -rA * x0 + 2 * rA * x1 - rA * x2 - rB * y0 + 2 * rB * y1 - rB * y2;
                double t1 = (sqrtDiscriminant + v1) / v2;
                double t2 = (-sqrtDiscriminant + v1) / v2;
                if ( t1 >= 0 && t1 <= 1 ) {
                    double ix = (1 - t1) * (1 - t1) * x0 + 2 * (1 - t1) * t1 * x1 + t1 * t1 * x2;
                    double iy = (1 - t1) * (1 - t1) * y0 + 2 * (1 - t1) * t1 * y1 + t1 * t1 * y2;
                    double t = 0;
                    if ( dx != 0 )
                        t = (ix - sx) / dx;
                    else
                        t = (iy - sy) / dy;
                    if ( t >= 0 && t < closestT ) {
                        closestT = t;
                    }

                }
                if ( t2 >= 0 && t2 <= 1 ) {
                    double ix = (1 - t2) * (1 - t2) * x0 + 2 * (1 - t2) * t2 * x1 + t2 * t2 * x2;
                    double iy = (1 - t2) * (1 - t2) * y0 + 2 * (1 - t2) * t2 * y1 + t2 * t2 * y2;
                    double t = 0;
                    if ( dx != 0 )
                        t = (ix - sx) / dx;
                    else
                        t = (iy - sy) / dy;
                    if ( t >= 0 && t < closestT ) {
                        closestT = t;
                    }
                }
            }
        }
        // Disfunctional Cubic Equation solver. Uses General Paths instead.
        /*for (int it = 0; it < sd.cubicList.size(); it++) {
            double[] td = asd.cubicListInfo.get(it);
			double a = (rA * td[0] + rB * td[4]);
			double b = (rA * td[1] + rB * td[5]);
			double c = (rA * td[2] + rB * td[6]);
			double d = (rA * td[3] + rB * td[7]);
			//double discriminant = 18*a*b*c*d-4*b*b*b*d+b*b*c*c-4*a*c*c*c-27*a*a*d*d;
			ComplexNumber d0 = new ComplexNumber(b*b-3*a*c, 0);
			double d1 = 2*b*b*b-9*a*b*c+27*a*a*d;
			ComplexNumber d2 = d0.multiply(d0).multiply(d0).multiply(-4).add(d1*d1);
			ComplexNumber C;
			if (d2.a < 0)
				C = new ComplexNumber(0, Math.sqrt(-d2.a)).add(d1).divide(2).cbrt();
			else 
				C = new ComplexNumber(Math.sqrt(d2.a), 0).add(d1).divide(2).cbrt();
			ComplexNumber tempC2;
			for (int i = 0; i < 3; i++) {
				tempC2 = u[i].multiply(C);
				tempC2 = tempC2.add(b).add(d0.divide(tempC2)).multiply(-1/(3*a));
				if (tempC2.b <= 0.00000005 && tempC2.b >= -0.00000005 && tempC2.a >= 0 && tempC2.a <= 1) {
					double cx = td[0]*tempC2.a*tempC2.a*tempC2.a + td[1]*tempC2.a*tempC2.a + td[2]*tempC2.a + td[3];
					double t = (cx - sx)/dx;
					if (t >= 0 && t < closestT) 
						closestT = t;
				}

			}
		}*/
        return new Point2D.Double( start.getX() + direction.getX() * closestT, start.getY() + direction.getY() * closestT );
    }

    /**
     * Casts a ray from the position vector <code>start</code> in the direction of <code>direction</code> at a collection of shapes stored as a {@link ShapeData}.
     *
     * @param sd         A collection of shapes designed to separate shapes from one another and speed up raycasting.
     * @param start      A {@link Vec} representing the initial coordinates of the ray.
     * @param direction  A {@link Vec} representing the direction of the ray.
     * @param shouldSort A boolean stating whether or not the returned list should be sorted by distance.
     * @return An {@link ArrayList} containing all the points at which the ray intersects the shape (sorted or not).
     */
    public static ArrayList<Point2D.Double> castRayAllPoints( ShapeData sd, Vec start, Vec direction, boolean shouldSort ) {
        Set<Double> al;
        if ( shouldSort )
            al = new TreeSet<Double>();
        else
            al = new HashSet<Double>();
        double sx = start.getX();
        double sy = start.getY();
        double dx = direction.getX();
        double dy = direction.getY();
        double sdx = sx + dx;
        double sdy = sy + dy;

        double rA = sdy - sy;
        double rB = sx - sdx;
        double rC = rA * sx + rB * sy;
        for ( Rectangle2D rec : sd.rectangleList ) {
            int outcode = rec.outcode( start.getX(), start.getY() );
            if ( outcode == 1 || outcode == 3 || outcode == 9 || outcode == 0 ) {
                double lx1 = rec.getMinX();
                double ly1 = rec.getMinY();

                double lx2 = rec.getMinX();
                double ly2 = rec.getMaxY();

                double A = ly2 - ly1;
                double B = lx1 - lx2;
                double C = A * lx1 + B * ly1;

                boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
                if ( firstCheck ) {
                    double t = (-A * sx - B * sy + C) / (A * dx + B * dy);
                    if ( t >= 0 ) {
                        al.add( t );
                    }
                }
            }
            if ( outcode == 2 || outcode == 3 || outcode == 6 || outcode == 0 ) {
                double lx1 = rec.getMinX();
                double ly1 = rec.getMinY();

                double lx2 = rec.getMaxX();
                double ly2 = rec.getMinY();

                double A = ly2 - ly1;
                double B = lx1 - lx2;
                double C = A * lx1 + B * ly1;

                boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
                if ( firstCheck ) {
                    double t = (-A * sx - B * sy + C) / (A * dx + B * dy);
                    if ( t >= 0 ) {
                        al.add( t );
                    }
                }
            }
            if ( outcode == 4 || outcode == 6 || outcode == 12 || outcode == 0 ) {
                double lx1 = rec.getMaxX();
                double ly1 = rec.getMinY();

                double lx2 = rec.getMaxX();
                double ly2 = rec.getMaxY();

                double A = ly2 - ly1;
                double B = lx1 - lx2;
                double C = A * lx1 + B * ly1;

                boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
                if ( firstCheck ) {
                    double t = (-A * sx - B * sy + C) / (A * dx + B * dy);
                    if ( t >= 0 ) {
                        al.add( t );
                    }
                }
            }
            if ( outcode == 8 || outcode == 12 || outcode == 9 || outcode == 0 ) {
                double lx1 = rec.getMinX();
                double ly1 = rec.getMaxY();

                double lx2 = rec.getMaxX();
                double ly2 = rec.getMaxY();

                double A = ly2 - ly1;
                double B = lx1 - lx2;
                double C = A * lx1 + B * ly1;

                boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
                if ( firstCheck ) {
                    double t = (-A * sx - B * sy + C) / (A * dx + B * dy);
                    if ( t >= 0 ) {
                        al.add( t );
                    }
                }
            }
        }
        for ( Line2D lin : sd.lineList ) {
            double lx1 = lin.getX1();
            double ly1 = lin.getY1();
            double lx2 = lin.getX2();
            double ly2 = lin.getY2();

            double A = ly2 - ly1;
            double B = lx1 - lx2;
            double C = A * lx1 + B * ly1;

            boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
            //boolean secondCheck = (lA * rx1 + lB * ry1 < lC) != (lA * direction.getX() + lB * direction.getY() < 0);
            if ( firstCheck ) { // intersects, determine at which point.
                //double ix = Ax/dA;
                //double iy = Ay/dA;
                double t = (-A * sx - B * sy + C) / (A * dx + B * dy);
                if ( t >= 0 ) {
                    al.add( t );
                }
            }
        }
        for ( Ellipse2D ell : sd.ellipseList ) {
            double xe = ell.getCenterX();
            double ye = ell.getCenterY();
            // a and b value of the ellipse.
            double a = ell.getWidth() / 2.0;
            double b = ell.getHeight() / 2.0;

            double stx = start.getX() - xe;
            double sty = start.getY() - ye;

            double discriminant = a * a * b * b * (a * a * dy * dy + b * b * dx * dx + 2 * dy * dx * stx * sty - dy * dy * stx * stx - dx * dx * sty * sty);
            double rootDiscriminant = Math.sqrt( discriminant );
            if ( discriminant >= 0 ) {
                if ( discriminant > 0 ) {
                    double t1 = (-rootDiscriminant + a * a * (-dy) * sty - b * b * dx * stx) / (a * a * dy * dy + b * b * dx * dx);
                    double t2 = (rootDiscriminant + a * a * (-dy) * sty - b * b * dx * stx) / (a * a * dy * dy + b * b * dx * dx);
                    if ( t1 >= 0 && t2 >= 0 ) {
                        if ( t1 <= t2 ) {
                            al.add( t1 );
                        } else if ( t2 < t1 ) {
                            al.add( t2 );
                        }
                    } else if ( t1 >= 0 && t2 < 0 ) {
                        al.add( t1 );
                    } else if ( t2 >= 0 && t1 < 0 ) {
                        al.add( t2 );
                    }

                } else {
                    double t = (rootDiscriminant + a * a * (-dy) * sy - b * b * dx * sx) / (a * a * dy * dy + b * b * dx * dx);
                    if ( t >= 0 ) {
                        al.add( t );
                    }
                }
            }
        }
        //determine everything else.
        for ( QuadCurve2D quad : sd.quadList ) {
            double x0 = quad.getX1();
            double y0 = quad.getY1();

            double x1 = quad.getCtrlX();
            double y1 = quad.getCtrlY();

            double x2 = quad.getX2();
            double y2 = quad.getY2();

            double discriminant = rA * rA * x1 * x1 - rA * rA * x0 * x2 - rA * rB * x2 * y0 + 2 * rA * rB * x1 * y1 - rA * rB * x0 * y2 + rA * rC * x0 - 2 * rA * rC * x1 + rA * rC * x2 + rB * rB * y1 * y1 - rB * rB * y0 * y2 + rB * rC * y0 - 2 * rB * rC * y1 + rB * rC * y2;
            double sqrtDiscriminant = Math.sqrt( discriminant );
            if ( discriminant >= 0 ) {
                double v1 = -rA * x0 + rA * x1 - rB * y0 + rB * y1;
                double v2 = -rA * x0 + 2 * rA * x1 - rA * x2 - rB * y0 + 2 * rB * y1 - rB * y2;
                double t1 = (sqrtDiscriminant + v1) / v2;
                double t2 = (-sqrtDiscriminant + v1) / v2;
                if ( t1 >= 0 && t1 <= 1 ) {
                    double ix = (1 - t1) * (1 - t1) * x0 + 2 * (1 - t1) * t1 * x1 + t1 * t1 * x2;
                    double iy = (1 - t1) * (1 - t1) * y0 + 2 * (1 - t1) * t1 * y1 + t1 * t1 * y2;
                    double t = 0;
                    if ( dx != 0 )
                        t = (ix - sx) / dx;
                    else
                        t = (iy - sy) / dy;
                    if ( t >= 0 ) {
                        al.add( t );
                    }

                }
                if ( t2 >= 0 && t2 <= 1 ) {
                    double ix = (1 - t2) * (1 - t2) * x0 + 2 * (1 - t2) * t2 * x1 + t2 * t2 * x2;
                    double iy = (1 - t2) * (1 - t2) * y0 + 2 * (1 - t2) * t2 * y1 + t2 * t2 * y2;
                    double t = 0;
                    if ( dx != 0 )
                        t = (ix - sx) / dx;
                    else
                        t = (iy - sy) / dy;
                    if ( t >= 0 ) {
                        al.add( t );
                    }
                }
            }
        }
        for ( int it = 0; it < sd.cubicList.size(); it++ ) {
            CubicCurve2D s = sd.cubicList.get( it );
            double[] td = sd.cubicListInfo.get( it );
            double a = (rA * td[0] + rB * td[4]);
            double b = (rA * td[1] + rB * td[5]);
            double c = (rA * td[2] + rB * td[6]);
            double d = (rA * td[3] + rB * td[7]);
            //double discriminant = 18*a*b*c*d-4*b*b*b*d+b*b*c*c-4*a*c*c*c-27*a*a*d*d;
            ComplexNumber d0 = new ComplexNumber( b * b - 3 * a * c, 0 );
            double d1 = 2 * b * b * b - 9 * a * b * c + 27 * a * a * d;
            ComplexNumber d2 = d0.multiply( d0 ).multiply( d0 ).multiply( -4 ).add( d1 * d1 );
            ComplexNumber C;
            if ( d2.a < 0 )
                C = new ComplexNumber( 0, Math.sqrt( -d2.a ) ).add( d1 ).divide( 2 ).cbrt();
            else
                C = new ComplexNumber( Math.sqrt( d2.a ), 0 ).add( d1 ).divide( 2 ).cbrt();
            ComplexNumber tempC2;
            for ( int i = 0; i < 3; i++ ) {
                tempC2 = u[i].multiply( C );
                tempC2 = tempC2.add( b ).add( d0.divide( tempC2 ) ).multiply( -1 / (3 * a) );
                if ( tempC2.b <= 0.00000005 && tempC2.b >= -0.00000005 && tempC2.a >= 0 && tempC2.a <= 1 ) {
                    double cx = td[0] * tempC2.a * tempC2.a * tempC2.a + td[1] * tempC2.a * tempC2.a + td[2] * tempC2.a + td[3];
                    double t = (cx - sx) / dx;
                    al.add( t );
                }

            }
        }
        ArrayList<Point2D.Double> returnList = new ArrayList<Point2D.Double>();
        for ( Iterator<Double> i = al.iterator(); i.hasNext(); ) {
            double t = i.next();
            double ix = sx + direction.getX() * t;
            double iy = sy + direction.getY() * t;
            returnList.add( new Point2D.Double( ix, iy ) );
        }
        return returnList;
    }
}