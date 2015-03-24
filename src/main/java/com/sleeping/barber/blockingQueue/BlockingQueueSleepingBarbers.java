package com.sleeping.barber.blockingQueue;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
 
public class BlockingQueueSleepingBarbers extends Thread {
    public static final int CHAIRS = 5;
 
    public static final long BARBER_TIME = 5000;
 
    private static final long CUSTOMER_TIME = 1500;
 
    public static final long OFFICE_CLOSE = BARBER_TIME * 2;
 
    public static BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(CHAIRS);
 
    class Customer extends Thread {
        int iD;
        boolean notCut = true;
 
        // Constructor for the Customer 
        BlockingQueue<Integer> queue = null;
 
        public Customer(int i, BlockingQueue<Integer> queue) {
            iD = i;
            this.queue = queue;
        }
 
        public void run() {
            while (true) { // as long as the customer is not cut he is in the queue or if not enough sits he is out 
                try {
                    this.queue.add(this.iD);
                    this.getHaircut(); // take a sit
                } catch (IllegalStateException e) {
 
                    System.out.println("There are no free seats. Customer "
                            + this.iD + " has left the barbershop.");
                }
                break;
            }
        }
        // take a seat 
        public void getHaircut() {
            System.out.println("Customer " + this.iD + " took a chair");
        }
    }
    class Barber extends Thread {
        BlockingQueue<Integer> queue = null;
        private String name;
 
        public Barber(BlockingQueue<Integer> queue, String name) {
            this.name = name;
            this.queue = queue;
        }
 
        public void run() {
            while (true) { // runs in an infinite loop
 
                try {
                        Integer i = this.queue.poll(OFFICE_CLOSE, TimeUnit.MILLISECONDS);
                        if (i==null) break; // barber slept for long time (OFFICE_CLOSE) no more clients in the queue - close office
                    this.cutHair(i); // cutting...
                     
                } catch (InterruptedException e) {
                }
            }
        }
 
        public void cutHair(Integer i) {
            System.out.println("The barber " + this.name + " is cutting hair for customer #" + i);
            try {
                sleep(BARBER_TIME);
            } catch (InterruptedException ex) {
            }
        }
    }
 
    public static void main(String args[]) {
        BlockingQueueSleepingBarbers barberShop = new BlockingQueueSleepingBarbers(); 
        barberShop.start(); // Let the simulation begin
    }
 
    public void run() {
        Barber barber01 = new Barber(BlockingQueueSleepingBarbers.queue, "01");
        barber01.start(); 
         
        Barber barber02 = new Barber(BlockingQueueSleepingBarbers.queue, "02");
        barber02.start(); 
         
        //  create new customers  
 
        for (int i = 1; i < 16; i++) {
            Customer aCustomer = new Customer(i, BlockingQueueSleepingBarbers.queue);
            aCustomer.start();
            try {
                sleep(CUSTOMER_TIME);
            } catch (InterruptedException ex) {};
        }
    }
}