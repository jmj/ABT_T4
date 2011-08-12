/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registrar;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author jjones
 */
public class Registrar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        /* Use console */
        Console console = System.console();
        Boolean doquit = false;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.print("Welcome to the student registrar service.\n");
        System.out.println("Type help for help\n");
        while (!doquit) {
            System.out.print("> ");
            String op = input.readLine();
            
            
            /* arh!  My version of netbeans doesn't do java7 yet.  Fall back to 
             * if-elsif-...-else
             */
             if ("help".indexOf(op) == 0) { printhelp(); }
             else if ("quit".indexOf(op) == 0) { doquit = true; }
             else if ("add".indexOf(op) == 0) { add(); }
             else if ("delete".indexOf(op) == 0) { delete(); }
             else if ("lookup".indexOf(op) == 0) { lookup(); }
             else if ("update".indexOf(op) == 0) { update(); }
             else if ("tuition".indexOf(op) == 0) { tuition(); }
             else if (op.indexOf("help") >= 0) { commandhelp(op); }
             else { System.out.println("Unreconized command"); }
        }
        
    }
    
    private static void printhelp() {
        System.out.println("Student Registrar System");
        System.out.print("Commands may be apreviated.  Type help <command>");
        System.out.print("for command specific help.  Commands are:\n");
        System.out.println("lookup\t\tlook up a student");
        System.out.println("add\t\tAdd a student to the database");
        System.out.println("delete\t\tRemove a student from the database");
        System.out.println("update\t\tUpdate a students information");
        System.out.println("quit\t\tExit the application");
        
    }
    
    private static void commandhelp(String op) {
        String[] command = op.split(" ");
        if ("add".equals(command[1])) {
            System.out.println("help add");
        }
        else if ("delete".equals(command[1])) {
            System.out.println("help delete");
        }
        else if ("update".equals(command[1])) {
            System.out.println("help update");
        }
        else if ("lookup".equals(command[1])) {
            System.out.println("help lookup");
        }
        else {
            System.out.println("Unknown command");
        }
    }
    
    private static void add() throws Exception {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String data = new String();
        Student srec = null; /* needed here for scoping reasins */
        
        while (true) {
            try {
                System.out.print("Student Type (Undergran/Grad/PartTime): ");
                data = input.readLine();
                if (data.equals("Undergrad")) {
                    srec = new UnderGraduate();
                }
                else if (data.equals("Grad")) {
                    srec = new Graduate();
                }
                else if (data.equals("PartTime")) {
                    srec = new PartTime();
                }
                else {
                    System.out.println("I didn't reconize your input.  Try again");
                    System.out.println(">>" + data + "<<");
                    continue;
                }
            }
            catch (Exception e) {
                System.out.println("Ane error occured reading input.  Try Again");
                throw e;
                //continue;
            }
            
            srec.add();
            return;
            
        }
        
    }
    
    private static void delete() throws DatabaseConnectionException, SQLException, DataBaseQueryException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String data = new String();
        Integer sid;
        
        System.out.println("Please note, deletes require a student ID for safety reasons");
        System.out.println("Type L to switch to the lookup function to find a Student's ID");
        try {
            System.out.print("Student ID: ");
            data = input.readLine();
            if ("L".equals(data) || "l".equals(data)) {
                lookup();
                return;
            }
        sid = Integer.parseInt(data);
        }
        catch (Exception e) {
            System.out.println("An error occured, returnning to main menu");
            return;
        }
        
        
        DBWrapper DB = new DBWrapper();
        ArrayList<Student> students = new ArrayList<Student>();
        students = DB.queryByID(sid);

        if (students.size() > 1) {
            /* Whoa!!  This should never happen */
            System.out.println("Umm, I think there's a problem.  "
                    + "A query by ID returned more than 1 result");
            return;
        }
        
        Student student = students.get(0);
        System.out.println("We will be removing the following student:");
        System.out.println(student.toString());
        System.out.print("Are you sure? [N/y] ");
        
        try {
            data = input.readLine();
            if ("Y".equals(data) || "y".equals(data)) {
                student.delete();
            }
        }
        catch (Exception e) {
            System.out.println("An error occured, returnning to main menu");
            return;
        }
    }
    
    private static void lookup() throws DatabaseConnectionException {
        
        DBWrapper DB = new DBWrapper();
        ArrayList<Student> students = new ArrayList<Student>();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String data = new String();
        
        try {
            System.out.print("Enter a Student name (First Last), Student ID or the word ALL: ");
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
        
        System.out.println("----------------");
        for (Student student : students) {
            System.out.println(student.toString());
            System.out.println("----------------");
        }
        
        
        
        
    }
    
    private static void update() throws DatabaseConnectionException, SQLException,
            DataBaseQueryException {
        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String data = new String();
        Integer sid;
        Integer swvar;
        Boolean doCommit = false;
        
        System.out.println("Please note, updates require a student ID for safety reasons");
        System.out.println("Type L to switch to the lookup function to find a Student's ID");
        try {
            System.out.print("Student ID: ");
            data = input.readLine();
            if ("L".equals(data) || "l".equals(data)) {
                lookup();
                return;
            }
        sid = Integer.parseInt(data);
        }
        catch (Exception e) {
            System.out.println("An error occured, returnning to main menu");
            return;
        }
        
        DBWrapper DB = new DBWrapper();
        ArrayList<Student> students = new ArrayList<Student>();
        students = DB.queryByID(sid);

        if (students.size() > 1) {
            /* Whoa!!  This should never happen */
            System.out.println("Umm, I think there's a problem.  "
                    + "A query by ID returned more than 1 result");
            return;
        }
        
        Student student = students.get(0);
        
        while (! doCommit) {
            
            System.out.println("Select the field number to edit, or type COMMIT to finish");
            System.out.println("(1) First Name: " + student.getFName());
            System.out.println("(2) Last Name: " + student.getLName());
            System.out.println("(3) Mentor: " + student.getMentor());
            String status = student.getStatus();
            System.out.println("(4) Status: " + status);
            System.out.println("(5) GPA: " + student.getGPA().toString());
            
            if ("Undergraduate".equals(status)) {
                System.out.println("(6)  Gade Level: " + ((UnderGraduate)student).getLevel());
            }
            else if ("Graduate".equals(status)) {
                System.out.println("(7) Thesis Title: " + ((Graduate)student).getThesisTitle());
                System.out.println("(8) Thesis Advisor: " + ((Graduate)student).getThesisAdvisor());
            }
            else if ("PartTime".equals(status)) {
                System.out.println("(9) Sponsoring Company: " + ((PartTime)student).getCompany());
            }
            else {
                System.out.println("I didn't reconize your input");
                continue;
            }
            
            try {
                System.out.print("Field: ");
                data = input.readLine();
                if ("COMMIT".equals(data)) {
                    student.update();
                    return;
                }
            
                swvar = Integer.parseInt(data);
                System.out.print("New value: ");
                data = input.readLine();
            
            }
            catch (Exception e) {
                System.out.println("I didn't reconize your input");
                continue;
            }
            
            
            switch (swvar) {
                case 1: student.setFName(data);                     break;
                case 2: student.setLName(data);                     break;
                case 3: student.setMentor(data);                    break;
                case 4: student.setStatus(data);                    break;
                case 5: student.setGPA(Double.parseDouble(data));   break;
                case 6: ((UnderGraduate)student).setLevel(data);    break;
                case 7: ((Graduate)student).setThesisTitle(data);   break;
                case 8: ((Graduate)student).setThesisAdvisor(data); break;
                case 9: ((PartTime)student).setCompany(data);       break;
                default:
            }
                    
            
        }
    }
    
    private static void tuition() throws DatabaseConnectionException, SQLException, DataBaseQueryException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String data = new String();
        Integer sid;
        Integer hours = 0;
        Integer status;
        
        System.out.println("Tutition calculation requires a student id");
        System.out.println("Type L to switch to the lookup function to find a Student's ID");
        try {
            System.out.print("Student ID: ");
            data = input.readLine();
            if ("L".equals(data) || "l".equals(data)) {
                lookup();
                return;
            }
        sid = Integer.parseInt(data);
        }
        catch (Exception e) {
            System.out.println("An error occured, returnning to main menu");
            return;
        }
        
        DBWrapper DB = new DBWrapper();
        ArrayList<Student> students = new ArrayList<Student>();
        students = DB.queryByID(sid);

        if (students.size() > 1) {
            /* Whoa!!  This should never happen */
            System.out.println("Umm, I think there's a problem.  "
                    + "A query by ID returned more than 1 result");
            return;
        }
        
        Student student = students.get(0);
        try {
            System.out.print("Please enter the number of credit hours this student is enrolled in: ");
            data = input.readLine();
            hours = Integer.parseInt(data);
            System.out.println("The student's residency status is");
            System.out.println("(1) Resident");
            System.out.println("(2) Non-Resident");
            System.out.print("Enter 1 or 2: ");
            data = input.readLine();
            status = Integer.parseInt(data);
            
            if (status != Student.RESIDENT && status != Student.NONRESIDENT ) {
                System.out.println("I didn't reconize your input.  Returning to main menu");
                return;
            }
            
            System.out.println("The students tuition is $" + student.calculateTuition(
                    hours, status));
        }
        catch (Exception e) {
            System.out.println("An error occured reading input.  Returning to main menu");
            return;
        }
        
    }
}
