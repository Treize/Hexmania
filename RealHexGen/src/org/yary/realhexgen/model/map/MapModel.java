package org.yary.realhexgen.model.map;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import org.yary.realhexgen.controller.events.HexEventHandler;
import org.yary.realhexgen.controller.events.HexEventRegister;
import org.yary.realhexgen.controller.events.configuration.ConfigurationChanged;
import org.yary.realhexgen.controller.events.map.MapModelReady;
import org.yary.realhexgen.controller.events.tile.TilesDistance;
import org.yary.realhexgen.model.ConfigurationData;
import org.yary.realhexgen.model.map.pathfind.Dijkstra;
import org.yary.realhexgen.model.map.pathfind.PathFindAlgorithm;

/**
 *
 * @author Yary Ribero
 */
public class MapModel {

    //private HashMap < String, PathFindAlgorithm > algorithms = new HashMap < String, PathFindAlgorithm > ();

    private TileModel [][] tiles;
    private TileModel selectedTile;
    
    // Atrapa. Przerobić na klasę (Base): // HAHAHAHAHA THERE IS NO TIME FOR A NICE CODE, DUUUUUUUDE!
    Player player1;
    Player player2;
    public Color blueTransp = new Color (0, 0, 1, 0.4f); // gracz1
    public Color redTransp = new Color (1, 0, 0, 0.4f); //gracz2
    BaseModel p1Base;
    BaseModel p2Base;
    public boolean p1BaseIsSet;
    public boolean p2BaseIsSet;
    
    private LinkedList < TileModel > tilesList;
    private LinkedList < TileConnection > connections = new LinkedList < TileConnection > ();

    //private LinkedList < TileModel > path;

    public boolean basesAreSet() {
        return (p1BaseIsSet && p2BaseIsSet) ? true:false;
    }
    
    public MapModel () {
        final HexEventRegister instance = HexEventRegister.getInstance ();
        player1 = new Player(1);
        player2 = new Player(2);
        player1.turn=true; // gracz1 zaczyna
        p1BaseIsSet=false;
        p2BaseIsSet=false;
        
        instance.registerHandlerForEvent (
            ConfigurationChanged.class,
            new HexEventHandler < ConfigurationChanged > () {
                @Override
                public void handle ( ConfigurationChanged event ) throws Exception {
                    ConfigurationData config = ConfigurationData.getInstance ();

                    tiles = new TileModel [ config.getRows () ] [ config.getColumns () ];
                    connections.clear ();
                    if ( tilesList != null )
                        tilesList = null;
                    int tileID = 0; // TU ID
                    for ( int row = 0; row < config.getRows (); row++ ) {
                        tiles [ row ] = new TileModel [ config.getColumns () ];

                        for ( int column = 0; column < config.getColumns (); column++ ) {
                            if ( row % 2 == 0 && column % 2 == 1 )
                                continue;
                            if ( row % 2 == 1 && column % 2 == 0 )
                                continue;

                            tiles [ row ] [ column ] = new TileModel ( row, column, tileID ); // TU ID
                            tileID++; // TU TEŻ ID
                        }
                    }

                    for ( int row = 0; row < config.getRows (); row++ ) {
                        for ( int column = 0; column < config.getColumns (); column++ ) {
                            if ( row % 2 == 0 && column % 2 == 1 )
                                continue;
                            if ( row % 2 == 1 && column % 2 == 0 )
                                continue;

                            if ( row + 1 < config.getRows () && column - 1 >= 0 ) {
                                TileModel neighbour = getTile ( row + 1, column - 1 );
                                TileConnection connection = new TileConnection ( tiles [ row ] [ column ], neighbour );
                                tiles [ row ] [ column ].addConnection ( neighbour, connection );
                                connections.add ( connection );
                                connections.add ( new TileConnection ( neighbour, tiles [ row ] [ column ] ) );
                                neighbour.addConnection ( tiles [ row ] [ column ], connection );
                            }

                            if ( row + 2 < config.getRows () ) {
                                TileModel neighbour = getTile ( row + 2, column );
                                TileConnection connection = new TileConnection ( tiles [ row ] [ column ], neighbour );
                                tiles [ row ] [ column ].addConnection ( neighbour, connection );
                                connections.add ( connection );
                                connections.add ( new TileConnection ( neighbour, tiles [ row ] [ column ] ) );
                                neighbour.addConnection ( tiles [ row ] [ column ], connection);
                            }

                            if ( row + 1 < config.getRows () && column + 1 < config.getColumns () ) {
                                TileModel neighbour = getTile ( row + 1, column + 1 );
                                TileConnection connection = new TileConnection ( tiles [ row ] [ column ], neighbour );
                                connections.add ( connection );
                                connections.add ( new TileConnection ( neighbour, tiles [ row ] [ column ] ) );
                                tiles [ row ] [ column ].addConnection ( neighbour, connection );
                                neighbour.addConnection ( tiles [ row ] [ column ], connection );
                            }
                        }
                    }

                    selectedTile = null;

                    //fitDijkstra ();

                    instance.triggerEvent ( new MapModelReady () );
                }
            },
            "new"
        );
    }

    // --------- DEPLOY --------- // Emi
    
    public void deploy(int gamerId, int unitId) {
        
    }
       
    // ----- end of DEPLOY ------
    
    
    
    /*private void fitDijkstra () {
        algorithms.put ( "Dijkstra", new Dijkstra ( this ) );
    }*/

    private int slopeFunction ( int arg ) {

        if ( arg > ConfigurationData.getInstance ().getHeight () / 2 - 1 )
            throw new IllegalArgumentException ( "The argument, " + arg + ", is greater than height / 2 - 1 = " + ( ConfigurationData.getInstance ().getHeight () / 2 - 1 ) );
        int f [] = new int [] { 0,1,1,2,2,3,3 };

        /**
         * The order of the operands in Math.floor is important. arg / 7 must return an integer which represents
         * the times the argument can be divided by seven. Do not move the 4 at the beginning of the expression 
         * or the result will change.
         */
        return f [ arg % 7 ] + ( int ) ( Math.floor ( arg / 7 * 4 ) );
    }

    /**
     * Given a certain hexagon, this method helps drawing the slopes by providing the gap between 
     * the enveloping rectangle and the exact point (given a vertical position) from that where to to 
     * draw the first pixel of the hexagon edge.
     * @param verticalPosition The distance from the top of the hexagon
     * @return The gap to leave on the x axis before drawing the first pixel of the line that the hexagon 
     * should have at the given vertical position. The same gap should be left from the end of the line to 
     * the edge of the envelopping rectangle.
     * @throws IllegalArgumentException The vertical position has bounds [0,hexagon's height).
     */
    public int slopeGap ( int verticalPosition ) {

        if ( verticalPosition < 0 || verticalPosition >= ConfigurationData.getInstance ().getHeight () )
            throw new IllegalArgumentException ( "The vertical position is out of bounds [0," + ConfigurationData.getInstance ().getHeight () + "): " + verticalPosition );

        int slopeValue;

        if ( verticalPosition < ConfigurationData.getInstance ().getHeight () / 2 )
            slopeValue = ConfigurationData.getInstance ().getEdge () / 2 - 1 - slopeFunction ( verticalPosition );
        else
            slopeValue = slopeFunction ( verticalPosition - ConfigurationData.getInstance ().getHeight () / 2 );

        return slopeValue;
    }

    public TileModel getTile ( int row, int column ) {
        if ( row < 0 || row >= ConfigurationData.getInstance ().getRows () )
            throw new IllegalArgumentException ( "Parameter row is out of bound [0," + ( ConfigurationData.getInstance ().getRows () - 1 ) + "]: " + row );

        if ( column < 0 || column >= ConfigurationData.getInstance ().getColumns () )
            throw new IllegalArgumentException ( "Parameter column is out of bound [0," + ( ConfigurationData.getInstance ().getColumns () - 1 ) + "]: " + column );

        if ( ( row % 2 == 0 && column % 2 == 1 ) || ( row % 2 == 1 && column % 2 == 0 ) )
            throw new IllegalArgumentException ( "The tile " + row + "," + column + " is not available in this hexagonal map." );

        return tiles [ row ] [ column ];
    }

    public TileModel getSelectedTile () {
        return selectedTile;
    }

    public void switchTurns() { // ustawia odpowiednio pola "turn" w obiektach BaseModel (dla każdej bazy)
        player1.switchTurn();
        player2.switchTurn();
    }
    
    public void setBases() {
        //if(!basesAreSet()){
            if(player1.turn) {
                selectedTile.setSelected ( true, Color.BLUE ); // gracz1 - niebieska baza
                System.out.println("stawiam baze niebieska");
                p1Base= new BaseModel(selectedTile.getRow(),selectedTile.getColumn(),selectedTile.getId(),player1.getId());
                p1Base.setNeighbors();
                p1BaseIsSet=true;
            } else if(player2.turn) {
                System.out.println("stawiam baze czerwona");
                selectedTile.setSelected ( true, Color.RED ); // gracz2 - czerwona baza
                p2Base= new BaseModel(selectedTile.getRow(),selectedTile.getColumn(),selectedTile.getId(),player2.getId());
                p2Base.setNeighbors();
                p2BaseIsSet=true;
            }
            selectedTile.setOccupied(true);
            switchTurns();
        //}
    }
    
    public void switchPlayers() { // raz jeden, raz drugi
        //setBases(); // to wywołać przed wywołaniem funkcji switchPlayer
        if(player1.turn) {
            selectedTile.setSelected ( true, blueTransp ); // ustawiam kolor (niebieski)
            System.out.println("blue");
            //switchTurns();
        } else if(player2.turn) {
            selectedTile.setSelected(true, redTransp);
            System.out.println("red");
            //switchTurns();
        }
        selectedTile.setOccupied(true);
        switchTurns();
    }
    
    public void setSelectedTile ( int row, int column ) throws Exception {
        if ( row < 0 || row >= ConfigurationData.getInstance ().getRows () )
            throw new IllegalArgumentException ( "Parameter row is out of bound [0," + ( ConfigurationData.getInstance ().getRows () - 1 ) + "]: " + row );

        if ( column < 0 || column >= ConfigurationData.getInstance ().getColumns () )
            throw new IllegalArgumentException ( "Parameter column is out of bound [0," + ( ConfigurationData.getInstance ().getColumns () - 1 ) + "]: " + column );

        if ( ( row % 2 == 0 && column % 2 == 1 ) || ( row % 2 == 1 && column % 2 == 0 ) )
            throw new IllegalArgumentException ( "The tile " + row + "," + column + " is not available in this hexagonal map." );

        if ( selectedTile != null ) {
            if ( selectedTile.getRow () == row && selectedTile.getColumn () == column ) {
                //selectedTile.setSelected(false, Color.LIGHT_GRAY);
                return; //selected the same already selected.
            }

            /*HexEventRegister.getInstance ().triggerEvent ( new TilesDistance ( TileModel.distance ( selectedTile, tiles [ row ] [column ] ) ) );

            if ( path != null )
                for ( TileModel tile : path )
                    tile.setInPath ( false );

            path = findPath ( selectedTile, tiles [ row ] [column ] );

            for ( TileModel tile : path )
                tile.setInPath ( true );

            selectedTile.setSelected ( false );*/
        }

        selectedTile = tiles [ row ] [column ];
        if(selectedTile.isBase) { // tylko do testu
            System.out.println("TO JEST BAZA "+selectedTile.getColor());
        }

        if ( selectedTile == null )
            throw new Exception ( "Tile " + row + "," + column + " is unexpectedly null" );

        if (!selectedTile.isOccupied) {
            if(!basesAreSet()) {
                setBases();
            } else {
                switchPlayers();
            }
        }
        //selectedTile.setSelected ( true );
    }

    public void setSelectedTile ( TileModel tile ) throws Exception {
        if ( tile == null )
            throw new IllegalArgumentException ( "The tile specified as argument for seleciton is null" );

        setSelectedTile ( tile.getRow (), tile.getColumn () );
    }

    /*public LinkedList < TileModel > findPath ( int row1, int column1, int row2, int column2 ) {
        if ( row1 < 0 )
            throw new IllegalArgumentException ( "Parameter row1 should be non negative, it is: " + row1 );

        if ( column1 < 0 )
            throw new IllegalArgumentException ( "Parameter column1 should be non negative, it is: " + column1 );

        if ( row2 < 0 )
            throw new IllegalArgumentException ( "Parameter row2 should be non negative, it is: " + row2 );

        if ( column2 < 0 )
            throw new IllegalArgumentException ( "Parameter column2 should be non negative, it is: " + column2 );

        return findPath ( getTile ( row1, column1 ), getTile ( row2, column2 ) );
    }*/

    /*public LinkedList < TileModel > findPath ( TileModel tile1, TileModel tile2 ) {
        if ( tile1 == null )
            throw new IllegalArgumentException ( "Parameter tile1 is null" );

        if ( tile2 == null )
            throw new IllegalArgumentException ( "Parameter tile2 is null" );

        LinkedList < TileModel > shortestPath = null;
        String algorithmName = null;

        for ( String name : algorithms.keySet () ) {
            LinkedList < TileModel > pathFound = findPath ( tile1, tile2, name, 20 );
            if ( shortestPath == null || shortestPath.size () > pathFound.size () ) {
                algorithmName = name;
                shortestPath = pathFound;
            }
        }

        if ( shortestPath != null )
            System.out.println ( "Solution found with " + algorithmName + "'s algorithm." );

        return shortestPath;
    }*/

    /*public LinkedList < TileModel > findPath ( TileModel tile1, TileModel tile2, String algorithmName, int depth ) {
        if ( tile1 == null )
            throw new IllegalArgumentException ( "Parameter tile1 is null" );

        if ( tile2 == null )
            throw new IllegalArgumentException ( "Parameter tile2 is null" );

        if ( algorithmName == null )
            throw new IllegalArgumentException ( "Parameter algorithmName is null" );

        PathFindAlgorithm algorithm = algorithms.get ( algorithmName );

        if ( algorithm == null )
            throw new IllegalArgumentException ( "Algorithm " + algorithmName + " not implemented" );

        LinkedList < TileModel > newPath = algorithm.findPathAndMeasureTime ( tile1, tile2, depth );

        if ( newPath == null )
            System.out.println ( "No solution found with algorithm " + algorithmName + "." );
        else
            System.out.println ( "One solution found with algorithm " + algorithmName + ": length is " + newPath.size () + " tiles, elapsed time: " + algorithm.getLastPerformance () + "ms." );

        return newPath;
    }*/

    /*public void setTileUnreachable ( int row, int column ) {
        if ( row < 0 || row >= ConfigurationData.getInstance ().getRows () )
            throw new IllegalArgumentException ( "Parameter row is out of bound [0," + ( ConfigurationData.getInstance ().getRows () - 1 ) + "]: " + row );

        if ( column < 0 || column >= ConfigurationData.getInstance ().getColumns () )
            throw new IllegalArgumentException ( "Parameter column is out of bound [0," + ( ConfigurationData.getInstance ().getColumns () - 1 ) + "]: " + column );

        if ( ( row % 2 == 0 && column % 2 == 1 ) || ( row % 2 == 1 && column % 2 == 0 ) )
            throw new IllegalArgumentException ( "The tile " + row + "," + column + " is not available in this hexagonal map." );

        tiles [ row ] [column ].switchReachableState ();
    }*/

    /*public void setTileUnreachable ( TileModel tile ) {
        if ( tile == null )
            throw new IllegalArgumentException ( "The tile specified as argument for seleciton is null" );

        setTileUnreachable ( tile.getRow (), tile.getColumn () );
    }*/

    public LinkedList < TileConnection > getConnections () {
        return connections;
    }

    public LinkedList < TileModel > getTiles () {
        if ( tilesList == null ) {
            tilesList = new LinkedList < TileModel > ();
            for ( int i = 0; i < tiles.length; i++ ) {
                if ( tiles [ i ] != null ) {
                    for ( int j = 0; j < tiles [ i ].length; j++ ) {
                        if ( tiles [ i ] [ j ] != null )
                            tilesList.add ( tiles [ i ] [ j ] );
                    }
                }
            }
        }

        return tilesList;
    }

}
