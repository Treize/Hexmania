package org.yary.realhexgen.controller.events;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Yary Ribero
 */
public class HexEventRegisterTest {
    
    public HexEventRegisterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class HexEventRegister.
     */
    @Test
    public void testGetInstance () {
        System.out.println ( "getInstance" );
        HexEventRegister result = HexEventRegister.getInstance ();
        assertNotNull ( result );
    }

    /**
     * Test of getNewHandlerID method, of class HexEventRegister.
     */
    @Test
    public void testGetNewHandlerID() {
        System.out.println("getNewHandlerID");
        HexEventRegister instance = HexEventRegister.getInstance ();
        long expResult = 0L;
        long result = instance.getNewHandlerID();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNewEventID method, of class HexEventRegister.
     */
    @Test
    public void testGetNewEventID() {
        System.out.println("getNewEventID");
        HexEventRegister instance = HexEventRegister.getInstance ();
        long expResult = 0L;
        long result = instance.getNewEventID();
        assertEquals(expResult, result);
    }

    /**
     * Test of registerHandlerForEvent method, of class HexEventRegister.
     */
    @Test
    public void testRegisterHandlerForEvent_3args() {
        System.out.println("registerHandlerForEvent");
        Class type = HexEvent.class;
        
        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        String[] names = null;
        HexEventRegister instance = HexEventRegister.getInstance ();

        instance.registerHandlerForEvent(type, handler, names);
        
        instance.triggerEvent ( new HexEvent ( "one" ) );

        assertEquals( v.value, "changed" );

        final Value v2 = new Value ();
        v2.value = "fix";

        HexEventHandler handler2 = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v2.value = "changed";
            }
        };

        instance.registerHandlerForEvent(type, handler2, new String [] { "two" } );

        instance.triggerEvent ( new HexEvent ( "one" ) );

        assertEquals( v2.value, "fix" );

        instance.triggerEvent ( new HexEvent ( "two" ) );

        assertEquals( v2.value, "changed" );
    }

    /**
     * Test of removeHandlerForEvent method, of class HexEventRegister.
     */
    @Test
    public void testRemoveHandlerForEvent() {
        Class type = HexEvent.class;
        
        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        String[] names = null;
        HexEventRegister instance = HexEventRegister.getInstance ();

        instance.registerHandlerForEvent(type, handler, names);
        
        instance.triggerEvent ( new HexEvent ( "one" ) );

        assertEquals( v.value, "changed" );

        v.value = "fix";

        instance.removeHandlerForEvent( type, handler.getID () );

        instance.triggerEvent ( new HexEvent ( "one" ) );

        assertEquals( v.value, "fix" );
    }

    private class Value {
        public String value;
    }
}
