package services;

import Exceptions.BusinessException;
import models.entities.CourseEntity;
import models.serviceEntities.Course;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CourseService extends BaseService implements ICourseService {
    @Inject
    public CourseService(JPAApi jpaApi, HttpExecutionContext ec) {
        super(jpaApi, ec);
    }

    public CompletionStage<Optional<Course>> get(int courseId) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = em.find(CourseEntity.class, courseId);
            if(courseEntity == null) {
                return Optional.empty();
            }
            return Optional.of(new Course(courseEntity));
        }), ec.current());
    }

    @Override
    public CompletionStage<Course> create(Course course) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = createCourseEntity(course);
            em.persist(courseEntity);
            return new Course(courseEntity);
        }),ec.current());
    }

    @Override
    public CompletionStage<Course> update(Course course) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = em.find(CourseEntity.class, course.getId());
            if(courseEntity == null) {
                throw new BusinessException();
            }
            courseEntity = updateCourseEntity(course, courseEntity);
            return new Course(em.merge(courseEntity));
        }), ec.current());
    }

    @Override
    public CompletionStage<Boolean> delete(int courseId) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = em.find(CourseEntity.class, courseId);
            if(courseEntity == null) {
                throw new BusinessException();
            }
            em.remove(courseEntity);
            return true;
        }), ec.current());
    }

    private CourseEntity createCourseEntity(Course course) {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setCreatedAt(new Date());
        return updateCourseEntity(course, courseEntity);
    }

    private CourseEntity updateCourseEntity(Course course, CourseEntity courseEntity) {
        courseEntity.setUpdatedAt(new Date());
        courseEntity.setImageurl(course.getImageurl());
        courseEntity.setSubject(course.getSubject());
        courseEntity.setTitle(course.getTitle());
        return courseEntity;
    }
}
