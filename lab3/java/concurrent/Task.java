import java.util.*;
import java.io.*;
import java.util.concurrent.*;

public class Task implements Runnable {

    String path;
    Map<String, HashMap<Long, Boolean>> fileFingerprints;
    Semaphore mutex;

    public Task(String path, Map<String, HashMap<Long, Boolean>> fingerprints) {
        this.path = path;
        this.fileFingerprints = fingerprints;
        mutex = new Semaphore(1);
    }

    public void run() {
        try {
            HashMap<Long, Boolean> chuncks = fileSum(this.path);
            mutex.acquire();
            fileFingerprints.put(this.path, chuncks);
            mutex.release();
        } catch(IOException e) {} catch(InterruptedException e) {}
    }

    public String getPath() {
        return path;
    }

    private static HashMap<Long, Boolean> fileSum(String filePath) throws IOException {
        File file = new File(filePath);
        HashMap<Long, Boolean> chunks = new HashMap<Long, Boolean>();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[100];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                long sum = sum(buffer, bytesRead);
                chunks.put(sum, true);
            }
        }
        return chunks;
    }

    private static long sum(byte[] buffer, int length) {
        long sum = 0;
        for (int i = 0; i < length; i++) {
            sum += Byte.toUnsignedInt(buffer[i]);
        }
        return sum;
    }

}
