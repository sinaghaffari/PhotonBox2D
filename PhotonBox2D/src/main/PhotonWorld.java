package main;

import entities.Line;
import entities.light_sources.LightSource;
import s2d.math.S2DRandom;
import s2d.math.Vec;
import s2d.util.Util;
import util.Color;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sina Ghaffari (sina.ghaffari321@gmail.com) on 10/12/14.
 *
 * @author Sina Ghaffari
 */
public class PhotonWorld {
    public static final Random RAND = new S2DRandom( new Random().nextLong() );
    private final byte COLOR_DEPTH = 4;
    private int width, height;
    private ArrayList<Line> lineList = new ArrayList<Line>();
    private ArrayList<LightSource> lightSources = new ArrayList<LightSource>();
    private BufferedImage worldImage;
    private double[] rawPixelInfo;
    private long globalRayCount = 0;
    private double exposure = 500;

    public PhotonWorld( int width, int height ) {
        this.width = width;
        this.height = height;
        worldImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        rawPixelInfo = new double[width * height * COLOR_DEPTH];
        lineList.add( new Line( Vec.createVectorAlgebraically( 0, 0 ), Vec.createVectorAlgebraically( width, 0 ), 0, 0, 0 ) );
        lineList.add( new Line( Vec.createVectorAlgebraically( width, 0 ), Vec.createVectorAlgebraically( width, height ), 0, 0, 0 ) );
        lineList.add( new Line( Vec.createVectorAlgebraically( width, height ), Vec.createVectorAlgebraically( 0, height ), 0, 0, 0 ) );
        lineList.add( new Line( Vec.createVectorAlgebraically( 0, height ), Vec.createVectorAlgebraically( 0, 0 ), 0, 0, 0 ) );
    }

    public void addLightSource( LightSource light ) {
        lightSources.add( light );
    }

    public void addLine( Line line ) {
        lineList.add( line );
    }

    public void smartTick( long time ) {
        long startTime = System.nanoTime();
        while ( System.nanoTime() - startTime < time ) {
            tick( 1 );
        }
    }

    // ticks each LightSource that many times
    public void tick( long rays ) {
        for ( LightSource ls : lightSources ) {
            ls.tick( rays );
            globalRayCount += rays;
        }
    }

    public void render( Graphics2D g ) {
        int[] convertedPixels = new int[width * height * COLOR_DEPTH];
        //Arrays.fill( convertedPixels, 255 );
        for ( int i = 0; i < rawPixelInfo.length; i += COLOR_DEPTH ) {
            double[] comp = new double[COLOR_DEPTH];
            for ( int l = 0; l < COLOR_DEPTH; l++ ) {
                comp[l] = rawPixelInfo[i + l] / globalRayCount * exposure * lightSources.size();
                if ( comp[l] > 1 ) comp[l] = 1;
                else if ( comp[l] < 0 ) comp[l] = 0;
            }
            double convertedComp[] = smartScreen( comp );
            convertedPixels[i + 0] = (int) (255 * convertedComp[0]);
            convertedPixels[i + 1] = (int) (255 * convertedComp[1]);
            convertedPixels[i + 2] = (int) (255 * convertedComp[2]);
            convertedPixels[i + 3] = (int) (255 * convertedComp[3]);
        }
        WritableRaster imageRaster = worldImage.getRaster();
        imageRaster.setPixels( 0, 0, width, height, convertedPixels );
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage( worldImage, null, 0, 0 );
        g2.dispose();
    }

    public double[] smartScreen( double[] src ) {
        if ( src[0] == 0 && src[1] == 0 && src[2] == 0 )
            return new double[]{ 0, 0, 0, src[3] };
        double alpha = Math.max( Math.max( src[0], src[1] ), src[2] );
        return new double[]{ src[0] / alpha, src[1] / alpha, src[2] / alpha, alpha };
    }

    public int screen( double src, double dst ) {
        return (int) (255 * (1 - (1 - dst) * (1 - src)));
    }

    public int normal( double src, double dst ) {
        return (int) (255 * src);
    }

    public int overlay( double src, double dst ) {
        return (int) (255 * ((dst < 0.5) ? (2 * dst * src) : (1 - 2 * (1 - dst) * (1 - src))));
    }

    public int alpha( double src, double dst, double src_a ) {
        return (int) (255 * src * src_a + dst * (1 - src_a));
    }

    public void resolveRay( Vec startPoint, Vec direction, Color color ) {
        Line prevLineIntersection = null;
        double xi = startPoint.getX();
        double yi = startPoint.getY();
        int interactionType = 0;
        double ra = 0;
        do {
            if ( interactionType == 0 ) {
                ra = 2 * Math.PI * RAND.nextDouble();
            } else if ( interactionType == 1 ) {
                Vec normal = prevLineIntersection.getNormal();
                Vec rVec = Vec.createVectorGeometrically( ra, 1 );
                double angleBetween = rVec.angleBetween( normal );
                double cross = rVec.cross( normal );
                ra = normal.opposite().getAngle() + Util.sgn( cross ) * angleBetween;
            }
            double dx = Math.cos( ra );
            double dy = Math.sin( ra );
            double closestT = Double.POSITIVE_INFINITY;
            Line closestLine = null;
            double sdx = xi + dx;
            double sdy = yi + dy;
            double rA = sdy - yi;
            double rB = xi - sdx;
            double rC = rA * xi + rB * yi;
            for ( Line lin : lineList ) {
                if ( prevLineIntersection == null || lin != prevLineIntersection ) {
                    double lx1 = lin.getP1().getX();
                    double ly1 = lin.getP1().getY();
                    double lx2 = lin.getP2().getX();
                    double ly2 = lin.getP2().getY();

                    double A = ly2 - ly1;
                    double B = lx1 - lx2;
                    double C = A * lx1 + B * ly1;
                    boolean firstCheck = (rA * lx1 + rB * ly1 > rC) != (rA * lx2 + rB * ly2 > rC) || (rA * lx1 + rB * ly1 == rC) != (rA * lx2 + rB * ly2 == rC);
                    if ( firstCheck ) { // intersects, determine at which point.
                        double t = (C - A * xi - B * yi) / (A * dx + B * dy);
                        if ( t >= 0 ) {
                            if ( t < closestT ) {
                                closestT = t;
                                closestLine = lin;
                            }
                        }
                    }
                }
            }
            prevLineIntersection = closestLine;
            double xf = xi + dx * closestT;
            double yf = yi + dy * closestT;
            addRay( xi, yi, xf, yf, color );
            xi = xf;
            yi = yf;
            double random = RAND.nextDouble();
            if ( random < closestLine.getDiffuse() ) {
                interactionType = 0;
            } else {
                random -= closestLine.getDiffuse();
                if ( random < closestLine.getReflect() ) {
                    interactionType = 1;
                } else {
                    random -= closestLine.getReflect();
                    if ( random < closestLine.getTransmit() ) {
                        interactionType = 2;
                    } else {
                        interactionType = 3;
                    }
                }
            }
        } while ( interactionType != 3 );
    }

    private void addRay( double x0, double y0, double x1, double y1, Color c ) {
        boolean steep = Math.abs( y1 - y0 ) >= Math.abs( x1 - x0 );
        double t;

        if ( steep ) {
            t = y0;
            y0 = x0;
            x0 = t;
            t = y1;
            y1 = x1;
            x1 = t;
        }
        if ( x0 > x1 ) {
            t = x1;
            x1 = x0;
            x0 = t;
            t = y1;
            y1 = y0;
            y0 = t;
        }
        double dx = x1 - x0;
        double dy = y1 - y0;
        double br = 0.5 * Math.sqrt( dx * dx + dy * dy ) / dx;
        double gradient = dy / dx;

        double x05 = x0 + 0.5f;
        int xend = (int) Math.floor( x05 );
        double yend = y0 + gradient * (xend - x0);
        double xgap = br * (1 - (x05 - xend));
        double xpxl1 = xend;
        double ypxl1 = Math.floor( yend );
        if ( steep ) {
            plotRay( (int) ypxl1, (int) xpxl1, (1 - (yend - ypxl1)) * xgap, c );
            plotRay( (int) ypxl1 + 1, (int) xpxl1, (yend - ypxl1) * xgap, c );
        } else {
            plotRay( (int) xpxl1, (int) ypxl1, (1 - (yend - ypxl1)) * xgap, c );
            plotRay( (int) xpxl1, (int) ypxl1 + 1, (yend - ypxl1) * xgap, c );
        }
        double intery = yend + gradient;

        double x15 = x1 + 0.5f;
        xend = (int) Math.floor( x15 );
        yend = y1 + gradient * (xend - x1);
        xgap = br * (x15 - xend);
        double xpxl2 = xend;
        double ypxl2 = Math.floor( yend );
        if ( steep ) {
            plotRay( (int) ypxl2, (int) xpxl2, (1 - (yend - ypxl2)) * xgap, c );
            plotRay( (int) ypxl2 + 1, (int) xpxl2, (yend - ypxl2) * xgap, c );
        } else {
            plotRay( (int) xpxl2, (int) ypxl2, (1 - (yend - ypxl2)) * xgap, c );
            plotRay( (int) xpxl2, (int) ypxl2 + 1, (yend - ypxl2) * xgap, c );
        }
        int fintery;
        if ( steep ) {
            for ( double x = xpxl1 + 1; x <= xpxl2 - 1; x++ ) {
                fintery = (int) Math.floor( intery );
                plotRay( fintery, (int) x, br * (1 - (intery - fintery)), c );
                plotRay( fintery + 1, (int) x, br * (intery - fintery), c );
                intery = intery + gradient;
            }
        } else {
            for ( double x = xpxl1 + 1; x <= xpxl2 - 1; x++ ) {
                fintery = (int) Math.floor( intery );
                plotRay( (int) x, fintery, br * (1 - (intery - fintery)), c );
                plotRay( (int) x, fintery + 1, br * (intery - fintery), c );
                intery = intery + gradient;
            }
        }
    }

    private void plotRay( int x, int y, double c, Color o ) {
        if ( x >= 0 && x < 1000 && y >= 0 && y < 600 ) {
            rawPixelInfo[(y * 1000 * COLOR_DEPTH) + (x * COLOR_DEPTH) + 0] += (c * o.getRed());
            rawPixelInfo[(y * 1000 * COLOR_DEPTH) + (x * COLOR_DEPTH) + 1] += (c * o.getGreen());
            rawPixelInfo[(y * 1000 * COLOR_DEPTH) + (x * COLOR_DEPTH) + 2] += (c * o.getBlue());
            rawPixelInfo[(y * 1000 * COLOR_DEPTH) + (x * COLOR_DEPTH) + 3] += (c);
        }
    }

    public long getGlobalRayCount() {
        return globalRayCount;
    }
}
