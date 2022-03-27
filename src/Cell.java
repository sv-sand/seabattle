public class Cell {
    private String name;
    private int x;
    private int y;
    private Boolean hit = false;
    private Boolean partOfShip = false;
    private Ship ship;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.name = getCellName(x+1, y+1);
    }

    // Interface

    public Boolean Hit(){
        hit = true;
        if(partOfShip) {
            ship.Hit();
            return true;
        }
        return false;
    }

    private String getCellName(int x, int y) {
        return getLetter(x-1) + String.valueOf(y);
    }

    // Getters & setters

    public String getName() {
        return name;
    }

    public Ship getShip() {
        return ship;
    }

    public Boolean setShip(Ship ship) {
        if(partOfShip)
            return false;

        partOfShip = true;
        this.ship = ship;

        return true;
    }

    public Boolean isHit() {
        return hit;
    }

    public Boolean isPartOfShip() {
        return partOfShip;
    }

    // Static helpful methods

    public static String getLetter(int index) {
        final String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.substring(index, index+1);
    }

}
