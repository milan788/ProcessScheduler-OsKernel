package com.etf.os2.project.scheduler;

import java.util.PriorityQueue;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.Pcb.ProcessState;

public class ShortestJobFirst extends Scheduler {
	private boolean preemptive;
	private double alfa;
	private PriorityQueue<SjfPcbData> queue; 
	
	public ShortestJobFirst(double alfa, boolean preemptive) {
		if(alfa > 1 || alfa < 0) {
			System.out.println("Pogresan unos za alfa");
			System.exit(1);
		}
		this.alfa = alfa;
		this.preemptive = preemptive;
		this.queue = new PriorityQueue<SjfPcbData>();
	}

	@Override
	public Pcb get(int cpuId) {
		SjfPcbData ret = queue.poll();
		if(ret == null) return null;
		return ret.getPcb();
	}

	@Override
	public void put(Pcb pcb) {
		if (pcb == null) return;
		
		if(pcb.getPcbData() == null) {
			pcb.setPcbData(new SjfPcbData(pcb,0)); 
		}
		
		SjfPcbData pcbData = (SjfPcbData) pcb.getPcbData();
		pcbData.setPrediction(pcb.getExecutionTime(), alfa);
		
		if(preemptive) 
			preemptOthers((SjfPcbData)pcb.getPcbData());
		
		queue.add((SjfPcbData) pcb.getPcbData());
	}

	private void preemptOthers(SjfPcbData pcbData) {
		Pcb running = null; 
		int i, minInd = -1;
		long minTime = Long.MAX_VALUE;
		for(i = 0; i < Pcb.RUNNING.length; i++) {
			running = Pcb.RUNNING[i];
			if(running.getPreviousState() == Pcb.ProcessState.IDLE) { running.preempt(); return; }
			
			long prediction = pcbData.getPrediction();
			long runningPred = ((SjfPcbData)running.getPcbData()).getPrediction();
			long runningExec = running.getExecutionTime();
			
			if(prediction < runningExec && runningExec < minTime) { minTime = runningExec; minInd = i; } 
			if(prediction < runningPred && runningPred < minTime) { minTime = runningPred; minInd = i; }
		}
		if(minInd != -1) Pcb.RUNNING[minInd].preempt();
	}
}
