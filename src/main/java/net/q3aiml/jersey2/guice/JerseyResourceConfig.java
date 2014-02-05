package net.q3aiml.jersey2.guice;

import com.google.inject.Injector;
import com.google.inject.Key;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Feature;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

public class JerseyResourceConfig extends ResourceConfig {
    private ServiceLocator serviceLocator;

    @Inject
    public void setServiceLocator(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Context
    ServletContext servletContext;

    public JerseyResourceConfig(Injector injector) {
        super();

        registerClasses(JerseyEventListener.class);
        register(new JerseyDiBinder());

        register(injector);
    }

    private void register(Injector injector) {
        while (injector != null) {
            for (Key<?> key : injector.getBindings().keySet()) {
                Type type = key.getTypeLiteral().getType();
                if (type instanceof Class) {
                    Class<?> c = (Class)type;
                    if (c.isAnnotationPresent(Path.class)) {
                        register(c);
                    } else if (c.isAnnotationPresent(Provider.class)) {
                        register(c);
                    } else if (Feature.class.isAssignableFrom(c)) {
                        register(c);
                    }
                }
            }
            injector = injector.getParent();
        }
    }

}
