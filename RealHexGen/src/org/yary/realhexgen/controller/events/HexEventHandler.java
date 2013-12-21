package org.yary.realhexgen.controller.events;

import java.util.HashMap;

/**
 * An abstract class for handling an event. This method is called by HexEventRegister when someone triggers the event
 * this handler was associated to.
 * Implementation example:
 * <pre>
 *  new HexEventHandler(){
 *      public void handle( HexEvent event  ) {
 *          if ( event == null ){
 *              Debug.message( "LoginSuccessful event handler in mainEntryPoint received a null Event" );
 *              return;
 *          }
 *          LoginSuccessful loginSuccessful = ( LoginSuccessful ) event;
 *          HexSettings.setIsDebugMode( loginSuccessful.getDebugMode() );
 *          HexUser.setUsername( loginSuccessful.getUsername() );
 *          getUserService().getDUMs( loadDUMs );
 *          getUserService().getModels( loadMODs );
 *      }
 *  }
 * </pre>
 * @author Yary Ribero
 */
public abstract class HexEventHandler < T extends HexEvent > {

    private HashMap < String, String [] > nameMask = new HashMap < String, String [] > ();
    private long handlerID;
    private boolean enabled = true;
    private boolean removeOnNextUse = false;
    private boolean disableOnNextUse = false;
    private boolean enableOnNextUse = false;

    public HexEventHandler () {
        handlerID = HexEventRegister.getInstance ().getNewHandlerID ();
    }

    public HexEventHandler ( boolean consumedOnFirstUse ) {
        this ();
        this.removeOnNextUse = consumedOnFirstUse;
    }

    public abstract void handle ( T event ) throws Exception;

    /**
     * Before calling the method handler checks whether the event passed is null.
     * The most probable cause of a null event passed is the casting between two incompatibles types.
     * @param event The event to be handled.
     */
    public void execute ( T event ) throws Exception {
        if ( event == null )
            throw new IllegalArgumentException ( "The event received is null" ) ;

        String [] names = nameMask.get ( event.getType () );

        /**
         * If names is null it means that the handler should react to all the events of the specified type
         */
        if ( names == null )
            handle ( event );
        else //else it will react only to those events of that type which have a specific name
            for ( String name : names )
                if ( name != null && name.equals ( event.getName () ) )
                    handle ( event );
    }

    public Long getID () {
        return handlerID;
    }

    public void setEventNameMask ( Class type, String ... names ) {
        if ( type == null )
            throw new IllegalArgumentException ( "Parameter type is null" );

        nameMask.put ( type.getName (), names );
    }

    public boolean isEnabled () {
        return enabled;
    }

    public void setEnabled ( boolean enabled ) {
        this.enabled = enabled;
    }

    public boolean isRemovedOnNextUse () {
        return removeOnNextUse;
    }

    public void removeOnNextUse () {
        this.removeOnNextUse = true;
    }

    public void registerForEvent ( Class event ) {
        registerForEvent ( event, ( String [] ) null );
    }

    public void registerForEvent ( Class event, String ... names ) {
        HexEventRegister.getInstance ().registerHandlerForEvent ( event, this, names );
    }

    public void unregisterForEvent ( Class event ) {
        HexEventRegister.getInstance ().removeHandlerForEvent ( event, handlerID );
    }

    public boolean isDisabledOnNextUse () {
        return disableOnNextUse;
    }

    public void setDisabledOnNextUse ( boolean disableOnNextUse ) {
        this.disableOnNextUse = disableOnNextUse;
    }

    public boolean isEnabledOnNextUse () {
        return enableOnNextUse;
    }

    public void setEnabledOnNextUse ( boolean enableOnNextUse ) {
        this.enableOnNextUse = enableOnNextUse;
    }
}
