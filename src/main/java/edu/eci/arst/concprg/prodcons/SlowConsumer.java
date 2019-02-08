/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public  class SlowConsumer extends Thread{
    
    private Queue<Integer> queue;
    private ArrayList<fastProducer> producers;
    
    
    public SlowConsumer(Queue<Integer> queue,ArrayList<fastProducer> producers){
        this.queue=queue;        
        this.producers=producers;
    }
    
    @Override
    public void run() {
        while (true) {
        	
            if (queue.size() > 0) {
                int elem=queue.poll();
                System.out.println("Consumer consumes "+elem);
                if(queue.size()==0) {
                	 for(fastProducer fp:producers) {
                		 synchronized(fp) {
                			 fp.notify();
                		 }
                     	
                     }
                	
                }
               
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
