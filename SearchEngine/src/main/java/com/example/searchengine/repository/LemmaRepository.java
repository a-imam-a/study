package com.example.searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.searchengine.entity.Lemma;

import java.util.HashSet;
import java.util.List;

public interface LemmaRepository extends JpaRepository<Lemma, Integer> {

    List<Lemma> findAllByLemmaInOrderByFrequency(Iterable<String> wordLemmas);

}
