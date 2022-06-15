import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TestDriver {
    public static void main(String[] args) {
        genData();
        BipartiteGraph g = new BipartiteGraph();
        g.dinitz();
        boolean noDupes = g.duplicateMatchesTest();
        if(!noDupes) {
            System.exit(1);
        }
    }

    public static void genData() {
        Random rand = new Random();
        int size = rand.nextInt(1000);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/program3data.txt"));
            writer.write(size * 2 + "\n");
            for (int i = 1; i <= size * 2; i++) {
                writer.write(i + "\n");
            }

            int numEdges = rand.nextInt(size * size);
            writer.write(numEdges + "\n");

            for (int i = 0; i < numEdges; i++) {
                int u = rand.nextInt(size - 1) + 1;
                int v = rand.nextInt(size - 1) + 1 + size;
                writer.write(u + " " + v + "\n");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
