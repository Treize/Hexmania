package org.yary.realhexgen.controller.events.configuration;

import org.yary.realhexgen.controller.events.HexEvent;

/**
 *
 * @author Yary Ribero
 */
public class ConfigurationChanged extends HexEvent {

    public ConfigurationChanged ( String property ) {
        super ( property );
    }

}
