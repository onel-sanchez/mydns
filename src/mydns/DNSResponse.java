/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydns;

/**
 *
 * @author One
 */
public class DNSResponse{
    private byte[] DNSmessage;    
    private int AnswerCount;
    private int AuthorityCount;
    private int AdditionalCount;
    private RR answerServer;
    private RR authorityServer;
    private RR additionalServer;
    private int pointer;

    public RR getAnswerServer() {
        return answerServer;
    }

    public RR getAuthorityServer() {
        return authorityServer;
    }

    public RR getAdditionalServer() {
        return additionalServer;
    }
    
    public int getAnswerCount() {
        return AnswerCount;
    }

    public int getAuthorityCount() {
        return AuthorityCount;
    }

    public int getAdditionalCount() {
        return AdditionalCount;
    }
        
    public DNSResponse(byte[] DNSmessage) {
            this.DNSmessage = DNSmessage;
            this.AnswerCount = DNSmessage[7];
            this.AuthorityCount = DNSmessage[9];
            this.AdditionalCount = DNSmessage[11];
            this.answerServer = new RR("","","");
            this.authorityServer = new RR("","","");
            this.additionalServer = new RR("","","");
            this.pointer = 11;
            
            System.out.println("Reply received. Content overview:");
            System.out.println("     " + String.valueOf(AnswerCount) 
                    + " Answers.");
            System.out.println("     " + String.valueOf(AuthorityCount) 
                    + " Intermediate Name Servers.");
            System.out.println("     " + String.valueOf(AdditionalCount) 
                    + " Additional Information Records.");
            
            jumpQuery();
            int flag = readAnswer();
            if(flag == 0){
               readRR();
               boolean ipAddressFound = false;
               while(!ipAddressFound){
                   jumpRR();
                   if(DNSmessage[pointer + 4] == 1)
                       ipAddressFound = true;
               }
               readIpRR();              
            } 
        }
    
    public void jumpQuery(){
        int wordLength = DNSmessage[12];
        pointer++;
        while(wordLength!=0){
            pointer += wordLength + 1; 
            wordLength = DNSmessage[pointer];
        }
        pointer += 4; //Jump QTYPE and QCLASS
    }
    
    public void jumpRR(){
        pointer += 12; 
        int dataLength = DNSmessage[pointer];
        pointer += dataLength;
    }
    
    private String readData(){
        int wordLength;
        String domain = "";
        pointer += 13; //Jump NAME TYPE CLASS TTL LENGTH
        wordLength = DNSmessage[pointer];
        pointer++;
        while(wordLength!=0 && wordLength > 0){
            for(int i = 0; i < wordLength; i++){
                domain += (char) DNSmessage[pointer];
                pointer++;
            }
            domain += ".";
            wordLength = DNSmessage[pointer];
            pointer++;
        }
        if(wordLength < 0)
            pointer++;
        pointer--; //return pointer to the begining of the next RR
        return domain;
    }
    
    private String readIpAddress(){
        String IP = String.valueOf(unsignedByteToInt(DNSmessage[pointer + 13]))
                + "." + String.valueOf(unsignedByteToInt(DNSmessage[pointer + 14]))
                + "." + String.valueOf(unsignedByteToInt(DNSmessage[pointer + 15]))
                + "." + String.valueOf(unsignedByteToInt(DNSmessage[pointer + 16]));
        return IP;
    }
    
    private int readAnswer(){
        if(AnswerCount == 0){
            System.out.println("Answer section:");
            return 0;
        }
        else{
           
           this.answerServer.setDomain(readName(DNSmessage[29]));
           pointer = 27;
           this.answerServer.setIpAddress(readIpAddress());
           System.out.println("Answer section:");
           System.out.println("\tName: " + this.answerServer.getDomain()
                   + "\tIP: " + this.answerServer.getIpAddress());
           return 1;
        }
    }
    
    private void readRR(){
        authorityServer.setDomain(readData());
        System.out.println("Authoritive section:");
        System.out.println("\tName Server: " + this.authorityServer.getDomain());
    }
    
    private void readIpRR(){
        authorityServer.setIpAddress(readIpAddress());
        System.out.println("Additional section:");
        System.out.println("\tName: " + this.authorityServer.getDomain() 
                + "\tIP: " + this.authorityServer.getIpAddress());
    }
    
    private String readName(int index){
        int length;
        String domain = "";
        int offset = index; //Jump NAME TYPE CLASS TTL LENGTH
        length = DNSmessage[offset];
        offset++;
        while(length!=0 && length > 0){
            for(int i = 0; i < length; i++){
                domain += (char) DNSmessage[offset];
                offset++;
            }
            domain += ".";
            length = DNSmessage[offset];
            offset++;
        }
        return domain;
    }
   
    
    private static int unsignedByteToInt(byte b) {
    return (int) b & 0xFF;
  }
}

