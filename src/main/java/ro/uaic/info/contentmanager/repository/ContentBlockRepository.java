package ro.uaic.info.contentmanager.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.contentmanager.entity.ContentBlock;

public interface ContentBlockRepository extends CrudRepository<ContentBlock,Integer> {
}
