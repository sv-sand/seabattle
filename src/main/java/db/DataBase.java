package db;

import db.objects.ScoreList;
import db.objects.UserList;

public class DataBase extends DataBaseImpl{

    private UserList userList = new UserList(this);
    private ScoreList scoreList = new ScoreList(this);

    // Getters & setters

    public UserList getUserList() {
        return userList;
    }

    public ScoreList getScoreList() {
        return scoreList;
    }
}
