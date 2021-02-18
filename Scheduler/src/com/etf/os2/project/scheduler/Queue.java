package com.etf.os2.project.scheduler;

import java.util.LinkedList;

public class Queue {
	private LinkedList<MfqPcbData> list = new LinkedList<>();
	
	public int size() { return list.size(); }
	public MfqPcbData getFirst() { 
		if(list.size() > 0) return list.removeFirst(); 
		else return null;
	}
	public MfqPcbData getLast()  { 
		if(list.size() > 0) return list.removeLast(); 
		else return null;
	}
	public void	put(MfqPcbData pcbData) { list.add(pcbData); }
	public boolean isEmpty() { return list.isEmpty(); }
	
}
