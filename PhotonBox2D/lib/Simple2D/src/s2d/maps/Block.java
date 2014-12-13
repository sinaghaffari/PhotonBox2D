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

/**
 * A block class used to maps and pathfinding.
 *
 * @author Sina Ghaffari
 * @author Tristan Homsi
 * @version 1.0
 * @since 1.0
 */
public class Block {
    public int x, y, id;
    public double pathLength;
    public boolean solidity;

    /**
     * Creates a block with the given parameters.
     *
     * @param x  The x-coordinate of the block.
     * @param y  The y-coordinate of the block.
     * @param id The id of the block.
     */
    public Block( int x, int y, int id ) {
        this( x, y, id, 1, false );
    }

    /**
     * Creates a block with the given parameters.
     *
     * @param x          The x-coordinate of the block.
     * @param y          The y-coordinate of the block.
     * @param id         The id of the block.
     * @param pathLength The length of the path of this block.
     */
    public Block( int x, int y, int id, double pathLength ) {
        this( x, y, id, pathLength, false );
    }

    /**
     * Creates a block with the given parameters.
     *
     * @param x          The x-coordinate of the block.
     * @param y          The y-coordinate of the block.
     * @param id         The id of the block.
     * @param pathLength The length of the path of this block.
     * @param solidity   Whether or not the block is passable.
     */
    public Block( int x, int y, int id, double pathLength, boolean solidity ) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.pathLength = pathLength;
        this.solidity = solidity;
    }

    /**
     * Creates a block with the same parameters of another block.
     *
     * @param in The block to copy.
     */
    public Block( Block in ) {
        this.x = in.x;
        this.y = in.y;
        this.id = in.id;
        this.pathLength = in.pathLength;
        this.solidity = in.solidity;
    }

    /**
     * Checks whether or not two blocks are equal.
     *
     * @param in The block to compare to.
     * @return Whether or not the blocks are equal.
     */
    public boolean equals( Block in ) {
        return this.x == in.x && this.y == in.y && this.id == in.id && this.solidity == in.solidity && this.pathLength == in.pathLength;
    }
}
