package com.example.searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.searchengine.entity.Site;

public interface SiteRepository extends CrudRepository<Site, Integer> {

}
