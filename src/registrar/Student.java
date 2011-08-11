/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registrar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jjones
 */
abstract class Student {
    // Student class holds the attributes first name, last name, student ID, GPA, status 
    // (resident or nonresident), and mentor
    
    private String FName;
    private String LName;
    private Integer ID;
    private Double GPA;
    private String Status;
    private String Mentor;
    
    public DBWrapper DB = null;
    
    public Student(String first, String last, Integer id,
            String mentor, Double gpa, String status) throws DatabaseConnectionException {
        FName = first;
        LName = last;
        ID = id;
        GPA = gpa;
        Status = status;
        Mentor = mentor;
        
        DB = new DBWrapper();
        
    }
    public Student() throws DatabaseConnectionException {
        FName = "";
        LName = "";
        ID = 0;
        GPA = 0.0;
        Status = "";
        Mentor = "";
        
        DB = new DBWrapper();
                
    }
    
    /* Can some one explain why we are _directed_ to use an anti-pattern
     * in the same course as one in which we need to demonstrate our
     * knowledge of design patterns
     */
    
    /* Evil, wrong, setter/getter block */
    public void setFName(String name) {
        FName = name;
    }
    public void setLName(String name) {
        LName = name;
    }
    public void setID(Integer id) {
        ID = id;
    }
    public void setGPA(Double gpa) {
        GPA = gpa;
    }
    public void setStatus(String status) {
        Status = status;
    }
    public void setMentor(String mentor){
        Mentor = mentor;
    }
    public String getFName() {
        return FName;
    }
    public String getLName() {
        return LName;
    }
    public Integer getID() {
        return ID;
    }
    public Double getGPA() {
        return GPA;
    }
    public String getStatus() {
        return Status;
    }
    public String getMentor() {
        return Mentor;
    }
    
    @Override
    public String toString() {
        return "Name: " + FName + " " + LName + "\nStudent ID: " + ID + "\nMentor: "
                + Mentor + "\nStatus: " + Status
                + "\nGPA: " + GPA.toString();
        
        
    }
    
    /* more anti-patterns; All of these share 99% of thier code, why are they 
     * abstract?  They should be implemented in Student w/ special case 
     * handeling for the extra fields required for various sub-classes
     * 
     * The Task description says this is about modular design, but the
     * task requirements make the final applicaion non-modular, and poorly
     * designed
     */
    abstract Double calculateTuition();
    abstract void update();
    abstract void query();
    abstract void add();
    abstract void delete();
    
    public void getData() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String data = new String();
        
        System.out.print("First Name: ");
        data = input.readLine();
        setFName(data);
        System.out.print("Last Name: ");
        data = input.readLine();
        setLName(data);
        System.out.print("Student ID: ");
        data = input.readLine();
        setID(Integer.parseInt(data));
        System.out.print("Student's Mentor: ");
        data = input.readLine();
        setMentor(data);
        System.out.print("GPA: ");
        data = input.readLine();
        setGPA(Double.parseDouble(data));
    }
}


