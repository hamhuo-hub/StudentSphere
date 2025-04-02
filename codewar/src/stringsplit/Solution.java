package stringsplit;

public class Solution {


    //56ms
//    public static String[] solution(String s) {
//        //Write your code here
//        int l = s.length();
//        if(l%2 != 0){
//            s = s+"_";
//            l += 1;
//        }
//        String[] result = new String[l/2];
//        int j = 0;
//        for(int i =0; i<=l-2;){
//                 substring complexity is O(n)
//            result[j]=(s.substring(i,i+2));
//            i+=2;
//            j++;
//        }
//        return result;
//    }


//34ms
    public static String[] solution(String s) {
        //Write your code here
        int l = s.length();
        if(l%2 != 0){
            s = s+"_";
            l += 1;
        }
        String[] result = new String[l/2];
        int j = 0;
        for(int i =0; i<=l-2;){
            result[j]=(s.charAt(i) + "" + s.charAt(i+1));
            i+=2;
            j++;
        }
        return result;
    }
}