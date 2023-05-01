import java.io.*;
import javax.swing.*;
import java.util.*;

/**
 * The displayResults program implements functions that are 
 * related to displaying the final results of the election
 * based on the election type as well as generate an audit 
 * file for a completed election. 
 *
 * @author Bryan Yen Sheng Lee
 * @author Cedric Tan Yee Shuen
 * @author Sherryl Ooi Shi Tyng
 * @version 2.0
 * @ since 2023-03-19
 */
public class displayResults {

    /**
     * A constructor of the displayResults class that takes in no parameter
     * and will display the results based on the final rankings.
     */
    public displayResults () {
        finalRanking rank = new finalRanking();
        showResults();
    }

    /**
     * A function of the displayResults class that generates the audit file for 
     * completed elections based on election type.
     */
    public void generateAuditFile () {
        try {

            // Create JFrame to display information
            JFrame parentFrame = new JFrame();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save audit file");
    
            int userSelection = fileChooser.showSaveDialog(parentFrame);
    
            // Checks whether approve (yes, ok) is chosen
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File outputFile = fileChooser.getSelectedFile();
                System.out.println("Save as file: " + outputFile.getAbsolutePath());
                
                // Generate output file
                outputFile.createNewFile();
                PrintWriter output = new PrintWriter(outputFile);
                StringBuffer csvData = new StringBuffer("");
                
                csvData.append("Election type: " + fileSystem.electionType + "\n");

                // If the election type is Closed Party Listing (CPL)
                if(fileSystem.electionType.equals("CPL")) {
                    // add election information into the audit file
                    csvData.append("Number of Parties: " + fileSystem.numOfCandidates + "\n");
                    csvData.append("Parties joined election: " + fileSystem.candidates + "\n");
                    for(int i = 0; i < fileSystem.numOfCandidates; i++) {
                        csvData.append("Candidates of party " + fileSystem.candidates.get(i) + ": " + fileSystem.candidatesList.get(i) + "\n");
                    }
                    csvData.append("Total seats elected: " + fileSystem.numOfSeats + "\n");
                    csvData.append("Total number of voters: " + fileSystem.numOfVotes + "\n");
                    
                    // add ranking for each candidate into GUI
                    List<Map.Entry<String, List<Integer>>> entries = new ArrayList<>(rankings.ranking.entrySet());
                    List<String> keys = new ArrayList<>(rankings.ranking.keySet());
                    for(int i = 0; i < fileSystem.numOfCandidates; i++) {
                        csvData.append(entries.get(i).getKey() + " is rank " + (keys.indexOf(entries.get(i).getKey()) + 1) + ".\n");
                    }
                    csvData.append("The final winners of the election are parties: \n");

                    // add final winners' ranking into the GUI
                    List<Map.Entry<String, List<Integer>>> entries2 = new ArrayList<>(finalRanking.finalRanking.entrySet());
                    List<String> keys2 = new ArrayList<>(finalRanking.finalRanking.keySet());
                    for(int i = 0; i < fileSystem.numOfSeats; i++) {
                        csvData.append(entries2.get(i).getKey() + " is rank " + (keys2.indexOf(entries2.get(i).getKey()) + 1) + ".\n");
                    }
                }

                // If the election type is Instant Runoff Voting (IR)
                else if(fileSystem.electionType.equals("IR")){
                    // add election information into the audit file
                    csvData.append("Number of Candidates: " + fileSystem.numOfCandidates + "\n");
                    csvData.append("Candidates joined election: " + fileSystem.candidates + "\n");
                    csvData.append("Total number of voters: " + fileSystem.numOfVotes + "\n");

                    // add the final winner and displays the results below the final winner
                    List<Map.Entry<String, List<Integer>>> entries = new ArrayList<>(rankings.ranking.entrySet());
                    csvData.append("The final winner of the election is candidate "  + entries.get(0).getKey() + "\n");
                    csvData.append("\nThe results are as below: \n");
                    for(int i = 0; i < rankings.displayList.size(); i++) {
                        csvData.append(rankings.displayList.get(i) + "\n");
                    }
                }
        
                output.write(csvData.toString());
                output.close();
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A function of the displayResults class that shows the final result of the 
     * election based on the election type. 
     */
    public void showResults () {
        // GUI interface to prompt user to choose whether to generate an audit file
        int yesOrNo = JOptionPane.showConfirmDialog(null,"Do you want to generate Audit File?","Generate Audit File",JOptionPane.YES_NO_OPTION);
        
        // If the user chose YES to generate an audit file
        if(yesOrNo == JOptionPane.YES_OPTION) {
            generateAuditFile();
        }

        // Create JFrame to display information 
        JFrame frame = new JFrame ("Final Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        JTextArea textArea  = new JTextArea();

        // Adds the election type onto the first line of the JFrame
        textArea.append("Election type: " + fileSystem.electionType + "\n");

        // If the election type is Closed Party Listing (CPL)
        if(fileSystem.electionType.equals("CPL")) {
            // add election information into the GUI
            textArea.append("Number of Parties: " + fileSystem.numOfCandidates + "\n");
            textArea.append("Parties joined election: " + fileSystem.candidates + "\n");
            for(int i = 0; i < fileSystem.numOfCandidates; i++) {
                textArea.append("Candidates of party " + fileSystem.candidates.get(i) + ": " + fileSystem.candidatesList.get(i) + "\n");
            }
            textArea.append("Total seats elected: " + fileSystem.numOfSeats + "\n");
            textArea.append("Total number of voters: " + fileSystem.numOfVotes + "\n");

            // add final winners into the GUI 
            List<Map.Entry<String, List<Integer>>> entries = new ArrayList<>(rankings.ranking.entrySet());
            List<String> keys = new ArrayList<>(rankings.ranking.keySet());
            for(int i = 0; i < fileSystem.numOfCandidates; i++) {
                textArea.append(entries.get(i).getKey() + " is rank " + (keys.indexOf(entries.get(i).getKey()) + 1) + ".\n");
            }
            textArea.append("The final winners of the election are parties: \n");

            // add final winners' ranking into the GUI
            List<Map.Entry<String, List<Integer>>> entries2 = new ArrayList<>(finalRanking.finalRanking.entrySet());
            List<String> keys2 = new ArrayList<>(finalRanking.finalRanking.keySet());
            for(int i = 0; i < fileSystem.numOfSeats; i++) {
                textArea.append(entries2.get(i).getKey() + " is rank " + (keys2.indexOf(entries2.get(i).getKey()) + 1) + ".\n");
            }
        }

        // If the election type is Instant Runoff Voting (IR)
        else if(fileSystem.electionType.equals("IR")){
            // add election information into the GUI
            textArea.append("Number of Candidates: " + fileSystem.numOfCandidates + "\n");
            textArea.append("Candidates joined election: " + fileSystem.candidates + "\n");
            textArea.append("Total number of voters: " + fileSystem.numOfVotes + "\n");

            // add the final winner and displays the results below the final winner
            List<Map.Entry<String, List<Integer>>> entries = new ArrayList<>(rankings.ranking.entrySet());
            textArea.append("The final winner of the election is candidate "  + entries.get(0).getKey() + "\n");
            textArea.append("\nThe results are as below: \n");
            for(int i = 0; i < rankings.displayList.size(); i++) {
                textArea.append(rankings.displayList.get(i) + "\n");
            }
        }

        // modifications to JFrame
        frame.add(textArea);
        textArea.setEditable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
