/*
 * @author  Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project SeaBattle
 * @created 08.04.2022
 */

package tools;

import static java.lang.Math.min;

public class Tools {

    public static String FormatString(String str, int length) {
        String result;
        result = str.substring(0, min(length, str.length()));

        // Add padding
        while(result.length()<length)
            result += " ";

        return result;
    }
}
