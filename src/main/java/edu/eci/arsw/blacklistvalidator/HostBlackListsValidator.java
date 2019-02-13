/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress,int N){
        
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        
        int ocurrencesCount=0;
        AtomicInteger atomic=new AtomicInteger();
        
        
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        int segmentSize=skds.getRegisteredServersCount()/N;
        
        int checkedListsCount=0;
        ArrayList<checkSegment> threads=new ArrayList<checkSegment>();
        int initial=0;
        for(int i=0;i<N;i++) {
        	checkSegment cs;
        	
        	if(i!=N-1) cs=new checkSegment(initial,initial+segmentSize,ipaddress,atomic,BLACK_LIST_ALARM_COUNT);
        	else cs=new checkSegment(initial,skds.getRegisteredServersCount(),ipaddress,atomic,BLACK_LIST_ALARM_COUNT);
        	
        	initial+=segmentSize;
        	threads.add(cs);
        	cs.start();
        }
        for(checkSegment c:threads) {
        	try {
				c.join();
				checkedListsCount+=c.inform();
	        	System.out.println("the number of blacklists checked is "+checkedListsCount+" of "+skds.getRegisteredServersCount());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	for(int i:c.blacklist()) {
        		blackListOcurrences.add(i);
        	}
        	ocurrencesCount+=c.found();
        	
        }
   
//        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
//            checkedListsCount.incrementAndGet();
//            
//            if (skds.isInBlackListServer(i, ipaddress)){
//                
//                blackListOcurrences.add(i);
//                
//                ocurrencesCount++;
//            }
//        }
        
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}
