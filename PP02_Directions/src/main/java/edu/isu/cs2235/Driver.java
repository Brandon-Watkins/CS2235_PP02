package edu.isu.cs2235;

import java.util.Scanner;

/**
 * Driver for the classification program
 *
 * @author Isaac Griffith
 */
public class Driver {

    /**
     * Runs the program
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClassificationTree tree = new ClassificationTree();
        String more = "Y";
        while (more.equals("Y")) {
            System.out.print("Do you have another animal to identify? (Y/N) > ");
            Scanner in = new Scanner(System.in);
            more = in.next().toUpperCase();

            if (more.equals("Y")) {
                tree.identify();
            }
        }
        tree.save();
    }
}
