package org.yary.realhexgen.model.map;

/**
 *
 * @author Yary Ribero
 */
public class TileConnection {

    private TileModel tile1;
    private TileModel tile2;

    public TileConnection ( TileModel tile1, TileModel tile2 ) {
        if ( tile1 == null )
            throw new IllegalArgumentException ( "Parameter tile1 is null" );

        if ( tile2 == null )
            throw new IllegalArgumentException ( "Parameter tile2 is null" );

        this.tile1 = tile1;
        this.tile2 = tile2;
    }

    /*private boolean enabled = true;

    public boolean isEnabled () {
        return enabled && tile1.isReachable () && tile2.isReachable() ;
    }

    public void setEnabled ( boolean enabled ) {
        this.enabled = enabled;
    }

    public TileModel getTile1 () {
        return tile1;
    }

    public TileModel getTile2 () {
        return tile2;
    }

    public TileModel getSource () {
        return tile1;
    }

    public TileModel getDestination () {
        return tile2;
    }

    public int getWeight () {
        return 1;
    }*/
}
