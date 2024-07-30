import java.io.File;
import java.io.FileNotFoundException;
import java.time.*;
import java.util.Scanner;

public class Reader {
    public static void main(String[] args) {
        Instant start = Instant.now();
        try {

            File myObj = new File(args[0]);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                @SuppressWarnings("unused")
                String data = myReader.nextLine();
                // System.out.println(data);
            }
            myReader.close();
            LocalTime finishTime = java.time.LocalDateTime.now().toLocalTime();
            Instant end = Instant.now();
            System.out.println("Process with PID: " + ProcessHandle.current().pid() + " File: " + args[0]
                    + " Finish time: " + finishTime
                    + " Time in process (millis): "
                    + Duration.between(start, end).toMillis() + "\n");
            // The read time includes the time the process in not running
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
