package com.tip.threads.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Demonstrates the behavior of CyclicBarrier in a real-world scenario.
 *
 * Scenario:
 * - Multiple worker threads perform independent validation tasks
 *   (Inventory, Fraud, Payment).
 * - ALL worker threads must reach a common synchronization point
 *   before ANY of them can proceed to the next phase.
 *
 * Key Characteristics of CyclicBarrier:
 * ✔ Worker threads WAIT for each other at a barrier point.
 * ✔ Faster threads are forced to PAUSE until slower threads arrive.
 * ✔ No separate coordinator thread is required.
 * ✔ Synchronization is TWO-DIRECTIONAL (workers ↔ workers).
 * ✔ A barrier action can be executed exactly once when all arrive.
 * ✔ CyclicBarrier is REUSABLE across multiple phases (cyclic).
 *
 * Important Observation:
 * - count is not just about completion, but about synchronized progression.
 * - Worker threads themselves block by calling await().
 *
 * Barrier Action:
 * - The optional barrier action runs AFTER all threads arrive
 *   and BEFORE any thread proceeds to the next phase.
 *
 * When to use:
 * - When tasks must complete a phase together and move forward together.
 * - Multi-phase algorithms, batch processing, coordinated workflows.
 *
 * When NOT to use:
 * - When only one thread needs to wait for others to finish
 *   (use CountDownLatch instead).
 */

public class OrderProcessingService {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3,()->{
            System.out.println("All validations are passed, proceeding with order confirmation!");
        });


        Runnable inventoryCheckTask = () -> {
            try {
                System.out.println("InventoryCheckTask Started");
                Thread.sleep(1000);
                System.out.println("InventoryCheckTask Completed");
                barrier.await();
                System.out.println("InventoryCheckTask moved to Next Phase");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        Runnable fraudCheckTask = () -> {
            try {
                System.out.println("fraudCheckTask Started");
                Thread.sleep(1000);
                System.out.println("fraudCheckTask Completed");
                barrier.await();
                System.out.println("fraudCheckTask moved to Next Phase");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        Runnable paymentCheckTask = () -> {
            try {
                System.out.println("paymentCheckTask Started");
                Thread.sleep(1000);
                System.out.println("paymentCheckTask Completed");
                barrier.await();
                System.out.println("paymentCheckTask moved to Next Phase");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(inventoryCheckTask);
        executorService.submit(fraudCheckTask);
        executorService.submit(paymentCheckTask);

        executorService.shutdown();
    }
}
