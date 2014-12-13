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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A representation of a map.
 *
 * @author Sina Ghaffari
 * @author Tristan Homsi
 * @version 1.0
 * @since 1.0
 */
public class Map {

    private int xSize = 0, ySize = 0;
    private int blockSize = 20;
    private Block[][] block = null;

    /**
     * Creates a new map from a text file.
     *
     * @param fileName The textfile containing the map imformation.
     */
    public Map( String fileName ) {
        try {
            createMapFromFile( fileName );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private void createMapFromFile( String fileName ) throws IOException {
        BufferedReader br = new BufferedReader( new FileReader( fileName ) );

        String t = br.readLine();
        xSize = t.length();
        while ( t != null ) {
            ySize++;
            t = br.readLine();
        }
        block = new Block[xSize][ySize];
        br.close();
        br = new BufferedReader( new FileReader( fileName ) );
        int counter = 0;
        t = br.readLine();
        while ( t != null ) {
            for ( int a = 0; a < xSize; a++ ) {
                int i = Character.getNumericValue( t.charAt( a ) );
                double pL = 1;
                if ( i == 1 ) {
                    pL = Double.POSITIVE_INFINITY;
                }
                block[a][counter] = new Block( a, counter, i, pL, i == 1 );
            }
            counter++;
            t = br.readLine();
        }
        br.close();
        //printMap();
    }

    /**
     * Prints the map information in the consol.
     */
    public void printMap() {
        if ( block == null )
            System.out.println( "No map!" );
        else {
            for ( int i = 0; i < ySize; ++i ) {
                for ( int j = 0; j < xSize; ++j )
                    System.out.print( block[i][j].id );
                System.out.println();
            }
        }
    }

    /**
     * @return The width of the map in blocks.
     */
    public int getXSize() {
        return xSize;
    }

    /**
     * @return The height of the map in blocks.
     */
    public int getYSize() {
        return ySize;
    }

    /**
     * @param x The x-coordinate of the block.
     * @param y The y-coordinate of the block.
     * @return The block at that coordinate.
     */
    public Block getBlock( int x, int y ) throws ArrayIndexOutOfBoundsException {
        return block[x][y];
    }
}