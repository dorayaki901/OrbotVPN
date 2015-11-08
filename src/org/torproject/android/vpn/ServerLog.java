package org.torproject.android.vpn;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.DatagramChannel;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Service;
import android.content.Intent;
import android.net.VpnService.Builder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class ServerLog extends Service implements Runnable {
	
	private Thread mThread;
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		mThread = new Thread(this, "ServerThread");
  	  	//start the service
  	  	mThread.start();
		//return super.onStartCommand(intent, flags, startId);
		return START_STICKY;  // Restart the service if got killed
	}



	@Override
	public void onDestroy() {
		Log.i("ServerLog", "Server Closing");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	int portNumber  = 9040;
	private ParcelFileDescriptor mInterface;
	  //a. Configure a builder for the interface.
	 // Builder builder = new Builder();
	@Override
	
	public synchronized void run() {
		Log.i("ServerLog", "Server Starting");
		String message = "";
        byte[] lmessage = new byte[3000];
        DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);
        DatagramSocket socket = null;
        
		try{
		/*	ServerSocket serverSocket = new ServerSocket(portNumber);
		    Socket clientSocket = serverSocket.accept();
		    //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		    BufferedReader stdIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		    String str = "";
		    Log.i("ServerLog", "Waiting read");  
			while ((str  = stdIn.readLine()) != null) {
		        Log.i("ServerLog", str + " " + stdIn);  
			}*/
	            socket = new DatagramSocket(portNumber);
	            while(true){
	            	Log.i("ServerLog", "receiveding");
	            	socket.receive(packet);
	            	message = new String(lmessage, 0, packet.getLength());
	            	Log.i("ServerLog", packet.getLength() + " Server rcv: " + message);

	            }
			/* while(true){
				 ;
			 }*/
			 
		}catch (Exception e) {
	  	  	  // Catch any exception
	  	  	  e.printStackTrace();
	  	  	} 
	  
	}

	
    public String getLocalIpAddress() {
        String ipv4;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    //System.out.println("ip1--:" + inetAddress);
                   // System.out.println("ip2--:" + inetAddress.getHostAddress());

                    // for getting IPV4 format
                    if ( !inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {

                        String ip = inetAddress.getHostAddress().toString();
                       // System.out.println("ip---::" + ip);
                        // return inetAddress.getHostAddress().toString();
                        return ipv4;
                    }
                }
            }
        }
        catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }
    
}
