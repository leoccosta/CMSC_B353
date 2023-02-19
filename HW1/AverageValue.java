/* 
* Part 2. Calculating Average Value from Assignment 1: Defensive Programming.
* Assignment details: https://docs.google.com/document/d/1dHNmVrQabqjfng21eX5wt7ms1D_fRJKk8Ssu9siH0js/
* 
* Program takes in the name of a CSV file and an index n and returns the average of numerical values at the 
* nth field of each line in the file.
* 
* @author lcosta
* Due Jan 31, 2023
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;

public class AverageValue {
    public static void main(String[] args) {
        
        // Note: I know you asked for us not to but a try/catch over everything for the lab but in this instance
        // I had to for scope reasons (i.e. so that n and file can be accessed later, as declaring them outside)
        if (args.length < 2) {
            System.out.println("Invalid data");
            return;
        }

        try {
            int n = Integer.parseInt(args[0]);
            Path file = Paths.get(args[1]);

            // https://docs.oracle.com/javase/tutorial/essential/io/file.html
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                String line = null;
                double sum = 0;
                int count = 0;
                boolean validInput = false;

                while ((line = reader.readLine()) != null) {
                    String[] splitNums = line.split(",");
                    try {
                        System.out.println("Length " + splitNums.length);
                        if (n >= 0 && splitNums.length > n) {
                            sum += Double.parseDouble(splitNums[n]);
                            count++;
                            validInput = true;
                        }
                        else {
                            // System.out.println("Invalid data");
                            continue;
                        }
                    } catch (NumberFormatException x) {
                        //System.err.format("Invalid data: %s%n", x);
                    } 
                }

                if (validInput) {
                    System.out.println("average: " + sum/count);
                }
                else {
                    System.out.println("Invalid data");
                }         

            } catch (IOException x) {
                System.out.println("Invalid data");
                //System.err.format("Invalid data: %s%n", x);
            }
        }
        catch (NumberFormatException x) {
            System.out.println("Invalid data");
            // System.err.format("Invalid data: %s%n", x);
        }
    }
}