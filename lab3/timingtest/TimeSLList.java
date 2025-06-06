package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<Integer>();
        AList<Double> times = new AList<Double>();
        AList<Integer> opCounts = new AList<Integer>();
        for(int i = 1000;i <= 64000;i = i*2){
            //创建一个临时SLList
            SLList<Integer> current = new SLList<Integer>();
            //处理addLast操作
            for(int j = 0;j <= i;j++){
                current.addLast(1);
            }
            //创建计时器
            Stopwatch sw = new Stopwatch();
            //处理getLast
            for(int j = 0;j <= 10000;j++)
            {
                current.getLast();
            }
            //存储时间
            double timeTaken = sw.elapsedTime();
            //向之前的SLList传递数据
            Ns.addLast(i);
            times.addLast(timeTaken);
            opCounts.addLast(10000);
        }
        //print输出
        printTimingTable(Ns,times,opCounts);
    }

}
