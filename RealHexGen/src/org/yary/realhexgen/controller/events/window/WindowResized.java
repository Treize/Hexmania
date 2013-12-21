package org.yary.realhexgen.controller.events.window;

import org.yary.realhexgen.controller.events.HexEvent;

/**
 *
 * @author Yary Ribero
 */
public class WindowResized extends HexEvent {
    public WindowResized () {
        super ( "windowResized" );
    }
}
