package net.q3aiml.jersey2.guice;

import com.google.inject.Injector;
import org.glassfish.hk2.api.Context;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.jvnet.hk2.guice.bridge.api.GuiceScope;
import org.jvnet.hk2.guice.bridge.internal.GuiceScopeContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;

public class JerseyDiBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(NullableGuiceScopeContext.class).to(NullableGuiceScopeContext.class)
                .to(GuiceScopeContext.class)
                .to(new TypeLiteral<Context<GuiceScope>>() {})
                .in(Singleton.class);
        bindFactory(GuiceProvider.class).to(Injector.class).in(Singleton.class);
    }

    @Singleton
    private static class NullableGuiceScopeContext extends GuiceScopeContext {
        @Override
        public boolean supportsNullCreation() {
            return true;
        }
    }

    private static class GuiceProvider implements Factory<Injector> {
        @Inject
        public void setServletContext(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        ServletContext servletContext;

        @Override
        public Injector provide() {
            Injector injector = (Injector)servletContext.getAttribute(Injector.class.getName());
            if (injector == null) {
                throw new NullPointerException("Unable to get injector from servlet context. "
                        + "Ensure that you are extending GuiceServletContextListener.");
            }
            return injector;
        }

        @Override
        public void dispose(Injector instance) {

        }
    }
}