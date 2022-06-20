package searchengine;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.parsing.ParsingWebPages;
import searchengine.service.IndexService;
import searchengine.service.LemmaService;
import searchengine.service.PageService;
import searchengine.service.SiteService;

import java.util.Set;
import java.util.concurrent.ForkJoinPool;

@RestController
@RequestMapping(path = "api")
public class ApiController {

    @GetMapping(path = "startIndexing")
    public void startIndexing() {

        prepareTables();

        String webSite = "http://www.playback.ru/";
        String siteName = "Playback";
        new Thread(new ParsingWebPages(webSite, siteName)).start();

    }

    @GetMapping(path = "stopIndexing")
    public void stopIndexing() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        threadSet.forEach(thread -> thread.interrupt());
    }

    private static void prepareTables() {

        new PageService().truncateTable();
        new LemmaService().truncateTable();
        new IndexService().truncateTable();
        new SiteService().truncateTable();
    }
}
