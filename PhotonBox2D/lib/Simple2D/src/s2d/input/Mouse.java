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


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * A simple and easy method of tracking and reading mouse info.
 * It is integrated into the {@link s2d.display.Display}.
 *
 * @author Sina Ghaffari
 * @version 1.1
 * @since 1.0
 */
public class Mouse {
    private static int[] mouseButton = { 0, 0, 0 };
    private static int prevMouseX, prevMouseY;
    private static int currentMouseX, currentMouseY;
    private static int currentMouseXRelativeToScreen, currentMouseYRelativeToScreen;
    private static int deltaMouseX, deltaMouseY;
    private static double deltaMouseWheel;
    private static double tempDeltaMouseWheel;
    private static int tempMouseX, tempMouseY;
    private static int[] tempMouseButton = { 0, 0, 0 };
    private static Component parentComponent = null;
    private static boolean isMouseAttached = false;
    private static boolean isMouseOnScreen = false;

    /**
     * Attaches the mouse to the given component to receive input. Must be done before the mouse will work.
     *
     * @param parent The component that handles mouse input.
     */
    public static void attachMouse( Component parent ) {
        isMouseAttached = true;
        parentComponent = parent;
    }

    /**
     * Polls all mouse events that happened since the last poll and makes them
     * available to the get methods.
     */
    public static void updateMouse() {
        if ( isMouseAttached ) {
            tempMouseX = MouseInfo.getPointerInfo().getLocation().x;
            tempMouseY = MouseInfo.getPointerInfo().getLocation().y;
            mouseButton = tempMouseButton;
            prevMouseX = currentMouseX;
            prevMouseY = currentMouseY;
            currentMouseX = tempMouseX - parentComponent.getLocationOnScreen().x;
            currentMouseY = tempMouseY - parentComponent.getLocationOnScreen().y;
            currentMouseXRelativeToScreen = tempMouseX;
            currentMouseYRelativeToScreen = tempMouseY;
            deltaMouseX = currentMouseX - prevMouseX;
            deltaMouseY = currentMouseY - prevMouseY;
            deltaMouseWheel = tempDeltaMouseWheel;
            tempDeltaMouseWheel = 0;
            if ( (tempMouseButton[0] & 4096) == 4096 )
                tempMouseButton[0] = 0;
            if ( (tempMouseButton[0] & 2048) == 2048 )
                tempMouseButton[0] &= ~2048;
            if ( (tempMouseButton[1] & 4096) == 4096 )
                tempMouseButton[1] = 0;
            if ( (tempMouseButton[1] & 2048) == 2048 )
                tempMouseButton[1] &= ~2048;
            if ( (tempMouseButton[2] & 4096) == 4096 )
                tempMouseButton[2] = 0;
            if ( (tempMouseButton[2] & 2048) == 2048 )
                tempMouseButton[2] &= ~2048;
            isMouseOnScreen = (parentComponent.getBounds().contains( currentMouseX, currentMouseY ));
        }
    }

    /**
     * Sets mouse events to be polled. Should be called from every java mouse
     * method.
     *
     * @param e The {@link MouseEvent} that should be used to poll.
     */
    public static void setMouse( MouseEvent e ) {
        int b = e.getButton();
        if ( b > 0 ) {
            int p = e.getID();
            boolean isPressed = (p & MouseEvent.MOUSE_PRESSED) == MouseEvent.MOUSE_PRESSED;
            boolean isReleased = (p & MouseEvent.MOUSE_RELEASED) == MouseEvent.MOUSE_RELEASED;
            tempMouseButton[b - 1] |= ((isPressed) ? 1024 : 0);
            tempMouseButton[b - 1] |= ((isPressed) ? 2048 : 0);
            tempMouseButton[b - 1] |= ((isReleased) ? 4096 : 0);
        }


    }

    /**
     * Sets mouse wheel events to be polled. Should be called from every java mouse wheel
     * method.
     *
     * @param e The {@link MouseWheelEvent} that should be used to poll.
     */
    public static void setMouse( MouseWheelEvent e ) {
        Mouse.tempDeltaMouseWheel += e.getPreciseWheelRotation();
    }

    /**
     * Determines whether or not the given mouse button is down.<br>
     * Button 0 - Left Mouse Button<br>
     * Button 1 - Middle Mouse Button<br>
     * Button 2 - Right Mouse Button
     *
     * @param i The mouse button that should be checked.
     * @return true if the mouse button is down.
     */
    public static boolean isButtonDown( int i ) {
        if ( (i < 0) || (i > 2) )
            return false;
        return (mouseButton[i] & 1024) == 1024;
    }

    /**
     * Determines whether or not the given mouse button was clicked.<br>
     * This method will only return true for the first update that the mouse
     * was pressed and will return false for all other updates until the
     * button is released and re-pressed.<br>
     * Button 0 - Left Mouse Button<br>
     * Button 1 - Middle Mouse Button<br>
     * Button 2 - Right Mouse Button
     *
     * @param i The mouse button that should be checked.
     * @return true if the mouse button was clicked.
     */
    public static boolean isButtonClicked( int i ) {
        if ( (i < 0) || (i > 2) )
            return false;
        return (mouseButton[i] & 2048) == 2048;
    }

    /**
     * Determines whether or not the given mouse button was released.<br>
     * Will only return true for the first update after being released. <br>
     * Button 0 - Left Mouse Button<br>
     * Button 1 - Middle Mouse Button<br>
     * Button 2 - Right Mouse Button
     *
     * @param i The mouse button that should be checked.
     * @return true if the mouse button was clicked.
     */
    public static boolean isButtonReleased( int i ) {
        if ( (i < 0) || (i > 2) )
            return false;
        return (mouseButton[i] & 4096) == 4096;
    }

    /**
     * Get the current X coordinate of the mouse as it was at the last poll.
     *
     * @return The X coordinate of the mouse relative to the top left of the
     * display.
     */
    public static int getX() {
        return currentMouseX;
    }

    /**
     * Applies an {@link AffineTransform} to the X coordinate of the mouse. <br>
     * This is useful when are scaling and translating a graphics object but want the mouse input to stay accurate. <br>
     * <b> Note:</b> This does NOT permanently change the mouse coordinate.
     *
     * @param af The {@link AffineTransform} that will be applied.
     * @return The translated Mouse coordinate as a double.
     * @throws NoninvertibleTransformException
     * @see AffineTransform
     */
    public static double getX( AffineTransform af ) throws NoninvertibleTransformException {
        Point2D trans = new Point2D.Double();
        af.inverseTransform( new Point2D.Double( currentMouseX, currentMouseY ), trans );
        return trans.getX();
    }

    /**
     * Get the current Y coordinate of the mouse as it was at the last poll.
     *
     * @return The Y coordinate of the mouse relative to the top left of the
     * display.
     */
    public static int getY() {
        return currentMouseY;
    }

    /**
     * Get the current X coordinate of the mouse on screen as it was at the last poll.
     *
     * @return The X coordinate of the mouse relative to the top left of the
     * screen.
     */
    public static int getXOnScreen() {
        return currentMouseXRelativeToScreen;
    }

    /**
     * Get the current Y coordinate of the mouse on screen as it was at the last poll.
     *
     * @return The Y coordinate of the mouse relative to the top left of the
     * screen.
     */
    public static int getYOnScreen() {
        return currentMouseYRelativeToScreen;
    }

    /**
     * Applies an {@link AffineTransform} to the Y coordinate of the mouse. <br>
     * This is useful when are scaling and translating a graphics object but want the mouse input to stay accurate. <br>
     * <b> Note:</b> This does NOT permanently change the mouse coordinate.
     *
     * @param transformation The {@link AffineTransform} that will be applied.
     * @return The translated Mouse coordinate as a double.
     * @throws NoninvertibleTransformException
     * @see AffineTransform
     */
    public static double getY( AffineTransform transformation ) throws NoninvertibleTransformException {
        Point2D trans = new Point2D.Double();
        transformation.inverseTransform( new Point2D.Double( currentMouseX, currentMouseY ), trans );
        return trans.getY();
    }

    /**
     * The amount that the X coordinate changed since the last poll.
     *
     * @return The change in the X coordinate since the last poll.
     */
    public static int getDX() {
        return deltaMouseX;
    }

    /**
     * The amount that the Y coordinate changed since the last poll.
     *
     * @return The change in the Y coordinate since the last poll.
     */
    public static int getDY() {
        return deltaMouseY;
    }

    /**
     * The number of clicks that the mouse wheel has turned since the last poll.
     *
     * @return The number of clicks that the mouse wheel has turned. Negative if the mouse wheel was turned away from the user.
     */
    public static double getDWheel() {
        return deltaMouseWheel;
    }

    /**
     * @return Whether or not the mouse is currently on the bounds of the parent screen.
     */
    public static boolean isOnScreen() {
        return isMouseOnScreen;
    }

}
