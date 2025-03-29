package numberfun;


public class NumberFun {

// 1278 ms

//    public static long findNextSquare(long sq) {
//
//        // perfect number should end with 0 , 9, 5, 4,1
//        int back = (int)(sq % 10);
//        // indicate input is perfect square or not
//        if(back != 0 && back != 9 && back != 5 && back != 4 && back != 1 && back != 6){
//            return -1L;
//        }
//
//        // maybe a perfect number
//        else{
//
//            // find square root
//            // use repeated substraction
//            int factor = 1, i = 0;
//            while(sq > 0){
//                sq -= factor;
//                i++;
//                factor += 2;
//            }
//            long sqrt = i+1;
//            if(sq == 0) return sqrt*sqrt;
//            else return -1L;
//        }
//    }


 //890 ms

    public static long findNextSquare(long sq) {
        long root = (long) Math.sqrt(sq);
        return root * root == sq ? (root + 1) * (root + 1) : -1;
    }


// 2909 ms
//    public static long findNextSquare(long sq) {
//        for(long i=1; i<sq; i++){
//            if(i*i == sq){
//                return (i+1)*(i+1);
//            } else if (i*i > sq) {
//                return -1;
//            }
//        }
//        return -1;
//    }


}




