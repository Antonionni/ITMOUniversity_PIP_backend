package services;

import models.entities.PassageEntityPK;
import models.serviceEntities.LessonPage;
import models.serviceEntities.Passage;
import models.serviceEntities.PassageItem;
import models.serviceEntities.VerifyPassageItemModel;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface IPassageService {
    CompletionStage<Collection<PassageItem>> listNeedToVerifiedPassageItems(PassageEntityPK id);

    CompletionStage<Collection<PassageItem>> savePassageItem(Collection<PassageItem> items);

    CompletionStage<PassageItem> verifyPassageItem(VerifyPassageItemModel model);

    CompletionStage<LessonPage> startPassage(int testId);

    CompletableFuture<Passage> closePassage();
}
