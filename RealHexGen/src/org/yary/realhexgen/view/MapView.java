package org.yary.realhexgen.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.yary.realhexgen.controller.events.HexEventHandler;
import org.yary.realhexgen.controller.events.HexEventRegister;
import org.yary.realhexgen.controller.events.configuration.ConfigurationChanged;
import org.yary.realhexgen.controller.events.map.MapModelReady;
import org.yary.realhexgen.controller.events.map.RedrawMap;
import org.yary.realhexgen.controller.events.tile.RedrawTile;
import org.yary.realhexgen.model.ConfigurationData;
import org.yary.realhexgen.model.map.MapModel;
import org.yary.realhexgen.model.map.TileModel;

/**
 *
 * @author Yary Ribero
 */
public class MapView {

    private MapModel mapModel;
    private BufferedImage map;

    private HexEventHandler redrawTileEventHandler = new HexEventHandler < RedrawTile > () {
        @Override
        public void handle ( RedrawTile event ) throws Exception {
            TileModel tile = event.getTile ();

            if ( tile == null )
                throw new Exception ( "The tile bound to the event is null" );

            redrawCell ( tile.getRow (), tile.getColumn () );
        }
    };

    private HexEventHandler redrawMapEventHandler = new HexEventHandler < RedrawMap > () {
        @Override
        public void handle ( RedrawMap event ) throws Exception {
            redrawMap ();
        }
    };

    private HexEventHandler modelReadyHandler = new HexEventHandler < MapModelReady > () {
        @Override
        public void handle ( MapModelReady event ) throws Exception {
            
            map = new BufferedImage (
                ConfigurationData.getInstance () .getEdge () / 2 + ( 3 * ConfigurationData.getInstance () .getEdge () / 2 ) * ConfigurationData.getInstance () .getColumns (),
                ConfigurationData.getInstance () .getHeight () + ( ConfigurationData.getInstance () .getHeight () / 2 ) * ( ConfigurationData.getInstance () .getRows () - 1 ),
                BufferedImage.TYPE_INT_ARGB
            );

            redrawMap ();
        }
    };

    public MapView ( MapModel mapModel ) throws Exception {
        if ( mapModel == null )
            throw new IllegalArgumentException ( "Parameter map model is null" );

        this.mapModel = mapModel;

        final HexEventRegister instance = HexEventRegister.getInstance ();

        instance.registerHandlerForEvent (
            RedrawTile.class,
            redrawTileEventHandler,
            "tileRequest"
        );

        instance.registerHandlerForEvent (
            RedrawMap.class,
            redrawMapEventHandler,
            "modelRequest"
        );

        instance.registerHandlerForEvent (
            MapModelReady.class,
            modelReadyHandler,
            ( String [] ) null
        );
    }

    public void redrawMap () throws Exception {

        if ( map == null )
            return;

        File img = new File("src/org/yary/realhexgen/images/mapa333.png");
        BufferedImage in = ImageIO.read(img);
        
        Graphics g = map.getGraphics();

        //g.setColor ( Color.yellow );
        
        g.fillRect ( 0, 0, map.getWidth (), map.getHeight () );
        g.drawImage(in, 0, 0, null);
        for ( int row = 0; row < ConfigurationData.getInstance () .getRows (); row++ ) {
            for ( int column = 0; column < ConfigurationData.getInstance () .getColumns (); column++ ) {
                if ( row % 2 == 0 && column % 2 == 1 )
                    continue;
                if ( row % 2 == 1 && column % 2 == 0 )
                    continue;

                redrawCell ( row, column, g );
            }
        }

        HexEventRegister.getInstance ().triggerEvent ( new RedrawMap ( "mapRequest" ) );
    }

    /**
     * Redraw one only cell. Since it calls getGraphics() every times it is called, 
     * this method should be used only in case there is need to redraw one only cell.
     * 
     * @param row
     * @param column
     * @throws Exception In case row and column point to a cell not available.
     */
    public void redrawCell ( int row, int column ) throws Exception {
        Graphics g = map.getGraphics();

        TileModel tile = mapModel.getTile ( row, column );

        g.setColor ( tile.getColor () );

        int startX = ( 3 * ConfigurationData.getInstance () .getEdge () / 2 ) * column;
        int startY = ( ConfigurationData.getInstance () .getHeight () / 2 ) * row;

        for ( int i = 0; i < ConfigurationData.getInstance () .getHeight (); i++ ) {
            g.drawLine (
                startX + mapModel.slopeGap ( ( startY + i ) - row * ConfigurationData.getInstance () .getHeight () / 2 ) + 1,
                startY + i,
                startX + 2 * ConfigurationData.getInstance () .getEdge () - 1 - mapModel.slopeGap ( ( startY + i ) - row * ConfigurationData.getInstance () .getHeight () / 2 ),
                startY + i
            );
        }

        HexEventRegister.getInstance ().triggerEvent ( new RedrawTile ( "mapRequest", tile ) );
    }

    /**
     * Redraw one only cell. Since this method uses the graphics passed from the caller, 
     * it can be used in a cells redraw loop.
     * 
     * @param row
     * @param column
     * @param g
     * @throws Exception In case row and column point to a cell not available.
     */
    public void redrawCell ( int row, int column, Graphics g ) throws Exception {
        g.setColor ( mapModel.getTile ( row, column ).getColor () );

        int startX = ( 3 * ConfigurationData.getInstance () .getEdge () / 2 ) * column;
        int startY = ( ConfigurationData.getInstance () .getHeight () / 2 ) * row;

        for ( int i = 0; i < ConfigurationData.getInstance () .getHeight (); i++ ) {
            g.drawLine (
                startX + mapModel.slopeGap ( ( startY + i ) - row * ConfigurationData.getInstance () .getHeight () / 2 ) + 1,
                startY + i,
                startX + 2 * ConfigurationData.getInstance () .getEdge () - 1 - mapModel.slopeGap ( ( startY + i ) - row * ConfigurationData.getInstance () .getHeight () / 2 ),
                startY + i
            );
        }
    }

    public void clean () {
        redrawTileEventHandler.unregisterForEvent ( RedrawTile.class );
        redrawMapEventHandler.unregisterForEvent ( RedrawMap.class );
    }

    public BufferedImage getMap () {
        return map;
    }
}
