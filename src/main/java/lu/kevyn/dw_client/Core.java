/**
 * 
 * BSD 3-Clause License
 * 
 * Copyright (c) 2018+ Kevin Olinger
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *    
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *    
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package lu.kevyn.dw_client;

import lu.kevyn.dw_client.util.LogUtil;
import lu.kevyn.dw_client.util.SystemUtil;

import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;

import lu.kevyn.dw_client.input.InputReader;
import lu.kevyn.dw_client.system.SurveillanceJob;

public class Core {
	
	public Boolean DEBUG = false;
	
	public LogUtil log;
	public InputReader IR;
	public SystemUtil sysInfo;
	public WebSocketClient client;
	public SurveillanceJob survJob;
	
	public static String host = "localhost";
	public static Integer port = 8080;
	
	public static void main(String[] args) {
		new Core();
	}
	
	public Core() {
		log = new LogUtil();
		
		log.info("Starting ..");
		log.info("Initializing logging util(s) ..");

		IR = new InputReader(this);
		sysInfo = new SystemUtil(this);
		survJob = new SurveillanceJob(this);
		
		try {
			log.info("Starting socket client on "+ host +":"+ port +" ..");
			log.socket.info("Starting socket client on "+ host +":"+ port +" ..");
			
			client = new Client(this);
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
		}
		
		IR.start();
		
		log.emptyLine();
		log.info("Connecting to "+ host +":"+ port +" ..");
		log.socket.info("Connecting to "+ host +":"+ port +" ..");
		
		client.connect();
		//client.send("Used memory: "+ sysInfo.memory.getUsed() +" - Free memory: "+ sysInfo.memory.getFree() +" - Total memory: "+ sysInfo.memory.getTotal());
	}
	
	public void quit() {
		log.emptyLine();
		log.info("Stopping ..");
		
		IR.stop();
		client.close(0);
		
		log.info("DeviceWatcher Client stopped.");
		log.emptyLine();
		
		System.exit(0);
	}

}
