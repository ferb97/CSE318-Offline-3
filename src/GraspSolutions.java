public class GraspSolutions {

    private double averageSemiGreedy;
    private double averageLocalSearch;
    private double averageLocalSearchIterations;
    private long maxCut;

    public GraspSolutions(double averageSemiGreedy, double averageLocalSearch, double averageLocalSearchIterations, long maxCut) {
        this.averageSemiGreedy = averageSemiGreedy;
        this.averageLocalSearch = averageLocalSearch;
        this.averageLocalSearchIterations = averageLocalSearchIterations;
        this.maxCut = maxCut;
    }

    public double getAverageSemiGreedy() {
        return averageSemiGreedy;
    }

    public void setAverageSemiGreedy(double averageSemiGreedy) {
        this.averageSemiGreedy = averageSemiGreedy;
    }

    public double getAverageLocalSearch() {
        return averageLocalSearch;
    }

    public void setAverageLocalSearch(double averageLocalSearch) {
        this.averageLocalSearch = averageLocalSearch;
    }

    public double getAverageLocalSearchIterations() {
        return averageLocalSearchIterations;
    }

    public void setAverageLocalSearchIterations(double averageLocalSearchIterations) {
        this.averageLocalSearchIterations = averageLocalSearchIterations;
    }

    public long getMaxCut() {
        return maxCut;
    }

    public void setMaxCut(long maxCut) {
        this.maxCut = maxCut;
    }
}
