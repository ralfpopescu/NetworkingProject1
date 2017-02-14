import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.PatternSyntaxException;


/**
 * Created by ralfpopescu on 1/26/17.
 */
public class smsengineTCP {

    public static void main(String args[]){

        String badInput = "0 -1 ERROR";

        int portNum = Integer.parseInt(args[0]);
        ArrayList<String> susWords = new ArrayList<String>();
        int spamScore = 0;
       String susWordsInSMS = "";
        OutputStreamWriter osw;

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
            }
        }

        try {
            ServerSocket welcomeSocket = new ServerSocket(portNum);
            String clientSentence ="";

            while(true)
            {
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                clientSentence = inFromClient.readLine();
                System.out.println("Received: " + clientSentence);
                String[] splitArray = clientSentence.split("\\s+");




                for(String word:susWords){
                    if(clientSentence.toLowerCase().contains(word.toLowerCase())){
                        susWordsInSMS = susWordsInSMS + " " + word;
                        for(int i = 0; i < splitArray.length ; i++){
                            if(splitArray[i].equals(word)){
                                spamScore++;
                            }
                        }
                    }
                }

                System.out.println(susWordsInSMS);
                float spamScoreFloat = (float)(splitArray.length) / (float)spamScore;

                String str = "" + susWords.size() + " " + spamScoreFloat + " " + susWordsInSMS + '\n';

                if(splitArray.length > 1000 || splitArray.length == 0){
                    str = badInput;
                }

                outToClient.writeBytes(str);
                osw =new OutputStreamWriter(connectionSocket.getOutputStream(), "UTF-8");
                osw.write(str, 0, str.length());
                spamScore = 0;
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
