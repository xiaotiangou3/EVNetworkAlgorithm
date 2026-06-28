package evnetwork;

public class EVNetworkDemo {
    public static void main(String[] args) {
        System.out.println("--- EV Charging Hubs Network Demo ---\n");

        try {
            // Pause the execution for 10 seconds (10000 milliseconds)
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // Restore interrupted state if thread is woken up prematurely
            Thread.currentThread().interrupt();
        }
        System.out.println("10 seconds sleep to finalize the JVM warm-up before running the performance tests...\n");

        // Test Case 1: Standard achievable highway
        int L1 = 1000;
        int[] dist1 = { 200, 400, 600, 800 };
        int[] costs1 = { 50, 100, 50, 100 };
        int R_max1 = 400;
        runPerformanceTest("Test Case 1: Standard Achievable Highway", L1, dist1, costs1, R_max1, true);

        // Test Case 2: Impossible scenario
        int L2 = 1000;
        int[] dist2 = { 200, 300, 800, 900 };
        int[] costs2 = { 50, 50, 50, 50 };
        int R_max2 = 400; // gap from 300 to 800 is 500 > 400
        runPerformanceTest("Test Case 2: Impossible Scenario (Gap > R_max)", L2, dist2, costs2, R_max2, true);

        // Test Case 3: Performance test with 10,000 nodes
        int n3 = 10000;
        runGeneratedPerformanceTest("Test Case 3: Performance test with 10,000 nodes", n3);

        // Test Case 4: Performance test with 20,000 nodes
        int n4 = 20000;
        runGeneratedPerformanceTest("Test Case 4: Performance test with 20,000 nodes", n4);

        // Test Case 5: Performance test with 40,000 nodes
        int n5 = 40000;
        runGeneratedPerformanceTest("Test Case 5: Performance test with 40,000 nodes", n5);

        // Test Case 6: Performance test with 60,000 nodes
        int n6 = 60000;
        runGeneratedPerformanceTest("Test Case 6: Performance test with 60,000 nodes", n6);

        // Test Case 7: Performance test with 80,000 nodes
        int n7 = 80000;
        runGeneratedPerformanceTest("Test Case 7: Performance test with 80,000 nodes", n7);

        // Test Case 8: Performance test with 100,000 nodes
        int n10x6 = (10 * 10 * 10 * 10 * 10 * 10);
        runGeneratedPerformanceTest("Test Case 8: Performance test with 1,000,000 nodes", n10x6);
    }

    private static void runGeneratedPerformanceTest(String testName, int n) {
        java.util.Random rand = new java.util.Random();
        int[] dist = new int[n];
        int[] costs = new int[n];
        int currentDist = 0;

        for (int i = 0; i < n; i++) {
            // Generate a random gap between 1 and 30 to ensure distances are strictly
            // increasing
            int gap = rand.nextInt(30) + 1;
            currentDist += gap;
            dist[i] = currentDist;

            // Generate a random positive cost between 1 and 100
            costs[i] = rand.nextInt(100) + 1;
        }

        // Total length is slightly after the last rest stop
        int L = currentDist + rand.nextInt(30) + 1;
        // R_max should be large enough to guarantee a valid path (max gap is 30)
        int R_max = 400;
        runPerformanceTest(testName, L, dist, costs, R_max, false);
    }

    private static void runPerformanceTest(String testName, int L, int[] dist, int[] costs, int R_max,
            boolean printPath) {
        System.out.println(testName);

        long startTimeBasic = System.nanoTime();
        EVNetworkResult resBasic = BasicEVNetwork.minCostEVNetwork(L, dist, costs, R_max);
        long endTimeBasic = System.nanoTime();
        long durationBasic = (endTimeBasic - startTimeBasic) / 1_000_000;

        long startTimeOpt = System.nanoTime();
        EVNetworkResult resOpt = OptimizedEVNetwork.minCostEVNetwork(L, dist, costs, R_max);
        long endTimeOpt = System.nanoTime();
        long durationOpt = (endTimeOpt - startTimeOpt) / 1_000_000;

        System.out.println("Basic Algorithm Time: " + durationBasic + " ms");
        System.out.println("Basic Cost: " + resBasic.minCost);
        if (printPath) {
            System.out.println("Basic Path: " + resBasic.selectedStops);
        }

        System.out.println("Optimized Algorithm Time: " + durationOpt + " ms");
        System.out.println("Optimized Cost: " + resOpt.minCost);
        if (printPath) {
            System.out.println("Optimized Path: " + resOpt.selectedStops);
        }

        System.out.println();
    }
}
