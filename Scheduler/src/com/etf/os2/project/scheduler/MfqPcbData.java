package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;

public class MfqPcbData extends PcbData {
	private MultilevelFeedbackQueue scheduler;
	private int currPriority; //red u kojem je bio pre nego sto je dobio procesor
	
	public MfqPcbData(Pcb pcb, MultilevelFeedbackQueue scheduler) {
		super(pcb);
		int priority = pcb.getPriority();
		if(priority > scheduler.getNumOfQueues() - 1)
			priority = scheduler.getNumOfQueues() - 1;
		this.currPriority = priority;
		this.scheduler = scheduler;
	}

	public void incPriority() {
		if(currPriority > 0)
			currPriority--;
	}

	public void decPriority() {
		if(currPriority < scheduler.getNumOfQueues() - 1)
			currPriority++;
	}
	
	public int getCurrPriority() {
		return currPriority;
	}
}
