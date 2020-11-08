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
public class mydns {

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
        String[] NSLookup = null;
        while(MyDns){
            Scanner scanner = new Scanner(System.in);
            NSLookup = scanner.nextLine().split(" ");
            if(NSLookup[0].equalsIgnoreCase("mydns"))
                MyDns = true;
            else
                System.out.println("Unknown command. Enter a new command line.");
        }
        String domainName = "cs.fiu.edu";//  NSLookup[1]; //
        String dnsServer =  "202.12.27.33";//NSLookup[2];  //

        boolean found = false;
        while(!found){
            try{
                InetAddress root = InetAddress.getByName(dnsServer);
                sendBuffer = encodeUDP(domainName);
                System.out.println("----------------------------------------------------------------");
                System.out.println("DNS server to query: " + dnsServer);

                receiveBuffer = new byte [512];
                packet = new DatagramPacket(sendBuffer, sendBuffer.length, root, 53);
                receive = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                datagramSocket.send(packet);
                datagramSocket.receive(receive);
                DNSResponse dnsResponse = new DNSResponse(receive.getData());
                dnsServer = dnsResponse.getAuthorityServer().getIpAddress();
                
                if(dnsResponse.getAnswerCount() > 0)
                    found = true;
            }
            catch(Exception ex){
            }
            
        }

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
