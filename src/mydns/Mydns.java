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
        String domainName = "cs.fiu.edu";
        String DNS_SERVER = "198.41.0.4";
        InetAddress root = InetAddress.getByName(DNS_SERVER);
        byte[] sendBuffer = encodeUDP(domainName);
        byte[] receiveBuffer = new byte [512];
   
        
        
        
        
        
        DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, root, 53);
        DatagramPacket receive = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        datagramSocket.send(packet);
        datagramSocket.receive(receive);
        for(int i = 0; i < receive.getLength(); i++)
            System.out.print(receive.getData()[i]+".");
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
    ByteBuffer test;
    public static byte[] decodeUDP(byte[] DNSMessage){
        return null;
    }
    
    public class DNSResponse{
        private int AnswerCount;
        private int AuthorityCount;
        private int AdditianlCount;
    } 
}
