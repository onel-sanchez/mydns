/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydns;

import java.io.*;
import java.net.*;
import java.io.IOException;
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
    public static void main(String[] args) throws SocketException{
        
        DatagramSocket datagramSocket = new DatagramSocket();
        String domainName = "cs.fiu.edu";
        String rootAddress ="202.12.27.323";
        InetAddress root = null;
        try {
            root = InetAddress.getByName(rootAddress);
        } catch (UnknownHostException ex) {
            System.out.println("Unknown Host");
            Logger.getLogger(Mydns.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] doaminName = domainName.getBytes();
        DatagramPacket packet = new DatagramPacket(doaminName, 0, root, 53);
        
  
        
    }
    
    public byte[] IPtoBytes(String IP){
        
    }
    
}
