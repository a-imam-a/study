package com.example.searchengine.controller;

import com.example.searchengine.parsing.*;

import com.example.searchengine.service.SiteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api")
public class ApiController {

    public ApiController(SiteService siteService) {
        this.siteService = siteService;
    }

    private final SiteService siteService;

    @GetMapping(path = "startIndexing")
    public void startIndexing() {
        //prepareTables();
        new ParsingWebPages().startParsingWebPages("http://www.playback.ru/", "Playback", siteService);
    }

    @GetMapping(path = "stopIndexing")
    public void stopIndexing() {

    }

    private void prepareTables() {

        //System.out.println(pageService.count());
        //pageService.deleteAll();
    /*    new PageService().deleteAll();
        new LemmaService().deleteAll();
        new IndexService().deleteAll();*/
        siteService.deleteAll();
    }
}
