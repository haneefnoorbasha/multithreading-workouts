package com.tip.threads.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Demonstrates the behavior of CountDownLatch in a real-world scenario.
 *
 * Scenario:
 * - Multiple independent worker threads perform setup tasks (Lighting, Camera, Actors).
 * - A single coordinating thread (Director / main thread) must WAIT until
 *   all setup tasks are completed before proceeding.
 *
 * Key Characteristics of CountDownLatch:
 * ✔ Worker threads DO NOT wait for each other.
 * ✔ Each worker signals completion using countDown() and exits immediately.
 * ✔ Only the main (Director) thread blocks using await().
 * ✔ Synchronization is ONE-DIRECTIONAL (workers → director).
 * ✔ CountDownLatch is a ONE-TIME use synchronization aid (not reusable).
 *
 * Important Observation:
 * - Workers have no awareness of other workers.
 * - The latch only coordinates completion, NOT progression.
 *
 * When to use:
 * - When one or more threads must wait for a fixed number of operations to finish.
 *
 * When NOT to use:
 * - When worker threads must wait for each other or proceed together
 *   (use CyclicBarrier or Phaser instead).
 */

public class MovieShootingService {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(3);

        Runnable lightingTask = () -> {
            System.out.println("[Lighting] Task STARTED");
            sleep(200);
            System.out.println("[Lighting] Task COMPLETED");
            System.out.println("[Lighting] Calling countDown() and LEAVING (not waiting)");
            latch.countDown(); // DOES NOT BLOCK
            System.out.println("[Lighting] Thread FINISHED execution");
        };

        Runnable cameraTask = () -> {
            System.out.println("[Camera] Task STARTED");
            sleep(300);
            System.out.println("[Camera] Task COMPLETED");
            System.out.println("[Camera] Calling countDown() and LEAVING (not waiting)");
            latch.countDown(); // DOES NOT BLOCK
            System.out.println("[Camera] Thread FINISHED execution");
        };

        Runnable actorsTask = () -> {
            System.out.println("[Actors] Task STARTED");
            sleep(100);
            System.out.println("[Actors] Task COMPLETED");
            System.out.println("[Actors] Calling countDown() and LEAVING (not waiting)");
            latch.countDown(); // DOES NOT BLOCK
            System.out.println("[Actors] Thread FINISHED execution");
        };

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(lightingTask);
        executor.submit(cameraTask);
        executor.submit(actorsTask);

        // MAIN THREAD BLOCKS HERE
        System.out.println("\n[Director] Waiting for ALL setup tasks to finish...");
        latch.await(); // ONLY main thread waits here
        System.out.println("[Director] All tasks finished. Shooting STARTS\n");

        executor.shutdown();
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
