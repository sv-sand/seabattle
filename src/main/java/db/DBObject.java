package db;

import java.util.List;

public class DBObject {
    protected final DataBase db;

    public long id;

    public DBObject(DataBase dataBase) {
        this.db = dataBase;
    }

    protected boolean CheckFillId() {
        boolean result = true;

        if(id==0) {
            Exception("Fill errors found:");
            Exception("   'Id' is empty.");
            result = false;
        }
        return result;
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
}


