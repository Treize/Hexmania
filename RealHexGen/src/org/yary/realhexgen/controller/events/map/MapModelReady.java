package org.yary.realhexgen.controller.events.map;

import org.yary.realhexgen.controller.events.HexEvent;

/**
 *
 * @author Yary Ribero
 */
public class MapModelReady extends HexEvent {
    public MapModelReady () {
        super ( "modelReady" );
    }
}
