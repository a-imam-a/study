package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import searchengine.entity.Index;

public interface IndexRepository extends CrudRepository<Index, Integer> {
}
