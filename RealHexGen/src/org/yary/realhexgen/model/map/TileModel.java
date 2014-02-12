package org.yary.realhexgen.model.map;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import org.yary.realhexgen.controller.events.HexEventRegister;
import org.yary.realhexgen.controller.events.tile.RedrawTile;
import org.yary.realhexgen.model.ConfigurationData;

// id hexa ! ! ! ! ! 
// atak, obrona, ruch
// dijkstra oblicza czy można położyć jednostkę (odl. < x)
// ew pop-up - info o niemożności położenia jednostki
// ruch(), atak(), akcja()
// czy jest jednostka, czy baza <- baza generuje jednostki
// JA - DEPLOY(ID_gracza, rodzaj_jednostki) - rozstawianie jednostek wokol bazy
// modyfikuje ilosc jednostek dla gracza - tablica globalna



/*
 *
 * @author Yary Ribero
 */
public class TileModel {

    public static final double ROW_DISTANCE_FACTOR = 0.5;
    public static final double COLUMN_DISTANCE_FACTOR = Math.sqrt ( 3 ) / 2;

    public static Color transpGray2 = new Color (0.753f, 0.753f, 0.753f, 0.2f); // R,G,B,Alpha
    public static Color transpGray3 = new Color (0.878f, 0.878f, 0.878f, 0.2f); // R,G,B,Alpha
    public static Color transpBlue = new Color (0.2f, 0.6f, 1f, 0.2f);
    public static Color transpGray1 = new Color (0.376f, 0.376f, 0.376f, 0.2f);
    
    private static Color [] colors = new Color [] { transpGray1, transpGray2, transpBlue, transpGray3};//Color.CYAN, Color.GREEN, transparentBlue, Color.ORANGE, transparentRed, Color.YELLOW };
    
    private int row;
    private int column;
    private Color baseColor;
    private int id;

    private boolean selected = false;
    private Color color;

    private HashMap < TileModel, TileConnection > connections = new  HashMap < TileModel, TileConnection > ();
    //private boolean inPath = false;

    //private boolean reachable = true;

    public Color redPlayer;
    public Color bluePlayer;
    
    public TileModel ( int row, int column, int id ) throws IllegalArgumentException {
        if ( row < 0 )
            throw new IllegalArgumentException ( "Parameter row out of bound [0,+inf]: " + row );

        if ( column < 0 )
            throw new IllegalArgumentException ( "Parameter column out of bound [0,+inf]: " + column );

        this.row = row;
        this.column = column;
        this.id = id;
        
        color = baseColor = colors [ ( int ) ( Math.random () * colors.length ) ];
    }

    public int getRow () {
        return row;
    }

    public int getColumn () {
        return column;
    }

    public Color getColor () {
        return color;
    }
    
    public int getId() {
        return id;
    }

    public void switchColor(Color c) {
        color = c;
    }
    
    public void setSelected ( boolean selected, Color c ) {
        if ( this.selected == selected ) {
            return;
        }

        this.selected = selected;
        System.out.println("selected id: "+getId()); // sprawdzam sobie id

        if ( selected ) {
            switchColor(c);
        }
        else
            color = baseColor;

        HexEventRegister.getInstance ().triggerEvent ( new RedrawTile ( "tileRequest", this ) );
    }

    public int getStartX () {
        return 3 * ConfigurationData.getInstance ().getEdge () / 2 * column;
    }

    public int getStartY () {
        return ( ConfigurationData.getInstance ().getHeight () / 2 ) * row;
    }

    public int getEndX () {
        return getStartX () + ConfigurationData.getInstance ().getEdge () * 2;
    }

    public int getEndY () {
        return getStartY () + ConfigurationData.getInstance ().getHeight ();
    }

    public static double distance ( int row1, int column1, int row2, int column2 ) {
        if ( row1 < 0 )
            throw new IllegalArgumentException ( "Parameter row1 should be non negative, it is: " + row1 );

        if ( column1 < 0 )
            throw new IllegalArgumentException ( "Parameter column1 should be non negative, it is: " + column1 );

        if ( row2 < 0 )
            throw new IllegalArgumentException ( "Parameter row2 should be non negative, it is: " + row2 );

        if ( column2 < 0 )
            throw new IllegalArgumentException ( "Parameter column2 should be non negative, it is: " + column2 );

        return Math.sqrt (
            Math.pow ( ( row2 - row1 ) * ROW_DISTANCE_FACTOR , 2 ) +
            Math.pow ( ( column2 - column1 ) * COLUMN_DISTANCE_FACTOR, 2 )
        );
    }

    public static double distance ( TileModel tile1, TileModel tile2 ) {
        if ( tile1 == null )
            throw new IllegalArgumentException ( "Parameter tile1 is null" );

        if ( tile2 == null )
            throw new IllegalArgumentException ( "Parameter tile2 is null" );

        return distance ( tile1.getRow (), tile1.getColumn (), tile2.getRow (), tile2.getColumn () );
    }

    public Set < TileModel > getNeighbours () {
        return connections.keySet ();
    }

    /*public LinkedList < TileModel > getReachableNeighbours () {
        LinkedList < TileModel > reachableNeighbours = new LinkedList < TileModel > ();

        for ( TileModel tile : connections.keySet () )
            if ( connections.get ( tile ).isEnabled () )
                reachableNeighbours.add ( tile );

        return reachableNeighbours;
    }*/

    public Collection < TileConnection > getConnections () {
        return connections.values ();
    }

    public HashMap < TileModel, TileConnection > getCompleteConnections () {
        return connections;
    }

    public boolean isAdjacent ( TileModel tile ) {
        if ( tile == null )
            throw new IllegalArgumentException ( "Parameter tile is null" );

        return isAdjacent ( tile.getRow (), tile.getColumn () );
    }

    public boolean isAdjacent ( int row, int column ) {
        if ( row < 0 )
            throw new IllegalArgumentException ( "Parameter row should be non negative, it is set to: " + row );

        if ( column < 0 )
            throw new IllegalArgumentException ( "Parameter column should be non negative, it is set to: " + column );

        for ( TileModel tile : connections.keySet () )
            if ( tile.getRow () == row && tile.getColumn () == column )
                return true;

        return false;
    }

    public void addConnection ( TileModel tile, TileConnection connection ) {
        if ( tile == null )
            throw new IllegalArgumentException ( "Parameter tile is null" );

        if ( connection == null )
            throw new IllegalArgumentException ( "Parameter connection is null" );

        if ( ! areAdjacent ( this, tile) )
            throw new IllegalArgumentException (
                "It is not possible to connect tile " + row + "," + column + " with tile " +
                tile.getRow () + "," + tile.getColumn () + " because they are not adjacent"
            );

        connections.put ( tile, connection );
    }

    // Przyda się do walki:
    public static boolean areAdjacent ( TileModel tile1, TileModel tile2 ) {
        if ( tile1 == null )
            throw new IllegalArgumentException ( "Parameter tile1 is null" );

        if ( tile2 == null )
            throw new IllegalArgumentException ( "Parameter tile2 is null" );

        return areAdjacent ( tile1.getRow (), tile1.getColumn (), tile2.getRow (), tile2.getColumn () );
    }

    public static boolean areAdjacent ( int row1, int column1, int row2, int column2 ) {
        if ( row1 < 0 )
            throw new IllegalArgumentException ( "Parameter row1 should be non negative, it is: " + row1 );

        if ( column1 < 0 )
            throw new IllegalArgumentException ( "Parameter column1 should be non negative, it is: " + column1 );

        if ( row2 < 0 )
            throw new IllegalArgumentException ( "Parameter row2 should be non negative, it is: " + row2 );

        if ( column2 < 0 )
            throw new IllegalArgumentException ( "Parameter column2 should be non negative, it is: " + column2 );

        if ( row1 == row2 - 1 && column1 == column2 - 1 )
            return true;

        if ( row1 == row2 + 1 && column1 == column2 + 1 )
            return true;

        if ( row1 == row2 + 1 && column1 == column2 - 1 )
            return true;

        if ( row1 == row2 - 1 && column1 == column2 + 1 )
            return true;

        if ( row1 == row2 - 2 && column1 == column2 )
            return true;

        if ( row1 == row2 + 2 && column1 == column2 )
            return true;

        return false;
    }

    /*public boolean canStepTo ( TileModel tile ) {
        if ( tile == null )
            throw new IllegalArgumentException ( "Parameter tile is null" );

        return canStepTo ( tile.getRow (), tile.getColumn () );
    }*/

    /*public boolean canStepTo ( int row, int column ) {
        if ( row < 0 )
            throw new IllegalArgumentException ( "Parameter row should be non negative, it is set to: " + row );

        if ( column < 0 )
            throw new IllegalArgumentException ( "Parameter column should be non negative, it is set to: " + column );

        for ( TileModel tile : connections.keySet () ) {
            if ( tile.getRow () == row && tile.getColumn () == column ) {
                if ( connections.get ( tile ).isEnabled () )
                    return true;
                else
                    return false;
            }
        }

        return false;
    }*/

    /*public void setInPath ( boolean inPath ) {
        if ( this.inPath == inPath )
            return;

        this.inPath = inPath;

        if ( inPath )
            color = Color.BLUE;
        else
            color = baseColor;

        HexEventRegister.getInstance ().triggerEvent ( new RedrawTile ( "tileRequest", this ) );
    }*/

    /*public boolean isInPath () {
        return inPath;
    }

    public void isClicked() {
        
    }
    
    public void setIsReachable ( boolean reachable ) {
        if ( this.reachable == reachable )
            return;

        this.reachable = reachable;

        if ( ! reachable )
            color = Color.RED;
        else
            color = baseColor;

        //HexEventRegister.getInstance ().triggerEvent ( new RedrawTile ( "tileRequest", this ) );
    }

    public boolean isReachable () {
        return reachable;
    }

    public void switchReachableState () {
        setIsReachable ( !reachable );
    }*/

    public boolean equals ( TileModel tile ) {
        if ( tile == null )
            return false;

        if ( this == tile )
            return true;

        if ( tile.getColumn () == column && tile.getRow () == row )
            return true;

        return false;
    }
}
