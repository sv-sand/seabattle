import tools.Rand;

public class BattleField {
    private Cell[][] field;
    private int width;
    private int height;
    private int shipCellsCount=0;

    BattleField(int width, int height) {
        this.width = width;
        this.height = height;

        // Initialize cells
        field = new Cell[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                field[x][y] = new Cell(x, y);
    }

    // Interface

    public Cell findCell(String name) {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if(field[x][y].getName().equals(name.toUpperCase()))
                    return field[x][y];
        return null;
    }

    public void printField(Boolean showShips) {

        // Print X-axis
        System.out.print("  ");
        for (int x = 0; x < height; x++)
            System.out.printf(" %s ", Cell.getLetter(x));

        System.out.print("\n");

        // Print filler
        System.out.print("  ");
        for (int x = 0; x < height; x++)
            System.out.print("---");

        System.out.print("\n");

        // Print Y-axis
        for (int y = 0; y < height; y++) {
            System.out.print(y+1);
            System.out.print("|");

            // Print content
            for(int x=0; x<width; x++) {
                Cell cell = field[x][y];
                if(cell.isPartOfShip()) {
                    if(cell.isHit())
                        System.out.print(" X ");
                    else if(showShips)
                        System.out.printf(" %s ", cell.getShip().getName());
                    else
                        System.out.print("   ");
                }
                else {
                    if(cell.isHit())
                        System.out.print(" * ");
                    else
                        System.out.print("   ");
                }

            }
            System.out.print("|\n");
        }

        // Print filler
        System.out.print("  ");
        for (int x = 0; x < height; x++)
            System.out.print("---");

        System.out.print("\n");
    }

    public void setShips(int count, int maxSize) {
        int shipNumber = 1;
        int errCount=0;

        while(shipNumber <= count) {
            Boolean isVertical = Rand.getBool();
            int size = Rand.getInt(1, maxSize) ;
            Ship ship = new Ship(String.valueOf(shipNumber), size);

            if(!setShip(isVertical, ship, size)) {
                // Something went wrong
                if(errCount>100) {
                    System.out.printf("Failed to set ship number %d", shipNumber);
                    return;
                }
                errCount++;
                continue;
            }

            errCount = 0;
            shipNumber++;
            shipCellsCount += size;
        }
    }

    public Boolean hasShips() {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                Cell cell = field[x][y];
                if(cell.isPartOfShip() & !cell.isHit())
                    return true;
            }
        return false;
    }

    // Inner methods

    private Boolean setShip(Boolean isVertical, Ship ship, int size) {
        if(isVertical)
            return setVerticalShip(ship, size);
        else
            return setHorizontalShip(ship, size);
    }

    private Boolean setVerticalShip(Ship ship, int size) {
        int x = Rand.getInt(0, width - 1);
        int y = Rand.getInt(0, height - size - 1);

        // Select cells
        Cell[] cells = new Cell[size];
        for(int cellNumber=0; cellNumber<size; cellNumber++)
            cells[cellNumber] = field[x][y+cellNumber];

        // Check of empty cells
        for(Cell cell:cells)
            if(cell.isPartOfShip())
                return false;

        // Set ship on sells
        for(Cell cell:cells)
            cell.setShip(ship);

        return true;
    }

    private Boolean setHorizontalShip(Ship ship, int size) {
        int x = Rand.getInt(0, width - size - 1);
        int y = Rand.getInt(0, height - 1);

        // Select cells
        Cell[] cells = new Cell[size];
        for(int cellNumber=0; cellNumber<size; cellNumber++)
            cells[cellNumber] = field[x+cellNumber][y];

        // Check of empty cells
        for(Cell cell:cells)
            if(cell.isPartOfShip())
                return false;

        // Set ship on sells
        for(Cell cell:cells)
            cell.setShip(ship);

        return true;
    }

}
