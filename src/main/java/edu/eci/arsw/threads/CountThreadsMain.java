/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;


/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {
    
    public static void main(String a[]){
    	CountThread A= new CountThread(0,99);
    	CountThread B= new CountThread(99,199);
    	CountThread C= new CountThread(200,299);
    	A.run();
    	B.run();
    	C.run();
        
    }
    
}
