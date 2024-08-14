import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FilesManager {

  static final int PAGE_LIMIT = 4096;

  private String folder;
  private String[] paths;
  private List<List<String>> list;
  private int limit = PAGE_LIMIT; // Each char equals 2 bytes; 4k = 4000 bytes
  private List<String> currentData;

  /**
   * @param folder folder path to work with (relative from your open folder)
   * @throws IllegalArgumentException
   *                                  if param is no folder
   */
  public FilesManager(String folder) throws IllegalArgumentException {
    if (!(new File(folder).isDirectory())) {
      throw new IllegalArgumentException();
    }
    this.folder = folder;
    this.list = new ArrayList<>();
    this.currentData = new ArrayList<>();
  }

  /**
   * Adds all the .csv files from the corresponding folder
   * 
   * @return number of .csv files found
   */
  public int addCsvFiles() {
    File directory = new File(this.folder);
    this.paths = directory.list(new FilenameFilter() {
      @Override
      public boolean accept(File directory, String name) {
        return name.toLowerCase().endsWith(".csv");
      }
    });
    return this.paths.length;
  }

  public void readAllFiles() {
    for (int i = 0; i < this.paths.length; i++) {
      this.readIndexFile(i);
    }
    if (currentData.get(0).length() > 0) {
      list.add(currentData);
    }
  }

  public void readIndexFile(int index) {
    try {
      Instant start = Instant.now();
      System.out.println("Reading file: " + this.paths[index]);
      File myObj = new File(this.folder + this.paths[index]);
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

      myReader.close();
      Instant end = Instant.now();
      System.out.println("Time in process (millis): " + Duration.between(start, end).toMillis());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public String getFolder() {
    return this.folder;
  }

  public String getPathsToString() {
    return Arrays.toString(this.paths);
  }

  public String[] getPaths() {
    return this.paths;
  }
}
