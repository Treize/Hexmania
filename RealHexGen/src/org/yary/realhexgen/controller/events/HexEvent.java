package org.yary.realhexgen.controller.events;

import java.util.Date;

/**
 *
 * @author Yary Ribero
 */
public class HexEvent {

    private long millis;
    private String name;
    private long eventID;

    public HexEvent ( String name ) {
        if ( name == null )
            throw new IllegalArgumentException ( "Parameter name is null" );

        if ( name.equals ( "" ) )
            throw new IllegalArgumentException ( "Parameter name is an empty string" );

        this.name = name;
        millis = new Date ().getTime ();
        eventID = HexEventRegister.getInstance ().getNewEventID ();
    }

    public String getType () {
        return getClass ().getName ();
    }

    public long getID () {
        return eventID;
    }

    public Date getIssueDate () {
        return new Date ( millis );
    }

    public String getName () {
        return name;
    }
}
