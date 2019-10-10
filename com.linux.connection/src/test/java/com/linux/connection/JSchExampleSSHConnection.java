package com.linux.connection;
import java.io.InputStream;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class JSchExampleSSHConnection {

	@Test
	@Parameters({"server"})
	public  void run(String server) {
		String host="ind-expertus01.emeter.com";
	    String user="eip";
	    String password="eippass";
	   // String server=System.getProperty("server");
	    String command1="expertus8_3 -e "+ server + " /home/eip/automation/UI\\ Automation/leena_rel8.3/getestdata/scripts/uidatacreation.suite -l debug"; 
	    try {
		java.util.Properties config = new java.util.Properties(); 
    	config.put("StrictHostKeyChecking", "no");
    	JSch jsch = new JSch();
    	Session session=jsch.getSession(user, host, 22);
		session.setPassword(password);
    	session.setConfig(config);
    	session.connect();
    	System.out.println("Connected");
    	
    	Channel channel=session.openChannel("exec");
      
       // ((ChannelExec)channel).setCommand("cd /home/eip/automation/UI Automation/leena_rel8.3/getestdata/scripts");
        ((ChannelExec)channel).setCommand(command1);
       
        
        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);
       
        InputStream in=channel.getInputStream();
        channel.connect();
        System.out.println("org created successfully");
        byte[] tmp=new byte[1024];
        while(true){
          while(in.available()>0){
            int i=in.read(tmp, 0, 1024);
            if(i<0)break;
            System.out.print(new String(tmp, 0, i));
          }
          if(channel.isClosed()){
            System.out.println("exit-status: "+channel.getExitStatus());
            break;
          }
          try{Thread.sleep(1000);}catch(Exception ee){}
        }
        channel.disconnect();
        session.disconnect();
        System.out.println("DONE");
    } catch(Exception e){
    	e.printStackTrace();
    }

	}

}
