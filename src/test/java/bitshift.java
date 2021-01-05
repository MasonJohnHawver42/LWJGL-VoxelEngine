public class bitshift {

    public static void main(String[] args) {
        int a  = 0; // 0
        int p = 31; // 1 1 1 1 1
        int l = 2; // 1 0

        p = p << 2;
        a += p + l;

        System.out.println(a);
    }
}
