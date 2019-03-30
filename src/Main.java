import hash.KetamaHash;
import hash.Murmur3Hash;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final String SAMPLE_FILE = "sample-data";

    public static String[] sampleData;

    public static void main(String[] args) throws InterruptedException {
//        fileWriter();
        sampleData = fileRead();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Date startTime = Calendar.getInstance().getTime();
        for (int i = 0; i < 10; i++) {
            executorService.submit(new Hasher(i * 100000, (i + 1) * 100000));
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        Date endTime = Calendar.getInstance().getTime();
        System.out.println("Time taken (ms): " + (endTime.getTime() - startTime.getTime()));

    }

    /**
     * Generate the sample data file.
     */
    private static void fileWriter() {
        UUID uuid;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(SAMPLE_FILE, true));
            for (int i = 0; i < 1000000; i++) {
                uuid = UUID.randomUUID();
                writer.append(uuid.toString());
                writer.append('\n');
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read sample data
     */
    private static String[] fileRead() {
        List<String> sampleData = new ArrayList<>();
        String[] output;

        try {
            File file = new File(SAMPLE_FILE);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                sampleData.add(st);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        output = new String[sampleData.size()];
        sampleData.toArray(output);

        return output;
    }

    public static long getKetamaHashedValue(String key) {
        return KetamaHash.hash(key);
    }

    public static long getMurmurHashedValue(String key) {
        return Murmur3Hash.hash(key);
    }

    public static long[] getMurmur128HashedValue(String key) {
        return Murmur3Hash.hash128(key);
    }


}

class Hasher implements Runnable {

    private int startPoint;
    private int stopPoint;

    public Hasher(int startPoint, int stopPoint) {

        this.startPoint = startPoint;
        this.stopPoint = stopPoint;
    }

    @Override
    public void run() {
        for (int i = startPoint; i < stopPoint; i++) {
            Main.getMurmurHashedValue(Main.sampleData[i]);
        }
    }
}
