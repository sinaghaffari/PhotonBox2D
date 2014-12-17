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
package s2d.input;

import s2d.display.Display;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A keyboard manager class that handles more convenient keyboard input than
 * that of Java's default keyboard methods.
 *
 * @author Sina Ghaffari
 * @version 1.0
 * @since 1.0
 */
public class Keyboard {
    private static Set<Integer> usedKeys = new HashSet<Integer>();
    private static Set<Integer> tempKeyPressed = new HashSet<Integer>();
    private static Set<Integer> keyTyped = new HashSet<Integer>();
    private static Set<Integer> keyPressed = new HashSet<Integer>();
    private static Set<Integer> keyNotPressed = new HashSet<Integer>();
    private static Set<Integer> keyReleased = new HashSet<Integer>();

    /**
     * Polls all Keyboard events. Should generally be called often if not
     * calling {@link Display#update()} or {@link Display#processDisplayEvents()}.
     *
     * @see Display#processDisplayEvents()
     * @see Display#swapBuffers()
     */
    public static void updateKeyboard() {
        Set<Integer> temp = new HashSet<Integer>();
        temp.addAll( keyPressed );
        Set<Integer> total = new HashSet<Integer>( temp );
        keyTyped.clear();
        keyPressed.clear();
        keyReleased.clear();
        keyPressed.addAll( tempKeyPressed );
        total.addAll( keyPressed );
        for ( Integer integer : total ) {
            int i = integer;
            if ( (keyNotPressed.contains( i ) && keyPressed.contains( i )) ) {
                keyTyped.add( i );
                keyNotPressed.remove( i );
                usedKeys.add( i );
            } else if ( !usedKeys.contains( i ) ) {
                if ( !temp.contains( i ) ) {
                    keyTyped.add( i );
                }
            }
            if ( temp.contains( i ) && !keyPressed.contains( i ) ) {
                keyReleased.add( i );
            }
        }

    }

    /**
     * Adds a key code to the current keyboard arrays. Should be called when a
     * key is pressed. It will be polled and usable when the keyboard is
     * updated. Note: This is handled automatically by the {@link Display}
     * class.
     *
     * @param i The {@link java.awt.event.KeyEvent} of the key that should be added.
     * @see java.awt.event.KeyEvent
     * @see Display
     */
    public static void addKey( int i ) {
        tempKeyPressed.add( i );
    }

    /**
     * Removes a key code to the current keyboard arrays. Should be called when
     * a key is released. Note: This is handled automatically by the
     * {@link Display} class.
     *
     * @param i The {@link java.awt.event.KeyEvent} of the key that should be added.
     * @see java.awt.event.KeyEvent
     * @see Display
     */
    public static void removeKey( int i ) {
        tempKeyPressed.remove( i );
        keyNotPressed.add( i );
    }

    /**
     * Determines whether or not a key is being held down or not from the
     * current poll.
     *
     * @param i The {@link java.awt.event.KeyEvent} of the key that should be checked.
     * @return True when the key is being pressed down.
     */
    public static boolean isKeyDown( int i ) {
        return keyPressed.contains( i );
    }

    /**
     * Returns true the first poll that this key was pressed down and false
     * every other poll until it is released. This is useful for doing a single
     * task upon a key press without repeating it every time the Keyboard is
     * updated. This will only return true once pressing and releasing a key.
     *
     * @param i The {@link java.awt.event.KeyEvent} of the key that should be checked.
     * @return True when the key is first pressed.
     */
    public static boolean isKeyTyped( int i ) {
        return keyTyped.contains( i );
    }

    /**
     * Returns true the first poll that this key was released and false
     * every other poll until is pressed and released again. This is useful for doing a single
     * task upon a key press without repeating it every time the Keyboard is
     * updated.
     *
     * @param i The {@link java.awt.event.KeyEvent} of the key that should be checked.
     * @return True when the key is first released.
     */
    public static boolean isKeyReleased( int i ) {
        return keyNotPressed.contains( i );
    }

    /**
     * @return an {@link java.util.ArrayList} containing every key that is currently pressed.
     */
    public static ArrayList<Integer> getAllKeysDown() {
        return new ArrayList<Integer>( keyPressed );
    }

    /**
     * @return an {@link java.util.ArrayList} containing every key has just been released.
     */
    public static ArrayList<Integer> getAllKeysReleased() {
        return new ArrayList<Integer>( keyReleased );
    }

    /**
     * @return an {@link java.util.ArrayList} containing every key has just been pressed.
     */
    public static ArrayList<Integer> getAllKeysTyped() {
        return new ArrayList<Integer>( keyTyped );
    }
}
