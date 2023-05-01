import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JFileChooser;

/**
 * The fileSystem program implements functions that are 
 * related to perform file handling within the voting system software. 
 *
 * @author Bryan Yen Sheng Lee
 * @author Cedric Tan Yee Shuen
 * @author Sherryl Ooi Shi Tyng
 * @version 2.0
 * @ since 2023-03-19
 */
public class fileSystem {

    // Instance variables that are used in fileSystem class
    static String electionType;
    static int numOfCandidates;
    static List<String> candidates = new ArrayList<>();
    static int numOfSeats;
    static int numOfVotes;
    static List<List<Integer>> ballot = new ArrayList<>();
    static List<List<String>> candidatesList = new ArrayList<>();
    static int fileCount = 0;


    /**
     * A constructor of the fileSystem class that takes in no parameter
     * and will open the selected files. It displays a message indicating 
     * if the file can be opened.
     */
    public fileSystem() {
        fileSystemRead();
        
        // Initialize message displaying option to select multiple files
        int yesOrNo = JOptionPane.showConfirmDialog(null,"Do you have other ballot files","Multiple file",JOptionPane.YES_NO_OPTION);

        // Prompt user option to read in multiple files
        while(yesOrNo == JOptionPane.YES_OPTION) {
            fileSystemRead();
            yesOrNo = JOptionPane.showConfirmDialog(null,"Do you have other ballot files","Multiple file",JOptionPane.YES_NO_OPTION);
        }

        // Display message showing total number of files
        JOptionPane.showMessageDialog(null,"You have a total of " + fileCount + " files opened.","INFO MESSAGE",JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * A function of the fileSystem class that gets the selected files
     * and opens when approved by the user in JOptionPane. 
     */
    public static void fileSystemRead() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        // Checks selected file to see if approve (yes, ok) is chosen
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Opens selected file 
            openFile(selectedFile);
        }

        else {
            JOptionPane.showMessageDialog(null,"There is an error opening the file.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * A function of the fileSystem class that reads in a file type 
     * and opens the file if the file format is correct. It displays 
     * a message indicating if the file can be opened.
     * @param fileName - a file type indicating the file name   
     */
    public static void openFile(File fileName) {

        // checks if file format is correct
        if(checkFileFormat(fileName)) {
            JOptionPane.showMessageDialog(null,"The file is open.","INFO MESSAGE",JOptionPane.INFORMATION_MESSAGE);
            // read file
            readFile(fileName);
        }

        // Displays error message if an error is detected
        else {
            JOptionPane.showMessageDialog(null,"There is an error opening the file.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * A function of the fileSystem class that reads in a file type 
     * and checks if the file format is correct.
     * @param fileName - a file type indicating the file name
     * @return boolean indicating if file format is correct
     */
    public static boolean checkFileFormat (File fileName) {

        // checks if file type is a .csv file type
        if(getFileExtension(fileName).equals("csv")) {
            return true;
        }
        
        if (fileName == null) {
            return false;
        }

        return false;
    }

    /**
     * A function of the fileSystem class that takes in a file type 
     * and reads the CSV file starting from the first line to indicate election type. 
     */
    public static void readFile (File fileName) {
        
        // Read lines inside CSV file 
        try {
            
            // Variables to store information that is read from CSV file
            if(fileCount == 0) {
                BufferedReader file = new BufferedReader(new FileReader(fileName));

                // Read election type on the first line of csv file
                electionType = file.readLine();

                // Read number of candidates on the second line of csv file
                numOfCandidates = Integer.parseInt(file.readLine());
                
                // add candidate names into ArrayList
                candidates.addAll(Arrays.asList(file.readLine().split(", ")));

                // read number of seats and votes if election type is CPL
                if(electionType.equals("CPL")) {
                    // adds the candidates from each party into ArrayList
                    for(int i = 0; i < numOfCandidates; i++) {
                        candidatesList.add(i, Arrays.asList(file.readLine()));
                    }
                    numOfSeats = Integer.parseInt(file.readLine());
                    numOfVotes = Integer.parseInt(file.readLine());
                }

                // read number of votes if election type is IR
                else if (electionType.equals("IR")) {
                    numOfVotes = Integer.parseInt(file.readLine());
                }

                // Display error message if invalid election type
                else {
                    JOptionPane.showMessageDialog(null,"The election type is not recognized.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                }
                
                String CurrentLine;

                // While loop to read in number of candidates from CSV file 
                while ((CurrentLine = file.readLine()) != null)
                {
                    // Splits the line into array of strings based on commas 
                    String [] fileLine = CurrentLine.split(",", -1);
                    List<Integer> subBallot = new ArrayList<>();
                    // Adds the candidates' ranking into ballot array 
                    for(int i = 0; i < numOfCandidates; i++) {
                        if(fileLine[i] != "") {
                            subBallot.add(Integer.parseInt(fileLine[i]));
                        }
                        else {
                            subBallot.add(0);
                        }
                    }
                    ballot.add(subBallot);
                }

                // Close file 
                file.close();

                // Update the count variable for the number of files opened
                fileCount ++;
            }
            else if(fileCount > 0) {
                // Read other files
                BufferedReader file = new BufferedReader(new FileReader(fileName));

                // readLine of new file to check if the header is identical
                String electionTypeNew = file.readLine();
                int numOfCandidatesNew = Integer.parseInt(file.readLine());
                List<String> candidatesNew = new ArrayList<>();
                candidatesNew.addAll(Arrays.asList(file.readLine().split(", ")));

                // if election type is similar and the number of candidates are similar for each other
                if(electionTypeNew.equals(electionType) && numOfCandidates == numOfCandidatesNew && candidatesNew.equals(candidates))
                {
                    // Read in values based on election type
                    // If the election type is Closed Party Listing (CPL)
                    if(electionType.equals("CPL")) {
                        List<List<String>> candidatesListNew = new ArrayList<>();
                        for(int i = 0; i < numOfCandidates; i++) {
                            candidatesListNew.add(i, Arrays.asList(file.readLine()));
                        }

                        // read new number of seats and votes from other files
                        int numOfSeatsNew = Integer.parseInt(file.readLine());
                        int numOfVotesNew = Integer.parseInt(file.readLine());
                    }

                    // If the election type is Instant Runoff Voting (IR)
                    else if (electionType.equals("IR")) {
                        int numOfVotesNew = Integer.parseInt(file.readLine());
                    }

                    // If the election type is invalid
                    else {
                        JOptionPane.showMessageDialog(null,"The election type is not recognized.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                    }
                    
                    String CurrentLine;

                    // While loop to read in number of candidates from CSV file 
                    while ((CurrentLine = file.readLine()) != null)
                    {
                        // Splits the line into array of strings based on commas 
                        String [] fileLine = CurrentLine.split(",", -1);
                        List<Integer> subBallot = new ArrayList<>();
                        // Adds the candidates' ranking into ballot array 
                        for(int i = 0; i < numOfCandidates; i++) {
                            if(fileLine[i] != "") {
                                subBallot.add(Integer.parseInt(fileLine[i]));
                            }
                            else {
                                subBallot.add(0);
                            }
                        }
                        ballot.add(subBallot);
                    }
                    // Close file 
                    file.close();

                    // Update the count variable for the number of files opened
                    fileCount++;
                }
                else {
                    JOptionPane.showMessageDialog(null,"The file does not have the same header.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Displays error message if an error is detected 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null,"There is an error opening the file.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    /**
     * A function that takes in a file type and gets the file extension
     * @param fullName - a file type indicating the name of the file
     * @return string indicating file type after "." in file name
     */
    public static String getFileExtension(File fullName) {

        // get file name and obtain file extension after "."
        String fileName = fullName.getName();
        int dotIndex = fileName.lastIndexOf('.');

        // Returns string indicating file type after "." in file name
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}