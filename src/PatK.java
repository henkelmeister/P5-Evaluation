import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PatK {
    private HashMap<Integer, HashMap<String, Integer>> qrels;
    private HashMap<Integer, HashMap<String, Integer>> bm25Rank;
    private HashMap<Integer, HashMap<String, Integer>> sdmRank;
    private HashMap<Integer, HashMap<String, Integer>> stressRank;
    private HashMap<Integer, HashMap<String, Integer>> qlRank;
    private int k;
    public PatK(){

        this.qrels = Setup.getQrels();
        this.bm25Rank = Setup.getbm25Rank();
        this.sdmRank = Setup.getsdmRank();
        this.stressRank = Setup.getstressRank();
        this.qlRank = Setup.getqlRank();
    }

    public ArrayList<String> calcPatK(int k){
        ArrayList<String> output = new ArrayList<String>();
        double bm25Final = calcForFile(bm25Rank, k);
        double sdmFinal = calcForFile(sdmRank, k);
        double stressFinal = calcForFile(stressRank, k);
        double qlFinal = calcForFile(qlRank, k);

        output.add(String.format("%-15s %-13s %.15f  \n","bm25.trecrun","P@" + k,bm25Final));
        output.add(String.format("%-15s %-13s %.15f  \n","sdm.trecrun","P@" + k,sdmFinal));
        output.add(String.format("%-15s %-13s %.15f  \n","stress.trecrun","P@" + k,stressFinal));
        output.add(String.format("%-15s %-13s %.15f  \n","ql.trecrun","P@" + k,qlFinal));

        //System.out.println(String.format("%-15s %-13s %.15f","stress.trecrun","P@" + k,stressFinal));


        return output;
    }

    public double calcForFile(HashMap<Integer, HashMap<String, Integer>> retrieved, int k){
        Set<Integer> allDocs = retrieved.keySet();
        ArrayList<Double> values = new ArrayList<Double>();

        for(int key: allDocs){
            Set<String> entrySet = MapUtil.sortByValue(retrieved.get(key)).keySet();
            HashMap<String,Integer> tempRelevant = qrels.get(key);
            Iterator<String> ittr = entrySet.iterator();

            //Getting the first K in each query
            int numRelInDoc = 0;
            String currDoc = "";
            for(int i=0; i < k; i++){
                if(ittr.hasNext() && tempRelevant.containsKey((currDoc = ittr.next())) && tempRelevant.get(currDoc) > 0){
                    numRelInDoc++;
                }
            }
            values.add((double) numRelInDoc / (double) k);

        }
        double numerator = 0.0;
        for(Double val: values){
            numerator += val;
        }
        return numerator/values.size();
    }


}
