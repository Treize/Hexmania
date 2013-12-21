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
public class HexEventHandlerTest {
    
    public HexEventHandlerTest() {
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
     * Test of setEventNameMask method, of class HexEventHandler.
     */
    @Test
    public void testSetEventNameMask() {
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

        v.value = "fix";
        handler.setEventNameMask ( type, new String [] { "two" } );
        instance.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals( v.value, "fix" );

        v.value = "fix";
        handler.setEventNameMask ( type, new String [] { "one", "two" } );
        instance.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals( v.value, "changed" );
    }

    /**
     * Test of isEnabled method, of class HexEventHandler.
     */
    @Test
    public void testIsEnabled() {
        System.out.println("isEnabled");
        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        assertEquals ( handler.isEnabled (), true );

        handler.setEnabled ( false );
        assertEquals ( handler.isEnabled (), false );

        handler.setEnabled ( true );
        assertEquals ( handler.isEnabled (), true );
    }

    /**
     * Test of setEnabled method, of class HexEventHandler.
     */
    @Test
    public void testSetEnabled() {
        System.out.println("setEnabled");
        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        handler.setEnabled ( false );
        assertEquals ( handler.isEnabled (), false );

        handler.setEnabled ( true );
        assertEquals ( handler.isEnabled (), true );
    }

    /**
     * Test of isRemovedOnNextUse method, of class HexEventHandler.
     */
    @Test
    public void testIsRemovedOnNextUse() {
        System.out.println("isRemovedOnNextUse");

        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        assertEquals ( handler.isRemovedOnNextUse (), false );

        HexEventHandler handler2 = new HexEventHandler ( true ) {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        assertEquals ( handler2.isRemovedOnNextUse (), true );
    }

    /**
     * Test of removeOnNextUse method, of class HexEventHandler.
     */
    @Test
    public void testRemoveOnNextUse() {
        System.out.println("removeOnNextUse");

        HexEventRegister register = HexEventRegister.getInstance ();

        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        handler.removeOnNextUse();

        register.registerHandlerForEvent( HexEvent.class, handler );

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "changed" );

        v.value = "fix";

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "fix" );
    }

    /**
     * Test of registerForEvent method, of class HexEventHandler.
     */
    @Test
    public void testRegisterForEvent_Class() {
        System.out.println("registerForEvent");

        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        handler.registerForEvent ( HexEvent.class );

        HexEventRegister register = HexEventRegister.getInstance ();

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "changed" );
    }

    /**
     * Test of registerForEvent method, of class HexEventHandler.
     */
    @Test
    public void testRegisterForEvent_Class_StringArr() {
        System.out.println("registerForEvent");
        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        handler.registerForEvent ( HexEvent.class, "two" );

        HexEventRegister register = HexEventRegister.getInstance ();

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "fix" );

        register.triggerEvent ( new HexEvent ( "two" ) );
        assertEquals ( v.value, "changed" );
    }

    /**
     * Test of unregisterForEvent method, of class HexEventHandler.
     */
    @Test
    public void testUnregisterForEvent() {
        System.out.println("unregisterForEvent");

        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        handler.registerForEvent ( HexEvent.class );

        HexEventRegister register = HexEventRegister.getInstance ();

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "changed" );

        v.value = "fix";

        handler.unregisterForEvent ( HexEvent.class );

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "fix" );
    }

    /**
     * Test of isDisabledOnNextUse method, of class HexEventHandler.
     */
    @Test
    public void testIsDisabledOnNextUse() {
        System.out.println("isDisabledOnNextUse");

        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        assertEquals ( handler.isDisabledOnNextUse (), false );
    }

    /**
     * Test of setDisabledOnNextUse method, of class HexEventHandler.
     */
    @Test
    public void testSetDisabledOnNextUse() {
        System.out.println("setDisabledOnNextUse");

        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        handler.registerForEvent ( HexEvent.class );
        handler.setDisabledOnNextUse ( true );

        HexEventRegister register = HexEventRegister.getInstance ();

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "changed" );

        v.value = "fix";

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "fix" );

        handler.setEnabled ( true );
        handler.setDisabledOnNextUse ( false );

        v.value = "fix";

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "changed" );

        v.value = "fix";

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "changed" );
    }

    /**
     * Test of isEnabledOnNextUse method, of class HexEventHandler.
     */
    @Test
    public void testIsEnabledOnNextUse() {
        System.out.println("isEnabledOnNextUse");

        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        assertEquals ( handler.isEnabledOnNextUse (), false );
    }

    /**
     * Test of setEnabledOnNextUse method, of class HexEventHandler.
     */
    @Test
    public void testSetEnabledOnNextUse() {
        System.out.println("setEnabledOnNextUse");

        final Value v = new Value ();
        v.value = "fix";

        HexEventHandler handler = new HexEventHandler () {
            @Override
            public void handle(HexEvent event) throws Exception {
                v.value = "changed";
            }
        };

        handler.registerForEvent ( HexEvent.class );
        handler.setEnabled ( false );
        handler.setEnabledOnNextUse ( true );

        HexEventRegister register = HexEventRegister.getInstance ();

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "fix" );

        v.value = "fix";

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "changed" );

        handler.setEnabled ( false );
        handler.setEnabledOnNextUse ( false );

        v.value = "fix";

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "fix" );

        v.value = "fix";

        register.triggerEvent ( new HexEvent ( "one" ) );
        assertEquals ( v.value, "fix" );
    }

    private class Value {
        public String value;
    }
}
