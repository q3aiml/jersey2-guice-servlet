package net.q3aiml.jersey2.guice;

import com.google.inject.Injector;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GuiceContainer extends ServletContainer {
    @Inject
    public GuiceContainer(Injector injector) {
        super(new JerseyResourceConfig(injector));
    }
}
