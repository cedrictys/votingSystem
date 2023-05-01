import javax.swing.*;
import java.lang.Math;
import java.util.*;
import java.util.Map.*;

/**
 * The rankings program implements functions that are 
 * related to determine the ranking of the candidates/parties 
 * of the election as well as check whether there is a majority 
 * to determine the winner of the election. 
 *
 * @author Bryan Yen Sheng Lee
 * @author Cedric Tan Yee Shuen
 * @author Sherryl Ooi Shi Tyng
 * @version 2.0
 * @ since 2023-03-19
 */
public class rankings {
    
    // A Linked Hash Map to store rankings of each candidate
    static Map<String, List<Integer>> ranking = new LinkedHashMap<>();

    // An array list to store ??
    static List<Map<String, List<Integer>>> displayList = new ArrayList<>();
    
    /**
     * A constructor of the rankings class that takes in no parameter
     * and counts the ballot. The rankings are then determined. 
     */
    public rankings () {
        countBallot ballotCount = new countBallot();
        checkMajority(ballotCount.ballotWithName);
    }

    /**
     * A function that reads the newBallots array and determines the ranking
     * for each candidate based on number of votes
     * @param ballotWithName — a hashmap indicating candidate names along with their number of votes
     * @return hashmap indicating candidates' rankings 
     */
    public Map<String, List<Integer>> checkRanking (Map<String, List<Integer>> ballotWithName) {

        // Check if election type is CPL
        if(fileSystem.electionType.equals("CPL")) {
            List<Map.Entry<String, List<Integer>>> entryList = new ArrayList<>(ballotWithName.entrySet());

            // Sort the entryList in descending order by second value in each list
            Collections.sort(entryList, Comparator.comparingInt((Map.Entry<String, List<Integer>> e) -> e.getValue().get(1)).reversed());

            // Create a new LinkedHashMap with the sorted entries
            for (Map.Entry<String, List<Integer>> entry : entryList) {
                ranking.put(entry.getKey(), entry.getValue());
            }

            // Print the sorted map
            return ranking;
        }

        // Check if election type is IR
        else if (fileSystem.electionType.equals("IR")) {
            List<Map.Entry<String, List<Integer>>> entryList = new ArrayList<>(ballotWithName.entrySet());

            // Sort the entryList in descending order by second value in each list
            Collections.sort(entryList, Comparator.comparingInt((Map.Entry<String, List<Integer>> e) -> e.getValue().get(1)).reversed());

            // Create a new LinkedHashMap with the sorted entries
            for (Map.Entry<String, List<Integer>> entry : entryList) {
                ranking.put(entry.getKey(), entry.getValue());
            }

            // Print the sorted map
            return ranking;
        }

        // Displays error message if an error is detected
        else {
            JOptionPane.showMessageDialog(null,"The election type is not recognized.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
        return ranking;
    }

    /**
     * A function that reads the totalBallots array and check if there is a majority
     * among the candidates. 
     * @param ballotWithName — a hashmap indicating candidate names along with their number of votes
     */
    public void checkMajority (Map<String, List<Integer>> ballotWithName) {

        // Checks if election type is CPL
        if(fileSystem.electionType.equals("CPL")) {
            ranking = checkRanking(ballotWithName);
        }

        // Checks if election type is IR
        else if (fileSystem.electionType.equals("IR")) {
            int majority = (int) fileSystem.numOfVotes * 50 / 100;
            ranking = checkRanking(ballotWithName);
            Map.Entry<String, List<Integer>>entry = ranking.entrySet().iterator().next();
            int initialRank = 1;

            // While there is no majority 
            while(entry.getValue().get(1) < majority) {

                // Update ranking by removing candidate that is ranked last
                displayList.add(new LinkedHashMap<>(ranking));
                List<Entry<String, List<Integer>>> entryList = new ArrayList<Map.Entry<String, List<Integer>>>(ranking.entrySet());
                Entry<String, List<Integer>> lastEntry = entryList.get(entryList.size() - 1);
                List<Integer> subList = lastEntry.getValue();
                subList.set(1, 0);
                ranking.put(lastEntry.getKey(), subList);
                ranking.remove(lastEntry.getKey());

                // Redistribute votes 
                List<List<Integer>> checkBallot = new ArrayList<>();
                checkBallot = fileSystem.ballot;
                int check = fileSystem.candidates.indexOf(lastEntry.getKey());
                for(int i = 0; i < fileSystem.numOfVotes; i++) {
                    if(fileSystem.ballot.get(i).get(check) == initialRank) {
                        for(int j = 0; j < fileSystem.numOfCandidates; j++) {
                            if(fileSystem.ballot.get(i).get(j) == initialRank+1) {
                                List<Integer> subBallotList = ranking.get(fileSystem.candidates.get(j));
                                int oldValue = subBallotList.get(1);
                                oldValue++;
                                subBallotList.set(1, oldValue);
                                ranking.put(fileSystem.candidates.get(j), subBallotList);
                            }
                        }
                    }
                }

                // Check ranking again 
                ranking = checkRanking(ranking);
                entry = ranking.entrySet().iterator().next();
            }
            displayList.add(new LinkedHashMap<>(ranking));

        }

        // Displays error message if an error is detected
        else {
            JOptionPane.showMessageDialog(null,"The election type is not recognized.","ERROR MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
    }
}
