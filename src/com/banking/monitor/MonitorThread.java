package com.banking.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * A dedicated monitoring thread that periodically queries and displays status information
 * (Name, State, Priority) of a collection of active worker threads.
 *
 * Demonstrates requirements:
 * - Thread Life Cycle (Displays states like NEW, RUNNABLE, WAITING, TERMINATED)
 * - Thread Naming (OutputsgetName())
 * - Thread Priorities (Outputs getPriority())
 * - Monitoring Thread (Periodically displays diagnostic information of all worker threads)
 */
public class MonitorThread extends Thread {
    
    private final List<Thread> threadsToMonitor;
    private final long intervalMs;
    private volatile boolean running = true;

    /**
     * Constructs the MonitorThread.
     *
     * @param threadsToMonitor List of worker threads to query.
     * @param intervalMs       Time interval in milliseconds between updates.
     */
    public MonitorThread(List<Thread> threadsToMonitor, long intervalMs) {
        super("System-Monitor-Thread"); // Set meaningful thread name
        this.threadsToMonitor = new ArrayList<>(threadsToMonitor);
        this.intervalMs = intervalMs;
        setDaemon(true); // Daemon thread so it doesn't block JVM shutdown
    }

    /**
     * Stop the monitoring loop.
     */
    public void shutdown() {
        this.running = false;
    }

    @Override
    public void run() {
        System.out.printf("[%s] Monitor Thread Started. Monitoring %d threads...%n", 
                getName(), threadsToMonitor.size());
        
        while (running) {
            boolean allFinished = true;
            
            System.out.printf("%n--- [%s] SYSTEM THREAD MONITOR REPORT ---%n", getName());
            System.out.printf("%-25s | %-15s | %-10s%n", "Thread Name", "State", "Priority");
            System.out.println("-----------------------------------------------------------------");
            
            for (Thread t : threadsToMonitor) {
                if (t != null) {
                    Thread.State state = t.getState();
                    int priority = t.getPriority();
                    String name = t.getName();
                    
                    System.out.printf("%-25s | %-15s | %-10d%n", name, state, priority);
                    
                    if (state != Thread.State.TERMINATED) {
                        allFinished = false;
                    }
                }
            }
            System.out.println("-----------------------------------------------------------------");
            
            // If all threads have reached TERMINATED state, we can stop monitoring.
            if (allFinished) {
                System.out.printf("[%s] All monitored worker threads have completed (TERMINATED). Exiting monitor...%n", getName());
                break;
            }

            try {
                // Periodically sleep to avoid busy-waiting
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                System.out.printf("[%s] Monitor thread was interrupted. Shutting down...%n", getName());
                break;
            }
        }
    }
}
