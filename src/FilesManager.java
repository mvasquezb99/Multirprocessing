import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FilesManager {

  private String folder;
  private String[] paths;
  private List<List<String>> list;
  private int limit = 2000; // Each char equals 2 bytes; 4k = 4000 bytes
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
              limit = 2000;
            } else {
              if (line.length() <= limit) {
                limit -= line.length();
                currentData.add(line);
              } else {
                if (currentData.get(0).length() > 0)
                  list.add(currentData);
                limit = 2000;
                currentData = new ArrayList<>();
                currentData.add(line);
                limit -= line.length();
              }
              break;
            }
          }
        } else {
          limit = 2000;
          if (currentData.get(0).length() > 0)
            list.add(currentData);
          currentData = new ArrayList<>();
          currentData.add(line);
          limit -= line.length();
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
