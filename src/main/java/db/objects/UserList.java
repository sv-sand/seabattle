package db.objects;

import db.DBObjectList;
import db.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserList extends DBObjectList {
    public static List<Score> getTop5List(DataBase db) {
        List<Score> list = new ArrayList<>();

        String sql = "SELECT TOP(5) id FROM scores ORDER BY score DESC";
        try(Statement statement =  db.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                Score score = new Score(db);
                score.Read(rs.getLong("id"));

                list.add(score);
            }
        } catch (SQLException e) {
            Exception(String.format("SQL list select failed: %s", e.getMessage()));
        }
        return list;
    }
}
