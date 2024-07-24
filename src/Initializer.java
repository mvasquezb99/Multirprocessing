import java.io.InputStreamReader;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.time.Duration;
import java.time.Instant;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Initializer {
    List<Process> processes = new ArrayList<>();
    public int pathsLength;
    public FilesManager manager;
    public String[] flag;

    /**
     * @param flag works to store the command flag received form App.class.
     *             At construction it creates a FileManger instance and saves all
     *             the .csv files in the path property, saving it's return
     *             value at this class property pathsLength.
     */
    public Initializer(String[] flag) {
        this.manager = new FilesManager("./output/");
        this.pathsLength = manager.addCsvFiles();
        this.flag = flag;
    }

    /**
     * @throws Exception
     *                   At calling the function is going to check for the flag
     *                   value and decide which of the listed actions in the project
     *                   is going to execute.
     */
    public int Initialize() throws Exception {
        Instant startTime = Instant.now();
        if (this.flag.length != 0 && this.flag[0].equals("-m")) {
            for (int i = 0; i < this.pathsLength; i++) {
                ProcessBuilder pb = new ProcessBuilder("java", "./Reader.java",
                        manager.getFolder() + manager.getPaths()[i]);
                Process p = pb.start();
                processes.add(p);
            }
        } else if (this.flag.length != 0 && this.flag[0].equals("-s")) {
            for (int i = 0; i < this.pathsLength; i++) {
                ProcessBuilder pb = new ProcessBuilder("java", "-XX:ActiveProcessorCount=1", "./Reader.java",
                        manager.getFolder() + manager.getPaths()[i]);
                Process p = pb.start();
                processes.add(p);
            }
        } else {
            manager.readAllFiles();
        }
        Instant endTime = Instant.now();
        long executionTime = Duration.between(startTime, endTime).toMillis();
        printData();
        System.out.println("Execute total time: " + executionTime + " miliseconds");
        return 0;
    }

    public void printData() throws IOException {
        if (this.processes.size() != 0) {
            for (int i = 0; i < this.pathsLength; i++) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(processes.get(i).getInputStream()))) {
                    String line;
                    if ((line = reader.readLine()) != null) {
                        System.out.println("Process with PID: " + processes.get(i).pid() + " " + line);
                    }
                    processes.get(i).destroy();
                }
            }
        }
    }

}
