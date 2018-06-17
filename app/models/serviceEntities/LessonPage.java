package models.serviceEntities;

import enumerations.LessonPageType;
import models.entities.LessonEntity;
import models.entities.MaterialEntity;
import models.entities.TestEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public class LessonPage {
    private LessonPageInfo lessonPageInfo;
    private Collection<ILessonPageItem> pageItems;
    private LessonPageType lessonPageType;

    public LessonPage() {}

    public LessonPage(LessonPageInfo lessonPageInfo, Collection<ILessonPageItem> pageItems, LessonPageType lessonPageType) {
        this.lessonPageInfo = lessonPageInfo;
        this.pageItems = pageItems;
        this.lessonPageType = lessonPageType;
    }

    public LessonPage(MaterialEntity materialEntity) {
        this(
                new LessonPageInfo(materialEntity),
                materialEntity.getMaterialContents()
                        .stream()
                        .map(Content::new)
                        .collect(Collectors.toList()),
                LessonPageType.Material);
    }

    public LessonPage(TestEntity testEntity) {
        this(
                new LessonPageInfo(testEntity),
                testEntity.getQuestions()
                .stream()
                .map(Question::new)
                .collect(Collectors.toList()),
                LessonPageType.Test);
    }

    public LessonPageInfo getLessonPageInfo() {
        return lessonPageInfo;
    }

    public void setLessonPageInfo(LessonPageInfo lessonPageInfo) {
        this.lessonPageInfo = lessonPageInfo;
    }

    public Collection<ILessonPageItem> getPageItems() {
        return pageItems;
    }

    public void setPageItems(Collection<ILessonPageItem> pageItems) {
        this.pageItems = pageItems;
    }

    public LessonPageType getLessonPageType() {
        return lessonPageType;
    }

    public void setLessonPageType(LessonPageType lessonPageType) {
        this.lessonPageType = lessonPageType;
    }
}
