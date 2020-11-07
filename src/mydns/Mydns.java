/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydns;

import java.io.*;
import java.net.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author One
 */
public class Mydns {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException{
        DatagramSocket datagramSocket = new DatagramSocket();
        byte[] sendBuffer;
        byte[] receiveBuffer;
        DatagramPacket packet;
        DatagramPacket receive = null;
        boolean MyDns = false;
        while(MyDns){
            Scanner scanner = new Scanner(System.in);
            String[] NSLookup = scanner.nextLine().split(" ");
            if(NSLookup[0].equalsIgnoreCase("mydns"))
                MyDns = true;
            else
                System.out.println("Unknown command. Enter a new command line.");
        }
        String domainName = "cs.fiu.edu";//NSLookup[1];
        String dnsServer = "198.41.0.4";//NSLookup[2];
        DNSResponse dnsResponse;
        boolean found = false;
        while(!found){
            
            InetAddress root = InetAddress.getByName(dnsServer);
            sendBuffer = encodeUDP(domainName);
            
            System.out.println("----------------------------------------------------------------");
            System.out.println("DNS server to query: " + dnsServer);
            
            receiveBuffer = new byte [512];
            packet = new DatagramPacket(sendBuffer, sendBuffer.length, root, 53);
            receive = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            datagramSocket.send(packet);
            datagramSocket.receive(receive);
            dnsResponse = new DNSResponse(receive.getData());

            
            found = true;
            if(dnsResponse.getAnswerCount() > 0)
                found = true;
        }

       // for(int i = 0; i < receive.getLength(); i++)
         //   System.out.println(receive.getData()[i]);
    /**
     *
     * @param domainAddress
     * @return
     */
    }
    public static byte[] encodeUDP(String domainAddress) throws IOException{
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(byteOutputStream); 
        String[] domain = domainAddress.split("\\.");
        
        data.writeByte(0);
        data.writeByte(42);
        data.writeByte(0);
        data.writeByte(0);
        data.writeByte(0);
        data.writeByte(1);
        data.writeByte(0);
        data.writeByte(0);
        data.writeByte(0);
        data.writeByte(0);
        data.writeByte(0);
        data.writeByte(0);
        for(int i = 0; i < domain.length; i++){
            data.writeByte(domain[i].length());
            data.writeBytes(domain[i]);
        }
        data.writeByte(0);
        data.writeByte(0);
        data.writeByte(1);
        data.writeByte(0);
        data.writeByte(1);
        
        return byteOutputStream.toByteArray();
    }
}
