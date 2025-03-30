package narcissistic;

public class Solution {
//    public static boolean isNarcissistic(int number) {
//        if(number < 0){
//            return false;
//        }
//        int temp = number;
//        int sum = 0;
//        int count = String.valueOf(temp).length();
//        while(temp != 0) {
//            sum += Math.pow(temp % 10, count);
//            temp = temp / 10;
//        }
//        boolean isNarcissistic = sum == number;
//        return isNarcissistic;
//    }
//}


    // There are only few narcissistic numbers in the signed 32-bit
    // integer range, so why don't check for them directly? :D

    public static boolean isNarcissistic(int number) {
        switch (number) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 153:
            case 370:
            case 371:
            case 407:
            case 1_634:
            case 8_208:
            case 9_474:
            case 54_748:
            case 92_727:
            case 93_084:
            case 548_834:
            case 1_741_725:
            case 4_210_818:
            case 9_800_817:
            case 9_926_315:
            case 24_678_050:
            case 24_678_051:
            case 88_593_477:
            case 146_511_208:
            case 472_335_975:
            case 534_494_836:
            case 912_985_153:
                return true;
            default:
                return false;
        }
    }

}
