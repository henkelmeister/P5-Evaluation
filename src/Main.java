import java.io.*;
import java.util.ArrayList;

/*
NDCG@10 (there are multi-value relevance judgments in the data: 0,1,2)
MRR
P@5
P@20
Recall@20
F1@20
Mean Average Precision (MAP)
 */


public class Main {

    public static void main(String[] args) throws IOException {
        Setup init = new Setup();
        RecallatK recallatK = new RecallatK();
        ArrayList<String> recallatKFormatted = recallatK.calcRecall(20);

        PatK patK = new PatK();
        ArrayList<String> pAt5Formatted = patK.calcPatK(5);
        ArrayList<String> pAt20Formatted = patK.calcPatK(20);

        NDCGatK ndcGatK = new NDCGatK();
        ArrayList<String> ndcGatKFormatted = ndcGatK.calcNDCGatK(10);

        MAP map = new MAP();
        ArrayList<String> mapFormatted = map.calcMAP();

        MRR mrr = new MRR();
        ArrayList<String> mrrFormatted = mrr.calcMRR();

        F1atK f1ak = new F1atK();
        ArrayList<String> f1akFormatted = f1ak.calcF1atK(20);

        File textFile = new File("output.metrics");
        FileWriter writer = new FileWriter("output.metrics");

        for(int i=0; i < 4; i++){
            writer.write(ndcGatKFormatted.get(i));
        }
        for(int i=0; i < 4; i++){
            writer.write(mrrFormatted.get(i));
        }
        for(int i=0; i < 4; i++){
            writer.write(pAt5Formatted.get(i));
        }
        for(int i=0; i < 4; i++){
            writer.write(pAt20Formatted.get(i));
        }
        for(int i=0; i < 4; i++){
            writer.write(recallatKFormatted.get(i));
        }
        for(int i=0; i < 4; i++){
            writer.write(f1akFormatted.get(i));
        }
        for(int i=0; i < 4; i++){
            writer.write(mapFormatted.get(i));
        }
        writer.close();

    }
}
