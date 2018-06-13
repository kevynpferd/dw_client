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

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import lu.kevyn.dw_client.event.ConnectEvent;
import lu.kevyn.dw_client.event.DisconnectEvent;

public class Client implements Runnable {

	Core DW;
	
	static Thread thread;
	private Socket socket = null;
	
	private String host = "localhost";
	private Integer port = 8080;
	
	public Client(Core DW) {
		this.DW = DW;
		
		//host = DW.config.get("Socket server > host");
		//port = DW.config.getInt("Socket server > port");
	}
	
	public void start() {
		if(thread != null) return;
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		DW.log.info("Stopping Socket.IO client ..");
		DW.log.socket.info("Stopping Socket.IO client ..");
		
		socket.disconnect();
		thread.interrupt();
		
		DW.log.socket.info("Stopped Socket.IO client.");
		
		thread = null;
	}
	
	@Override
	public void run() {
		try {
			socket = IO.socket("http://"+ host +":"+ port);
			socket.connect();
			
			socket.on(Socket.EVENT_CONNECT, new ConnectEvent(DW));
			socket.on(Socket.EVENT_DISCONNECT, new DisconnectEvent(DW));
		} catch (URISyntaxException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
}
