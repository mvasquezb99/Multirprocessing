import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Reader {
    public static void main(String[] args) {
        try {
            System.out.println("read file: " + args[0] + "\n");
            File myObj = new File(args[0]);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                @SuppressWarnings("unused")
                String data = myReader.nextLine();
                // System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  
    }
}
