package evnetwork;

import java.util.List;

public class EVNetworkResult {
    public final int minCost;
    public final List<Integer> selectedStops;

    public EVNetworkResult(int minCost, List<Integer> selectedStops) {
        this.minCost = minCost;
        this.selectedStops = selectedStops;
    }

    @Override
    public String toString() {
        return "Minimum Cost: RM " + minCost + "\nSelected Stops (distances): " + selectedStops;
    }
}
