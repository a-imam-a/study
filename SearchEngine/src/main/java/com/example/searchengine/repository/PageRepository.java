package com.example.searchengine.repository;

import com.example.searchengine.entity.Lemma;
import org.springframework.data.repository.CrudRepository;
import com.example.searchengine.entity.Page;

import java.util.List;

public interface PageRepository extends CrudRepository<Page, Integer> {

    List<Page> findAllByPathIn(Iterable<String> paths);

    boolean existsByPath(String path);

    List<Page> findAllByLemmaList(Lemma lemma);

    List<Page> findAllByLemmaListAndPathIn(Lemma lemma, Iterable<Page> indexesPages);

    long count();
}
