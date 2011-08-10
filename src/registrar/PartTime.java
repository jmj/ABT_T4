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
public class PartTime extends Student {
    private String Company;
    
    
    
    public PartTime() throws DatabaseConnectionException {
        /* Java requires that superclass constructor is call as first line of
         * conscructor - Yet another reason Java is pile of ****
         */
        /* Init super w/ empty values */
        super("", "", 0, "", 0.0, "PartTime");
        Company = "";
    }
    public PartTime(String first, String last, Integer id,
            String mentor, Double gpa, String status) throws DatabaseConnectionException {
        super(first, last, id, mentor, gpa, status);
        Company = "";
    }
    public PartTime (String first, String last, Integer id,
            String mentor, Double gpa, String status, String company) throws DatabaseConnectionException {
        super(first, last, id, mentor, gpa, status);
        Company = company;
    }
    public PartTime(String company) throws DatabaseConnectionException {
        super("", "", 0, "", 0.0, "PartTime");
        Company = company;
    }
    
    /* getters/setters, grrrrr */
    public void setCompany(String company) {
        Company = company;
    }
    public String getCompany() {
        return Company;
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nSponsoring Company: " + Company;
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
                System.out.print("Sponsoring Company: ");
                data = input.readLine();
                
                setCompany(data);
                DB.add(getID(), getStatus(), getFName(), getLName(), getGPA(),
                        getMentor(), Company);
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
