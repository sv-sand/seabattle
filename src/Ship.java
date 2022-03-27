public class Ship {
    private String name;
    private int size;
    private int hitCount = 0;

    Ship(String name, int size) {
        this.name = name;
        this.size = size;
    }

    // Interface

    public void Hit() {
        if(hitCount<size)
            hitCount++;
    }

    public Boolean isDestroy() {
        return size == hitCount;
    }

    //
    // Getters & setters
    //

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
