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
public class Graduate extends Student{
    private String thesisTitle;
    private String thesisAdvisor;
    
    public Graduate() throws DatabaseConnectionException {
        /* Java requires that superclass constructor is call as first line of
         * conscructor - Yet another reason Java is pile of ****
         */
        /* Init super w/ empty values */
        super("", "", 0, "", 0.0, "Graduate");
        thesisTitle = "";
        thesisAdvisor = "";
    }
    public Graduate(String first, String last, Integer id,
            String mentor, Double gpa, String status) throws DatabaseConnectionException {
        super(first, last, id, mentor, gpa, status);
        thesisTitle = "";
        thesisAdvisor = "";
    }
    public Graduate (String first, String last, Integer id,
            String mentor, Double gpa, String status, String title, String advisor) throws DatabaseConnectionException {
        super(first, last, id, mentor, gpa, status);
        thesisTitle = title;
        thesisAdvisor = advisor;
    }
    public Graduate (String title, String advisor) throws DatabaseConnectionException {
        super("", "", 0, "", 0.0, "Graduate");
        thesisTitle = title;
        thesisAdvisor = advisor;
    }
    
    
    /* setters/getter, sigh */
    public void setThesisAdvisor(String advisor) {
        thesisAdvisor = advisor;
    }
    public void setThesisTitle(String title) {
        thesisTitle = title;
    }
    public String getThesisAdvisor() {
        return thesisAdvisor;
    }
    public String getThesisTitle() {
        return thesisTitle;
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
                System.out.print("Thesis Title: ");
                data = input.readLine();
                setThesisTitle(data);
                System.out.print("Thesis Advisor: ");
                data = input.readLine();
                setThesisAdvisor(data);
                DB.add(getID(), getStatus(), getFName(), getLName(), getGPA(),
                        getMentor(), thesisTitle, thesisAdvisor);
                return;
                        
            }
            catch (Exception e) {
                System.out.println("An error occured, please try again");
                continue;
            }
        }        
        
    }
    @Override
    void delete() {
        
    }
}
