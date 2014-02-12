package org.yary.realhexgen.model;

import org.yary.realhexgen.controller.events.HexEventRegister;
import org.yary.realhexgen.controller.events.configuration.ConfigurationChanged;

/**
 *
 * @author Yary Ribero
 */
public class ConfigurationData {

    private static ConfigurationData instance;

    private int rows = 18;//10
    private int columns = 22;//10
    private int edge = 40;//40
    private int height = 20;//70

    public final int MAX_ROWS = 10000;
    public final int MAX_COLUMNS = 10000;
    public final int MAX_EDGE = 200;

    public int getRows () {
        return rows;
    }

    public void setRows ( int rows ) throws IllegalArgumentException {
        if ( rows < 0 )
            throw new IllegalArgumentException ( "Parameter rows must be positive it was set to " + rows );

        if ( rows > MAX_ROWS )
            throw new IllegalArgumentException ( "Parameter rows must be less than " + MAX_ROWS + " it was set to " + rows );

        this.rows = rows;

        HexEventRegister.getInstance ().triggerEvent ( new ConfigurationChanged ( "rows" ) );
    }

    public int getColumns () {
        return columns;
    }

    public void setColumns ( int columns ) throws IllegalArgumentException {
        if ( columns < 0 )
            throw new IllegalArgumentException ( "Parameter columns must be positive it was set to " + columns );

        if ( columns > MAX_COLUMNS )
            throw new IllegalArgumentException ( "Parameter columns must be less than " + MAX_COLUMNS + " it was set to " + columns );

        this.columns = columns;

        HexEventRegister.getInstance ().triggerEvent ( new ConfigurationChanged ( "columns" ) );
    }

    public int getEdge () {
        return edge;
    }

    public void setEdge ( int edge ) {
        if ( edge < 8 )
            throw new IllegalArgumentException ( "Parameter edge must be not less than 8 it was set to " + edge );

        if ( edge > MAX_EDGE )
            throw new IllegalArgumentException ( "Parameter edge must be less than " + MAX_EDGE + " it was set to " + edge );

        if ( edge % 8 != 0 )
            edge = ( int ) ( ( Math.round ( ( double ) edge ) / 8 ) * 8 );

        this.edge = edge;

        double preciseHeight = edge * Math.sqrt ( 3 );

        height = ( int ) Math.round ( preciseHeight );

        if ( height % 2 == 1 ) {
            if ( height > preciseHeight )
                height--;
            else
                height++;
        }

        HexEventRegister.getInstance ().triggerEvent ( new ConfigurationChanged ( "edge" ) );
    }

    public int getHeight () {
        return height;
    }

    public static ConfigurationData getInstance () {
        if ( instance == null )
            instance = new ConfigurationData ();

        return instance;
    }

    public void ready () {
        HexEventRegister.getInstance ().triggerEvent ( new ConfigurationChanged ( "new" ) );
    }
}
