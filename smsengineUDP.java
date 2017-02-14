import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;

/**
 * Created by ralfpopescu on 1/28/17.
 */
public class smsengineUDP {
    public static void main(String args[]) throws Exception
    {
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


        DatagramSocket serverSocket = new DatagramSocket(portNum);
        byte[] sendData = new byte[1024];


        while(true)
        {

            try {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket); //get packet from client
                spamScore = 0;

                String sentence = new String(receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);

                sentence = sentence.replace("!", ""); //remove all questionable characters
                sentence = sentence.replace("?", "");
                sentence = sentence.replace(",", "");
                sentence = sentence.replace(".", "");


                String[] splitArray = sentence.split("\\s+"); //split by spaces


                for (String word : susWords) { //counts number of suspicious words in message
                    if (sentence.toLowerCase().contains(word.toLowerCase())) {
                        susWordsInSMS = susWordsInSMS + " " + word;
                        for (int i = 0; i < splitArray.length; i++) {
                            if (splitArray[i].equals(word)) {
                                spamScore++;
                            }
                        }
                    }
                }




                InetAddress IPAdd = receivePacket.getAddress();
                int port = receivePacket.getPort(); // packet information

                float spamScoreFloat = (float)(splitArray.length) / (float)spamScore; //calculate actual score

                String str = "" + susWords.size() + " " + spamScoreFloat + " " + susWordsInSMS + '\n';

                if(splitArray.length > 1000 || splitArray.length == 0){
                    str = badInput;
                }


                sendData = str.getBytes(); //prepare data

                if (port != -1) {

                    DatagramPacket sendPacket =
                            new DatagramPacket(sendData, sendData.length, IPAdd, port);
                    serverSocket.send(sendPacket);
                }
            } catch (Exception e){
                System.out.println("none");
            }
        }
    }
}
