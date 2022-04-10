/**
 * @author  Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project SeaBattle
 * @created 08.04.2022
 */

package db.objects;

import db.DataBase;
import db.DBObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Score extends DBObject {

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

    public void Write() {
        if(CheckFillErrors()) {
            Exception(String.format("Failed to write score '%s'. Fill errors found.", getRepresentation()));
            return;
        }

        if(isEmpty())
            Create();
        else
            Save();
    }

    private void Create() {
        String sql = "INSERT INTO scores(user_id, date, score) VALUES (?,?,?);";
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

    private void Save() {
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
            Exception(String.format("Failed to delete score '%s'. ID is empty.", getRepresentation()));
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


