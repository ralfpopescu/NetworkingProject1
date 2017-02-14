import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;

/**
 * Created by ralfpopescu on 1/28/17.
 */
public class smsclientUDP {

    public static void main(String args[]) throws Exception
    {

        String IPAddressArg = args[0];
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
            }
        }

        String str = "";
        for(String s:smsMessage){
            str = str.concat(" " + s);
        }

        int attempts = 0;
        boolean timeOut = false;



        DatagramSocket clientSocket = new DatagramSocket();
        clientSocket.setSoTimeout(2000);



        InetAddress IPAddress = InetAddress.getByName(IPAddressArg);
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        sendData = str.getBytes();

        String modifiedSentence = "no responses.";

        boolean received = false;
        while(attempts < 3 && !received) {
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNum);
                clientSocket.send(sendPacket);
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                modifiedSentence = new String(receivePacket.getData());
                received = true;
            } catch (SocketTimeoutException e) {
                attempts++;
                System.out.println("The server has not responded in 2 seconds. Trying again...");
                if (attempts > 2) {
                    timeOut = true;
                    System.out.println("No responses.");
                }
            }
        }

        //modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();
    }
}
