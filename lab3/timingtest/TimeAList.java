package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<Integer>();
        AList<Double> times = new AList<Double>();
        AList<Integer> opCounts = new AList<Integer>();
        for(int i = 1000;i <= 64000;i = i*2) {
            //创建一个临时Alist去用于操作addLast方法
            AList<Integer> current = new AList<Integer>();
            //用于计算时间
            Stopwatch sw = new Stopwatch();
            //addLast方法调用
            int j;
            for(j = 0;j <= i;j++){
                current.addLast(1);
            }
            //调用sw的elapsedTime去计算时间
            double timeTaken = sw.elapsedTime();
            //存储个数
            Ns.addLast(i);
            //存储时间
            times.addLast(timeTaken);
            //存储操作数
            opCounts.addLast(j);
        }
        //调用printTimingTable去输出表格
        printTimingTable(Ns,times,opCounts);
    }
}
