/**
 * @author  Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project SeaBattle
 * @created 08.04.2022
 */

package game;

public class Cell {
    private final String name;
    private final int x;
    private final int y;
    private boolean hit = false;
    private boolean partOfShip = false;
    private Ship ship;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.name = getCellName(x+1, y+1);
    }

    // Interface

    public boolean Hit(){
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

    public boolean setShip(Ship ship) {
        if(partOfShip)
            return false;

        partOfShip = true;
        this.ship = ship;

        return true;
    }

    public boolean isHit() {
        return hit;
    }

    public boolean isPartOfShip() {
        return partOfShip;
    }

    // Static helpful methods

    public static String getLetter(int index) {
        final String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.substring(index, index+1);
    }

}
