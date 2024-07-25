import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
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
            Instant end = Instant.now();
            System.out.println("Process with PID: " + ProcessHandle.current().pid() + "File: " + args[0]
                    + " Available processors: "
                    + Runtime.getRuntime().availableProcessors() + " Start time (current seconds): "
                    + start.atZone(ZoneId.systemDefault()).getSecond() + " Time in process (millis): "
                    + Duration.between(start, end).toMillis() + "\n");
            // The read time includes the time the process in not running
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
