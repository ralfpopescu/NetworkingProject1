import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;


/**
 * Created by ralfpopescu on 1/26/17.
 */
public class smsengineTCP {

    public static void main(String args[]){
        int portNum = Integer.parseInt(args[0]);
        ArrayList<String> susWords = new ArrayList<String>();
        int spamScore = 0;
       String susWordsInSMS = "";

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
                susWords.add(s);
                System.out.println(s);
            }
        }

        try {
            ServerSocket welcomeSocket = new ServerSocket(portNum);
            String clientSentence;
            String capitalizedSentence;

            while(true)
            {
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                clientSentence = inFromClient.readLine();
                System.out.println("Received: " + clientSentence);

                for(String word:susWords){
                    if(clientSentence.toLowerCase().contains(word.toLowerCase())){
                        if(!susWords.contains(word)) {
                            susWordsInSMS = susWordsInSMS + " " + word;
                        }
                        spamScore++;
                    }
                }

                capitalizedSentence = "" + susWords.size() + " " + spamScore + susWordsInSMS;
                outToClient.writeBytes(capitalizedSentence);
                spamScore = 0;
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
