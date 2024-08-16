import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.io.RandomAccessFile;

public class Reader {

    static final int PAGE_LIMIT = 4096; // Each char equals 2 bytes; 4k = 4000 bytes

    public static void main(String[] args) throws IOException {
        Instant start = Instant.now();
        List<byte[]> list = new ArrayList<>();
        long limit = PAGE_LIMIT;
        long pointer = 0;
        long difference = 0;
        try {
            RandomAccessFile myRaf = new RandomAccessFile(args[0], "r");
            // File myObj = new File("./src/output/MXvideos.csv");
            myRaf.seek(0);
            long length = myRaf.length();
            byte[] line = new byte[PAGE_LIMIT];
            while (pointer <= length) {
                myRaf.readFully(line, 0, (int) limit);
                list.add(line);
                pointer += PAGE_LIMIT;
                myRaf.seek(pointer);
                difference = length - pointer;
                if (difference > 0 && difference < PAGE_LIMIT) {
                    limit = difference;
                    line = new byte[(int) limit];
                } else if (difference > PAGE_LIMIT) {
                    line = new byte[PAGE_LIMIT];
                }
            }
            myRaf.close();
            // for (int i = 0; i < list.size(); i++) {
            // System.out.println(list.get(i).length);
            // }
            LocalTime finishTime = java.time.LocalDateTime.now().toLocalTime();
            Instant end = Instant.now();
            System.out.println("Process with PID: " + ProcessHandle.current().pid() +
                    "File: " + args[0]
                    + " Finish time: " + finishTime
                    + " Time in process (millis): "
                    + Duration.between(start, end).toMillis()
                    + " # of pages " + list.size() + "\n");
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
