import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RecallatK {

    private HashMap<Integer, HashMap<String, Integer>> qrels;
    private HashMap<Integer, HashMap<String, Integer>> bm25Rank;
    private HashMap<Integer, HashMap<String, Integer>> sdmRank;
    private HashMap<Integer, HashMap<String, Integer>> stressRank;
    private HashMap<Integer, HashMap<String, Integer>> qlRank;


    public RecallatK(){


        this.qrels = Setup.getQrels();
        this.bm25Rank = Setup.getbm25Rank();
        this.sdmRank = Setup.getsdmRank();
        this.stressRank = Setup.getstressRank();
        this.qlRank = Setup.getqlRank();
    }


    public double calcForFile(HashMap<Integer, HashMap<String, Integer>> retrieved, int k){
        ArrayList<String> output = new ArrayList<String>();
        Set<Integer> allDocs = retrieved.keySet();
        ArrayList<Double> values = new ArrayList<>();

        for(int key: allDocs){
            Set<String> entrySet = MapUtil.sortByValue(retrieved.get(key)).keySet();
            HashMap<String,Integer> tempRelevant = qrels.get(key);
            Iterator<String> ittr = entrySet.iterator();
            int relSize = getNumRelevant(tempRelevant);
            //Getting the first K in each query
            int numRelInDoc = 0;
            String currDoc = "";
            for(int i=0; i < k; i++){
                if(ittr.hasNext() && tempRelevant.containsKey((currDoc = ittr.next())) && tempRelevant.get(currDoc) > 0){
                    numRelInDoc++;
                }
            }
            values.add((double) numRelInDoc / (double) relSize);

        }
        double numerator = 0.0;
        for(Double val: values){
            numerator += val;
        }
        return numerator/values.size();
    }


    //Recall = |A and B| / |A|
    // A is relevant set of docs
    // B is the set of retrieved docs
    public ArrayList<String> calcRecall(Integer k){
        //L is ranked list of retrieved results
        //R is a sortable map of relevant documents
        ArrayList<String> output = new ArrayList<String>();
        double bm25Final = calcForFile(bm25Rank, k);
        double sdmFinal = calcForFile(sdmRank, k);
        double stressFinal = calcForFile(stressRank, k);
        double qlFinal = calcForFile(qlRank, k);

        output.add(String.format("%-15s %-13s %.15f  \n","bm25.trecrun","Recall@" + k,bm25Final));
        output.add(String.format("%-15s %-13s %.15f  \n","sdm.trecrun","Recall@" + k,sdmFinal));
        output.add(String.format("%-15s %-13s %.15f  \n","stress.trecrun","Recall@" + k,stressFinal));
        output.add(String.format("%-15s %-13s %.15f  \n","ql.trecrun","Recall@" + k,qlFinal));

        return output;
        /*  I WROTE WAY TOO MUCH CODE FOR THIS, this was the first file I made and soon realized there was a much better way to do this.

            Again, keeping this here so you can see how much of a goofball i am...


        //Start BM25
        ArrayList<String> output = new ArrayList<String>(); //contains all values for output, already formatted
        Set<Integer> allDocsbm25 = bm25Rank.keySet();
        ArrayList<Double> valuesbm25 = new ArrayList<Double>();

        //For bm25
        for(int key: allDocsbm25){
            Set<String> entrySet = MapUtil.sortByValue(bm25Rank.get(key)).keySet();
            HashMap<String,Integer> tempRelevant = qrels.get(key);
            Iterator<String> ittr = entrySet.iterator();

            int relSize = getNumRelevant(tempRelevant);
            //Getting the first K in each query
            int numRelInDoc = 0;
            String currDoc = "";
            for(int i=0; i < k; i++){
                if(ittr.hasNext() && tempRelevant.containsKey((currDoc = ittr.next())) && tempRelevant.get(currDoc) > 0){
                    numRelInDoc++;
                }
            }
            valuesbm25.add((double) numRelInDoc / (double) relSize);


        }
        double numeratorBm25 = 0.0;
        for(Double val: valuesbm25){
            numeratorBm25 += val;
        }
        double bm25FinalVal = numeratorBm25 / valuesbm25.size(); //Averaging values for bm25
        output.add(String.format("%-15s %-13s %.15f","bm25.trecrun","Recall@20",bm25FinalVal));
        //End BM25

        //Start stress
        Set<Integer> allDocsstress = stressRank.keySet();
        ArrayList<Double> valuesstress = new ArrayList<Double>();

        for(int key: allDocsstress){
            Set<String> entrySet = MapUtil.sortByValue(stressRank.get(key)).keySet();
            HashMap<String,Integer> tempRelevant = qrels.get(key);
            Iterator<String> ittr = entrySet.iterator();

            int relSize = getNumRelevant(tempRelevant);
            //Getting the first K in each query
            int numRelInDoc = 0;
            String currDoc = "";
            for(int i=0; i < k; i++){
                if(ittr.hasNext() && tempRelevant.containsKey((currDoc = ittr.next())) && tempRelevant.get(currDoc) > 0){
                    numRelInDoc++;
                }
            }
            valuesstress.add((double) numRelInDoc / (double) relSize);


        }
        double numeratorStress = 0.0;
        for(Double val: valuesstress){
            numeratorStress += val;
        }
        double stressFinalVal = numeratorStress / valuesstress.size(); //Averaging values for bm25
        output.add(String.format("%-15s %-13s %.15f","stress.trecrun","Recall@20",stressFinalVal));
        //System.out.println(String.format("%-18s %-13s %.15f","stress.trecrun","Recall@20",stressFinalVal));
        //End Stress

        //Start sdm
        Set<Integer> allDocssdm = sdmRank.keySet();
        ArrayList<Double> valuessdm = new ArrayList<Double>();

        for(int key: allDocssdm){
            Set<String> entrySet = MapUtil.sortByValue(sdmRank.get(key)).keySet();
            HashMap<String,Integer> tempRelevant = qrels.get(key);
            Iterator<String> ittr = entrySet.iterator();

            int relSize = getNumRelevant(tempRelevant);
            //Getting the first K in each query
            int numRelInDoc = 0;
            String currDoc = "";
            for(int i=0; i < k; i++){
                if(ittr.hasNext() && tempRelevant.containsKey((currDoc = ittr.next())) && tempRelevant.get(currDoc) > 0){
                    numRelInDoc++;
                }
            }
            valuessdm.add((double) numRelInDoc / (double) relSize);


        }
        double numeratorSdm = 0.0;
        for(Double val: valuessdm){
            numeratorSdm += val;
        }
        double sdmFinalVal = numeratorSdm / valuessdm.size(); //Averaging values for bm25
        output.add(String.format("%-15s %-13s %.15f","sdm.trecrun","Recall@20",sdmFinalVal));
        //End sdm

        //Start ql
        Set<Integer> allDocsql = qlRank.keySet();
        ArrayList<Double> valuesql = new ArrayList<Double>();

        for(int key: allDocsql){
            Set<String> entrySet = MapUtil.sortByValue(qlRank.get(key)).keySet();
            HashMap<String,Integer> tempRelevant = qrels.get(key);
            Iterator<String> ittr = entrySet.iterator();

            int relSize = getNumRelevant(tempRelevant);
            //Getting the first K in each query
            int numRelInDoc = 0;
            String currDoc = "";
            for(int i=0; i < k; i++){
                if(ittr.hasNext() && tempRelevant.containsKey((currDoc = ittr.next())) && tempRelevant.get(currDoc) > 0){
                    numRelInDoc++;
                }
            }
            valuesql.add((double) numRelInDoc / (double) relSize);

        }
        double numeratorql = 0.0;
        for(Double val: valuesql){
            numeratorql += val;
        }
        double qlFinalVal = numeratorql / valuesql.size(); //Averaging values for bm25
        output.add(String.format("%-15s %-13s %.15f \n","ql.trecrun","Recall@20",qlFinalVal));
        //End ql

        //Wow... I could have made all this in like 40 lines of code by making this a function that took the hash maps and k as inputs...


        return output;
        */
    }

    private int getNumRelevant(HashMap<String, Integer> map){

        Set<String> keys = map.keySet();
        int numRel = 0;

        for(String key: keys){
            if(map.get(key) > 0){
                numRel++;
            }
        }
        return numRel;
    }
}
