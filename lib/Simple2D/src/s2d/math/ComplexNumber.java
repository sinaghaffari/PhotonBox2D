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

/**
 * A representation of the complex number <code>a + bi</code> where:<br>
 * <code>a</code> is the real part of the complex number.<br>
 * <code>b</code> is the imaginary part of the complex number.<br>
 * and <code>i</code> is
 *
 * @author Sina Ghaffari
 * @version 1.0
 * @since 1.0
 */
public class ComplexNumber {
    public double a;
    public double b;

    /**
     * Created a complex number <code>a + bi</code>
     *
     * @param r The real portion of a complex number.
     * @param i The imaginary coefficient of a complex number.
     */
    public ComplexNumber( double r, double i ) {
        this.a = r;
        this.b = i;
    }

    /**
     * Adds a real number to the complex number.
     *
     * @param n A real number to be added to the complex number.
     * @return A complex number that is the sum of the two.
     */
    public ComplexNumber add( double n ) {
        return new ComplexNumber( n + a, b );
    }

    /**
     * Adds two complex numbers together.
     *
     * @param n A real number to be added to the complex number.
     * @return A {@link s2d.math.ComplexNumber} that is the sum of the two.
     */
    public ComplexNumber add( ComplexNumber n ) {
        return new ComplexNumber( n.a + a, n.b + b );
    }

    /**
     * Subtracts n from the complex number.
     *
     * @param n A real number to be subtracted from the complex number.
     * @return A complex number representing the difference of the two numbers.
     */
    public ComplexNumber subtract( double n ) {
        return new ComplexNumber( a - n, b );
    }

    /**
     * Subtracts n from the complex number.
     *
     * @param n A complex number to be subtracted from the complex number.
     * @return A complex number representing the difference of the two numbers.
     */
    public ComplexNumber subtract( ComplexNumber n ) {
        return new ComplexNumber( a - n.a, n.b - b );
    }

    /**
     * Multiplies the complex number by n.
     *
     * @param n A real number that is multiplied into the complex number.
     * @return A complex number that is the product of the two numbers.
     */
    public ComplexNumber multiply( double n ) {
        return new ComplexNumber( n * a, n * b );
    }

    /**
     * Multiplies the complex number by n.
     *
     * @param n A complex number that is multiplied into the current complex value.
     * @return A complex number that is the product of the two numbers.
     */
    public ComplexNumber multiply( ComplexNumber n ) {
        return new ComplexNumber( (a * n.a - b * n.b), (b * n.a + a * n.b) );
    }

    /**
     * Divides the complex number by n.
     *
     * @param n A real number that is divided into the complex number.
     * @return A complex number that is the quotient of the two numbers.
     */
    public ComplexNumber divide( double n ) {
        return new ComplexNumber( a / n, b / n );
    }

    /**
     * Divides the complex number by n.
     *
     * @param n A complex number that is divided into the current complex value.
     * @return A complex number that is the quotient of the two numbers.
     */
    public ComplexNumber divide( ComplexNumber n ) {
        return new ComplexNumber( (a * n.a + b * n.b) / (n.a * n.a + n.b * n.b), (b * n.a - a * n.b) / (n.a * n.a + n.b * n.b) );
    }

    /**
     * Determines the cube root of a complex number
     *
     * @return A complex number representing the cube root of the current complex number.
     */
    public ComplexNumber cbrt() {
        // Change to polar form.
        double r = Math.sqrt( this.a * this.a + this.b * this.b );
        double t = Math.atan2( (this.b), (this.a) );
        //determine and return cube root.
        double cbrtR = Math.cbrt( r );
        return new ComplexNumber( cbrtR * Math.cos( t / 3 ), cbrtR * Math.sin( t / 3 ) );
    }

}
