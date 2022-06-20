package com.example.searchengine.service;

import com.example.searchengine.entity.Site;
import com.example.searchengine.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteService {

    @Autowired
    SiteRepository siteRepository;

    public void insert(Site site) {
        siteRepository.save(site);
    }

    public void deleteAll() {
        siteRepository.deleteAll();
    }
}
