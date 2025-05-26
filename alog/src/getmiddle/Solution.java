package getmiddle;

public class Solution {

    // 23824ms
//    public static String getMiddle(String word) {
//        //Code goes here!
//        int length = word.length();
//        if(length == 0) return null;
//        return length%2 != 0 ? String.valueOf(word.charAt(length/2)) : word.substring(length/2-1,length/2+1);
//    }

    // 22577 ms
    public static String getMiddle(String word) {
        //Code goes here!
        int length = word.length();
        if(length == 0) return null;
        return length%2 != 0 ? String.valueOf(word.charAt(length/2)) : word.charAt(length/2-1) + "" + word.charAt(length/2);
    }
}
