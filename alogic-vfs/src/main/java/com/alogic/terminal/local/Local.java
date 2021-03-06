package com.alogic.terminal.local;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.alogic.terminal.Command;
import com.alogic.terminal.Resolver;
import com.alogic.terminal.Terminal;
import com.anysoft.util.BaseException;
import com.anysoft.util.DefaultProperties;
import com.anysoft.util.IOTools;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 本地ProcessBuilder实现
 * 
 * @author duanyy
 * 
 * @version 1.6.7.11 [20170203 duanyy] <br>
 * - 修正linux操作系统下无cmd指令问题 <br>
 */
public class Local extends Terminal.Abstract{
	protected String encoding = "gbk";

	@Override
	public int exec(Resolver resolver,String... cmd) {
		Command simple = new Command.Simple(resolver,cmd);
		return exec(simple);
	}

	@Override
	public int exec(Command command) {
		String[] cmds = command.getCommands();
		int ret = 0;
		for (String cmd:cmds){
			if (StringUtils.isNotEmpty(cmd)){
				try {
					ProcessBuilder pb = new ProcessBuilder(cmd.split(" "));
					
					pb.redirectErrorStream(true);
					Process p = pb.start();
					resolveResult(command,cmd,p.getInputStream());
					
					ret = p.waitFor();
				} catch (IOException e) {
					throw new BaseException("core.e1004",String.format("Command[%s] execute error:%s",cmd,e.getMessage()));
				} catch (InterruptedException e) {
					throw new BaseException("core.e1006",String.format("Command[%s] execute error:%s",cmd,e.getMessage()));
				}
			}
		}
		return ret;
	}
	
	protected void resolveResult(Command command,String cmd, InputStream in) throws IOException{
		BufferedReader br = null;
		try {
	        br = new BufferedReader(new InputStreamReader(in,encoding));  
	        
	        Object cookies = command.resolveBegin(cmd);
	        while (true)  
	        {  
	            String line = br.readLine();
	            if (line == null)  
	                break;  
	            command.resolveLine(cookies, line);
	        } 
	        
	        command.resolveEnd(cookies);
		}finally{
			IOTools.close(br);
		}
	}
	
	@Override
	public boolean changePassword(String newPwd,Resolver resolver) {
		// nothing to do
		return false;
	}	

	@Override
	public void configure(Properties p) {
		encoding = PropertiesConstants.getString(p,"encoding",encoding,true);
	}

	@Override
	public void close()  {
		disconnect();
	}

	@Override
	public void connect() {
		// nothing to do
	}

	@Override
	public void disconnect() {
		// nothing to do
	}

	public static void main(String[] args){
		Properties p = new DefaultProperties();

		Terminal shell = new Local();
		shell.configure(p);
		
		try {
			shell.connect();
			System.out.println(shell.exec(new Command.Simple("echo 0")));
			shell.disconnect();
		}finally{
			IOTools.close(shell);
		}
	}
}