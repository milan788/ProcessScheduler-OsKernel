package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.Pcb;
import java.util.PriorityQueue;;

public class CompletelyFair extends Scheduler {
	private PriorityQueue<CfPcbData> queue = new PriorityQueue<>(); 
	
	@Override
	public Pcb get(int cpuId) {
		CfPcbData pcbData = queue.poll();
		if(pcbData == null) return null;
		
		pcbData.updateWaitTime();
		
		Pcb pcb = pcbData.getPcb();
		pcb.setTimeslice(pcbData.getNewTimeslice());
		return pcb;
	}

	@Override
	public void put(Pcb pcb) {
		if(pcb.getPcbData() == null) 
			pcb.setPcbData(new CfPcbData(pcb));
		
		CfPcbData pcbData = (CfPcbData) pcb.getPcbData();
		
		pcbData.upadateExecTime();
		
		pcbData.startMessuringWaitTime();
		queue.add(pcbData);
	}

}
