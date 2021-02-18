package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.*;

public class CfPcbData extends PcbData implements Comparable<CfPcbData>{
	private long execTime;
	private long waitTime;
	private long startWaitTime;
	
	public CfPcbData(Pcb pcb) {
		super(pcb);
	}
	
	@Override
	public int compareTo(CfPcbData pcbData) {
		if(this.execTime > pcbData.execTime) return 1;
		else if(this.execTime == pcbData.execTime) return 0;
		else return -1;
	}

	
	public void upadateExecTime() {
		Pcb pcb = getPcb();
		Pcb.ProcessState lastState = pcb.getPreviousState();
		if(lastState == Pcb.ProcessState.BLOCKED || lastState == Pcb.ProcessState.CREATED) {
			//ako je novi nalet izvrsavanja, vreme izvrsavanja se meri od 0
			execTime = 0;
		}
		else if(lastState == Pcb.ProcessState.RUNNING) {
			//ako mu je istekao timeslice, dodaje se vreme izvrsavanja
			execTime += pcb.getExecutionTime();
		}
		else { System.out.println("Greska"); return; } 
	}
	
	public void startMessuringWaitTime() { startWaitTime = Pcb.getCurrentTime(); }
	public void updateWaitTime() { waitTime =  Pcb.getCurrentTime() - startWaitTime; }
	public long getNewTimeslice() { return waitTime / Pcb.getProcessCount(); }
	
}
