package forOlderJava.absurdityAppForJava.domain.item.repository;

import forOlderJava.absurdityAppForJava.domain.item.ItemCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCacheRepository extends CrudRepository<ItemCache, String> {

}
