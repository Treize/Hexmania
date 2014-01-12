package org.yary.realhexgen.controller.events.tile;

import org.yary.realhexgen.controller.events.HexEvent;
import org.yary.realhexgen.model.map.TileModel;

/**
 *
 * @author Yary Ribero
 */
public class RedrawTile extends HexEvent {

    private TileModel tile;

    public RedrawTile ( String name, TileModel tile ) {
        super ( name );

        if ( tile == null )
            throw new IllegalArgumentException ( "Parameter tile is null" );

        this.tile = tile;
    }

    public TileModel getTile () {
        return tile;
    }
}
