package edu.eci.arst.concprg.prodcons;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class fastProducer extends Thread {

    private Queue<Integer> queue = null;

    private int dataSeed = 0;
    private Random rand=null;
    private final long stockLimit;
    

    public fastProducer(Queue<Integer> queue,long stockLimit) {
        this.queue = queue;
        rand = new Random(System.currentTimeMillis());
        this.stockLimit=stockLimit;
        
    }

    @Override
    public void run() {
        while (true) {
        	if(queue.size()==stockLimit) {
        		synchronized(this) {
        			try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        		}
        	}
            dataSeed = dataSeed + rand.nextInt(100);
            System.out.println("Producer added " + dataSeed);
            queue.add(dataSeed);
        }
    }
}
