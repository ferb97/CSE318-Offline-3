import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static Set<Integer> setX = new HashSet<>();
    public static Set<Integer> setY = new HashSet<>();
    private static Map<String, String> upperBoundMap = new HashMap<>();
    public static final int INPUT_FILE_COUNT = 54;

    public static void initializeUpperBoundMap(){
        String[] upperBoundValues = {" ", "12078", "12084", "12077", " ", " ", " ", " ", " ", " ", " ", "627", "621", "645", "3187", "3169", "3172", " ", " ",
                " ", " ", " ", "14123", "14129", "14131", " ", " ", " ", " ", " ", " ", " ", "1560", "1537", "1541", "8000", "7996",
                "8009", " ", " ", " ", " ", " ", "7027", "7022", "7020", " ", " ", "6000", "6000", "5988", " ", " ", " ", " "};

        for(int i = 1; i <= INPUT_FILE_COUNT; i++){
            upperBoundMap.put("G" + i, upperBoundValues[i]);
        }
    }

    public static long calculateMaxCut(int[][] adjacencyMatrix){
        long maxCut = 0;
        for(Integer elementX: setX){
            for(Integer elementY: setY){
                if(adjacencyMatrix[elementX][elementY] != 0){
                    maxCut += adjacencyMatrix[elementX][elementY];
                }
            }
        }
        return maxCut;
    }
    public static long semiGreedyAlgorithm(int n, int[][] adjacencyMatrix, double alpha){
        setX.clear();
        setY.clear();

        List<Edge> rclEdge = new ArrayList<>();
        Set<Integer> unusedVertex = new HashSet<>();
        int minWeightEdge = Integer.MAX_VALUE;
        int maxWeightEdge = Integer.MIN_VALUE;

        for(int i = 1; i <= n; i++){
            unusedVertex.add(i);
        }

        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= i; j++){
                if(adjacencyMatrix[i][j] != 0){
                    if(adjacencyMatrix[i][j] < minWeightEdge){
                        minWeightEdge = adjacencyMatrix[i][j];
                    }
                    if(adjacencyMatrix[i][j] > maxWeightEdge){
                        maxWeightEdge = adjacencyMatrix[i][j];
                    }
                }
            }
        }

        double w = minWeightEdge + alpha * (maxWeightEdge - minWeightEdge);

        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= i; j++){
                if(adjacencyMatrix[i][j] != 0 && adjacencyMatrix[i][j] >= w){
                    Edge edge = new Edge(i, j, adjacencyMatrix[i][j]);
                    rclEdge.add(edge);
                }
            }
        }

        Random random = new Random();
        int selected = random.nextInt(rclEdge.size());
        setX.add(rclEdge.get(selected).getIndex1());
        setY.add(rclEdge.get(selected).getIndex2());
        unusedVertex.remove(rclEdge.get(selected).getIndex1());
        unusedVertex.remove(rclEdge.get(selected).getIndex2());
        //System.out.println("x: " + rclEdge.get(selected).getIndex1() + ", y: " + rclEdge.get(selected).getIndex2());

        int totalSelected = 2;
        long[] xValue = new long[n + 1];
        long[] yValue = new long[n + 1];

        for(int i = 1; i <= n; i++){
            xValue[i] = 0;
            yValue[i] = 0;
        }

        for(Integer unused: unusedVertex) {

            if(adjacencyMatrix[unused][rclEdge.get(selected).getIndex1()] != 0){
                yValue[unused] = adjacencyMatrix[unused][rclEdge.get(selected).getIndex1()];
            }

            if(adjacencyMatrix[unused][rclEdge.get(selected).getIndex2()] != 0){
                xValue[unused] = adjacencyMatrix[unused][rclEdge.get(selected).getIndex2()];
            }
        }

        while(totalSelected < n){
            long wMin = Long.MAX_VALUE;
            long wMax = Long.MIN_VALUE;
            List<Integer> rclVertex = new ArrayList<>();

            for(Integer unused: unusedVertex){

                if(wMin > xValue[unused]){
                    wMin = xValue[unused];
                }
                if(wMin > yValue[unused]){
                    wMin = yValue[unused];
                }

                if(wMax < xValue[unused]){
                    wMax = xValue[unused];
                }
                if(wMax < yValue[unused]){
                    wMax = yValue[unused];
                }
            }

            w = wMin + alpha * (wMax - wMin);

            for(Integer unused: unusedVertex){
                if(xValue[unused] >= w){
                    rclVertex.add(unused);
                }
                else if(yValue[unused] >= w){
                    rclVertex.add(unused);
                }
            }

            selected = random.nextInt(rclVertex.size());
            int selectedVertex = rclVertex.get(selected);

            if(xValue[selectedVertex] > yValue[selectedVertex]){
                setX.add(selectedVertex);

                for(Integer unused: unusedVertex){
                    if(adjacencyMatrix[unused][selectedVertex] != 0){
                       yValue[unused] += adjacencyMatrix[unused][selectedVertex];
                    }
                }
                //System.out.println("x: " + rclVertex.get(selected) + " " + xValue[rclVertex.get(selected)]);
            }
            else{
                setY.add(selectedVertex);
                //System.out.println("y: " + rclVertex.get(selected) + " " + yValue[rclVertex.get(selected)]);
                for(Integer unused: unusedVertex){
                    if(adjacencyMatrix[unused][selectedVertex] != 0){
                        xValue[unused] += adjacencyMatrix[unused][selectedVertex];
                    }
                }
            }
            unusedVertex.remove(rclVertex.get(selected));
            totalSelected++;
        }

//        System.out.println("After semi greedy: ");
        long maxCut = calculateMaxCut(adjacencyMatrix);
//        System.out.println("Max cut: " + maxCut);
        return maxCut;
    }

    public static long greedyAlgorithm(int n, int[][] adjacencyMatrix){
        setX.clear();
        setY.clear();

        Set<Integer> unusedVertex = new HashSet<>();
        int maxWeightEdge = Integer.MIN_VALUE;

        for(int i = 1; i <= n; i++){
            unusedVertex.add(i);
        }

        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j++){
                if(adjacencyMatrix[i][j] != 0){
                    if(adjacencyMatrix[i][j] > maxWeightEdge){
                        maxWeightEdge = adjacencyMatrix[i][j];
                    }
                }
            }
        }

        boolean done = false;
        long[] xValue = new long[n + 1];
        long[] yValue = new long[n + 1];

        for(int i = 1; i <= n; i++){
            xValue[i] = 0;
            yValue[i] = 0;
        }

        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j++){
                if(adjacencyMatrix[i][j] != 0 && adjacencyMatrix[i][j] == maxWeightEdge){
                    setX.add(i);
                    setY.add(j);
                    unusedVertex.remove(i);
                    unusedVertex.remove(j);

                    for(Integer unused: unusedVertex) {
                        if(adjacencyMatrix[unused][i] != 0){
                            yValue[unused] = adjacencyMatrix[unused][i];
                        }

                        if(adjacencyMatrix[unused][j] != 0){
                            xValue[unused] = adjacencyMatrix[unused][j];
                        }
                    }
                    done = true;
                    break;
                }
            }
            if(done)
              break;
        }

        int totalSelected = 2;

        while(totalSelected < n){
            long wMax = Long.MIN_VALUE;

            for(Integer unused: unusedVertex){

                if(wMax < xValue[unused]){
                    wMax = xValue[unused];
                }
                if(wMax < yValue[unused]){
                    wMax = yValue[unused];
                }
            }

            for(Integer unused: unusedVertex){
                if(xValue[unused] == wMax){
                    setX.add(unused);
                    unusedVertex.remove(unused);
                    for(Integer unused1: unusedVertex){
                        if(adjacencyMatrix[unused1][unused] != 0){
                            yValue[unused1] += adjacencyMatrix[unused1][unused];
                        }
                    }
                    break;
                }
                else if(yValue[unused] == wMax){
                    setY.add(unused);
                    unusedVertex.remove(unused);
                    for(Integer unused1: unusedVertex){
                        if(adjacencyMatrix[unused1][unused] != 0){
                            xValue[unused1] += adjacencyMatrix[unused1][unused];
                        }
                    }
                    break;
                }
            }
            totalSelected++;
        }

//        System.out.println("After greedy: ");
        long maxCut = calculateMaxCut(adjacencyMatrix);
//        System.out.println("Max cut: " + maxCut);
        return maxCut;
    }

    public static long randomizedAlgorithm(int n, int[][] adjacencyMatrix){

        setX.clear();
        setY.clear();
        for(int i = 1; i <= n; i++){
            Random random = new Random();
            int selected = random.nextInt(2) + 1;
            if(selected == 1){
               setX.add(i);
            }
            else{
               setY.add(i);
            }
        }

//        System.out.println("After randomized: ");
        long maxCut = calculateMaxCut(adjacencyMatrix);
//        System.out.println("Max cut: " + maxCut);
        return maxCut;
    }

    public static int localSearchAlgorithm(int n, int[][] adjacencyMatrix){
        boolean change = true;
        int iterationCount = 0;

        while(change){
            change = false;
            for(int i = 1; i <= n; i++){
                long xVal = 0;
                long yVal = 0;

                for(Integer elementX: setX){
                    if(adjacencyMatrix[elementX][i] != 0){
                        yVal += adjacencyMatrix[elementX][i];
                    }
                }

                for(Integer elementY: setY){
                    if(adjacencyMatrix[elementY][i] != 0){
                        xVal += adjacencyMatrix[elementY][i];
                    }
                }

                if(setX.contains(i) && yVal > xVal){
                    setX.remove(i);
                    setY.add(i);
                    change = true;
                    iterationCount++;
                    break;
                }

                if(setY.contains(i) && xVal > yVal){
                    setY.remove(i);
                    setX.add(i);
                    change = true;
                    iterationCount++;
                    break;
                }
            }
        }

//        System.out.println("After Local Search: ");
//        System.out.println("Max iteration: " + iterationCount);
//        long maxCut = calculateMaxCut(adjacencyMatrix);
//        System.out.println("Max cut: " + maxCut);
        return iterationCount;
    }

    public static GraspSolutions graspAlgorithm(int n, int[][] adjacencyMatrix, int graspIteration){
        long maxCut = Long.MIN_VALUE;
        Random random = new Random();
        long totalSemiGreedy = 0, totalLocalSearch = 0, totalLocalSearchIterations = 0;

        for(int i = 1; i <= graspIteration; i++){
            double alpha = random.nextDouble();
            totalSemiGreedy += semiGreedyAlgorithm(n, adjacencyMatrix, alpha);
            totalLocalSearchIterations += localSearchAlgorithm(n, adjacencyMatrix);
            long cur = calculateMaxCut(adjacencyMatrix);
            totalLocalSearch += cur;
            if(cur > maxCut){
               maxCut = cur;
            }
        }

        double averageSemiGreedy = (totalSemiGreedy * 1.0) / graspIteration;
        double averageLocalSearch = (totalLocalSearch * 1.0) / graspIteration;
        double averageLocalSearchIterations = (totalLocalSearchIterations * 1.0) / graspIteration;
        GraspSolutions graspSolutions = new GraspSolutions(averageSemiGreedy, averageLocalSearch, averageLocalSearchIterations, maxCut);
        return graspSolutions;
    }

    public static void main(String[] args) {

        initializeUpperBoundMap();
        String csvFilePath = "data.csv";
        FileWriter csvWriter;
        try {
            csvWriter = new FileWriter(csvFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            csvWriter.write(" , , , , , , , , , , " + "\n");
            csvWriter.write(" ,Problem, , ,Constructive Algorithm, ,Local Search, ,GRASP, ,Known Best Solution " + "\n");
            csvWriter.write(" , , , , , , , , , , " + "\n");
            csvWriter.write("Name,|V| or n,|E| or m,Randomized-1,Greedy-1,Semi-Greedy-1,No. of Iterations,Best Value,No. of Iteartions,Best Value,Upper Bound" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int iterationCount  = 10, graspIteration = 10;
//        double alpha = 0.8;

        for(int k = 1; k <= INPUT_FILE_COUNT; k++) {

            System.out.println("For File: g" + k);
            String inputFileName = "g" + k + ".rud";
            File inputFile = new File(inputFileName);

            Scanner scn;
            try {
                scn = new Scanner(inputFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            int n = scn.nextInt();
            int m = scn.nextInt();

            int[][] adjacencyMatrix = new int[n + 1][n + 1];

            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    adjacencyMatrix[i][j] = 0;
                }
            }
            for (int i = 0; i < m; i++) {
                int ind1, ind2, cost;
                ind1 = scn.nextInt();
                ind2 = scn.nextInt();
                cost = scn.nextInt();
                adjacencyMatrix[ind1][ind2] = cost;
                adjacencyMatrix[ind2][ind1] = cost;
            }

//            double averageSemiGreedy = 0.0, averageRandomized = 0.0;
////            int iterationCount = 10;
//            long total = 0;
//
//            for (int i = 1; i <= iterationCount; i++) {
//                total += semiGreedyAlgorithm(n, adjacencyMatrix, alpha);
//            }
//            averageSemiGreedy = (total * 1.0) / iterationCount;
//            System.out.println("Average semi greedy max cut: " + averageSemiGreedy);

            long greedyMaxCut = greedyAlgorithm(n, adjacencyMatrix);
            System.out.println("Greedy max cut: " + greedyMaxCut);

            long total = 0;
            for (int i = 1; i <= iterationCount; i++) {
                total += randomizedAlgorithm(n, adjacencyMatrix);
            }
            double averageRandomized = (total * 1.0) / iterationCount;
            System.out.println("Average randomized max cut: " + averageRandomized);

//            semiGreedyAlgorithm(n, adjacencyMatrix, alpha);
//            int maxIterationLocalSearch = localSearchAlgorithm(n, adjacencyMatrix);
//            long localSearchMaxCut = calculateMaxCut(adjacencyMatrix);
//            System.out.println("Max Iterations: " + maxIterationLocalSearch + " Max Cut: " + localSearchMaxCut);

            graspIteration = 10;
            GraspSolutions graspSolutions = graspAlgorithm(n, adjacencyMatrix, graspIteration);
            System.out.println("Grasp iteration: " + graspIteration + ", max cut: " + graspSolutions.getMaxCut());

            String newRow = "G" + k + "," + n + "," + m + "," + averageRandomized + "," + greedyMaxCut + "," + graspSolutions.getAverageSemiGreedy() + "," + graspSolutions.getAverageLocalSearchIterations() + "," +
                    graspSolutions.getAverageLocalSearch() + "," + graspIteration + "," + graspSolutions.getMaxCut() + "," + upperBoundMap.get("G" + k);
            try {
                csvWriter.write(newRow + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            graspIteration = 20;
            GraspSolutions graspSolutions1 = graspAlgorithm(n, adjacencyMatrix, graspIteration);
            System.out.println("Grasp iteration: " + graspIteration + ", max cut: " + graspSolutions1.getMaxCut());

            String newRow1 = "G" + k + "," + n + "," + m + "," + averageRandomized + "," + greedyMaxCut + "," + graspSolutions1.getAverageSemiGreedy() + "," + graspSolutions1.getAverageLocalSearchIterations() + "," +
                    graspSolutions1.getAverageLocalSearch() + "," + graspIteration + "," + graspSolutions1.getMaxCut() + "," + upperBoundMap.get("G" + k);
            try {
                csvWriter.write(newRow1 + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            graspIteration = 50;
            GraspSolutions graspSolutions2 = graspAlgorithm(n, adjacencyMatrix, graspIteration);
            System.out.println("Grasp iteration: " + graspIteration + ", max cut: " + graspSolutions2.getMaxCut());

            String newRow2 = "G" + k + "," + n + "," + m + "," + averageRandomized + "," + greedyMaxCut + "," + graspSolutions2.getAverageSemiGreedy() + "," + graspSolutions2.getAverageLocalSearchIterations() + "," +
                    graspSolutions2.getAverageLocalSearch() + "," + graspIteration + "," + graspSolutions2.getMaxCut() + "," + upperBoundMap.get("G" + k);
            try {
                csvWriter.write(newRow2 + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            scn.close();
        }

        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
