package services;

import Exceptions.BusinessException;
import models.PagedResult;
import models.entities.CourseEntity;
import models.entities.UserEntity;
import models.entities.CourseSubscriptionEntity;
import models.serviceEntities.CourseInfo;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CourseListService extends BaseService implements ICourseListService {
    private IUserService UserService;

    @Inject
    public CourseListService(JPAApi jpaApi, HttpExecutionContext ec, IUserService userService) {
        super(jpaApi, ec);
        UserService = userService;
    }

    // haha so unperformance
    @Override
    public CompletionStage<List<CourseInfo>> listRandomCourses(int size) {
        return supplyAsync(() -> wrap(em -> {
            List<CourseEntity> courses = em.createQuery("select course from CourseEntity course", CourseEntity.class).getResultList();
            Collections.shuffle(courses);
            return courses.stream().limit(size).map(CourseInfo::new).collect(Collectors.toList());
        }), ec.current());
    }

    public CompletionStage<List<CourseInfo>> listAllCourses() {
        return supplyAsync(() -> wrap(em -> {
            List<CourseEntity> courses = em.createQuery("select course from CourseEntity course", CourseEntity.class).getResultList();
            return courses.stream().map(CourseInfo::new).collect(Collectors.toList());
        }), ec.current());
    }

    @Override
    public CompletionStage<List<CourseInfo>> findCourses(String keyword) {
        return supplyAsync(() -> wrap(em -> {
            TypedQuery<CourseEntity> courseQuery =  em.createQuery("select course from CourseEntity course where title like %:keyword%", CourseEntity.class);
            courseQuery.setParameter("keyword", keyword);
            /*courseQuery.setFirstResult(pageNumber * pageSize);
            courseQuery.setMaxResults(pageSize + 1);*/
            List<CourseEntity> coursesResult = courseQuery.getResultList();
            //int resultSize = coursesResult.size();
            //return new PagedResult<>(shrinkedResult, resultSize > pageSize);
            return courseQuery.getResultList().stream()/*.limit(pageSize)*/.map(CourseInfo::new).collect(Collectors.toList());
        }), ec.current());
    }

    @Override
    public CompletionStage<Boolean> subscribeToCourse(int courseId) {
        return supplyAsync(() -> wrap(em -> {
            UserEntity user = UserService.getCurrentUser();
            if(user == null) {
                //todo do this shit
                throw new BusinessException();
            }
            CourseEntity course = em.find(CourseEntity.class, courseId);
            if(course == null) {
                throw new BusinessException();
            }
            em.persist(createCourseSubscriptionEntity(user, course));
            return true;
        }), ec.current());
    }

    private CourseSubscriptionEntity createCourseSubscriptionEntity(UserEntity user, CourseEntity course) {
        CourseSubscriptionEntity subscription = new CourseSubscriptionEntity();
        subscription.setCreatedAt(new Date());
        return updateCourseSubscriptionEntity(subscription, user, course);
    }

    private CourseSubscriptionEntity updateCourseSubscriptionEntity(CourseSubscriptionEntity subscription, UserEntity user, CourseEntity course) {
        subscription.setUser(user);
        subscription.setCourse(course);
        subscription.setUpdatedAt(new Date());
        return subscription;
    }
}
