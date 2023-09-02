import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * Convenience class for getting input from a text file.
 * You do not need to understand the code, but should understand
 * the purpose of each method in this class.
 * 
 * @author ckinnard (Modified by STakahashi)
 * @version 9/15/19
 */
public class MediaFile  
{

    private static String inputFile = "default.txt";
    private static String outputFile = "default.txt";
    private static Scanner in;
    private static BufferedWriter out;

    /**
     * Changes the name of the file that will be read.
     * There should be no spaces in the name and this 
     * method automatically appends .txt to the name.
     * 
     * @param file the name of the file name without .txt
     */
    public static void setInputFile(String file) {
        inputFile = file + ".txt";
        in = null;
    }

    /**
     * Changes the name of the file that will be written.
     * There should be no spaces in the name and this 
     * method automatically appends .txt to the name.
     * 
     * @param file the name of the file name without .txt
     */
    public static void setOutputFile(String file) {
        outputFile = file + ".txt";
        out = null;
    }

    /**
     * Opens a file to be used for input (if not already open),
     * reads a line from the file, and returns the entire line of data.
     * 
     * @return a line of text from the input file
     */
    public static String readString() {
        if (in == null) {
            try {
                in = new Scanner(new File(inputFile));
            }
            catch (Exception e) {
                System.err.println("Cannot open file for input!");
                e.printStackTrace();
            }
        }
        try {
            if (in.hasNext()) {
                String s = in.nextLine();
                return s;
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            System.err.println("Cannot read  file!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Opens a file to be used for output (if not already open),
     * writes a string to the file and then writes a newline.
     * 
     * @param s The string text to be written. Follwing the string, a newline is added to the file.
     */
    public static void writeString(String s, boolean newLine) {
        if (out == null) {
            try {
                out = new BufferedWriter(new FileWriter(outputFile));
            }
            catch (Exception e) {
                System.err.println("Cannot create file for output!");
                e.printStackTrace();
            }
        }

        try {
            out.write(s);
            if(newLine)
                out.newLine();
        }
        catch (Exception e) {
            System.err.println("Cannot write file!");
            e.printStackTrace();
        }

    }

    /**
     * Saves and closes the file (when opened for either input or output). 
     * 
     * Note: If the program terminates before the file is closed,
     * no data will be saved or written to the file.
     */
    public static void saveAndClose() {
        if (in != null) {
            try {
                in.close();
                in = null;
            }
            catch (Exception e) {
                System.err.println("Cannot close input file!");
                e.printStackTrace();
            }
        }      
        if (out != null) {
            try {
                out.close();
                out = null;
            }
            catch (Exception e) {
                System.err.println("Cannot close output file!");
                e.printStackTrace();
            }
        }
    }
}