import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Reader {

    static final int PAGE_LIMIT = 4096; // Each char equals 2 bytes; 4k = 4000 bytes

    public static void main(String[] args) {
        Instant start = Instant.now();
        List<List<String>> list = new ArrayList<>();
        int limit = PAGE_LIMIT;
        List<String> currentData = new ArrayList<>();
        try {
            File myObj = new File(args[0]);
            // File myObj = new File("./src/output/MXvideos.csv");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                while (true) {
                    if (line.getBytes(StandardCharsets.UTF_8).length == limit) {
                        currentData.add(line);
                        list.add(currentData);
                        currentData = new ArrayList<>();
                        limit = PAGE_LIMIT;
                        break;
                    } else if (line.getBytes().length < limit) {
                        currentData.add(line);
                        limit -= line.getBytes().length;
                        break;
                    } else {
                        byte[] sub = Arrays.copyOf(line.getBytes(), limit);
                        currentData.add(new String(sub, StandardCharsets.UTF_8));
                        list.add(currentData);
                        currentData = new ArrayList<>();
                        line = line.substring(new String(sub, StandardCharsets.UTF_8).length());
                        limit = PAGE_LIMIT;
                    }
                }
            }
            if (currentData.get(0).length() > 0) {
                list.add(currentData);
            }
            myReader.close();
            LocalTime finishTime = java.time.LocalDateTime.now().toLocalTime();
            Instant end = Instant.now();
            System.out.println("Process with PID: " + ProcessHandle.current().pid() + "File: " + args[0]
                    + " Finish time: " + finishTime
                    + " Time in process (millis): "
                    + Duration.between(start, end).toMillis() + "\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // for (List<String> d : list) {
        // System.out.println("--------------------");
        // for (String s : d) {
        // System.out.println(s.getBytes().length);
        // }
        // }
        // System.out.println("pag: " + list.size());
    }
}
