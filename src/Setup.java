import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Setup {
    private static HashMap<Integer, HashMap<String, Integer>> qrels;
    private static HashMap<Integer, HashMap<String, Integer>> bm25Rank;
    private static HashMap<Integer, HashMap<String, Integer>> sdmRank;
    private static HashMap<Integer, HashMap<String, Integer>> stressRank;
    private static HashMap<Integer, HashMap<String, Integer>> qlRank;

    public Setup() throws IOException {
        qrels = new HashMap<Integer, HashMap<String, Integer>>();
        bm25Rank = new HashMap<Integer, HashMap<String, Integer>>();
        sdmRank = new HashMap<Integer, HashMap<String, Integer>>();
        stressRank = new HashMap<Integer, HashMap<String, Integer>>();
        qlRank = new HashMap<Integer, HashMap<String, Integer>>();


        File qltrecrun = new File("ql.trecrun");
        FileReader fr1 = new FileReader(qltrecrun);
        BufferedReader trecReader = new BufferedReader(fr1);
        //System.out.println(trecReader.readLine());
        indexQL(trecReader);

        File qrel = new File("qrels");
        FileReader fr2 = new FileReader(qrel);
        BufferedReader qrelReader = new BufferedReader(fr2);
        //System.out.println(qrelReader.readLine());
        indexQrels(qrelReader);

        File sdm = new File("sdm.trecrun");
        FileReader fr3 = new FileReader(sdm);
        BufferedReader sdmReader = new BufferedReader(fr3);
        //System.out.println(sdmReader.readLine());
        indexSdm(sdmReader);

        File stress = new File("stress.trecrun");
        FileReader fr4 = new FileReader(stress);
        BufferedReader stressReader = new BufferedReader(fr4);
        //System.out.println(stressReader.readLine());
        indexStress(stressReader);

        File bm25 = new File("bm25.trecrun");
        FileReader fr5 = new FileReader(bm25);
        BufferedReader bm25Reader = new BufferedReader(fr5);
        //System.out.println(bm25Reader.readLine());
        indexBM25(bm25Reader);

        //System.out.println(qrels.get(612).values());

        //Get the rank list for a particular doc
        Set<String> entrySet = MapUtil.sortByValue(bm25Rank.get(612)).keySet();
        Iterator<String> it = entrySet.iterator();
        //System.out.println(it.next());
    }

    private void indexQL(BufferedReader br) throws IOException {
        String line;
        while((line = br.readLine()) != null){
            String[] split = line.split(" ");
            if(qlRank.containsKey(Integer.parseInt(split[0]))){
                qlRank.get(Integer.parseInt(split[0])).put(split[2],Integer.parseInt(split[3]));
            }
            else{
                qlRank.put(Integer.parseInt(split[0]), new HashMap<String, Integer>());
                qlRank.get(Integer.parseInt(split[0])).put(split[2],Integer.parseInt(split[3]));
            }
        }
    }

    private void indexBM25(BufferedReader br) throws IOException {
        String line;
        while((line = br.readLine()) != null){
            String[] split = line.split(" ");
            if(bm25Rank.containsKey(Integer.parseInt(split[0]))){
                bm25Rank.get(Integer.parseInt(split[0])).put(split[2],Integer.parseInt(split[3]));
            }
            else{
                bm25Rank.put(Integer.parseInt(split[0]), new HashMap<String, Integer>());
                bm25Rank.get(Integer.parseInt(split[0])).put(split[2],Integer.parseInt(split[3]));
            }
        }
    }

    private void indexStress(BufferedReader br) throws IOException {
        String line;
        while((line = br.readLine()) != null){
            String[] split = line.split(" ");
            if(stressRank.containsKey(Integer.parseInt(split[0]))){
                stressRank.get(Integer.parseInt(split[0])).put(split[2],Integer.parseInt(split[3]));
            }
            else{
                stressRank.put(Integer.parseInt(split[0]), new HashMap<String, Integer>());
                stressRank.get(Integer.parseInt(split[0])).put(split[2],Integer.parseInt(split[3]));
            }
        }
    }

    private void indexSdm(BufferedReader br) throws IOException {
        String line;
        while((line = br.readLine()) != null){
            String[] split = line.split(" ");
            if(sdmRank.containsKey(Integer.parseInt(split[0]))){
                sdmRank.get(Integer.parseInt(split[0])).put(split[2],Integer.parseInt(split[3]));
            }
            else{
                sdmRank.put(Integer.parseInt(split[0]), new HashMap<String, Integer>());
                sdmRank.get(Integer.parseInt(split[0])).put(split[2],Integer.parseInt(split[3]));
            }
        }
    }

    private void indexQrels(BufferedReader br) throws IOException {
        String line;
        while((line = br.readLine()) != null){
            String[] split = line.split(" ");
            if(qrels.containsKey(Integer.parseInt(split[0]))){
                qrels.get(Integer.parseInt(split[0])).put(split[2],Integer.parseInt(split[3]));
            }
            else{
                qrels.put(Integer.parseInt(split[0]), new HashMap<String, Integer>());
                qrels.get(Integer.parseInt(split[0])).put(split[2],Integer.parseInt(split[3]));
            }
        }
    }

    public static HashMap<Integer, HashMap<String, Integer>> getqlRank() {
        return qlRank;
    }

    public static  HashMap<Integer, HashMap<String, Integer>> getbm25Rank() {
        return bm25Rank;
    }

    public static HashMap<Integer, HashMap<String, Integer>> getQrels() {
        return qrels;
    }

    public static HashMap<Integer, HashMap<String, Integer>> getsdmRank() {
        return sdmRank;
    }

    public static HashMap<Integer, HashMap<String, Integer>> getstressRank() {
        return stressRank;
    }
    
}


