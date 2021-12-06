import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MRR {
    private HashMap<Integer, HashMap<String, Integer>> qrels;
    private HashMap<Integer, HashMap<String, Integer>> bm25Rank;
    private HashMap<Integer, HashMap<String, Integer>> sdmRank;
    private HashMap<Integer, HashMap<String, Integer>> stressRank;
    private HashMap<Integer, HashMap<String, Integer>> qlRank;

    public MRR() {

        this.qrels = Setup.getQrels();
        this.bm25Rank = Setup.getbm25Rank();
        this.sdmRank = Setup.getsdmRank();
        this.stressRank = Setup.getstressRank();
        this.qlRank = Setup.getqlRank();
    }

    public ArrayList<String> calcMRR() {
        ArrayList<String> output = new ArrayList<String>();
        double bm25Final = calcForFile(bm25Rank);
        double sdmFinal = calcForFile(sdmRank);
        double stressFinal = calcForFile(stressRank);
        double qlFinal = calcForFile(qlRank);

        output.add(String.format("%-15s %-13s %.15f  \n", "bm25.trecrun", "MRR", bm25Final));
        output.add(String.format("%-15s %-13s %.15f  \n", "sdm.trecrun", "MRR", sdmFinal));
        output.add(String.format("%-15s %-13s %.15f  \n", "stress.trecrun", "MRR", stressFinal));
        output.add(String.format("%-15s %-13s %.15f  \n", "ql.trecrun", "MRR", qlFinal));


        return output;
    }

    private double calcForFile(HashMap<Integer, HashMap<String, Integer>> retrieved) {
        Set<Integer> allDocs = retrieved.keySet();
        ArrayList<Double> values = new ArrayList<Double>();
        double score = 0;

        for (int key : allDocs) {
            Set<String> entrySet = MapUtil.sortByValue(retrieved.get(key)).keySet();
            HashMap<String, Integer> tempRelevant = qrels.get(key);
            Iterator<String> ittr = entrySet.iterator();
            score += RR(tempRelevant, ittr);


        }

        return score / (double) allDocs.size();
    }


    private double RR(HashMap<String, Integer> relevantDocs, Iterator<String> ittr) {
        String currDoc = "";
        int i = 0;
        while (ittr.hasNext()) {
            if (ittr.hasNext() && relevantDocs.containsKey((currDoc = ittr.next())) && relevantDocs.get(currDoc) > 0) {
                return 1.0 / (i + 1);
            }
            i++;
        }
        return 0;

    }

}

