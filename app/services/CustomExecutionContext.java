package services;

import akka.actor.ActorSystem;
import com.google.inject.Inject;
import scala.concurrent.ExecutionContext;
import scala.concurrent.ExecutionContextExecutor;

public class CustomExecutionContext implements ExecutionContextExecutor {
    private final ExecutionContext executionContext;

    private static final String name = "database.dispatcher";

    @Inject
    public CustomExecutionContext(ActorSystem actorSystem) {
        this.executionContext = actorSystem.dispatchers().lookup(name);
    }

    @Override
    public ExecutionContext prepare() {
        return executionContext.prepare();
    }

    @Override
    public void execute(Runnable command) {
        executionContext.execute(command);
    }

    @Override
    public void reportFailure(Throwable cause) {
        executionContext.reportFailure(cause);
    }
}
