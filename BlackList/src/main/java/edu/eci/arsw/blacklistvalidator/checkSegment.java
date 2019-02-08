package edu.eci.arsw.blacklistvalidator;

import java.util.LinkedList;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class checkSegment extends Thread{
	private int a,b,found;
	private String host;
	private LinkedList<Integer> blacklist;
	
	public checkSegment(int a,int b,String host) {
		this.a=a;
		this.b=b;
		this.host=host;
		found=0;
		blacklist=new LinkedList<Integer>();
	}
	public void run() {
		HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
		for(int i=a;i<b;i++) {
			if (skds.isInBlackListServer(i, host)){
				found++;
				blacklist.add(i);
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
		return b-a;
	}
	
}
