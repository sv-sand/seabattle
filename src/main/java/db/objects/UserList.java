package db.objects;

import db.DBObjectList;
import db.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserList  extends DBObjectList {
    public DataBase db;

    public UserList(DataBase db) {
        this.db = db;
    }

    public User Create() {
        User object = new User(db);
        return object;
    }

    public User FindById(long id) {
        User object = new User(db);

        if(id==0)
            return object;

        String sql = "SELECT id, name FROM users WHERE id=?";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                object.id = rs.getLong("id");
                object.name = rs.getString("name");
            }
        } catch (SQLException e) {
            Exception(String.format("SQL select failed: %s", e.getMessage()));
        }
        return object;
    }

    public User FindByName(String name) {
        User object = new User(db);

        if(name.isEmpty())
            return object;

        String sql = "SELECT id, name FROM users WHERE name=?";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                object.id = rs.getLong("id");
                object.name = rs.getString("name");
            }
        } catch (SQLException e) {
            Exception(String.format("SQL select failed: %s", e.getMessage()));
        }
        return object;
    }

}
