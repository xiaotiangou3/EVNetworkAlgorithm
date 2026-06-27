package evnetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class OptimizedEVNetwork {
    public static EVNetworkResult minCostEVNetwork(int L, int[] distances, int[] costs, int R_max) {
        int n = distances.length;
        int N = n + 2;
        int[] dist = new int[N];
        int[] cst = new int[N];

        // Preprocessing the input
        dist[0] = 0;
        cst[0] = 0;
        for (int i = 0; i < n; i++) {
            dist[i + 1] = distances[i];
            cst[i + 1] = costs[i];
        }
        dist[N - 1] = L;
        cst[N - 1] = 0;

        long[] dp = new long[N];
        int[] parent = new int[N];
        
        for (int i = 1; i < N; i++) {
            dp[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        dp[0] = 0;
        
        Deque<Integer> deque = new LinkedList<>();
        deque.addLast(0);
        
        for (int i = 1; i < N; i++) {
            // Remove all stops that are unreachable from this stop
            while (!deque.isEmpty() && dist[i] - dist[deque.peekFirst()] > R_max) {
                deque.pollFirst();
            }
            
            if (deque.isEmpty()) {
                // Cannot reach this node from any valid previous node
                continue;
            }
            
            int bestParent = deque.peekFirst();
            dp[i] = dp[bestParent] + cst[i];
            parent[i] = bestParent;

            // Found a cheaper stop with a closer distance from the subsequent stops
            while (!deque.isEmpty() && dp[deque.peekLast()] >= dp[i]) {
                deque.pollLast();
            }
            deque.addLast(i);
        }
        
        if (dp[N - 1] >= Integer.MAX_VALUE) {
            return new EVNetworkResult(-1, new ArrayList<>());
        }
        
        List<Integer> path = new ArrayList<>();
        int curr = parent[N - 1];
        while (curr > 0) {
            path.add(dist[curr]);
            curr = parent[curr];
        }
        Collections.reverse(path);
        
        return new EVNetworkResult((int) dp[N - 1], path);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter total length of highway (L): ");
        int L = scanner.nextInt();
        System.out.print("Enter max EV range (R_max): ");
        int R_max = scanner.nextInt();
        System.out.print("Enter number of rest stops: ");
        int n = scanner.nextInt();
        
        int[] distances = new int[n];
        int[] costs = new int[n];
        
        if (n > 0) {
            System.out.println("Enter distances of rest stops separated by space:");
            for (int i = 0; i < n; i++) {
                distances[i] = scanner.nextInt();
            }
            
            System.out.println("Enter costs of rest stops separated by space:");
            for (int i = 0; i < n; i++) {
                costs[i] = scanner.nextInt();
            }
        }
        
        EVNetworkResult result = minCostEVNetwork(L, distances, costs, R_max);
        System.out.println(result);
        
        scanner.close();
    }
}
