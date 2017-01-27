import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;


/**
 * Created by ralfpopescu on 1/26/17.
 */
public class smsengineTCP {

    public static void main(String args[]){
        String portNum = args[0];
        ArrayList<String> susWords = new ArrayList<String>();

        Scanner sc2 = null;
        try {
            sc2 = new Scanner(new File(args[1]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc2.hasNextLine()) {
            Scanner s2 = new Scanner(sc2.nextLine());
            while (s2.hasNext()) {
                String s = s2.next();
                System.out.println(s);
            }
        }
    }
}
