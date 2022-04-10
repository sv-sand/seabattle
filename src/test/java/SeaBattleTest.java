/*
 * @author  Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project SeaBattle
 * @created 08.04.2022
 */

import db.DataBase;
import db.objects.User;
import game.BattleField;
import game.Cell;
import org.junit.jupiter.api.Test;

class SeaBattleTest {

    DataBase db;
    BattleField battleField;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        db = new DataBase();
        db.Connect();

        battleField = new game.BattleField(5, 8);
        battleField.setShips(4, 4);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        db.Disconnect();
    }

    @Test
    void gameTest() {
        cellSelect(battleField, "a1");
        cellSelect(battleField, "a2");
        cellSelect(battleField, "b6");
        cellSelect(battleField, "c6");
        cellSelect(battleField, "c1");
        cellSelect(battleField, "d7");
        cellSelect(battleField, "e8");
        cellSelect(battleField, "e7");
        cellSelect(battleField, "a8");
        cellSelect(battleField, "a8");
    }

    void cellSelect(BattleField battleField, String name) {
        Cell cell = battleField.findCell(name);
        cell.Hit();
        battleField.printField(true);
    }

    @Test
    void AddNewScore() {
        SeaBattle.AddScore(db, "test", 0);
        SeaBattle.ShowScoreRating(db);

        SeaBattle.AddScore(db, "test", battleField.getNoHitCells());
        SeaBattle.ShowScoreRating(db);

        SeaBattle.AddScore(db, "test1", 100);
        SeaBattle.ShowScoreRating(db);

        User user;
        user = db.getUserList().FindByName("test");
        db.getScoreList().DeleteScoreOfUser(user);
        user.Delete();

        user = db.getUserList().FindByName("test1");
        db.getScoreList().DeleteScoreOfUser(user);
        user.Delete();

        SeaBattle.ShowScoreRating(db);
    }
}