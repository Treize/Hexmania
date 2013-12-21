package org.yary.realhexgen.model;

import javax.swing.JPanel;
import org.yary.realhexgen.view.MapView;

/**
 *
 * @author Yary Ribero
 */
public class WindowModel {

    private int offsetX = 0;
    private int offsetY = 0;

    private JPanel drawSpace;
    private MapView mapView;

    public WindowModel ( MapView mapView ) {
        if ( mapView == null )
            throw new IllegalArgumentException ( "Parameter mapView is null" );

        this.mapView = mapView;
    }

    public int getOffsetX () {
        return offsetX;
    }

    public void setOffsetX ( int offsetX ) {
        this.offsetX = offsetX;
        boundOffsetX ();
    }

    public void incrementOffsetX ( int difference ) {
        offsetX += difference;
        boundOffsetX ();
    }

    public int getOffsetY () {
        return offsetY;
    }

    public void setOffsetY ( int offsetY ) {
        this.offsetY = offsetY;
        boundOffsetY ();
    }

    public void incrementOffsetY ( int difference ) {
        offsetY += difference;
        boundOffsetY ();
    }

    public void setDrawSpace ( JPanel drawSpace ) {
        if ( drawSpace == null )
            throw new IllegalArgumentException ( "Parameter drawSpace is null" );

        this.drawSpace = drawSpace;
    }

    public void setMapView (MapView mapView ) {
        if ( mapView == null )
            throw new IllegalArgumentException ( "Parameter mapView is null" );

        this.mapView = mapView;
    }

    private void boundOffsetX () {
        offsetX = Math.max ( 0, Math.min ( offsetX, Math.max ( 0, mapView.getMap().getWidth ( null ) - drawSpace.getWidth () ) ) );
    }

    private void boundOffsetY () {
        offsetY = Math.max ( 0, Math.min ( offsetY, Math.max ( 0, mapView.getMap().getHeight ( null ) - drawSpace.getHeight () ) ) );
    }
}
