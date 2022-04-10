/**
 * @author  Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project SeaBattle
 * @created 08.04.2022
 */

package db;

import java.util.List;

public abstract class DBObject {
    protected final DataBase db;
    public long id;

    public DBObject(DataBase dataBase) {
        this.db = dataBase;
    }

    public boolean isEmpty() {
        return id==0;
    }

    protected void PrintErrorList(String header, List<String> errors) {
        if(errors.isEmpty())
            return;

        Exception(header);

        for (String msg: errors) {
            Exception("   " + msg);
        }
    }

    protected void Exception(String message) {
        System.out.println(message);
    }

    // Implementation in child class
    public abstract String getRepresentation();
    public abstract boolean CheckFillErrors();

    // SQL data manipulation
    public abstract void Write();
    public abstract void Delete();

}


