package ro.uaic.info.contentmanager.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.contentmanager.entity.Course;

public interface CourseRepository extends CrudRepository<Course, Integer> {
}
