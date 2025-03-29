package palindromes;

public class Palindromes {
    public static boolean isPalindrome(long n) {
        long transform = 0;
        while (n != 0) {
            // get right bit
            transform = transform * 10 + n % 10;
            if(n == transform){
                return true;
            }
            n = n / 10;
        }
        return false;
    }

//    public static boolean isPalindrome(long n) {
//        if(n == 0) return false;
//        String s = Long.toString(n);
//        int left = 0, right = s.length() - 1;
//
//        while (left < right) {
//            if (s.charAt(left) != s.charAt(right)) {
//                return false;
//            }
//            left++;
//            right--;
//        }
//        return true;
//    }

}
