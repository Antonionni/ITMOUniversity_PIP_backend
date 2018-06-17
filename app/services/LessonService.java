package services;

import Exceptions.BusinessException;
import models.entities.CourseEntity;
import models.entities.LessonEntity;
import models.serviceEntities.Lesson;
import models.serviceEntities.LessonInfo;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
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
            return new LessonInfo(createLessonEntity(lesson, courseEntity));
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
            return new LessonInfo(updateLessonEntity(lesson, courseEntity, lessonEntity));
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

    private LessonEntity createLessonEntity(LessonInfo lesson, CourseEntity course) {
        LessonEntity lessonEntity = new LessonEntity();
        return updateLessonEntity(lesson, course, lessonEntity);
    }

    private LessonEntity updateLessonEntity(LessonInfo lesson, CourseEntity course, LessonEntity lessonEntity) {
        lessonEntity.setTitle(lesson.getTitle());
        lessonEntity.setCourse(course);
        return lessonEntity;
    }
}
