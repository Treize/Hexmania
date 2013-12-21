package org.yary.realhexgen.controller.events.tile;

import org.yary.realhexgen.controller.events.HexEvent;

/**
 *
 * @author Yary Ribero
 */
public class TilesDistance extends HexEvent {

    private double distance;

    public TilesDistance ( double distance ) {
        super ( "tilesDistance" );

        if ( distance < 0 )
            throw new IllegalArgumentException ( "The distance between two tiles cannot be negative" );

        this.distance = distance;
    }

    public double getDistance () {
        return distance;
    }
}
