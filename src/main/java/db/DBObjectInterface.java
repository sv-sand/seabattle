package db;

public interface DBObjectInterface {

    public String getRepresentation();
    public boolean CheckFillErrors();

    // SQL data manipulation
    public void Create();
    public void Read(long id);
    public void Write();
    public void Delete();
}
