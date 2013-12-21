package org.yary.realhexgen.model.map.pathfind;

import java.util.LinkedList;
import org.yary.realhexgen.model.map.TileModel;

/**
 *
 * @author Yary Ribero
 */
public class Wanderer extends PathFindAlgorithm {

    public static enum Direction { FORWARD, BACKWARD };

    private Direction direction = Direction.FORWARD;

    @Override
    public LinkedList < TileModel> findPath ( TileModel tile1, TileModel tile2, int maxDepth ) {
        if ( tile1 == null )
            throw new IllegalArgumentException ( "Parameter tile1 is null" );

        if ( tile2 == null )
            throw new IllegalArgumentException ( "Parameter tile2 is null" );

        LinkedList < TileModel > initPath = new LinkedList < TileModel > ();
        LinkedList < TileModel > finalPath = null;

        /*switch ( direction ) {
            case FORWARD:
                initPath.add ( tile1 );
                finalPath = recursion ( tile1, tile2, 0, initPath, maxDepth );
                if ( finalPath != null )
                    finalPath.add ( tile2 );
                break;
            case BACKWARD:
                initPath.add ( tile2 );
                finalPath = recursion ( tile2, tile1, 0, initPath, maxDepth );
                if ( finalPath != null )
                    finalPath.add ( tile1 );
                break;
            default:
                break;
        }*/

        return finalPath;
    }

    /*public LinkedList < TileModel> findPath ( TileModel tile1, TileModel tile2, int maxDepth, Direction direction ) {
        if ( tile1 == null )
            throw new IllegalArgumentException ( "Parameter tile1 is null" );

        if ( tile2 == null )
            throw new IllegalArgumentException ( "Parameter tile2 is null" );

        if ( direction == null )
            throw new IllegalArgumentException ( "Parameter direction is null" );

        LinkedList < TileModel > initPath = new LinkedList < TileModel > ();
        LinkedList < TileModel > finalPath = null;

        switch ( direction ) {
            case FORWARD:
                initPath.add ( tile1 );
                finalPath = recursion ( tile1, tile2, 0, initPath, maxDepth );
                if ( finalPath != null )
                    finalPath.add ( tile2 );
                break;
            case BACKWARD:
                initPath.add ( tile2 );
                finalPath = recursion ( tile2, tile1, 0, initPath, maxDepth );
                if ( finalPath != null )
                    finalPath.add ( tile1 );
                break;
            default:
                break;
        }

        return finalPath;
    }

    private LinkedList < TileModel> recursion ( TileModel tile1, TileModel tile2, int depth, LinkedList < TileModel > path, int maxDepth ) {

        if ( path == null )
            throw new IllegalArgumentException ( "Parameter path is null" );

        if ( depth > maxDepth ) {
            return null;
        }

        TileModel [] reachableTiles = new TileModel [ tile1.getReachableNeighbours().size() ];
        reachableTiles = tile1.getReachableNeighbours().toArray ( reachableTiles );

        TileModel swap;

        for ( int i = 0; i < tile1.getReachableNeighbours().size(); i++ ) {
            for ( int j = i; j < tile1.getReachableNeighbours().size(); j++ ) {
                if (
                    TileModel.distance ( reachableTiles [ j ], tile2 ) <
                    TileModel.distance ( reachableTiles [ i ], tile2 )
                ) {
                    swap = reachableTiles [ i ];
                    reachableTiles [ i ] = reachableTiles [ j ];
                    reachableTiles [ j ] = swap;
                }
            }
        }

        for ( TileModel tile : reachableTiles ) {
            if ( path.contains ( tile ) )
                continue;

            if ( tile.canStepTo ( tile2 ) ) {
                path.add ( tile );
                return path;
            }

            path.add ( tile );
            LinkedList < TileModel > newPath = recursion ( tile, tile2, depth + 1, path, maxDepth );

            if ( newPath != null ) {
                return newPath;
            } else
                path.removeLast ();
        }

        return null;
    }*/

    public void setDirection ( Direction direction ) {
        if ( direction == null )
            throw new IllegalArgumentException ( "Parameter direction is null" );

        this.direction = direction;
    }

    public void setForward () {
        setDirection ( Direction.FORWARD );
    }

    public void setBackward () {
        setDirection ( Direction.BACKWARD );
    }

    public Direction getDirection () {
        return direction;
    }
}
