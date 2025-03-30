package validatepin;

import java.util.regex.Pattern;

public class Solution {
    // '\d' means 0-9, but in java '\' has been defined, need '\\d' instead
    //  no space in regex '\\d{4} |' means 4 digits with a space
    static Pattern pattern = Pattern.compile("\\d{4}|\\d{6}");
    // 1416ms
//    public static boolean validatePin(String pin) {
//        // Your code here...
//        int l = pin.length();
////        boolean o = (l == 4) || (l == 6) ? true : false;
//        //be aware of ture and false
//        boolean o = (l==4) || (l==6);
////        for reach can't apply to string
//        for(char c : pin.toCharArray()) {
//            if(c<48 || c>57){
//                o = false;
//            }
//        }
//        return o;
//    }

    public static boolean validatePin(String pin) {
        // pre compile improve performance
        return pattern.matcher(pin).matches();
    }

//    public static boolean validatePin(String pin) {
//        return pin.matches("\\d{4}|\\d{6}");
//    }
}
