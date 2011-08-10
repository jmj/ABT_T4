/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registrar;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

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
    
    private static void delete() {
        System.out.print("delete()\n");
    }
    
    private static void lookup() {
        System.out.print("lookup()\n");
    }
    
    private static void update() {
        System.out.print("update()\n");
    }
}
