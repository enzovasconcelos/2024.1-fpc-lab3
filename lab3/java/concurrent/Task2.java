import java.io.*;
import java.util.*;

public class Task2 implements Runnable {

    HashMap<Long, Boolean> h1;
    HashMap<Long, Boolean> h2;
    float result;
    String file1;
    String file2;

    public Task2(HashMap<Long, Boolean> h1, HashMap<Long, Boolean> h2, String file1, String file2) {
        this.h1 = h1;
        this.h2 = h2;
        this.file1 = file1;
        this.file2 = file2;
    }

    public void run() {
        result = similarity(h1, h2);
        System.out.println("Similarity between " + file1 + " and " + file2 + ": " + (result * 100) + "%");
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

    public float getResult() {
        return result;
    }

}
