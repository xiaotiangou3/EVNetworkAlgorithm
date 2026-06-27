package evnetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicEVNetwork {
    public static EVNetworkResult minCostEVNetwork(int L, int[] distances, int[] costs, int R_max) {
        int n = distances.length;
        int N = n + 2;
        int[] dist = new int[N];
        int[] cst = new int[N];
        
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
        
        for (int i = 1; i < N; i++) {
            long minPrevCost = Integer.MAX_VALUE;
            int bestParent = -1;
            
            for (int j = 0; j < i; j++) {
                if (dist[i] - dist[j] <= R_max && dp[j] != Integer.MAX_VALUE) {
                    if (dp[j] < minPrevCost) {
                        minPrevCost = dp[j];
                        bestParent = j;
                    }
                }
            }
            
            if (bestParent != -1) {
                dp[i] = minPrevCost + cst[i];
                parent[i] = bestParent;
            }
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
}
