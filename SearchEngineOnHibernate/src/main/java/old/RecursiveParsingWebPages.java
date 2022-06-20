package old;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class RecursiveParsingWebPages {

    private static final Logger LOGGER = LogManager.getLogger(RecursiveParsingWebPages.class);
    private static final Marker PAGES_HISTORY_MARKER = MarkerManager.getMarker("PAGES_HISTORY");
    private static final Marker EXCEPTIONS_MARKER = MarkerManager.getMarker("EXCEPTIONS");

    private static String rootWebSite;

    public static void startParse(String webPage) {

        DBTables.startConnection();
        rootWebSite = webPage.substring(0, webPage.length() -1);
        parsingPage(webPage);
        DBTables.closeConnection();
    }

    private static void parsingPage(String webPage) {

        String rootPath = webPage.replace(rootWebSite, "");
        if (DBTables.pathIsExist(rootPath)) {
            LOGGER.info(PAGES_HISTORY_MARKER, "page {} is exist in table", rootPath);
            return;
        }

        Response response = getResponse(webPage);
        if (response == null) {
            return;
        }

        DBTables.insertToTablePage(rootPath, response.statusCode(), response.body());

        HashSet<String> pageLinks = getPageLinks(response, webPage);
        if (pageLinks == null || pageLinks.isEmpty()) {
            LOGGER.info(PAGES_HISTORY_MARKER, "no links on page {}", webPage);
            return;
        }

        ArrayList<String> pathsFromTablePage = DBTables.getPathsFromTablePage(pageLinks);

        pathsFromTablePage.forEach(t -> pageLinks.remove(t));
        pageLinks.forEach(t ->parsingPage(rootWebSite + t));

    }

    private static Response getResponse(String page) {
        try {
            Thread.sleep(100);
            Response response = Jsoup.connect(page)
                    .userAgent("Mozilla/5.0 (Windows; U; " +
                            "WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .execute();
            return response;
        } catch (IOException | InterruptedException e) {
            LOGGER.error(EXCEPTIONS_MARKER, page + ": " + e.getMessage());
            return null;
        }
    }

    private static HashSet<String> getPageLinks(Response response, String page) {

        try {
            Document doc = response.parse();
            HashSet<String> pageLinks = (HashSet<String>) doc.select("a[href]").stream()
                    .map(t -> t.attr("href"))
                    .filter(url -> url.matches( "^[/][^.#].*[^\\.jpg]$"))
                    .collect(Collectors.toSet());
            return pageLinks;
        } catch (IOException e) {
            LOGGER.error(EXCEPTIONS_MARKER, page + ": " + e.getMessage());
            return null;
        }
    }
}
