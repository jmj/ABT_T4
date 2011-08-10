/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registrar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author jjones
 */
public class UnderGraduate extends Student{
    
    private String Level;
    
    
    
    public UnderGraduate() throws DatabaseConnectionException {
        /* Java requires that superclass constructor is call as first line of
         * conscructor - Yet another reason Java is pile of ****
         */
        /* Init super w/ empty values */
        super("", "", 0, "", 0.0, "Undergraduate");
        Level = "";
    }
    public UnderGraduate(String first, String last, Integer id,
            String mentor, Double gpa, String status) throws DatabaseConnectionException {
        super(first, last, id, mentor, gpa, status);
        Level = "";
    }
    public UnderGraduate (String first, String last, Integer id,
            String mentor, Double gpa, String status, String level) throws DatabaseConnectionException {
        super(first, last, id, mentor, gpa, status);
        Level = level;
    }
    public UnderGraduate(String level) throws DatabaseConnectionException {
        super("", "", 0, "", 0.0, "Undergraduate");
        Level = level;
    }
    
    /* getters/setters, I'm about tired of these */
    public void setLevel(String level) {
        Level = level;
    }
    public String getLevel() {
        return Level;
    }
    
    
    
    /* abstract methods */
    @Override
    Double calculateTuition() {
        return 12345.0;
        
    }
    @Override
    void update() {
        
    }
    @Override
    void query() {
        /* WHY!!!!  All 3 query() cases are identical.  This NEEDS to be in 
         * Student, not implemented in each subclass.  Why are we being required
         * to write crap software?
         */
        ArrayList<Student> students = null;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String data = new String();
        
        try {
            System.out.println("Enter a Student name (First Last), Student ID or the word  ALL");
            data = input.readLine();
            try {
                Integer sid = Integer.parseInt(data);
                students = DB.queryByID(sid);
            }
            
            catch (Exception e) {
                students = DB.queryByName(data);
            }    
        }
        catch (Exception e) {
            System.out.println("An error occured. Return  to main menu.");
            return;
        }
        
    }
    @Override
    void add() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String data = new String();
        
        while(true) {
            try {
                getData();
                System.out.print("Grade Level (senior/junior/sophmore/freshman: ");
                data = input.readLine();
                if (!"freshman".equals(data) && !"sophmore".equals(data) &&
                        !"junior".equals(data) && !"senior".equals(data)) {
                    System.out.println("I didn't reconize the grade level");
                    continue;
                }
                setLevel(data);
                
                DB.add(getID(), getStatus(), getFName(), getLName(), getGPA(),
                        getMentor(), Level);
                return;
                        
            }
            catch (Exception e) {
                System.out.println("An error occured, please try again");
                //e.printStackTrace();
                continue;
            }
        }        
    }
    @Override
    void delete() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String data = new String();
        
        System.out.println("Please note, deletes require a student ID for safety reasons");
        System.out.println("Type L to switch to the lookup function to find a Student's ID");
        try {
            System.out.print("Student ID: ");
            data = input.readLine();
            if ("L".equals(data) || "l".equals(data)) {
                query();
                return;
                
            }
        }
        catch (Exception e) {
            System.out.println("An error occured, returnning to main menu");
            return;
        }
        
        
        
        
    }
    
}
