import db.DBObject;
import db.objects.Score;
import db.objects.UserList;
import game.BattleField;
import game.Cell;
import game.Ship;
import db.DataBase;
import db.objects.User;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.min;

public class SeaBattle {

    public static void main(String[] args) {

        System.out.println("Welcome to The Sea battle game.");
        System.out.println("For exit write 'exit'");
        System.out.println("To see the map write 'map'");

        game.BattleField battleField;
        battleField = new game.BattleField(9, 9);
        battleField.setShips(5, 5);

        startGame(battleField);

        DataBase db = new DataBase();
        db.Connect();
        AddScore(db, battleField.getNoHitCells());
        ShowScoreRating(db);
        db.Disconnect();

        endGame();
    }

    private static void startGame(BattleField battleField) {
        System.out.println("The game has started");

        boolean showShips = false;
        Scanner scanner = new Scanner(System.in);
        while(battleField.hasShips()) {
            battleField.printField(showShips);

            System.out.print("Input cell name: ");
            String cellName = scanner.nextLine();

            if(cellName.equals("exit")) {
                return;
            }
            if(cellName.equals("map")) {
                showShips = true;
                continue;
            }

            Cell cell = battleField.findCell(cellName);
            if(cell==null) {
                System.out.printf("Can't find cell %s\n", cellName);
                continue;
            }

            if(cell.isHit()) {
                System.out.printf("You have shot at cell %s already\n", cellName);
                continue;
            }

            if(!cell.Hit()) {
                System.out.println("You missed");
                continue;
            }

            Ship ship = cell.getShip();
            if(ship.isDestroy())
                System.out.printf("You destroy the ship '%s'!\n", ship.getName());
            else
                System.out.printf("Hit the ship '%s'\n", ship.getName());
        }

        System.out.println("Congratulations! You win!");
    }

    private static void endGame() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Press Enter...");
        scanner.nextLine();
    }

    private static void AddScore(DataBase db, int scoreCount){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        // Create user
        User user = new User(db);
        user.name = name;
        user.Create();

        // Create score
        Score score = new Score(db);
        score.user = user;
        score.date = new Date();
        score.scoreCount = scoreCount;
        score.Create();
    }

    private static void ShowScoreRating(DataBase db) {
        List<Score> list = UserList.getTop5List(db);
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        System.out.println("Top 5 scores list:");
        System.out.println(" -----------------------------------------------");
        System.out.println("| PLACE | NAME            | SCORE  | DATE       |");

        for (Score score: list) {
            String place = String.valueOf(list.indexOf(score) + 1);
            String name = score.user.name;
            String scoreCount = String.valueOf(score.scoreCount);
            String date = df.format(score.date);

            place = FormatString(place, 5);
            name = FormatString(name, 15);
            scoreCount = FormatString(scoreCount, 6);
            date = FormatString(date, 10);

            System.out.printf("| %s | %s | %s | %s |\n", place, name, scoreCount, date);
        }

        System.out.println(" -----------------------------------------------");
    }

    private static String FormatString(String str, int length) {
        String result;
        result = str.substring(0, min(length, str.length()));

        // Add padding
        while(result.length()<length)
            result += " ";

        return result;
    }
}
