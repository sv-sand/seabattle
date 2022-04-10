/*
 * @author  Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project SeaBattle
 * @created 08.04.2022
 */

package game;

public class Ship {
    private final String name;
    private final int size;
    private int hitCount = 0;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
    }

    // Interface

    public void Hit() {
        if(hitCount<size)
            hitCount++;
    }

    public boolean isDestroy() {
        return size == hitCount;
    }

    // Getters & setters

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
