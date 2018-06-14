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

package lu.kevyn.dw_client.system;

import java.util.Timer;
import java.util.TimerTask;

import lu.kevyn.dw_client.Core;

public class SurveillanceJob implements Runnable {
	
	Core DW;

	static Thread thread = null;
	static Timer timer = null;
	
	public SurveillanceJob(Core DW) {
		this.DW = DW;
	}
	
	public void start() {
		if(thread != null) return;
		
		thread = new Thread(this);
		thread.start();
		
		DW.log.info("Surveillance job started.");
	}
	
	public void stop() {
		timer.cancel();
		timer.purge();
		timer = null;
		
		thread.interrupt();
		thread = null;
		
		DW.log.info("Surveillance job finished.");
	}
	
	@Override
	public void run() {
		timer = new Timer();
		
		timer.schedule(new TimerTask() { 
			public void run() {
				DW.client.send("Used memory: "+ DW.sysInfo.memory.getUsed() +" - Free memory: "+ DW.sysInfo.memory.getFree() +" - Total memory: "+ DW.sysInfo.memory.getTotal());
				DW.sysInfo.cpu.getUsage();
				DW.client.sendPing();
			}
		}, 0, 2000);
	}

}
