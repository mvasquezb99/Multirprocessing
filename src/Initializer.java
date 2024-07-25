import java.io.InputStreamReader;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.time.Duration;
import java.time.Instant;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Initializer {
    private List<Process> processes = new ArrayList<>();
    private int pathsLength;
    private FilesManager manager;
    private String[] flag;

    /**
     * @param flag works to store the command flag received form App.class.
     *             At construction it creates a FileManger instance and saves all
     *             the .csv files in the path property, saving it's return
     *             value at this class property pathsLength.
     */
    public Initializer(String[] flag) {
        try {
            if (flag.length == 3) {
                this.manager = new FilesManager(flag[2]);
                this.pathsLength = manager.addCsvFiles();
                this.flag = flag;
            } else {
                this.manager = new FilesManager(flag[1]);
                this.pathsLength = manager.addCsvFiles();
                this.flag = flag;
            }

        } catch (Exception e) {
            System.err.println("La direccion dada no es valida");
        }
    }

    /**
     * @throws Exception
     *                   At calling the function is going to check for the flag
     *                   value and decide which of the listed actions in the project
     *                   is going to execute.
     */
    public int Initialize() throws IOException {
        Instant startTime = Instant.now();
        long mainId = ProcessHandle.current().pid();

        if (this.flag.length != 0 && this.flag.length == 3 && this.flag[0].equals("-m")) { // path -m
            for (int i = 0; i < this.pathsLength; i++) {
                ProcessBuilder pb = new ProcessBuilder("java", "./Reader.java",
                        manager.getFolder() + manager.getPaths()[i]);
                Process p = pb.start();
                processes.add(p);
            }
        } else if (this.flag.length != 0 && this.flag.length == 3 && this.flag[0].equals("-s")) { // path -s
            for (int i = 0; i < this.pathsLength; i++) {

                // Get parent PID and current core
                String[] commands = { "ps", "-o", "psr", "-p", "" + mainId };
                Process proc = Runtime.getRuntime().exec(commands);
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                // Read the output from the command
                String s;
                String core = "0";
                while ((s = stdInput.readLine()) != null) {
                    if (!(s.equals("PSR".strip()))) {
                        core = s.strip();
                    }
                }

                // Create and start new process
                ProcessBuilder pb = new ProcessBuilder("taskset", "-c", core, "java", "./Reader.java",
                        manager.getFolder() + manager.getPaths()[i]);
                Process p = pb.start();
                processes.add(p);
            }
        } else { // path no flag
            manager.readAllFiles();
        }

        Instant endTime = Instant.now();
        long executionTime = Duration.between(startTime, endTime).toMillis();
        printData();
        // System.out.println("Parent PID: " + mainId);
        System.out.println("Execute total time: " + executionTime + " milliseconds");
        return 0;
    }

    private void printData() throws IOException {
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
