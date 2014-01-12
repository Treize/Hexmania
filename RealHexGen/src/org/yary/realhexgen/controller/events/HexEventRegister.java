package org.yary.realhexgen.controller.events;

import java.util.HashMap;

/**
 *
 * @author Yary Ribero
 */
public class HexEventRegister extends HashMap < String, HashMap < Long, HexEventHandler > > {

    private static HexEventRegister instance;

    private long handlerCounter = 0;
    private long eventCounter = 0;

    public static HexEventRegister getInstance () {
        if ( instance == null )
            instance = new HexEventRegister ();

        return instance;
    }

    public long getNewHandlerID () {
        return handlerCounter ++;
    }

    public long getNewEventID () {
        return eventCounter ++;
    }

    /**
     * Adds an event to the list. The event is known by its type, it is just a declaration for further use,
     * not the instantiation of a new real event.
     * @param type Event Type.
     * @return Either the method generated a new list or the list already exists, This method returns
     * the list of Handlers linked to the specified event.
     */
    private HashMap < Long, HexEventHandler > addEvent ( String type ) {
        if ( type == null )
            throw new IllegalArgumentException ( "Parameter type is null" );

        if ( type.equals( "" ) )
            throw new IllegalArgumentException ( "Parameter type is an empty string" );

        HashMap < Long, HexEventHandler > folder = get ( type );

        if ( folder != null )
            return folder; //event already exists

        folder = new HashMap < Long, HexEventHandler > ();

        put ( type, folder );

        return folder;
    }

    /**
     * Register an handler for an event type. The event handler will handle only those events whose name 
     * is in the names list.
     * @param type The event class.
     * @param handler The handler instance.
     * @param names The handler will react only to the events of the registered type whose name is 
     * in this list. If the list is null the handler will ignore it and will react to any event of 
     * the given type.
     */
    public void registerHandlerForEvent ( Class type, HexEventHandler handler, String ... names ) {
        if ( type == null )
            throw new IllegalArgumentException ( "Parameter type is null" );

        if ( handler == null )
            throw new IllegalArgumentException ( "Parameter handler is null" );

        HashMap < Long, HexEventHandler > folder = addEvent ( type.getName () );

        if ( folder == null )
            throw new IllegalArgumentException ( "The event type " + type.getName () + " cannot be handled" );

        if ( folder.containsKey ( handler.getID () ) ) //already registered
            return;

        handler.setEventNameMask ( type, names );

        folder.put ( handler.getID (), handler );
    }

    /**
     * Register an handler for an event type. The handler will react to all the events with that type.
     * 
     * @param type The event class.
     * @param handler The handler instance.
     */
    public void registerHandlerForEvent ( Class type, HexEventHandler handler ) {
        registerHandlerForEvent ( type, handler, ( String [] ) null );
    }

    public void removeEvent ( Class type ) {
        if ( type == null )
            throw new IllegalArgumentException ( "Parameter type is null" );

        remove ( type.getName () );
    }

    public void removeHandlerForEvent ( Class type, Long handlerID ) {
        if ( type == null )
            throw new IllegalArgumentException ( "Parameter type is null" );

        HashMap < Long, HexEventHandler > folder = get ( type.getName () );

        if ( folder == null )
            throw new IllegalArgumentException ( "Event not handled: " + type.getName () );

        if ( handlerID == null )
            throw new IllegalArgumentException ( "Parameter handlerID is null" );

        folder.remove ( handlerID );
    }

    public void triggerEvent ( HexEvent event ) {
        if ( event == null )
            throw new IllegalArgumentException ( "Parameter event is null" );

        HashMap < Long, HexEventHandler > folder = get ( event.getType () );

        if ( folder == null || folder.values() == null || folder.values().isEmpty() )
            return;

        HexEventHandler eventHandler = null;

        for ( Long handlerID : folder.keySet () ){

            eventHandler = folder.get( handlerID );

            if ( eventHandler == null ) {
                folder.remove ( handlerID );
                continue;
            }

            if ( eventHandler.isEnabled () ) {
                try {
                    eventHandler.execute ( event );
                } catch ( Exception e ) {
                    e.printStackTrace ( System.err );
                }

                if ( eventHandler.isDisabledOnNextUse () )
                    eventHandler.setEnabled ( false );
            } else if ( eventHandler.isEnabledOnNextUse () )
                eventHandler.setEnabled ( true );

            if ( eventHandler.isRemovedOnNextUse () )
                folder.remove ( handlerID );
        }
    }
}
