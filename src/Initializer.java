import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.io.BufferedReader;
import java.util.*;

public class Initializer {
    List<Process> processes = new ArrayList<>();
    public int pathsLength;
    public FilesManager manager;
    public String flag;

    /**
     * @param flag works to store the command flag received form App.class.
     *             At construction it creates a FileManger instance and saves all
     *             the .csv files in the path property, saving it's return
     *             value at this class property pathsLength.
     */
    public Initializer(String flag) {
        this.manager = new FilesManager("./output/");
        this.pathsLength = manager.addCsvFiles();
        this.flag = flag;
    }

    /**
     * @throws Exception
     *                   At calling the function is going to check for the flag
     *                   value and create a Process instance with the specified
     *                   command
     *                   for each of the files in the output folder.
     */
    public int Initialize() throws Exception {
        if (this.flag == "-s") {
            for (int i = 0; i < this.pathsLength; i++) {
                ProcessBuilder pb = new ProcessBuilder("java", "./Reader.java",
                        manager.getFolder() + manager.getPaths()[i]);
                Process p = pb.start();
                processes.add(p);
            }

            for (int i = 0; i < this.pathsLength; i++) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(processes.get(i).getInputStream())) ) {
                    String line; 
                    if ((line = reader.readLine()) != null) { 
                        System.out.println("Process with PID: " + processes.get(i).pid() + " " + line); 
                    }
                }
            }

            return 0;
        } else if (this.flag == "-m") {
            // TODO
            return 0;
        } else {
            manager.readAllFiles();
            return 0;
        }
    }

}
