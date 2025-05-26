package spinwords;

public class Solution {

    // 1910ms/36ms
//    public static String spinWords(String sentence) {
//        //TODO: Code stuff here
//        StringBuilder result = new StringBuilder();
//        StringBuilder word = new StringBuilder();
//        int count = 0;
//        for (int i = 0; i < sentence.length(); i++) {
//            char ch = sentence.charAt(i);
//            if (ch == ' ') {
//                if(count >= 5) {
//                    result.append(reverse(word));
//                }
//                else {
//                    result.append(word.toString());
//                }
//                result.append(" ");
//                word = new StringBuilder("");
//                count = 0;
//                continue;
//            }
//            word.append(ch);
//            count++;
//            if (i == sentence.length() - 1) {
//                if(count >= 5) {
//                    result.append(reverse(word));
//                }
//                else result.append(word.toString());
//            }
//        }
//        return result.toString();
//    }
//
//    public static String reverse(StringBuilder word) {
//        StringBuilder result = new StringBuilder();
//        for (int i = word.length() - 1; i >= 0; i--) {
//            result.append(word.charAt(i));
//        }
//        return result.toString();
//    }

    public static String spinWords(String sentence)  {
        //preprocess
        String[] words = sentence.split(" ");
        // what if replaced with enhanced-loop?
        //  words[i] is directly modified because we assign a new string to the same index in the array.
        // 
        for(int i=0; i<words.length; i++) {
            if (words[i].length() >= 5){
                words[i] = new StringBuilder(words[i]).reverse().toString();
            }
        }
        return String.join(" ", words);
    }
}