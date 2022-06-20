package searchengine;

import searchengine.parsing.ParsingWebPages;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();
        //ParsingWebPages.startParsingWebPages();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000 + " sec");
        //new TextSearch().foundPages("надо бы купить телефон");
        
    }

}
