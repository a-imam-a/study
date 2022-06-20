package com.example.searchengine.repository;

import com.example.searchengine.entity.Lemma;
import com.example.searchengine.entity.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.searchengine.entity.Index;

import java.util.List;

public interface IndexRepository extends CrudRepository<Index, Integer> {

    @Query(value = "SELECT t.page, sum(t.rank) as sum_rank from Index" +
            " t where t.page in ?indexesPages and t.lemma in ?lemma" +
            " group by t.page order by sum(t.rank) desc", nativeQuery=true)
    List<Object[]> findPagesWithRank(Iterable<Lemma> orderedLemmas, Iterable<Page> indexesPages);

}
