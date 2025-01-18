package repo.database.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class for database access
 */
public class DataBaseAcces {
    private Connection connection;
    private String url;
    private String password;
    private String username;

    /**
     * Constructor
     * @param url - the url
     * @param username - the username
     * @param password - the password
     */
    public DataBaseAcces(String url, String username, String password) {
        this.url = url;
        this.password = password;
        this.username = username;
    }

    /**
     * Method to create a connection
     * @throws SQLException
     */
    public void createConnection() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    /**
     * Method to create a statement
     * @param query - the query
     * @return PreparedStatement
     * @throws SQLException
     */
    public PreparedStatement createStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    /**
     * Method to set the username
     * @param username - the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Method to get the username
     * @return username - the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method to get the url
     * @return url - the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Method to set the url
     * @param url - the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Method to get the password
     * @return password - the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method to set the password
     * @param password - the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method to get the connection
     * @return connection - the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Method to close the connection
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * Method to close the statement
     * @param statement - the statement
     * @throws SQLException
     */
    public void closeStatement(PreparedStatement statement) throws SQLException {
        statement.close();
    }
}
