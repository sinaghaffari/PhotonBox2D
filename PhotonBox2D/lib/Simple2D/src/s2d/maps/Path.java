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
package s2d.maps;

import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 * An object that contains detailed information about the
 * Path returned by {@link DJPathfinding}.
 *
 * @author Sina Ghaffari
 * @version 1.0
 * @since 1.0
 */
public class Path {
    /**
     * An {@link ArrayList} of ordered {@link Block}s from target to source.
     */
    public ArrayList<Block> pl = new ArrayList<Block>();
    /**
     * A {@link GeneralPath} tracing the path from target to source.
     */
    public GeneralPath gp = new GeneralPath();
    private Map map;
    private boolean crossWalls;

    public Path( Map map, double[][] scores, int x, int y, boolean crossWalls ) throws ArrayIndexOutOfBoundsException {
        if ( !(x >= 0 && x < map.getXSize() && y >= 0 && y < map.getYSize()) )
            throw new ArrayIndexOutOfBoundsException( "target coordinates are outside of map bounds" );
        this.map = map;
        this.crossWalls = crossWalls;
        gp.moveTo( x, y );
        tracePath( scores, new int[]{ x, y } );
    }

    private int[] getBestDirection( double[][] distance, int[] xy ) throws ArrayIndexOutOfBoundsException {
        double bestScore = distance[xy[0]][xy[1]];
        int[] bestDirection = new int[]{ 0, 0 };
        for ( int a = -1; a <= 1; a++ ) {
            for ( int b = -1; b <= 1; b++ ) {
                if ( !(a == 0 && b == 0) && distance[xy[0] + a][xy[1] + b] < bestScore && !((a != 0 && b != 0) && (!crossWalls && (map.getBlock( xy[0] + a, xy[1] ).solidity || map.getBlock( xy[0], xy[1] + b ).solidity))) ) {
                    bestScore = distance[xy[0] + a][xy[1] + b];
                    bestDirection[0] = a;
                    bestDirection[1] = b;
                }
            }
        }
        return bestDirection;
    }

    private void tracePath( double[][] distance, int[] xy ) throws ArrayIndexOutOfBoundsException {

        int[] xyn = getBestDirection( distance, xy );
        if ( xyn[0] == 0 && xyn[1] == 0 )
            return;
        pl.add( map.getBlock( xy[0] + xyn[0], xy[1] + xyn[1] ) );
        gp.lineTo( xy[0] + xyn[0], xy[1] + xyn[1] );
        tracePath( distance, new int[]{ xy[0] + xyn[0], xy[1] + xyn[1] } );
    }
}
