/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registrar;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
        
        System.out.println(stmt.toString());
        
        
        stmt.executeUpdate();
        System.out.println(first + " " + last + " added.");
        
    }
    
    public String[] query(String querystr) {
        String[] foo = new String[10];
        
        return foo;
    }
    
    public void update(String querystr) {
        
    }
    
    public void delete(String querystr) {
        
    }    
}
