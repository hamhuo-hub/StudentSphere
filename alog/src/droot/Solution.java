package droot;

public class Solution {
//    public static int digital_root(int n) {
//        int res = n;
//        while (res / 10 > 0) {
//            int sum = 0;
//            while (res > 0) {
//                sum += res % 10;
//                res /= 10;
//            }
//            res = sum;
//        }
//        return res;
//    }

    // digit root ~
    public static int digital_root(int n) {
        return (n != 0 && n%9 == 0) ? 9 : n % 9;
    }

}
