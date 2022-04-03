package tools;

public class Rand {

    public static int getInt(int min, int max) {
        double rnd =  Math.random();
        int range = max - min + 1;
        int rndDelta = (int) (rnd * range);
        return min + rndDelta;
    }

    public static Boolean getBool() {
        return getInt(0, 2) >= 1;
    }
}
