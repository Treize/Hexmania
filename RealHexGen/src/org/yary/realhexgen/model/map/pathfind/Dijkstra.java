package org.yary.realhexgen.model.map.pathfind;

import java.util.*;
import org.yary.realhexgen.model.map.MapModel;
import org.yary.realhexgen.model.map.TileConnection;
import org.yary.realhexgen.model.map.TileModel;

/**
 *
 * @author Lars Vogel (http://www.vogella.com/articles/JavaAlgorithmsDijkstra/article.html), adapted to the project by Yary Ribero
 */
public class Dijkstra extends PathFindAlgorithm {

    private final List < TileModel > nodes;
    private final List < TileConnection > edges;
    private Set < TileModel > settledNodes;
    private Set < TileModel > unSettledNodes;
    private Map < TileModel, TileModel > predecessors;
    private Map < TileModel, Integer > distance;

    public Dijkstra ( MapModel graph ) {
        // Create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList < TileModel > ( graph.getTiles() );
        this.edges = new ArrayList < TileConnection > ( graph.getConnections () );

        System.err.println ( "Nodes: " + nodes.size () + ", Connections: " + edges.size () + "." );
    }

    @Override
    public LinkedList < TileModel > findPath ( TileModel tile1, TileModel tile2, int maxDepth ) {
        //execute ( tile1 );
        return getPath ( tile2 );
    }

   /* public void execute ( TileModel  source ) {
        settledNodes = new HashSet < TileModel > ();
        unSettledNodes = new HashSet < TileModel > ();
        distance = new HashMap < TileModel , Integer > ();
        predecessors = new HashMap < TileModel ,  TileModel > ();
        distance.put ( source, 0 );
        unSettledNodes.add ( source );
        while ( unSettledNodes.size () > 0 ) {
            TileModel  node = getMinimum ( unSettledNodes );
            settledNodes.add ( node );
            unSettledNodes.remove ( node );
            findMinimalDistances ( node );
        }
    }*/
/*
    private void findMinimalDistances( TileModel  node) {
        List< TileModel > adjacentNodes = getNeighbors ( node );
        for ( TileModel  target : adjacentNodes) {
            if ( getShortestDistance ( target ) > getShortestDistance ( node ) + getDistance ( node, target ) ) {
                distance.put ( target, getShortestDistance ( node ) + getDistance ( node, target ) );
                predecessors.put ( target, node );
                unSettledNodes.add ( target );
            }
        }
    }*/
/*
    private int getDistance ( TileModel  node, TileModel  target ) {
        for ( TileConnection edge : edges) {
            if ( edge.getSource ().equals ( node ) && edge.getDestination ().equals ( target ) ) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List < TileModel > getNeighbors ( TileModel node ) {
        List < TileModel > neighbors = new ArrayList< TileModel > ();
        for (TileConnection edge : edges) {
            if ( edge.getSource ().isReachable() && edge.getSource ().equals ( node ) && ! isSettled( edge.getDestination () ) ) {
                neighbors.add ( edge.getDestination () );
            }
        }
        return neighbors;
    }

    private TileModel getMinimum ( Set < TileModel > vertexes ) {
        TileModel minimum = null;
        for ( TileModel  vertex : vertexes ) {
            if ( minimum == null ) {
                minimum = vertex;
            } else {
                if ( getShortestDistance ( vertex ) < getShortestDistance ( minimum ) ) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled ( TileModel  vertex ) {
        return settledNodes.contains ( vertex );
    }

    private int getShortestDistance( TileModel  destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }*/

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
    public LinkedList< TileModel > getPath ( TileModel  target ) {
        LinkedList< TileModel > path = new LinkedList < TileModel >();
        TileModel  step = target;
        // Check if a path exists
        if ( predecessors.get ( step ) == null ) {
            return null;
        }
        path.add ( step );
        while ( predecessors.get ( step ) != null ) {
            step = predecessors.get  (step );
            path.add ( step );
        }
        // Put it into the correct order
        Collections.reverse ( path );
        return path;
    }
}
