package services;

import Exceptions.BusinessException;
import enumerations.LessonPageType;
import models.entities.CourseEntity;
import models.entities.LessonEntity;
import models.entities.MaterialEntity;
import models.entities.TestEntity;
import models.serviceEntities.Lesson;
import models.serviceEntities.LessonInfo;
import models.serviceEntities.LessonPage;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class LessonService extends BaseService implements ILessonService {

    @Inject
    public LessonService(JPAApi jpaApi, HttpExecutionContext ec) {
        super(jpaApi, ec);
    }

    @Override
    public CompletionStage<LessonInfo> create(LessonInfo lesson, int courseId) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = em.find(CourseEntity.class, courseId);
            if(courseEntity == null) {
                throw new BusinessException();
            }
            LessonEntity lessonEntity = createLessonEntity(lesson, courseEntity);
            em.persist(lessonEntity);
            return new LessonInfo(lessonEntity);
        }), ec.current());
    }

    @Override
    public CompletionStage<LessonInfo> update(LessonInfo lesson, int courseId) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = em.find(CourseEntity.class, courseId);
            if(courseEntity == null) {
                throw new BusinessException();
            }
            LessonEntity lessonEntity = em.find(LessonEntity.class, lesson.getId());
            if(lessonEntity == null) {
                throw new BusinessException();
            }
            return new LessonInfo(em.merge(updateLessonEntity(lesson, courseEntity, lessonEntity)));
        }), ec.current());
    }

    @Override
    public CompletionStage<Boolean> delete(int lessonId) {
        return supplyAsync(() -> wrap(em -> {
            LessonEntity lessonEntity = em.find(LessonEntity.class, lessonId);
            if(lessonEntity == null) {
                throw new BusinessException();
            }
            em.remove(lessonEntity);
            return true;
        }), ec.current());
    }

    @Override
    public CompletionStage<Optional<Lesson>> get(int id) {
        return supplyAsync(() -> wrap(em -> {
            LessonEntity lessonEntity = em.find(LessonEntity.class, id);
            if(lessonEntity == null) {
                return Optional.empty();
            }
            return Optional.of(new Lesson(lessonEntity, lessonEntity.getLessonTests(), lessonEntity.getMaterials()));
        }), ec.current());
    }

    public CompletionStage<Optional<LessonPage>> getMaterial(int id) {
        return supplyAsync(() -> wrap(em -> {
            MaterialEntity materialEntity = em.find(MaterialEntity.class, id);
            if(materialEntity == null) {
                return Optional.empty();
            }
            return Optional.of(new LessonPage(materialEntity));
        }), ec.current());
    }

    public CompletionStage<LessonPage> createMaterial(LessonPage page, int lessonId) {
        return supplyAsync(() -> wrap(em -> {
            if(page.getLessonPageType() != LessonPageType.Material) {
                throw new BusinessException();
            }
            LessonEntity lessonEntity = em.find(LessonEntity.class, lessonId);
            if(lessonEntity == null) {
                throw new BusinessException();
            }
            MaterialEntity materialEntity = createMaterialEntity(page, lessonEntity);
            em.persist(materialEntity);
            return new LessonPage(materialEntity);
        }), ec.current());
    }

    public CompletionStage<LessonPage> updateMaterial(LessonPage page, int lessonId) {
        return supplyAsync(() -> wrap(em -> {
            if(page.getLessonPageType() != LessonPageType.Material) {
                throw new BusinessException();
            }
            LessonEntity lessonEntity = em.find(LessonEntity.class, lessonId);
            if(lessonEntity == null) {
                throw new BusinessException();
            }
            MaterialEntity materialEntity = em.find(MaterialEntity.class, page.getLessonPageInfo().getId());
            if(materialEntity == null) {
                throw new BusinessException();
            }
            materialEntity = updateMaterialEntity(page, lessonEntity, materialEntity);
            em.merge(materialEntity);
            return new LessonPage(materialEntity);
        }), ec.current());
    }

    public CompletionStage<Boolean> deleteMaterial(int materialId) {
        return supplyAsync(() -> wrap(em -> {
            MaterialEntity materialEntity = em.find(MaterialEntity.class, materialId);
            if(materialEntity == null) {
                throw new BusinessException();
            }
            em.remove(materialEntity);
            return true;
        }), ec.current());
    }

    public CompletionStage<LessonPage> createTest(LessonPage page, int lessonId) {
        return supplyAsync(() -> wrap(em -> {
            if(page.getLessonPageType() != LessonPageType.Test) {
                throw new BusinessException();
            }
            LessonEntity lessonEntity = em.find(LessonEntity.class, lessonId);
            if(lessonEntity == null) {
                throw new BusinessException();
            }
            TestEntity testEntity = createTestEntity(page, lessonEntity);
            em.persist(testEntity);
            return new LessonPage(testEntity);
        }), ec.current());
    }

    public CompletionStage<LessonPage> updateTest(LessonPage page, int lessonId) {
        return supplyAsync(() -> wrap(em -> {
            if(page.getLessonPageType() != LessonPageType.Test) {
                throw new BusinessException();
            }
            LessonEntity lessonEntity = em.find(LessonEntity.class, lessonId);
            if(lessonEntity == null) {
                throw new BusinessException();
            }
            TestEntity testEntity = em.find(TestEntity.class, page.getLessonPageInfo().getId());
            if(testEntity == null) {
                throw new BusinessException();
            }
            testEntity = updateTestEntity(page, lessonEntity, testEntity);
            em.merge(testEntity);
            return new LessonPage(testEntity);
        }), ec.current());
    }

    public CompletionStage<Boolean> deleteTest(int testId) {
        return supplyAsync(() -> wrap(em -> {
            TestEntity testEntity = em.find(TestEntity.class, testId);
            if(testEntity == null) {
                throw new BusinessException();
            }
            em.remove(testEntity);
            return true;
        }), ec.current());
    }

    public CompletionStage<Optional<LessonPage>> getTest(int id) {
        return supplyAsync(() -> wrap(em -> {
            TestEntity testEntity = em.find(TestEntity.class, id);
            if(testEntity == null) {
                return Optional.empty();
            }
            return Optional.of(new LessonPage(testEntity));
        }), ec.current());
    }

    private LessonEntity createLessonEntity(LessonInfo lesson, CourseEntity course) {
        LessonEntity lessonEntity = new LessonEntity();
        return updateLessonEntity(lesson, course, lessonEntity);
    }

    private LessonEntity updateLessonEntity(LessonInfo lesson, CourseEntity course, LessonEntity lessonEntity) {
        lessonEntity.setTitle(lesson.getTitle());
        lessonEntity.setCourse(course);
        return lessonEntity;
    }

    private MaterialEntity createMaterialEntity(LessonPage page, LessonEntity lessonEntity) {
        MaterialEntity materialEntity = new MaterialEntity();
        materialEntity.setCreatedAt(new Date());
        return updateMaterialEntity(page, lessonEntity, materialEntity);
    }

    private MaterialEntity updateMaterialEntity(LessonPage page, LessonEntity lessonEntity, MaterialEntity materialEntity) {
        materialEntity.setUpdatedAt(new Date());
        materialEntity.setLesson(lessonEntity);
        materialEntity.setTitle(page.getLessonPageInfo().getTitle());
        return materialEntity;
    }

    private TestEntity createTestEntity(LessonPage page, LessonEntity lessonEntity) {
        TestEntity testEntity = new TestEntity();
        testEntity.setCreatedAt(new Date());
        return updateTestEntity(page, lessonEntity, testEntity);
    }

    private TestEntity updateTestEntity(LessonPage page, LessonEntity lessonEntity, TestEntity testEntity) {
        testEntity.setUpdatedAt(new Date());
        testEntity.setLesson(lessonEntity);
        testEntity.setTitle(page.getLessonPageInfo().getTitle());
        return testEntity;
    }
}
