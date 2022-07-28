package org.acme.microprofile.faulttolerance;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jboss.logging.Logger;

@Path("/coffee")
public class CoffeeResource {

    private static final Logger LOGGER = Logger.getLogger(CoffeeResource.class);

    @Inject
    CoffeeRepositoryService coffeeRepository;

    private AtomicLong counter = new AtomicLong(0);

    @GET
    public List<Coffee> coffees() {
        final Long invocationNumber = counter.getAndIncrement();

        maybeFail(String.format("CoffeeResource#coffees() invocation #%d failed", invocationNumber));

        LOGGER.infof("CoffeeResource#coffees() invocation #%d returning successfully", invocationNumber);
        return coffeeRepository.getAllCoffees();
    }

    private void maybeFail(String failureLogMessage) {
        if (new Random().nextBoolean()) {
            LOGGER.error(failureLogMessage);
            throw new RuntimeException("Resource failure.");
        }
    }
}
