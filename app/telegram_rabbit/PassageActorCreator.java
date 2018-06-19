package telegram_rabbit;

import akka.japi.Creator;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class PassageActorCreator implements Creator<PassageActor> {
    @Inject
    Injector injector;

    @Override
    public PassageActor create() throws Exception {
        return injector.getInstance(PassageActor.class);
    }
}
