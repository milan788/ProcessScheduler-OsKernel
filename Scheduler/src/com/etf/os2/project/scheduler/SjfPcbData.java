package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.*;

public class SjfPcbData extends PcbData implements Comparable<SjfPcbData> {
	private long prediction;

	public SjfPcbData(Pcb pcb, long prediction) {
		super(pcb);
		this.prediction = prediction;
	}

	
	public void setPrediction(long executionTime, double alfa) {
		prediction = (long)(alfa*executionTime + (1 - alfa)*prediction);
	}

	public long getPrediction() {
		return prediction;
	}
	
	@Override
	public int compareTo(SjfPcbData pcbData) {
		if(this.prediction > pcbData.prediction) return 1;
		else if(this.prediction == pcbData.prediction) return 0;
		else return -1;
	}
	
	
}
