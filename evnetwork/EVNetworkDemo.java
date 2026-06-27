package evnetwork;

public class EVNetworkDemo {
    public static void main(String[] args) {
        System.out.println("--- EV Charging Hubs Network Demo ---\n");
        
        // Test Case 1: Standard achievable highway
        int L1 = 1000;
        int[] dist1 = {200, 400, 600, 800};
        int[] costs1 = {50, 100, 50, 100};
        int R_max1 = 400;
        System.out.println("Test Case 1: Standard Achievable Highway");
        System.out.println(OptimizedEVNetwork.minCostEVNetwork(L1, dist1, costs1, R_max1));
        System.out.println();

        // Test Case 2: Impossible scenario
        int L2 = 1000;
        int[] dist2 = {200, 300, 800, 900};
        int[] costs2 = {50, 50, 50, 50};
        int R_max2 = 400; // gap from 300 to 800 is 500 > 400
        System.out.println("Test Case 2: Impossible Scenario (Gap > R_max)");
        System.out.println(OptimizedEVNetwork.minCostEVNetwork(L2, dist2, costs2, R_max2));
        System.out.println();
    }
}
