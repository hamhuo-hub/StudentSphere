package phonenumber;

public class Solution {
    //74 ms
//    public static String createPhoneNumber(int[] numbers) {
//        // Your code here!
//        String country = numbers[0] + "" + numbers[1] + "" + numbers[2];
//        String area = numbers[3] + "" + numbers[4] + "" + numbers[5];
//        String pop = "";
//        for (int i = 6; i <= numbers.length - 1; i++) {
//            pop = pop + numbers[i] + "";
//        }
//        return "(" + country + ")" + " " + area + "-" + pop;
//    }

    // 70 ms
    public static String createPhoneNumber(int[] numbers) {
        // Your code here!
       return new StringBuilder()
               .append("(")
               .append(numbers[0])
               .append(numbers[1])
               .append(numbers[2])
               .append(") ")
               .append(numbers[3])
               .append(numbers[4])
               .append(numbers[5])
               .append("-")
               .append(numbers[6])
               .append(numbers[7])
               .append(numbers[8])
               .append(numbers[9])
               .toString();
    }

    //86 ms
//    public static String createPhoneNumber(int[] numbers){
//        return String.format("(%d%d%d) %d%d%d-%d%d%d%d",numbers[0],numbers[1],numbers[2],numbers[3],numbers[4],numbers[5],numbers[6],numbers[7],numbers[8],numbers[9] );
//    }
}
