package hu.application.cash.cdi;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producer osztály, amely az SLF4J {@link Logger} injectálásához szükséges.
 *
 * @author Pregh András
 */
@Dependent
public class LoggerProducer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getBean().getBeanClass());
    }

}
