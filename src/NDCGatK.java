import java.lang.reflect.Array;
import java.util.*;

public class NDCGatK {
    private HashMap<Integer, HashMap<String, Integer>> qrels;
    private HashMap<Integer, HashMap<String, Integer>> bm25Rank;
    private HashMap<Integer, HashMap<String, Integer>> sdmRank;
    private HashMap<Integer, HashMap<String, Integer>> stressRank;
    private HashMap<Integer, HashMap<String, Integer>> qlRank;
    public NDCGatK(){


        this.qrels = Setup.getQrels();
        this.bm25Rank = Setup.getbm25Rank();
        this.sdmRank = Setup.getsdmRank();
        this.stressRank = Setup.getstressRank();
        this.qlRank = Setup.getqlRank();
    }

    public ArrayList<String> calcNDCGatK(int k){
        ArrayList<String> output = new ArrayList<String>();
        double bm25Final = calcForFile(bm25Rank, k);
        double sdmFinal = calcForFile(sdmRank, k);
        double stressFinal = calcForFile(stressRank, k);
        double qlFinal = calcForFile(qlRank, k);

        output.add(String.format("%-15s %-13s %.15f  \n","bm25.trecrun","NDCG@" + k,bm25Final));
        output.add(String.format("%-15s %-13s %.15f  \n","sdm.trecrun","NDCG@" + k,sdmFinal));
        output.add(String.format("%-15s %-13s %.15f  \n","stress.trecrun","NDCG@" + k,stressFinal));
        output.add(String.format("%-15s %-13s %.15f  \n","ql.trecrun","NDCG@" + k,qlFinal));


        return output;
    }

    private double calcForFile(HashMap<Integer, HashMap<String,Integer>> retrieved, int k){


        Set<Integer> allDocs = retrieved.keySet();
        ArrayList<Double> values = new ArrayList<Double>();

        for(int key: allDocs){
            Set<String> entrySet = MapUtil.sortByValue(qrels.get(key)).keySet();
            ArrayList<String> arrList = new ArrayList<>(entrySet);
            Collections.reverse(arrList);
            Set<String> rankedQuery = MapUtil.sortByValue(retrieved.get(key)).keySet();
            Iterator<String> rankedIttr = rankedQuery.iterator();
            HashMap<String,Integer> tempRelevant = qrels.get(key);

            double IDCG = tempRelevant.get(arrList.get(0));
            for(int i=1; i < k; i++){
                double rel = tempRelevant.get(arrList.get(i));
                IDCG += rel / (Math.log(i + 1)/Math.log(2));
            }
            double rel = 0.0;
            String currDoc = "";

            if(rankedIttr.hasNext() && tempRelevant.containsKey((currDoc = rankedIttr.next())) && tempRelevant.get(currDoc) > 0){
                rel = tempRelevant.get(currDoc);
            }
            for(int i=1; i < k; i++) {
                if (rankedIttr.hasNext() && tempRelevant.containsKey((currDoc = rankedIttr.next())) && tempRelevant.get(currDoc) > 0){
                    int rel_i = tempRelevant.get(currDoc);
                    rel += rel_i/(Math.log(i + 1)/Math.log(2));
                }
            }
            values.add(rel/IDCG);
        }
        double numerator = 0.0;
        for(Double val: values){
            numerator += val;
        }
        return numerator/values.size();
    }
}
