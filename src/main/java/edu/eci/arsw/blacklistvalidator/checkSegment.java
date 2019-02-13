package edu.eci.arsw.blacklistvalidator;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class checkSegment extends Thread{
	private int a,b,found,limit,checked;
	private String host;
	private LinkedList<Integer> blacklist;
	private AtomicInteger atomic;
	
	public checkSegment(int a,int b,String host,AtomicInteger atomic,int limit) {
		this.a=a;
		this.b=b;
		this.host=host;
		found=0;
		blacklist=new LinkedList<Integer>();
		this.atomic=atomic;
		this.limit=limit;
		
	}
	public void run() {
		HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
		for(int i=a;i<b;i++) {
			if (skds.isInBlackListServer(i, host)){
				found++;

				blacklist.add(i);
				atomic.incrementAndGet();
			}
			checked++;
			if(atomic.get()==limit) {
				break;
			}
		} 
	}
	public int found() {
		return found;
	}
	public LinkedList<Integer> blacklist(){
		return blacklist;
	}
	public int inform() {
		return checked;
	}
	
}
