import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        findSumOfFactorial();
        getDistance();
        findRightBrackets();
    }

    // Task#1
    public static int findRightBrackets() {
        // testing array
        String[] arithmeticEquations = {
                "(4+2)*4+((2+1)-2)",
                "(3+(3+3)-1)",
                ")1-5)+(4-0))",
                "1+(1+4)+(2/1))"
        };
        // array where the corrected data will be written
        String[] brackets = new String[arithmeticEquations.length];

        // in a cycle we process each element
        for (int i = 0; i < arithmeticEquations.length; i++) {
            // for writing use StringBuilder
            StringBuilder stringBuilder = new StringBuilder();
            for (char ch : arithmeticEquations[i].toCharArray()) {
                //skip everything except the brackets
                if (ch == '(' || ch == ')') {
                    stringBuilder.append(ch);
                }
            }
            brackets[i] = stringBuilder.toString();
        }

        //Now my array 'arithmeticEquations' looks like this = {"(())))", "(())", "((()", "()))", "(((()"};
        int validCount = 0;
        int number;
        // for read using BufferedReader
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            number = Integer.parseInt(reader.readLine());
            if (number < 0) {
                throw new IllegalArgumentException("The number must ne positive: " + number);
            }
        } catch (IOException e) {
            throw new RuntimeException("Incorrect input data", e);
        }
        //in a cycle we process each element
        for (String item : brackets) {
            //create count for open/close brackets
            int openBracketsCount = 0;
            int closeBracketsCount = 0;
            // stack open/close brackets in every element in brackets array
            for (char ch : item.toCharArray()) {
                // the number of closing parentheses cannot exceed the number of closing ones or the equation is incorrect
                if (ch == '(' && (closeBracketsCount <= openBracketsCount)) {
                    openBracketsCount++;
                } else if (ch == ')') {
                    closeBracketsCount++;
                }
            }
            if (openBracketsCount == closeBracketsCount && openBracketsCount == number) {
                validCount++;
            }
        }
        return validCount;
    }

    // Task#3
    public static int findSumOfFactorial() {
        // since the number 100! is huge, we use BigInteger to store it
        BigInteger factorial = BigInteger.valueOf(1);
        int sumOfFactorial;
        for (int i = 1; i <= 100; i++) {
            // calculate 100!
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        //save as a string type
        String resultTOString = factorial.toString();
        sumOfFactorial = resultTOString.chars() //using steam() for stacking
                .map(Character::getNumericValue) // take each value and convert it from a string to a number
                .sum(); // stacking
        // or use forEach
        // for (char chars: resultTOString.toCharArray()) {
        //    sumOfFactorial += Character.getNumericValue(chars);
        //}
        return sumOfFactorial;
    }

    //Task#2
    /*
    the task was solved with the help of forums, google, wikipedia
    it is difficult I have not encountered graph theory before
     */
    public static void getDistance() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            // read quantity of tests
            int testCases = Integer.parseInt(reader.readLine());
            if (testCases < 1 || testCases > 10) {
                throw new RuntimeException("Number of test cases must be between 1 and 10.");
            }

            while (testCases-- > 0) {
                // read quantity of towns
                int n = Integer.parseInt(reader.readLine());
                if (n < 1 || n > 10000) {
                    throw new RuntimeException("Number of cities must be between 1 and 10000.");
                }

                List<String> cityNames = new ArrayList<>(); // List cities names
                List<List<Edge>> graph = new ArrayList<>(); // Graph

                // read and write towns names
                for (int i = 0; i < n; i++) {
                    String cityName = reader.readLine();
                    if (cityName.length() > 10 || !cityName.matches("[a-z]+")) {
                        throw new RuntimeException("The name must be no longer than 10 characters.");
                    }
                    cityNames.add(cityName);
                    graph.add(new ArrayList<>());

                    // we read the cost - this is our graph edge
                    int neighbors = Integer.parseInt(reader.readLine());
                    for (int j = 0; j < neighbors; j++) {
                        String[] neighborData = reader.readLine().split(" ");
                        int neighborIndex = Integer.parseInt(neighborData[0]) - 1;
                        int cost = Integer.parseInt(neighborData[1]);
                        graph.get(i).add(new Edge(neighborIndex, cost));
                    }
                }

                // read the number of requests
                int queries = Integer.parseInt(reader.readLine());
                if (queries < 1 || queries > 100) {
                    throw new RuntimeException("Number of queries must be between 1 and 100.");
                }

                for (int i = 0; i < queries; i++) {
                    String[] query = reader.readLine().split(" ");
                    String source = query[0];
                    String destination = query[1];

                    // find indexes of town
                    int sourceIndex = cityNames.indexOf(source);
                    int destinationIndex = cityNames.indexOf(destination);

                    // calculate minimal cost, using algorithm Dijkstra
                    int result = dijkstra(sourceIndex, destinationIndex, graph, n);
                    System.out.println(result);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("You insert invalid data ", e);
        }
    }

    private static int dijkstra(int source, int destination, List<List<Edge>> graph, int n) {
        // array to save minimal cost
        int[] distances = new int[n];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;

        // using interface Queue and his implementation PriorityQueue to compare and save
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        priorityQueue.add(new int[]{source, 0});

        while (!priorityQueue.isEmpty()) {
            int[] current = priorityQueue.poll();
            int currentNode = current[0];
            int currentDistance = current[1];

            // If the current distance is greater than the one already found, skip her
            if (currentDistance > distances[currentNode]) continue;

            // processing the neighbors of the current node
            for (Edge edge : graph.get(currentNode)) {
                int newDistance = currentDistance + edge.cost;
                if (newDistance < distances[edge.destination]) {
                    distances[edge.destination] = newDistance;
                    priorityQueue.add(new int[]{edge.destination, newDistance});
                }
            }
        }
        return distances[destination];
    }

    static class Edge {
        int destination;
        int cost;

        public Edge(int destination, int cost) {
            this.destination = destination;
            this.cost = cost;
        }
    }
}
