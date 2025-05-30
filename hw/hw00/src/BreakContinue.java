public class BreakContinue {
    public static void windowPosSum(int[] a, int n) {
        /** your code here */
        // 1 2 -3 4 5 4
        // 0 1  2 3 4 5
        int sum = 0;
        for(int i = 0;i <= a.length - 1;i++)
        {
            sum = a[i];
            for(int j = 0;j < n;j++)
            {
                if(a[i] <= 0)
                {continue;}
                if(j + i == a.length - 1)
                {break;}
                sum += a[j+i+1];
            }
            a[i] = sum;
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 2, -3, 4, 5, 4};
        int n = 3;
        windowPosSum(a, n);

        // Should print 4, 8, -3, 13, 9, 4
        System.out.println(java.util.Arrays.toString(a));
    }
}