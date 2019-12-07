package hu.application.cash.cdi;

import java.io.Serializable;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * Producer osztály, amely az aktuális használatban lévő {@link WeldContainer}-t tartalmazza.
 * Ez a container tartalmazza a CDI által vezérelt osztályokat.
 *
 * @author Pregh András
 */
public class WeldContainerProducer implements Serializable {

    private static final Long serialVersionUID = 1L;

    private static WeldContainer weldContainer;

    /**
     * Ha még nincs {@link WeldContainer}, akkor létrehoz egyet és beállítja, amúgy pedig vissza adja az aktuálisat.
     *
     * @return az aktuális {@link WeldContainer}
     */
    public static WeldContainer getStaticWeldContainer() {
        if (weldContainer == null) {
            weldContainer = new Weld().initialize();
        }
        return weldContainer;
    }
}
