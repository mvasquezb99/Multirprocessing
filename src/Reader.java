import java.io.File;
import java.io.FileNotFoundException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {

    static final int PAGE_LIMIT = 2000;

    public static void main(String[] args) {
        Instant start = Instant.now();
        List<List<String>> list = new ArrayList<>();
        int limit = PAGE_LIMIT; // Each char equals 2 bytes; 4k = 4000 bytes
        List<String> currentData = new ArrayList<>();
        try {
            File myObj = new File(args[0]);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if (line.length() <= limit) {
                    limit -= line.length();
                    currentData.add(line);
                } else if (line.length() >= limit) {
                    while (true) {
                        if (line.length() >= limit) {
                            String sub1 = line.substring(0, limit);
                            currentData.add(sub1);
                            if (currentData.get(0).length() > 0)
                                list.add(currentData);
                            currentData = new ArrayList<>();
                            line = line.substring(limit);
                            limit = PAGE_LIMIT;
                        } else {
                            if (line.length() <= limit) {
                                limit -= line.length();
                                currentData.add(line);
                            } else {
                                if (currentData.get(0).length() > 0)
                                    list.add(currentData);
                                limit = PAGE_LIMIT;
                                currentData = new ArrayList<>();
                                currentData.add(line);
                                limit -= line.length();
                            }
                            break;
                        }
                    }
                } else {
                    limit = PAGE_LIMIT;
                    if (currentData.get(0).length() > 0)
                        list.add(currentData);
                    currentData = new ArrayList<>();
                    currentData.add(line);
                    limit -= line.length();
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
    }
}
