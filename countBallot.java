import javax.swing.*;
import java.util.*;

/**
 * The countBallot program implements functions that are 
 * related to counting the ballots based on election type 
 * using the information given into the voting system 
 * software through a .csv file. 
 *
 * @author Bryan Yen Sheng Lee
 * @author Cedric Tan Yee Shuen
 * @author Sherryl Ooi Shi Tyng
 * @version 2.0
 * @ since 2023-03-19
 */
public class countBallot {

    // An array list that stores the count of total ballots for each candidate
    static List<List<Integer>> totalBallots = new ArrayList<>();

    // A HashMap that stores the count of total ballots for each candidate along with the candidate name
    static Map <String, List<Integer>> ballotWithName = new HashMap<>();

    /**
     * A constructor of the countBallot class that takes in no parameter
     * and will calculate the total number of ballots for each candidate.
     */
    public countBallot () {

        // Create fileSystem object
        fileSystem files = new fileSystem();

        // If the election type is Closed Party Listing (CPL)
        if(fileSystem.electionType.equals("CPL")) {

            // creates subBallotCount and adds subBallotCount into totalBallots
            for(int i = 0; i < fileSystem.numOfCandidates; i++) {
                List<Integer> subBallotCount = new ArrayList<>();
                subBallotCount.add(0);
                subBallotCount.add(0);
                totalBallots.add(subBallotCount);
            }

            // Loops through number of votes and candidates to allocate ballots based on parties
            for (int i = 0; i < fileSystem.numOfCandidates; i++) {
                for (int j = 0; j < fileSystem.numOfVotes; j++) {

                    // if the ballot received by the party is 1
                    if(fileSystem.ballot.get(j).get(i) == 1) {
                        int oldValue = totalBallots.get(i).get(1);
                        oldValue++; // increment the count by 1
                        totalBallots.get(i).set(1, oldValue); // update the number of 1s the party received
                    }

                    // if the ballot received by the party is 0
                    else {
                        int oldValue = totalBallots.get(i).get(0);
                        oldValue++; // increment the count by 1
                        totalBallots.get(i).set(0, oldValue); // update the total number of 0s the party received
                    }
                }
            }
        }

        // If the election type is Instant Runoff Voting (IR)
        else if(fileSystem.electionType.equals("IR")){
            
            // creates subBallotCount and adds subBallotCount into totalBallots
            for(int i = 0; i < fileSystem.numOfCandidates; i++) {
                List<Integer> subBallotCount = new ArrayList<>();
                for(int j = 0; j <= fileSystem.numOfCandidates; j++) {
                    subBallotCount.add(0);
                }
                totalBallots.add(subBallotCount);
            }

            // Loops through number of votes and candidates to allocate the ballots based on the ranking of each candidate
            for (int i = 0; i < fileSystem.numOfCandidates; i++) {
                for (int j = 0; j < fileSystem.numOfVotes; j++) {
                    for (int k = 0; k <= fileSystem.numOfCandidates; k++) {
                        if(fileSystem.ballot.get(j).get(i) == k) {
                            int oldValue = totalBallots.get(i).get(k);
                            oldValue++; // increment the count by 1
                            totalBallots.get(i).set(k, oldValue); // update the total number of ballots the candidate received
                        }
                    }
                }
            }
        }

        // If the election type is neither IR nor CPL 
        else {
            JOptionPane.showMessageDialog(null,"The election type is not recognized.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }

        // Loop through ballotWithName and add each candidate along with totalBallots into the hashmap 
        for(int i = 0; i < fileSystem.numOfCandidates; i++) {
            ballotWithName.put(fileSystem.candidates.get(i), totalBallots.get(i));
        }

    }
}
