package old;

import java.util.ArrayList;
import java.util.HashSet;

public class DBTables {

    private DBTables() {}

    public static void startConnection() {
        JBDCConnectionDriver.startConnection();
    }

    public static void closeConnection() {
        JBDCConnectionDriver.startConnection();
    }

    public static boolean pathIsExist(String path) {
        return JBDCConnectionDriver.pathIsExist(path);
    }

    public static void insertToTablePage(String path, int code, String content) {
        JBDCConnectionDriver.insertToTablePage(path, code, content);
    }

    public static ArrayList<String> getPathsFromTablePage(HashSet<String> pageLinks) {

        StringBuilder builder = new StringBuilder();
        for (String pageLink: pageLinks) {
            builder.append(((builder.length() != 0) ? ", '" : "'"))
                   .append(pageLink)
                   .append("'");
        }
        return JBDCConnectionDriver.selectPathsFromTablePage(builder.toString());

    }

}
