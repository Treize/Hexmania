package org.yary.realhexgen.model.map.pathfind;

import java.util.Date;
import java.util.LinkedList;
import org.yary.realhexgen.model.map.TileModel;

/**
 *
 * @author Yary Ribero
 */
public abstract class PathFindAlgorithm {

    private long lastPerformance = 0;

    public abstract LinkedList < TileModel > findPath ( TileModel tile1, TileModel tile2, int maxDepth );

    public long benchmark ( TileModel tile1, TileModel tile2, int maxDepth ) {
        long startTime = new Date ().getTime ();

        findPath ( tile1, tile2, maxDepth );

        lastPerformance = new Date ().getTime () - startTime;
        
        return lastPerformance;
    }

    public LinkedList < TileModel > findPathAndMeasureTime ( TileModel tile1, TileModel tile2, int maxDepth ) {
        long startTime = new Date ().getTime ();

        LinkedList < TileModel > path = findPath ( tile1, tile2, maxDepth );

        lastPerformance = new Date ().getTime () - startTime;

        return path;
    }

    public long getLastPerformance () {
        return lastPerformance;
    }
}
