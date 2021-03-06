/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registrar;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
    void update() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String data = new String();
        
        
        System.out.println("Committing to database:");
        System.out.println(toString());
        System.out.print("Are you sure [N/y]: ");
        
        try {
            data = input.readLine();
            if ("y".equals(data) || "Y".equals(data)) {
                DB.update(getID(), getStatus(), getFName(), getLName(), getGPA(),
                        getMentor(), Company);
            }
        }
        catch (Exception e) {
            System.out.println("An update errror occured");
            return;
        }  
        
    }
    @Override
    void query() {
        /* This isn't actually used */
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
        DB.delete(super.getID());
        
    }
    
    @Override
    Integer calculateTuition(Integer creditHours, Integer residency){
        Integer multiplyer = 0;
        Integer fees = 0;
        
        if (residency == Student.RESIDENT) {
            multiplyer = 250;
        }
        else if (residency == Student.NONRESIDENT) {
            multiplyer = 450;
        }
        
        return creditHours * multiplyer;
    }
}
