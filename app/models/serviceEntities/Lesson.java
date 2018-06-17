package models.serviceEntities;

import models.entities.LessonEntity;
import models.entities.MaterialEntity;
import models.entities.TestEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Lesson {
    private LessonInfo lessonInfo;
    private Collection<LessonPage> lessonPages;

    public Lesson() {}

    public Lesson(LessonInfo lessonInfo, Collection<LessonPage> lessonPages) {
        this.lessonInfo = lessonInfo;
        this.lessonPages = lessonPages;
    }

    public Lesson(LessonEntity lessonEntity, Collection<TestEntity> lessonTestEntities, Collection<MaterialEntity> lessonMaterialEntities) {
        this.lessonInfo = new LessonInfo(lessonEntity);
        Collection<LessonPage> lessonTests = lessonTestEntities.stream().map(LessonPage::new).collect(Collectors.toList());
        Collection<LessonPage> lessonMaterials = lessonMaterialEntities.stream().map(LessonPage::new).collect(Collectors.toList());
        Collection<LessonPage> lessonPages = new ArrayList<>();
        lessonPages.addAll(lessonTests);
        lessonPages.addAll(lessonMaterials);
        ((ArrayList<LessonPage>) lessonPages).sort(Comparator.comparing(x -> x.getLessonPageInfo().getCreatedAt()));
        this.lessonPages = lessonPages;
    }
}
