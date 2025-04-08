package pangramchecker;

import java.util.Arrays;
import java.util.HashSet;

public class Solution {
    // old version
//    public static boolean check(String sentence){
//        // sort chars in sentences
//        char[] charArray = sentence.toLowerCase().trim().toCharArray();
//        Arrays.sort(charArray);
//        HashSet<Character> set = new HashSet<>();
//        for (int i = 0; i < charArray.length; i++) {
//            if(set.contains(charArray[i])){
//                continue;
//            }else if(charArray[i] >= 97 && charArray[i] <= 122){
//                set.add(charArray[i]);
//            }
//        }
//        System.out.println(set);
//        return set.size() == 26;
//    }


    // new version, with stream and filter 55ms
//    public static boolean check(String sentence) {
//        return sentence.chars().map(Character::toLowerCase).filter(Character::isAlphabetic).distinct().count() == 26;
//    }

    //42 ms
    public static boolean check(String sentence) {
        sentence = sentence.toLowerCase().trim();
        for (char c = 'a'; c < 'z'; c++) {
            if (!sentence.contains("" + c)) {
                return false;
            }
        }
        return true;
    }
}
