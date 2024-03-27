import java.util.*;
import java.io.*;

public class Graph
{
    public int V;
    public int KinMin;
    public int KinMax;
    public int KoutMin;
    public int KoutMax;
    private Random random = new Random();
    public List<List<Integer>> adjacencyList;
    public int[] pathLength;
    public int[] parentV;

    public Graph(int V, int KinMin, int KinMax, int KoutMin, int KoutMax) throws IllegalArgumentException
    {
        if (V <= 0 || KinMin < 0 || KinMin > KinMax || KinMax >= V || KoutMin < 0 || KoutMin > KoutMax || KoutMax >= V)
        {
            throw new IllegalArgumentException("Pazeistos ribos");
        }
        this.V = V;
        this.KinMin = KinMin;
        this.KinMax = KinMax;
        this.KoutMin = KoutMin;
        this.KoutMax = KoutMax;
        adjacencyList = new ArrayList<>(V);

        for (int i = 0; i < V; i++)
        {
            adjacencyList.add(new ArrayList<>());
        }

        generateB();
    }

    public Graph(int V) throws IllegalArgumentException
    {
        if (V <= 0)
        {
            throw new IllegalArgumentException("Virsuniu skaicius turi buti teigiamas");
        }

        this.V = V;
        adjacencyList = new ArrayList<>(V);

        for (int i = 0; i < V; i++)
        {
            adjacencyList.add(new ArrayList<>());
        }
    }

    private boolean checkIfHasB(int startV, int endV)
    {
        for (int i = 0; i < adjacencyList.get(startV).size(); i++)
        {
            int K = adjacencyList.get(startV).get(i);
            if (K == endV)
            {
                return true;
            }
        }
        return false;
    }

    private void addOrientedB(int u, int v)
    {
        adjacencyList.get(u).add(v);
    }

    private void removeOrientedB(int u, int v)
    {
        adjacencyList.get(u).remove(Integer.valueOf(v));
    }

    public void generateB()
    {
        Random random = new Random();
        int[][] degreesV = new int[V][2];

        for (int i = 0; i < V; i++)
        {
            while (degreesV[i][0] < KoutMin)
            {
                int B = random.nextInt(V);
                if (B != i && degreesV[B][1] <= KinMax
                        && degreesV[B][0] <= KoutMax
                        && !checkIfHasB(i, B))
                {
                    adjacencyList.get(i).add(B);
                    degreesV[i][0]++;
                    degreesV[B][1]++;
                }
            }
        }

        for (int i = 0; i < V; i++)
        {
            List<Integer> BList = new ArrayList<>();
            for (int j = 0; j < V; j++)
            {
                if (i != j
                        && degreesV[j][0] < KoutMax
                        && degreesV[j][1] < KinMax)
                {
                    BList.add(j);
                }
            }

            while (degreesV[i][1] < KinMin
                    && !BList.isEmpty())
            {
                int B = BList.remove(random.nextInt(BList.size()));
                if (!checkIfHasB(B, i))
                {
                    adjacencyList.get(B).add(i);
                }
                degreesV[B][0]++;
                degreesV[i][1]++;
            }
        }
    }

    public void findFurthestVBFS(int graphV)
    {
        pathLength = new int[V];
        parentV = new int[V];
        Arrays.fill(pathLength, -1);

        Queue<Integer> queue = new LinkedList<>();
        queue.add(graphV);
        pathLength[graphV] = 0;

        while (!queue.isEmpty())
        {
            int current = queue.remove();
            for (int i = 0; i < adjacencyList.get(current).size(); i++)
            {
                int K = adjacencyList.get(current).get(i);
                if (pathLength[K] == -1)
                {
                    pathLength[K] = pathLength[current] + 1;
                    parentV[K] = current;
                    queue.add(K);
                }
            }
        }

        int maxV = -1;
        int maxRange = -1;

        for (int i = 0; i < V; i++)
        {
            if (pathLength[i] > maxRange)
            {
                maxV = i;
                maxRange = pathLength[i];
            }
        }

        if (maxV != -1)
        {
            System.out.println("Kelias iki labiausiai nutolusios virsunes " + graphV + " iki " + maxV + " ilgis: " + maxRange + ":");
            List<Integer> path = new ArrayList<>();
            while (maxV != graphV)
            {
                path.add(maxV);
                maxV = parentV[maxV];
            }
            path.add(graphV);
            Collections.reverse(path);
            System.out.println(path);
        }
    }

    public void printGraph()
    {
        for (int i = 0; i < V; i++)
        {
            System.out.print(i + " ==> ");
            List<Integer> K = adjacencyList.get(i);
            if (!K.isEmpty())
            {
                for (int j = 0; j < K.size(); j++)
                {
                    int singleK = K.get(j);
                    System.out.print(singleK + " ");
                }
                System.out.println();
            }
            else
            {
                System.out.println("Briaunu nera");
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length == 1)
        {
            int V = Integer.parseInt(args[0]);

            Graph graph = new Graph(V);

            for (int i = 0; i < V; i++)
            {
                System.out.println("Irasyti kaimynus virsunej " + i + " rasyti 'baigti', kad pereiti prie kitos virsunes");
                Scanner scanner = new Scanner(System.in);
                String inputK = scanner.nextLine();
                while (!inputK.equalsIgnoreCase("baigti"))
                {
                    int K = Integer.parseInt(inputK);
                    if (K < 0 || K >= V)
                    {
                        System.err.println("Pasirinkti reikia tarp 0 ir " + (V-1) +  " Netinkama virsune: " + K);
                    }
                    else
                    {
                        graph.adjacencyList.get(i).add(K);
                    }
                    inputK = scanner.nextLine();
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.print("Irasyti is kurios virsunes norima pradeti BFS tolimiausios virsunes paieska: ");
            int searchV = scanner.nextInt();
            scanner.nextLine();
            graph.printGraph();
            graph.findFurthestVBFS(searchV);
        }
        else if (args.length == 5) // grafo generavimas
        {
            int V = Integer.parseInt(args[0]);
            int KinMin = Integer.parseInt(args[1]);
            int KinMax = Integer.parseInt(args[2]);
            int KoutMin = Integer.parseInt(args[3]);
            int KoutMax = Integer.parseInt(args[4]);

            Graph graph = new Graph(V, KinMin, KinMax, KoutMin, KoutMax);

            graph.printGraph();
            graph.findFurthestVBFS(1);
        }
        else
        {
            System.err.println("java Graphr V KinMin KinMax KoutMin KoutMax ARBA java Graph (Virsuniu Skaicius)");
            System.exit(1);
        }
    }
}