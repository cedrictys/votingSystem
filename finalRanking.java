import javax.swing.*;
import java.lang.Math;
import java.util.*;

/**
 * The finalRanking program implements functions that are 
 * related to determine the final ranking of the candidates/parties 
 * of the election and perform check for tie and flip a coin toss if 
 * necessary to determine the winner through an unbiased method. 
 *
 * @author Bryan Yen Sheng Lee
 * @author Cedric Tan Yee Shuen
 * @author Sherryl Ooi Shi Tyng
 * @version 2.0
 * @ since 2023-03-19
 */
public class finalRanking {

    static Map<String, List<Integer>> finalRanking = new LinkedHashMap<>();

    /**
     * A constructor of the finalRanking class that takes in no parameter
     * and will checks if there is a tie.
     */
    public finalRanking () {
        finalRanking = checkForTie();
    }

    /**
     * A function that checks for tie and prompts users if they want to perform 
     * a coin toss. Performs a coin toss based on election type if there is a tie.
     * @return a hashmap indicating the final rankings for all candidates
     */
    public Map<String, List<Integer>> checkForTie () {
        rankings rank = new rankings();
        int yesOrNo;
        int finalResult;
        
        // If the election type is Closed Party Listing (CPL)
        if(fileSystem.electionType.equals("CPL")) {
            List<Map.Entry<String, List<Integer>>> entries = new ArrayList<>(rankings.ranking.entrySet());
            int totalSeats = fileSystem.numOfSeats;
            // check if the last rank from num of seats is tie with the next one
            for(int i = totalSeats - 1; i < fileSystem.numOfCandidates; i++) {
                if(entries.get(i).getValue().get(1) == entries.get(i+1).getValue().get(1)) {
                    if(entries.get(i+1).getValue().get(1) == entries.get(i+2).getValue().get(1)){
                        // if more than 2 have equal value
                        yesOrNo = JOptionPane.showConfirmDialog(null,"Do you want to run a pool coin toss","Pool Coin Toss",JOptionPane.YES_NO_OPTION);
                        if(yesOrNo == JOptionPane.YES_OPTION) {
                            // if yes, run a pool coin toss
                            finalResult = poolCoinToss(i + 2);
                            // update final ranking based on pool coin toss result
                            if(finalResult == 0) {
                                for(int k = 0; k < totalSeats - 1; k++) {
                                    finalRanking.put(entries.get(k).getKey(), entries.get(k).getValue());
                                }
                                finalRanking.put(entries.get(i).getKey(), entries.get(i).getValue());
                            }
                            else if(finalResult == 1){
                                for(int k = 0; k < totalSeats - 1; k++) {
                                    finalRanking.put(entries.get(k).getKey(), entries.get(k).getValue());
                                }
                                finalRanking.put(entries.get(i+1).getKey(), entries.get(i).getValue());
                            }
                            else {
                                for(int k = 0; k < totalSeats - 1; k++) {
                                    finalRanking.put(entries.get(k).getKey(), entries.get(k).getValue());
                                }
                                finalRanking.put(entries.get(i+2).getKey(), entries.get(i).getValue());
                            }
                            return finalRanking;
                        }
                    }
                    else {
                        // if only 2 has equal value
                        yesOrNo = JOptionPane.showConfirmDialog(null,"Do you want to run a fair coin toss","Fair Coin Toss",JOptionPane.YES_NO_OPTION);
                        if(yesOrNo == JOptionPane.YES_OPTION) {
                            // if yes, run a fair coin toss.
                            finalResult = fairCoinToss();  
                            // update final ranking based on fair coin toss result
                            if(finalResult == 0){
                                for(int k = 0; k < totalSeats - 1; k++) {
                                    finalRanking.put(entries.get(k).getKey(), entries.get(k).getValue());
                                }
                                finalRanking.put(entries.get(i).getKey(), entries.get(i).getValue());
                            }
                            else {
                                for(int k = 0; k < totalSeats - 1; k++) {
                                    finalRanking.put(entries.get(k).getKey(), entries.get(i+1).getValue());
                                }
                                finalRanking.put(entries.get(i+1).getKey(), entries.get(i).getValue());
                            }
                            return finalRanking;                        }
                    }
                }
                else {
                    for(int k = 0; k < totalSeats; k++) {
                        finalRanking.put(entries.get(k).getKey(), entries.get(k).getValue());
                    }
                }
            }
            return finalRanking;
        }

        // If the election type is Instant Runoff Voting (IR)
        else if(fileSystem.electionType.equals("IR")) {
            List<Map.Entry<String, List<Integer>>> entries = new ArrayList<>(rankings.ranking.entrySet());
            int i = 0;
            // check if there is a tie between the first place
            if(entries.get(i).getValue().get(1) == entries.get(i+1).getValue().get(1)) {
                if(entries.get(i+1).getValue().get(1) == entries.get(i+2).getValue().get(1)){
                    // check if more than 2 has tie
                    yesOrNo = JOptionPane.showConfirmDialog(null,"Do you want to run a pool coin toss","Pool Coin Toss",JOptionPane.YES_NO_OPTION);
                    if(yesOrNo == JOptionPane.YES_OPTION) {
                        // run a pool coin toss if there is a tie
                        finalResult = poolCoinToss(i + 2);
                        // update final ranking based on pool coin toss result
                        if(finalResult == 0) {
                            finalRanking.put(entries.get(i).getKey(), entries.get(i).getValue());
                        }
                        else if(finalResult == 1){
                            finalRanking.put(entries.get(i+1).getKey(), entries.get(i).getValue());
                        }
                        else {
                            finalRanking.put(entries.get(i+2).getKey(), entries.get(i).getValue());
                        }
                    }
                }
                else {
                    // if there is only 2 has tie
                    yesOrNo = JOptionPane.showConfirmDialog(null,"Do you want to run a fair coin toss","Fair Coin Toss",JOptionPane.YES_NO_OPTION);
                    if(yesOrNo == JOptionPane.YES_OPTION) {
                        // run a fair coin toss if there is a tie
                        finalResult = fairCoinToss();    
                        // update final ranking based on fair coin toss result                        
                        if(finalResult == 0) {
                            finalRanking.put(entries.get(i).getKey(), entries.get(i).getValue());
                        }
                        else {
                            finalRanking.put(entries.get(i+1).getKey(), entries.get(i).getValue());
                        }
                    }
                }
            }
            return finalRanking;
        }

        // If the election type is invalid
        else {
            JOptionPane.showMessageDialog(null,"The election type is not recognized.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }

        return finalRanking;
    }

    /**
     * A function that performs fair coin toss if there is a tie
     * between two parties or candidates. 
     * @return an integer representing the result
     */
    public int fairCoinToss () {
        // If the election type is Closed Party Listing (CPL)
        if(fileSystem.electionType.equals("CPL")) {
            return (int)(Math.random() * 2) + 0;
        }

        // If the election type is Instant Runoff Voting (IR)
        else if(fileSystem.electionType.equals("IR")) {
            return (int)(Math.random() * 2) + 0;
        }

        // Displays error message if an error is detected 
        else {
            JOptionPane.showMessageDialog(null,"The election type is not recognized.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    /**
     * A function that performs pool coin toss if there is a tie
     * between parties or candidates based on the range. 
     * @param range indicating the number of parties/ candidates with the same number of votes
     * @return an integer representing the result
     */
    public int poolCoinToss (int range) {
        // If the election type is Closed Party Listing (CPL)
        if(fileSystem.electionType.equals("CPL")) {
            return (int)(Math.random() * range) + 0;
        }

        // If the election type is Instant Runoff Voting (IR)
        else if(fileSystem.electionType.equals("IR")) {
            return (int)(Math.random() * range) + 0;
        }

        // Displays error message if an error is detected 
        else {
            JOptionPane.showMessageDialog(null,"The election type is not recognized.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }
}
