import java.io.*;
import java.util.*;

public class FileSimilarity {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java Sum filepath1 filepath2 filepathN");
            System.exit(1);
        }

        // Create a map to store the fingerprint for each file
        Map<String, HashMap<Long, Boolean>> fileFingerprints = new HashMap<>();

        List<Thread> threads = new ArrayList<Thread>();
        List<Task> tasks = new ArrayList<Task>();
        // Calculate the fingerprint for each file
        for (String path : args) {
            Task task = new Task(path, fileFingerprints);
            Thread thread = new Thread(task, path);
            thread.start();
            tasks.add(task);
            threads.add(thread);
        }

        // joining
        for (Thread t : threads)
            t.join();

        // Compare each pair of files
        for (int i = 0; i < args.length; i++) {
            for (int j = i + 1; j < args.length; j++) {
                String file1 = args[i];
                String file2 = args[j];
                HashMap<Long, Boolean> fingerprint1 = fileFingerprints.get(file1);
                HashMap<Long, Boolean> fingerprint2 = fileFingerprints.get(file2);
                Thread t = new Thread(new Task2(fingerprint1, fingerprint2, file1, file2), i + " " + j);
                t.start();
                //float similarityScore = similarity(fingerprint1, fingerprint2);
                //System.out.println("Similarity between " + file1 + " and " + file2 + ": " + (similarityScore * 100) + "%");
            }
        }
    }


    private static float similarity(HashMap<Long, Boolean> base, HashMap<Long, Boolean> target) {
        int counter = 0;
        HashMap<Long, Boolean> targetCopy = new HashMap<Long, Boolean>(target);

        for (Long value : base.keySet()) {
            if (targetCopy.containsKey(value)) {
                counter++;
                targetCopy.remove(value);
            }
        }

        return (float) counter / base.size();
    }
}
