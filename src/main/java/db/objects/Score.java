package db.objects;

import db.DBObjectList;
import db.DataBase;
import db.DBObjectInterface;
import db.DBObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Score extends DBObject implements DBObjectInterface {

    private final String OBJECT_NAME = "Score";

    public User user;
    public Date date;
    public int scoreCount;

    public Score(DataBase db) {
        super(db);
    }

    // Methods

    public String getRepresentation() {
        return "";
    }

    public boolean CheckFillErrors() {
        List<String> errors = new ArrayList<>();

        if(user==null)
            errors.add("'User' is empty.");

        if(date==null)
            errors.add("'Date' is empty.");

        PrintErrorList("Fill errors found:", errors);
        return !errors.isEmpty();
    }

    // SQL data manipulation

    public void Create() {
        if(CheckFillErrors()) {
            Exception(String.format("Failed to create %s '%s'. Fill errors found.", OBJECT_NAME, getRepresentation()));
            return;
        }

        String sql = "INSERT INTO scores(user_id, date, score) VALUES (?, ?, ?);";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, user.id);
            statement.setDate(2, new java.sql.Date(date.getTime()));
            statement.setInt(3, scoreCount);
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

        String sql = "SELECT id, user_id, date, score FROM scores WHERE id=?";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                this.id = rs.getLong("id");
                this.date = new Date(rs.getDate("date").getTime());
                this.scoreCount = rs.getInt("score");

                this.user = new User(db);
                this.user.Read((int) rs.getLong("user_id"));

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

        String sql = "UPDATE scores SET user_id=?, date=?, score=? WHERE id=?";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setLong(1, user.id);
            statement.setDate(2, new java.sql.Date(date.getTime()));
            statement.setInt(3, scoreCount);
            statement.setLong(4, id);
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

        String sql = "DELETE FROM scores WHERE id=?;";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            Exception(String.format("SQL deleting failed: %s", e.getMessage()));
        }
    }
}


