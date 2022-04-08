package db.objects;

import db.DBObjectList;
import db.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoreList extends DBObjectList {
    public DataBase db;

    public ScoreList(DataBase db) {
        this.db = db;
    }

    public Score Create() {
        Score object = new Score(db);
        return object;
    }

    public Score FindById(long id) {
        Score object = new Score(db);

        if(id==0)
            return object;

        String sql = "SELECT id, user_id, date, score FROM scores WHERE id=?";
        try(PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                object.id = rs.getLong("id");
                object.date = new Date(rs.getDate("date").getTime());
                object.scoreCount = rs.getInt("score");
                object.user = db.getUserList().FindById(
                        rs.getLong("user_id")
                );
            }
        } catch (SQLException e) {
            Exception(String.format("SQL select failed: %s", e.getMessage()));
        }
        return object;
    }

    public List<Score> getTop5List() {
        List<Score> list = new ArrayList<>();

        String sql = "SELECT TOP(5) id FROM scores ORDER BY score DESC";
        try(Statement statement =  db.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                Score score = db.getScoreList().FindById(
                        rs.getLong("id")
                );

                list.add(score);
            }
        } catch (SQLException e) {
            Exception(String.format("SQL list select failed: %s", e.getMessage()));
        }
        return list;
    }
}
