package hu.application.cash.cdi;

import javafx.util.Callback;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * Osztály, amely a JavaFX alkalmazásunk CDI által való vezérléséhez szükséges.
 *
 * @author Pregh András
 */
public class CDIControllerFactory implements Callback<Class<?>, Object> {

    @Inject
    private Instance<Object> instance;

    public Object call(Class<?> type) {
        return instance.select(type).get();
    }

}
