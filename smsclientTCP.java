import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.*;

/**
 * Created by ralfpopescu on 1/26/17.
 */
public class smsclientTCP {

    public static void main(String args[]){
        String IPAddress = args[0];
        int portNum = Integer.parseInt(args[1]);
        ArrayList<String> smsMessage = new ArrayList<String>();
        OutputStreamWriter osw;

        Scanner sc2 = null;
        try {
            sc2 = new Scanner(new File(args[2]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc2.hasNextLine()) {
            Scanner s2 = new Scanner(sc2.nextLine());
            while (s2.hasNext()) {
                String s = s2.next();
                smsMessage.add(s);
                System.out.println(s);
            }
        }

        Socket MyClient;
        try {
            String sentence;
            String modifiedSentence;
            //BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
            Socket clientSocket = new Socket(IPAddress, portNum);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            osw =new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8");

            //sentence = inFromUser.readLine();

            String str = "";
            for(String s:smsMessage){
                str = str.concat(" " + s);
            }


            outToServer.writeBytes(str + '\n');
            osw.write(str, 0, str.length());
            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);
            clientSocket.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

}