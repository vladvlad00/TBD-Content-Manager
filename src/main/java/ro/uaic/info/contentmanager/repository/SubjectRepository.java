package ro.uaic.info.contentmanager.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.contentmanager.entity.Subject;

public interface SubjectRepository extends CrudRepository<Subject, Integer> {
}
