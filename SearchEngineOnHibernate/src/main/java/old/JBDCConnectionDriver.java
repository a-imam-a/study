package old;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.sql.*;
import java.util.ArrayList;

public class JBDCConnectionDriver{

    private static Connection connection;
    private static final Logger LOGGER = LogManager.getLogger(JBDCConnectionDriver.class);
    private static final Marker EXCEPTIONS_MARKER = MarkerManager.getMarker("EXCEPTIONS");

    private JBDCConnectionDriver(){
    }

    protected static void startConnection() {
        if (connection == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/search_engine";
                String dbUser = "root";
                String dbPass = "testtest";
                connection = DriverManager.getConnection(url, dbUser, dbPass);
            }
            catch (SQLException e) {
                LOGGER.error(EXCEPTIONS_MARKER, e.getMessage());
            }
        }
    }

    protected static void closeConnection() {
        if (connection == null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error(EXCEPTIONS_MARKER, e.getMessage());
            }
        }
    }

    protected static boolean insertToTablePage(String path, int code, String content) {

        try {
            if (pathIsExist(path)) {
                return false;
            }
            String sqlQuery = "INSERT INTO page (path, code, content) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, path);
            preparedStatement.setInt(2, code);
            preparedStatement.setString(3, content);
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        }
        catch (SQLException e) {
            LOGGER.error(EXCEPTIONS_MARKER, path + ": " + e.getMessage());
            return false;
        }
    }

    protected static ArrayList<String> selectPathsFromTablePage(String paths) {

        try {
            String sqlQuery = "SELECT path FROM page WHERE path in (" + paths + ")";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            ArrayList<String> pathFromTable = new ArrayList<>();
            while (resultSet.next()) {
                pathFromTable.add(resultSet.getString("path"));
            }
            resultSet.close();

            return pathFromTable;
        }
        catch (SQLException e) {
            LOGGER.error(EXCEPTIONS_MARKER, e.getMessage());
            return new ArrayList<>();
        }

    }

    protected static boolean pathIsExist(String path) {

        try {
            String sqlQuery = "SELECT id FROM page WHERE path = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, path);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean exist = false;
            if (resultSet.next()){
                exist = true;
            }
            resultSet.close();
            preparedStatement.close();

            return exist;
        }
        catch (SQLException e) {
            LOGGER.error(EXCEPTIONS_MARKER, path + ": " + e.getMessage());
            return false;
        }

    }

}
