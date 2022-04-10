/*
 * @author  Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project SeaBattle
 * @created 08.04.2022
 */

package tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandTest {

    @Test
    void getInt() {
        assertEquals(0, Rand.getInt(0,0));
        assertEquals(5, Rand.getInt(5,5));

        for(int counter=1; counter<100; counter++)
            getIntTest(2, 5);
    }

    void getIntTest(int min, int max) {
        int actual = Rand.getInt(min,max);
        if(actual<min | max<actual)
            fail(String.format("Result %s is failed for arguments min=%d max=%d", actual, min, max));
    }
}