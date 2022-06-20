package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import searchengine.entity.Page;

public interface PageRepository extends CrudRepository<Page, Integer> {

}
