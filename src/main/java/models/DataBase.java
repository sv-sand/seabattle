
import java.io.*;
import java.sql.*;
import java.util.stream.Collectors;

public class DataBase {
    private final int VERSION = 1;
    private final String DB_DRIVER_NAME = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:";
    private final String DB_FOLDER = "./db/";
    private final String DB_FILE_NAME = "db";

    private boolean hasException = false;
    private String exceptionMessage = "";

    private Connection connection = null;

    public DataBase() {
        try {
            Class.forName(DB_DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            setException(String.format("Driver name '%s' not found", DB_DRIVER_NAME));
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

        boolean isNew = !isDataBaseExist();

        try {
            connection = DriverManager.getConnection(DB_URL + DB_FOLDER + DB_FILE_NAME);
        } catch (SQLException e) {
            setException(String.format("Connection failed: %s", e.getMessage()));
            return;
        }

        if(isNew)
            InitializeDataBase();

        UpdateDataBase();
    }

    private void InitializeDataBase() {
        if (hasException)
            return;

        System.out.println("Initializing empty database...");
        String query;

        // Create tables
        query = getQuery("sql/init.sql");
        try(Statement statement = connection.createStatement()) {
            statement.execute(query);
        }catch (SQLException e) {
            setException(String.format("SQL query failed: %s", e.getMessage()));
        }

        // Fill service tables
        query = "INSERT INTO _params(version) VALUES (?);";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, VERSION);
            statement.executeUpdate();
        }catch (SQLException e) {
            setException(String.format("SQL query failed: %s", e.getMessage()));
        }
    }

    // Service methods

    public boolean isDataBaseExist() {
        if(hasException)
            return true;

        File file = new File(DB_FOLDER+DB_FILE_NAME+".mv.db");
        return file.exists();
    }

    private int getDataBaseVersion() {
        if(hasException)
            return 0;

        String query = "SELECT version FROM _params";

        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            setException(String.format("Query failed: %s", e.getMessage()));
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

    // Data base updating

    private void UpdateDataBase() {
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

    // Exception handling

    public void setException(String message) {
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

    // Getters & setters

    public Connection getConnection() {
        return connection;
    }
}
