package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.Pcb;

public class MultilevelFeedbackQueue extends Scheduler {
	private final int numCpu;
	private final int numQueues;
	private long[] timeSlices;
	private Queue[][] queues;
	private int sizes[];
	public MultilevelFeedbackQueue(int numCpu, long[] timeSlices) {
		this.numCpu = numCpu;
		this.numQueues = timeSlices.length;
		this.queues = new Queue[numCpu][];
		for(int i = 0; i < numCpu; i++) {
			 this.queues[i] = new Queue[numQueues];
			 for(int j = 0; j < numQueues; j++) 
				 this.queues[i][j] = new Queue();
		}
		this.sizes = new int[timeSlices.length];
		this.timeSlices = timeSlices;
	}
	
	public int getNumOfQueues() {
		return numQueues;
	}

	@Override
	public Pcb get(int cpuId) {
		for(int i = 0; i < numQueues; i++) {
			if(sizes[i] != 0) {
				MfqPcbData ret = queues[cpuId][i].getFirst();

				if(ret == null) {
					balance(cpuId, i);
					ret = queues[cpuId][i].getFirst();
				}

				ret.getPcb().setTimeslice(timeSlices[i]);
				ret.getPcb().setAffinity(cpuId);
				sizes[i]--;
				return ret.getPcb();
			}
		}
		return null;
	}

	private void balance(int cpuId, int q) {
		int maxInd = 0, maxSize = queues[0][q].size();
		for(int i = 1; i < numCpu; i++) {
			int sz = queues[i][q].size();
			if(sz > maxSize) {
				maxInd = i;
				maxSize = sz;
			}
		}
		
		queues[cpuId][q].put(queues[maxInd][q].getLast());
	}

	@Override
	public void put(Pcb pcb) {
		if(pcb.getPcbData() == null) 
			pcb.setPcbData(new MfqPcbData(pcb,this));
		
		Pcb.ProcessState lastState = pcb.getPreviousState();
		if(lastState == Pcb.ProcessState.BLOCKED)
			((MfqPcbData)pcb.getPcbData()).incPriority();
		else if(lastState == Pcb.ProcessState.RUNNING)
			((MfqPcbData)pcb.getPcbData()).decPriority();
		else if(lastState == Pcb.ProcessState.FINISHED || lastState == Pcb.ProcessState.IDLE) {
			System.out.println("Greska: poslednje stanje procesa je: " + lastState.toString());
			return;
		}
		
		int cpuInd = pcb.getAffinity();
		int currPrior = ((MfqPcbData)pcb.getPcbData()).getCurrPriority();
		queues[cpuInd][currPrior].put((MfqPcbData)pcb.getPcbData());
		sizes[currPrior]++;
	}

}
