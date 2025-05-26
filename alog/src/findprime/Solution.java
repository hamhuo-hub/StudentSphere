package findprime;

public class Solution {
    public static boolean isPrime(int num) {
        if (num == 2 || num == 5) {
            return true;
        } else if (num <= 0 || num == 1) {
            return false;
        }
        switch (num % 10) {
            case 0:
            case 2:
            case 4:
            case 5:
            case 6:
            case 8:
                return false;
        }
        for (int i = 3; i <= Math.sqrt(num); i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}
