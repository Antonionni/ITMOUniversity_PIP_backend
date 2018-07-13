package services;

import Exceptions.BusinessException;
import models.PagedResult;
import models.entities.CourseEntity;
import models.entities.CoursePeriodEntity;
import models.entities.UserEntity;
import models.serviceEntities.Course;
import models.serviceEntities.CourseInfo;
import models.serviceEntities.CoursePeriod;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CourseService extends BaseService implements ICourseService {
    private final IUserService UserService;

    @Inject
    public CourseService(JPAApi jpaApi, HttpExecutionContext ec, IUserService userService) {
        super(jpaApi, ec);
        UserService = userService;
    }

    public CompletionStage<Optional<Course>> get(int courseId) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = em.find(CourseEntity.class, courseId);
            if(courseEntity == null) {
                return Optional.empty();
            }
            return Optional.of(new Course(courseEntity, courseEntity.getCoursePeriods(), courseEntity.getLessons()));
        }), ec.current());
    }

    @Override
    public CompletionStage<CourseInfo> create(CourseInfo courseInfo) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = createCourseEntity(courseInfo);
            em.persist(courseEntity);
            return new CourseInfo(courseEntity);
        }),ec.current());
    }

    @Override
    public CompletionStage<CourseInfo> update(CourseInfo courseInfo) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = em.find(CourseEntity.class, courseInfo.getId());
            if(courseEntity == null) {
                throw new BusinessException();
            }
            courseEntity = updateCourseEntity(courseInfo, courseEntity);
            return new CourseInfo(em.merge(courseEntity));
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

    //todo: Make Unique Period!113113131
    public CompletionStage<CoursePeriod> createPeriod(CoursePeriod coursePeriod, int courseId) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = em.find(CourseEntity.class, courseId);
            if(courseEntity == null) {
                throw new BusinessException();
            }
            CoursePeriodEntity coursePeriodEntity = createCoursePeriodEntity(coursePeriod, courseEntity);
            em.persist(coursePeriodEntity);
            return new CoursePeriod(coursePeriodEntity);
        }), ec.current());
    }

    public CompletionStage<CoursePeriod> updatePeriod(CoursePeriod coursePeriod, int courseId) {
        return supplyAsync(() -> wrap(em -> {
            CourseEntity courseEntity = em.find(CourseEntity.class, courseId);
            if(courseEntity == null) {
                throw new BusinessException();
            }
            CoursePeriodEntity coursePeriodEntity = em.find(CoursePeriodEntity.class, coursePeriod.getId());
            if(coursePeriodEntity == null) {
                throw new BusinessException();
            }
            coursePeriodEntity = updateCoursePeriodEntity(coursePeriod, courseEntity, coursePeriodEntity);
            return new CoursePeriod(em.merge(coursePeriodEntity));
        }), ec.current());
    }

    public CompletionStage<Boolean> deletePeriod(int coursePeriodId) {
        return supplyAsync(() -> wrap(em -> {
            CoursePeriodEntity coursePeriodEntity = em.find(CoursePeriodEntity.class, coursePeriodId);
            if(coursePeriodEntity == null) {
                throw new BusinessException();
            }
            em.remove(coursePeriodEntity);
            return true;
        }), ec.current());
    }

    private CoursePeriodEntity createCoursePeriodEntity(CoursePeriod coursePeriod, CourseEntity course) {
        CoursePeriodEntity coursePeriodEntity = new CoursePeriodEntity();
        return updateCoursePeriodEntity(coursePeriod, course, coursePeriodEntity);
    }

    private CoursePeriodEntity updateCoursePeriodEntity(CoursePeriod coursePeriod, CourseEntity course, CoursePeriodEntity coursePeriodEntity) {
        coursePeriodEntity.setCourse(course);
        coursePeriodEntity.setEnddate(coursePeriod.getEndDate());
        coursePeriodEntity.setStartdate(coursePeriod.getStartDate());
        return coursePeriodEntity;
    }

    private CourseEntity createCourseEntity(CourseInfo courseInfo) {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setCreatedAt(new Date());
        UserEntity currentUser = UserService.getCurrentUser();
        ArrayList<UserEntity> teachers = new ArrayList<>();
        teachers.add(currentUser);
        courseEntity.setCourseTeachers(teachers);
        courseEntity.setCoursePeriods(new ArrayList<>());
        courseEntity.setLessons(new ArrayList<>());
        return updateCourseEntity(courseInfo, courseEntity);
    }

    private CourseEntity updateCourseEntity(CourseInfo courseInfo, CourseEntity courseEntity) {
        courseEntity.setUpdatedAt(new Date());
        courseEntity.setImageurl(courseInfo.getImageurl());
        courseEntity.setSubject(courseInfo.getSubject());
        courseEntity.setTitle(courseInfo.getTitle());
        return courseEntity;
    }
}
