package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.stream.Collectors;

public class DataBase {
    private final String dbDriverName = "org.h2.Driver";
    private final String dbURL = "jdbc:h2:./db/db";

    private boolean hasException = false;
    private String exceptionMessage = "";

    private Connection connection;

    public DataBase() {
        try {
            Class.forName(dbDriverName);
        } catch (ClassNotFoundException e) {
            setException(String.format("Driver name '%s' not found", dbDriverName));
        }
    }

    public void Disconnect() {
        if(connection==null)
            return;

        try{
            connection.close();
        } catch (SQLException e) {
            setException(String.format("Disconnection failed: %s", e.getMessage()));
        }
    }

    public void Connect() {
        if(hasException)
            return;

        try {
            connection = DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            setException(String.format("Connection failed: %s", e.getMessage()));
        }
    }

    public void InitializeDataBase() {
        if(hasException)
            return;

        System.out.println("Initializing empty database...");
        String query = getQuery("sql/init.sql");

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException e) {
            setException(String.format("SQL query failed: %s", e.getMessage()));
        }

    }

    private String getQuery(String name) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(name)) {
            if (is == null)
                return "";

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    // Exception handling

    private void setException(String message) {
        hasException = true;
        exceptionMessage = message;

        System.out.println("Data base exception: " + message);
    }

    public Boolean hasException() {
        return hasException;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
