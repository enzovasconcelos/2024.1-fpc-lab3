import java.util.*;
import java.io.*;

public class Task implements Runnable {

    String path;
    List<Long> chuncks;

    public Task(String path) {
        this.path = path;
    }

    public void run() {
        try {
            chuncks = fileSum(this.path);
        } catch(IOException e) {}
    }

    public String getPath() {
        return path;
    }

    public List<Long> getFingerprint() {
        return chuncks;
    }

    private static List<Long> fileSum(String filePath) throws IOException {
        File file = new File(filePath);
        List<Long> chunks = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[100];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                long sum = sum(buffer, bytesRead);
                chunks.add(sum);
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
