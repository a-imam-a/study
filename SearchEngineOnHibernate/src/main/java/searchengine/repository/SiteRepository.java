package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import searchengine.entity.Site;

public interface SiteRepository extends CrudRepository<Site, Integer> {
}
