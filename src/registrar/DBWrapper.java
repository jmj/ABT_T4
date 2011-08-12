/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registrar;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author jjones
 */

/* Here we're going to try to compat the incredibly poor design requirements
 * that come from the task requirements for the task.  What we're going to do
 * here is include DBWrapper inst in Student.  DBWrapper will contain the 
 * actual implementations of add(), query(), delete(), and update().
 * 
 * This will be somewhat ineffecient as every Student subclass inst will
 * also contain a DBWrapper (thus DB connection), however is _should_ reduce
 * the amount of duplicated code
 */
public class DBWrapper {
    
    private Connection con;

    public DBWrapper() throws DatabaseConnectionException {
        /* make a DB con using defaults */
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/registrar",
                    "root", "");
        }
        catch( Exception e ) {
            throw new DatabaseConnectionException("Failed to connect to the database! Grrr!");
        }
    }
        
    public DBWrapper(String constring) throws DatabaseConnectionException {
        /* make a DB con using args */
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(constring,
                    "root", "");
        }
        catch (Exception e) {
            throw new DatabaseConnectionException("Failed to connect to the database");            
        }
    }
    
    
    public void add(Integer id, String type, String first, String last, Double gpa,
            String mentor, String... extra) throws DataBaseQueryException, SQLException {
        System.out.println("DBWrapper.add():extra.length: " + extra.length);
        if (extra.length < 1) {
            throw new DataBaseQueryException("Incorrect number of arguments");
        }
        
        PreparedStatement stmt = con.prepareStatement("INSERT INTO student ("
                + "studentID, firstName, lastName, gpa, "
                + "status, mentor, level, thesisTitle, thesisAdvisor, company) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        
        
        stmt.setInt(1, id);
        stmt.setString(2, first);
        stmt.setString(3, last);
        stmt.setFloat(4, gpa.floatValue());
        stmt.setString(5, type);
        stmt.setString(6, mentor);
        
        
        
        if ("Undergraduate".equals(type)) {
            stmt.setString(7, extra[0]);
            stmt.setString(8, "");
            stmt.setString(9, "");
            stmt.setString(10, "");
        }
        else if ("Graduate".equals(type)) {
            stmt.setString(7, "");
            stmt.setString(8, extra[0]);
            stmt.setString(9, extra[1]);
            stmt.setString(10, "");
        }
        else if ("PartTime".equals(type)) {
            stmt.setString(7, "");
            stmt.setString(8, "");
            stmt.setString(9, "");
            stmt.setString(10, extra[0]);
        }
        else {
            stmt.setString(7, "");
            stmt.setString(8, "");
            stmt.setString(9, "");
            stmt.setString(10, "");
        }        
        
        stmt.executeUpdate();
        System.out.println(first + " " + last + " added.");
        
    }
    
    public ArrayList<Student> queryByID(Integer id) throws SQLException, DataBaseQueryException {
        
        ArrayList<Student> students = new ArrayList<Student>();
        ResultSet r = null;
        
        PreparedStatement stmt = con.prepareStatement("SELECT studentID, firstName, lastName, "
                + "gpa, status, mentor, level, thesisTitle, thesisAdvisor, company "
                + "from student where studentID=?");
        
        stmt.setInt(1, id);
        
        try {
            r = stmt.executeQuery();
            students = makeStudentFromResult(r);
        }
        catch (Exception e) {
            throw new DataBaseQueryException("ID lookup failed");
        }
        
        return students;
    }
    
    public ArrayList<Student> queryByName(String name)
            throws SQLException, DataBaseQueryException {        
        
        ArrayList<Student> students = new ArrayList<Student>();
        ResultSet r = null;
        String[] names = new String[0];
        
        String q = "SELECT studentID, firstName, lastName, "
                + "gpa, status, mentor, level, thesisTitle, thesisAdvisor, company "
                + "from student";
        
        /* We should get either firstname lastname or ALL */
        if ("ALL".equals(name)) {
        }
        else {
            System.out.println("here");
            names = name.split(" ");
            if (names.length < 2) {
                throw new DataBaseQueryException("Incorrect name format");
            }
            q += " where firstName=? and lastName=?";
            
        }
        
        PreparedStatement stmt = con.prepareStatement(q);
        
        if (names.length > 0) {
            stmt.setString(1, names[0]);
            stmt.setString(2, names[1]);
        }
        
        try {
            r = stmt.executeQuery();
            students = makeStudentFromResult(r);
        }
        catch (Exception e) {
            throw new DataBaseQueryException("Name lookup failed");
        }
        
        return students;
        
    }
    
    public void update(Integer id, String type, String first, String last, Double gpa,
            String mentor, String... extra) {
        
        String q = "update student set "
                + "firstName=?, lastName=?, gpa=?, status=?, mentor=?, ";
        
        if ("Undergraduate".equals(type)) {
            q += "level=? ";
        }
        else if ("Graduate".equals(type)) {
            q += "thesisTitle=?, thesisAdvisor=? ";
            
        }
        else if ("PartTime".equals(type)) {
            q += "company=? ";
        }
        
        q += " where studentID=?";  
        
        try {
        
            PreparedStatement stmt = con.prepareStatement(q);
        
            stmt.setString(1, first);
            stmt.setString(2, last);
            stmt.setFloat(3, gpa.floatValue());
            stmt.setString(4, type);
            stmt.setString(5, mentor);        
        
            if ("Undergraduate".equals(type)) {
                stmt.setString(6, extra[0]);
                stmt.setInt(7, id);
            }
            else if ("Graduate".equals(type)) {
                stmt.setString(6, extra[0]);
                stmt.setString(7, extra[1]);
                stmt.setInt(8, id);
            }
            else if ("PartTime".equals(type)) {
                stmt.setString(6, extra[0]);
                stmt.setInt(7, id);
            }
            stmt.executeUpdate();
        }
        catch (Exception e) {
            
            System.out.println("Database update failed");
        }
    }
    
    public void delete(Integer id) {
        try {
            PreparedStatement stmt = con.prepareStatement("delete from student where "
                + "studentID=?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("Something went wrong while deleting student" +
                    id);
            return;
        }
    }
    
    private ArrayList<Student> makeStudentFromResult(ResultSet r) throws DataBaseQueryException, SQLException {        
        Student student = null;
        ArrayList<Student> students = new ArrayList<Student>();
        String status = new String();
        
        
        while (r.next()) {
            try{            
                status = r.getString("status");
            }
            catch (Exception e) {
                throw new DataBaseQueryException("status field missing or invalid");
            }
  
        
            if ("Undergraduate".equals(status)) {
                try {
                    student = new UnderGraduate(r.getString("level"));
                }
                catch (Exception e) {
                    throw new DataBaseQueryException("field missing in query results");
                }
            }
            else if ("Graduate".equals(status)) {
                try {
                    student = new Graduate(r.getString("thesisTitle"),
                        r.getString("thesisAdvisor"));    
                }
                catch (Exception e) {
                    throw new DataBaseQueryException("field missing in query results");
                }
            }
            else if ("PartTime".equals(status)) {
                try {
                    student = new PartTime(r.getString("company"));
                }
                catch (Exception e) {
                    throw new DataBaseQueryException("field missing in query results");
                }
            }
            else {
                throw new DataBaseQueryException("Unreconized data in status field");
            }
        
            try {
                student.setID(r.getInt("studentID"));
                student.setFName(r.getString("firstName"));
                student.setLName(r.getString("lastName"));
            
                // Silly hack to deal with differing precision between Float and Double
                Float foo = r.getFloat("gpa");
                student.setGPA(Double.parseDouble(foo.toString()));
                student.setStatus(r.getString("status"));
                student.setMentor(r.getString("mentor"));
            }
            catch (Exception e) {
                throw new DataBaseQueryException("Error in query data");
            }
            students.add(student);
        }
        
        return students;
    }
}
