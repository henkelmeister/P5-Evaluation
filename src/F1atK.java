import java.util.ArrayList;
import java.util.HashMap;

public class F1atK {
    private HashMap<Integer, HashMap<String, Integer>> qrels;
    private HashMap<Integer, HashMap<String, Integer>> bm25Rank;
    private HashMap<Integer, HashMap<String, Integer>> sdmRank;
    private HashMap<Integer, HashMap<String, Integer>> stressRank;
    private HashMap<Integer, HashMap<String, Integer>> qlRank;

    public F1atK(){
        this.qrels = Setup.getQrels();
        this.bm25Rank = Setup.getbm25Rank();
        this.sdmRank = Setup.getsdmRank();
        this.stressRank = Setup.getstressRank();
        this.qlRank = Setup.getqlRank();
    }

    public ArrayList<String> calcF1atK(int k){
        ArrayList<String> output = new ArrayList<String>();
        double bm25Final = calcForFile(bm25Rank, k);
        double sdmFinal = calcForFile(sdmRank, k);
        double stressFinal = calcForFile(stressRank, k);
        double qlFinal = calcForFile(qlRank, k);

        output.add(String.format("%-15s %-13s %.15f  \n","bm25.trecrun","F1@" + k,bm25Final));
        output.add(String.format("%-15s %-13s %.15f  \n","sdm.trecrun","F1@" + k,sdmFinal));
        output.add(String.format("%-15s %-13s %.15f  \n","stress.trecrun","F1@" + k,stressFinal));
        output.add(String.format("%-15s %-13s %.15f  \n","ql.trecrun","F1@" + k,qlFinal));


        return output;
    }

    public double calcForFile(HashMap<Integer, HashMap<String, Integer>> retrieved,int k){
        PatK patK = new PatK();
        RecallatK recallatK = new RecallatK();

        double P = patK.calcForFile(retrieved,k);
        double R = recallatK.calcForFile(retrieved,k);

        //Used formula from page 310 of the book 2RP/(R + P), was the only one i was not sure if I did right or wrong
        return (R * P * 2.0)/ (R + P);
    }
}
