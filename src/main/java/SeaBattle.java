import model.DataBase;

import java.util.Scanner;

public class SeaBattle {

    public static void main(String[] args) {

        DataBase db = new DataBase();
        db.Connect();
        db.InitializeDataBase();
        db.Disconnect();


        /*
        System.out.println("Welcome to The Sea battle game.");
        System.out.println("For exit write 'exit'\n");
        System.out.println("To see the map write 'map'\n");

        BattleField battleField;
        battleField = new BattleField(9, 9);
        battleField.setShips(5, 5);

        startGame(battleField);
        endGame();
        */
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


}
