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

import java.util.Comparator;
import java.util.TreeSet;

/**
 * A Dijikstra's pathfinding class.
 *
 * @author Tristan Homsi
 * @version 1.0
 * @since 1.0
 */
public class DJPathfinding {

    private static final double r2 = Math.sqrt( 2 ) - 1;
    private double[][] distance;
    private boolean[][] visited;
    private Map map;
    private int findX, findY;
    private TreeSet<Block> open = new TreeSet<Block>( new BlockComparator() );

    /**
     * Creates a new Dijkstra's pathfinding object.
     *
     * @param in     The Map object to pathfind in.
     * @param blockx The x-coordinate of the target.
     * @param blocky The x-coordinate of the target.
     */
    public DJPathfinding( Map in, int blockx, int blocky ) throws ArrayIndexOutOfBoundsException {
        map = in;
        distance = new double[map.getXSize()][map.getYSize()];
        if ( blockx >= 0 && blockx < map.getXSize() && blocky >= 0 && blocky < map.getYSize() ) {
            findX = blockx;
            findY = blocky;
        } else {
            throw new ArrayIndexOutOfBoundsException( "Target coordinates are outside of map bounds." );
        }
    }

    /**
     * @return A double array representing the scored of each coordinate.
     */
    public double[][] getScores() {
        return distance;
    }

    /**
     * Recursively finds a path that should be followed.
     *
     * @param x          The x coordinate of the source.
     * @param y          The y coordinate of the source.
     * @param crossWalls Whether or not the source should walk diagonally over the corners of walls.
     * @return A path object that represents the closest path from the source to the target.
     */
    public Path returnPath( int x, int y, boolean crossWalls ) throws ArrayIndexOutOfBoundsException {
        return new Path( map, getScores(), x, y, crossWalls );
    }

    /**
     * Updates pathfinding for new targets and blocks.
     */
    public void updatePath() {
        visited = new boolean[map.getXSize()][map.getYSize()];
        for ( int i = 0; i < map.getXSize(); ++i ) {
            for ( int j = 0; j < map.getYSize(); ++j ) {
                distance[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        Block target = map.getBlock( findX, findY );
        distance[target.x][target.y] = 0;
        open.add( target );
        scoreBlocks();
    }

    private void scoreBlocks() {
        Block check;
        while ( !open.isEmpty() ) {
            check = open.pollFirst();
            updateSurrounding( check.x, check.y );
            visited[check.x][check.y] = true;
        }
    }

    private void updateSurrounding( int x, int y ) {
        if ( !visited[x + 1][y] && distance[x][y] + map.getBlock( x + 1, y ).pathLength < distance[x + 1][y] ) {
            distance[x + 1][y] = distance[x][y] + map.getBlock( x + 1, y ).pathLength;
            open.add( map.getBlock( x + 1, y ) );
        }
        if ( !visited[x][y + 1] && distance[x][y] + map.getBlock( x, y + 1 ).pathLength < distance[x][y + 1] ) {
            distance[x][y + 1] = distance[x][y] + map.getBlock( x, y + 1 ).pathLength;
            open.add( map.getBlock( x, y + 1 ) );
        }
        if ( !visited[x - 1][y] && distance[x][y] + map.getBlock( x - 1, y ).pathLength < distance[x - 1][y] ) {
            distance[x - 1][y] = distance[x][y] + map.getBlock( x - 1, y ).pathLength;
            open.add( map.getBlock( x - 1, y ) );
        }
        if ( !visited[x][y - 1] && distance[x][y] + map.getBlock( x, y - 1 ).pathLength < distance[x][y - 1] ) {
            distance[x][y - 1] = distance[x][y] + map.getBlock( x, y - 1 ).pathLength;
            open.add( map.getBlock( x, y - 1 ) );
        }

        if ( !visited[x + 1][y + 1] && distance[x][y] + r2 + map.getBlock( x + 1, y + 1 ).pathLength < distance[x + 1][y + 1] ) {
            distance[x + 1][y + 1] = distance[x][y] + r2 + map.getBlock( x + 1, y + 1 ).pathLength;
            open.add( map.getBlock( x + 1, y + 1 ) );
        }
        if ( !visited[x + 1][y - 1] && distance[x][y] + r2 + map.getBlock( x + 1, y - 1 ).pathLength < distance[x + 1][y - 1] ) {
            distance[x + 1][y - 1] = distance[x][y] + r2 + map.getBlock( x + 1, y - 1 ).pathLength;
            open.add( map.getBlock( x + 1, y - 1 ) );
        }
        if ( !visited[x - 1][y + 1] && distance[x][y] + r2 + map.getBlock( x - 1, y + 1 ).pathLength < distance[x - 1][y + 1] ) {
            distance[x - 1][y + 1] = distance[x][y] + r2 + map.getBlock( x - 1, y + 1 ).pathLength;
            open.add( map.getBlock( x - 1, y + 1 ) );
        }
        if ( !visited[x - 1][y - 1] && distance[x][y] + r2 + map.getBlock( x - 1, y - 1 ).pathLength < distance[x - 1][y - 1] ) {
            distance[x - 1][y - 1] = distance[x][y] + r2 + map.getBlock( x - 1, y - 1 ).pathLength;
            open.add( map.getBlock( x - 1, y - 1 ) );
        }
    }

    /**
     * Changes the location of the target
     *
     * @param x The x-coordinate of the new target.
     * @param y The y-coordinate of the new target.
     */
    public void changeTarget( int x, int y ) {
        findX = x;
        findY = y;
    }

    /**
     * Changes the map object that is used for pathfinding.
     *
     * @param in The new map object.
     */
    public void changeMap( Map in ) {
        map = in;
    }

    class BlockComparator implements Comparator<Block> {
        @Override
        public int compare( Block a, Block b ) {
            if ( a.equals( b ) )
                return 0;
            else if ( distance[a.x][a.y] < distance[b.x][b.y] )
                return -1;
            else
                return 1;
        }
    }

}