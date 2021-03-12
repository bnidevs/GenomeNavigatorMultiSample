package presentation;

/* 
 *
 * Server I/O - CGI GET
 *
*/ 
 
import java.net.*;
import java.io.*;

public class browserServio
{
    private String sHost;
    private int    sPort;
    public aBrowser parent;
    static Socket           sock;
    static PrintStream      dout;
    static DataInputStream  din;

    public browserServio(String host, int port, aBrowser parent) {
	this.parent = parent;
        sHost = host;
        sPort = port;
        if (port < 0) sPort = 80;
    }

    public browserServio(String host, aBrowser parent) {
	this.parent = parent;
        sHost = host;
        sPort = 80;
    }

    public DataInputStream openServerConnection(String data){
        String line;

        try {
            sock = new Socket(sHost, sPort);
            dout = new PrintStream(sock.getOutputStream());
            din  = new DataInputStream(sock.getInputStream());
        
	    /* send get data */
            dout.print("GET "+data+" HTTP/1.0\r\n");
//            dout.print("GET "+script+"?A=PUT&MSG="+URLEncoder.encode(data)+" HTTP/1.0\r\n");
            dout.print("\r\n");

	    /* skip http header */
            while((line = din.readLine()) != null){
		System.out.println("Script: "+line);
              if(line.startsWith("DERBROWSER")) break;
	    }
	    line = din.readLine();
	    System.out.println("Last Script: "+line); 
	    if(line.equals("OK")) return (din);
	    else{
	      if(!line.equals("ERROR")) System.out.println("Invalid ACK: "+line);
	      return null;
	    }
        }
        catch (Exception e){
            System.out.println("openServerConnection failed: "+sHost+sPort);
            return null;
        }
    }

    public boolean closeServerConnection(){
        try {
            dout.close();
            din.close();
            sock.close();
            return true;
        }
        catch (Exception e){
            System.out.println("closeServerConnection failed: "+sHost+sPort);
            return false;
        }
    }
}
