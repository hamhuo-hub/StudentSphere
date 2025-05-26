package rgbtohex;


public class Solution {
    public static String rgb(int r, int g, int b) {
        if(r >= 0 && g >= 0 && b >= 0) {
            String sr = String.format("%02d", Integer.toHexString(r));
            String sg = String.format("%02d", Integer.toHexString(g));
            String sb = String.format("%02d", Integer.toHexString(b));
            return sr + sg + sb;
        }
        return null;
    }
}




