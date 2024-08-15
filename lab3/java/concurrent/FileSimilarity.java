import java.io.*;
import java.util.*;

public class FileSimilarity {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java Sum filepath1 filepath2 filepathN");
            System.exit(1);
        }

        // Create a map to store the fingerprint for each file
        Map<String, List<Long>> fileFingerprints = new HashMap<>();

        List<Thread> threads = new ArrayList<Thread>();
        List<Task> tasks = new ArrayList<Task>();
        // Calculate the fingerprint for each file
        for (String path : args) {
            Task task = new Task(path);
            Thread thread = new Thread(task, path);
            thread.start();
            tasks.add(task);
            threads.add(thread);
        }

        // joining
        for (Thread t : threads)
            t.join();

        // Put in fingerprints
        for (Task t : tasks) {
            List<Long> fingerprint = t.getFingerprint();
            fileFingerprints.put(t.getPath(), fingerprint);
        }

        // Compare each pair of files
        for (int i = 0; i < args.length; i++) {
            for (int j = i + 1; j < args.length; j++) {
                String file1 = args[i];
                String file2 = args[j];
                List<Long> fingerprint1 = fileFingerprints.get(file1);
                List<Long> fingerprint2 = fileFingerprints.get(file2);
                float similarityScore = similarity(fingerprint1, fingerprint2);
                System.out.println("Similarity between " + file1 + " and " + file2 + ": " + (similarityScore * 100) + "%");
            }
        }
    }


    private static float similarity(List<Long> base, List<Long> target) {
        int counter = 0;
        List<Long> targetCopy = new ArrayList<>(target);

        for (Long value : base) {
            if (targetCopy.contains(value)) {
                counter++;
                targetCopy.remove(value);
            }
        }

        return (float) counter / base.size();
    }
}
