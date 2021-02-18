package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.Pcb;
public abstract class Scheduler {
    public abstract Pcb get(int cpuId);

    public abstract void put(Pcb pcb);

    public static Scheduler createScheduler(String[] args) {
		if(args.length < 1) { 
			System.out.println("Nedovoljan broj argumenata");
			System.exit(0); 
		}
		
		String scheduler = args[0].toUpperCase();
		if(scheduler.equals("SJF")) {
			// argumenti: SJF alfa preepmtive
			return new ShortestJobFirst(Double.parseDouble(args[1]),Boolean.parseBoolean(args[2]));
		}	
		if(scheduler.equals("MFQ")) {
			// argumenti: MFQ numCpu timeSlice1 timeSlice2 ... timeSliceN
			long[] timeSlices = new long[args.length - 1];
			int numCpus = Integer.parseInt(args[1]);
			for(int i = 2; i < args.length; i++) 
				timeSlices[i - 2] = Long.parseLong(args[i]);
			return new MultilevelFeedbackQueue(numCpus, timeSlices);
			//return new MultilevelFeedbackQueue(timeSlices);
		}
		if(scheduler.equals("CF")) {
			// argumenti: CF
			return new CompletelyFair();
		}
		
		return null;
    }
}
