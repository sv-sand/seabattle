package db;

import java.io.*;
import java.sql.*;
import java.util.stream.Collectors;

public class DataBase {
    private final int VERSION = 1;
    private final String DB_DRIVER_NAME = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:";
    private final String DB_FOLDER = "./db/";
    private final String DB_FILE_NAME = "db";

    private Connection connection = null;
    private Status status;
    private String errorMessage = "";

    enum Status {
        READY,
        CONNECTED,
        ERROR
    }

    public DataBase() {
        try {
            Class.forName(DB_DRIVER_NAME);
            status = Status.READY;
        } catch (ClassNotFoundException e) {
            Exception(String.format("Driver name '%s' not found", DB_DRIVER_NAME));
        }
    }

    public void Disconnect() {
        if(status != Status.CONNECTED)
            return;

        try{
            connection.close();
            status = Status.READY;
        } catch (SQLException e) {
            Exception(String.format("Disconnection failed: %s", e.getMessage()));
        }
    }

    public void Connect() {
        if(status != Status.READY)
            return;

        boolean isNew = !isDataBaseExist();

        try {
            connection = DriverManager.getConnection(DB_URL + DB_FOLDER + DB_FILE_NAME);
            status = Status.CONNECTED;
        } catch (SQLException e) {
            Exception(String.format("Connection failed: %s", e.getMessage()));
            return;
        }

        if(isNew)
            InitializeDataBase();

        UpdateDataBase();
    }

    private void InitializeDataBase() {
        if (status != Status.CONNECTED)
            return;

        System.out.println("Initializing empty database...");
        String query;

        // Create tables
        query = getQuery("sql/init.sql");
        try(Statement statement = connection.createStatement()) {
            statement.execute(query);
        }catch (SQLException e) {
            Exception(String.format("SQL query failed: %s", e.getMessage()));
        }

        // Fill service tables
        query = "INSERT INTO _params(version) VALUES (?);";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, VERSION);
            statement.executeUpdate();
        }catch (SQLException e) {
            Exception(String.format("SQL query failed: %s", e.getMessage()));
        }
    }

    // Service methods

    public boolean isDataBaseExist() {
        File file = new File(DB_FOLDER+DB_FILE_NAME+".mv.db");
        return file.exists();
    }

    private int getDataBaseVersion() {
        if (status != Status.CONNECTED)
            return 0;

        String query = "SELECT version FROM _params";

        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("version");
            }
        } catch (SQLException e) {
            Exception(String.format("Query failed: %s", e.getMessage()));
        }
        return 0;
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

    private void Exception(String message) {
        status = Status.ERROR;
        errorMessage = message;

        System.out.println("Data base exception: " + message);
    }

    // Data base updating

    private void UpdateDataBase() {
        if (status != Status.CONNECTED)
            return;

        int dbVersion = getDataBaseVersion();

        while(dbVersion != VERSION) {
            switch(dbVersion) {
                case 1: UpdateDataBaseToVersion2(); break;
            }
            dbVersion = getDataBaseVersion();
        }
    }

    private void UpdateDataBaseToVersion2() {

    }

    // Getters & setters

    public Status getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Connection getConnection() {
        return connection;
    }
}
