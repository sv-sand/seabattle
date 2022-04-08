package db;

public interface DBObjectInterface {

    public String getRepresentation();
    public boolean CheckFillErrors();

    // SQL data manipulation
    public void Write();
    public void Delete();
}
