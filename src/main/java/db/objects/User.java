package db.objects;

import db.DataBase;
import db.DBObjectInterface;
import db.DBObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class User extends DBObject implements DBObjectInterface {

    private final String OBJECT_NAME = "User";
    public String name="";

    public User(DataBase db) {
        super(db);
    }

    public String getRepresentation() {
        return name;
    }

    public boolean CheckFillErrors() {
        List<String> errors = new ArrayList<>();

        if(name.equals(""))
            errors.add("'Name' is empty.");

        PrintErrorList("Fill errors found:", errors);
        return !errors.isEmpty();
    }

    // SQL data manipulation

    public void Create() {
        if(CheckFillErrors()) {
            Exception(String.format("Failed to create %s '%s'. Fill errors found.", OBJECT_NAME, getRepresentation()));
            return;
        }

        String sql = "INSERT INTO users(name) VALUES (?);";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next())
                id = rs.getLong("id");
            else
                Exception("SQL insertion failed: Can't get new id.");
        } catch (SQLException e) {
            Exception(String.format("SQL insertion failed: %s", e.getMessage()));
        }
    }

    public void Read(long id) {
        if(id==0) {
            Exception(String.format("Failed to delete %s '%s'. ID is empty.", OBJECT_NAME, getRepresentation()));
            return;
        }

        String sql = "SELECT id, name FROM users WHERE id=?";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                this.id = rs.getLong("id");
                this.name = rs.getString("name");
            } else {
                Exception(String.format("SQL select failed: Can't find object with id=%s", id));
            }
        } catch (SQLException e) {
            Exception(String.format("SQL select failed: %s", e.getMessage()));
        }
    }

    public void Write() {
        if(id==0) {
            Exception(String.format("Failed to delete %s '%s'. ID is empty.", OBJECT_NAME, getRepresentation()));
            return;
        }
        if(CheckFillErrors()) {
            Exception(String.format("Failed to write %s '%s'. Fill errors found.", OBJECT_NAME, getRepresentation()));
            return;
        }

        String sql = "UPDATE users SET name=? WHERE id=?";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            Exception(String.format("SQL updating failed: %s", e.getMessage()));
        }
    }

    public void Delete() {
        if(id==0) {
            Exception(String.format("Failed to delete %s '%s'. ID is empty.", OBJECT_NAME, getRepresentation()));
            return;
        }

        String sql = "DELETE FROM users WHERE id=?;";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            Exception(String.format("SQL deleting failed: %s", e.getMessage()));
        }
    }

    // Object list

}
