import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Scanner;

public class FilesManager {

  private String folder;
  private String[] paths;

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
  }

  public void readIndexFile(int index) {
    try {
      Instant start = Instant.now();
      System.out.println("Reading file: " + this.paths[index]);
      File myObj = new File(this.folder + this.paths[index]);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        @SuppressWarnings("unused")
        String data = myReader.nextLine();
        // System.out.println(data);
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
