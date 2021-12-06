import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MAP {
    private HashMap<Integer, HashMap<String, Integer>> qrels;
    private HashMap<Integer, HashMap<String, Integer>> bm25Rank;
    private HashMap<Integer, HashMap<String, Integer>> sdmRank;
    private HashMap<Integer, HashMap<String, Integer>> stressRank;
    private HashMap<Integer, HashMap<String, Integer>> qlRank;

    public MAP() {
        this.qrels = Setup.getQrels();
        this.bm25Rank = Setup.getbm25Rank();
        this.sdmRank = Setup.getsdmRank();
        this.stressRank = Setup.getstressRank();
        this.qlRank = Setup.getqlRank();
    }

    public ArrayList<String> calcMAP() {
        ArrayList<String> output = new ArrayList<String>();
        double bm25Final = calcForFile(bm25Rank);
        double sdmFinal = calcForFile(sdmRank);
        double stressFinal = calcForFile(stressRank);
        double qlFinal = calcForFile(qlRank);

        output.add(String.format("%-15s %-13s %.15f  \n", "bm25.trecrun", "MAP", bm25Final));
        output.add(String.format("%-15s %-13s %.15f  \n", "sdm.trecrun", "MAP", sdmFinal));
        output.add(String.format("%-15s %-13s %.15f  \n", "stress.trecrun", "MAP", stressFinal));
        output.add(String.format("%-15s %-13s %.15f  \n", "ql.trecrun", "MAP", qlFinal));



        return output;
    }

    private double calcForFile(HashMap<Integer, HashMap<String, Integer>> retrieved) {

        Set<Integer> allDocs = retrieved.keySet();
        ArrayList<Double> values = new ArrayList<Double>();
        for (int key : allDocs) {
            Set<String> entrySet = MapUtil.sortByValue(retrieved.get(key)).keySet();
            HashMap<String, Integer> tempRelevant = qrels.get(key);
            Iterator<String> ittr = entrySet.iterator();
            int relSize = getNumRelevant(tempRelevant);
            double AP = 0;
            int i = 0;
            int relSeen = 0;
            while (ittr.hasNext()) {
                String currDoc = "";
                if (ittr.hasNext() && tempRelevant.containsKey((currDoc = ittr.next())) && tempRelevant.get(currDoc) > 0) {
                    relSeen++;
                    AP += (double) relSeen/(double)(i + 1);
                }

                i++;
            }
            AP = AP/(double) relSize;
            values.add(AP);
        }

        double numerator = 0.0;
        for(Double val: values){
            numerator += val;
        }
        return (numerator/values.size());
    }

    private int getNumRelevant(HashMap<String, Integer> map) {

        Set<String> keys = map.keySet();
        int numRel = 0;

        for (String key : keys) {
            if (map.get(key) > 0) {
                numRel++;
            }
        }
        return numRel;
    }



}
