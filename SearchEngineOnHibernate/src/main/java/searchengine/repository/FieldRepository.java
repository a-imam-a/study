package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import searchengine.entity.Field;

public interface FieldRepository extends CrudRepository<Field, Integer> {
}
