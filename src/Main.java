public class Main {

    public static void main(String[] args) throws Exception {

        String code =
                "set 0 2;"
                        + "set 1 8;"
                        + "add 0 1;"
                        + "set0 1;";
//                "set0 60;"
//                + "set0 61"
//                + "set0 63;"
//                + ":R0>R1"
//                + "if is0 1"
//                + "if is0 0"
//                + "inc 63"
//                + "endif"
//                + ":recupR0>R1"
//                + "if is0 61"
//                + "if is0 60"
//                + "goto endR0>R1"
//                + "endif"
//                + "dec 0"
//                + "dec 60"
//                + "goto recupR0>R1"
//                + "endif"
//                + "inc 1"
//                + "inc 0"
//                + "dec 61"
//                + "goto recupR0>R1"
//                + "endif"
//                + "dec 1"
//                + "inc 61"
//                + "if is0 0"
//                + "inc 60"
//                + "endif"
//                + "dec 0"
//                + "goto R0>R1"
//                + ":endR0>R1";

        Norma n = new Norma(10);
        n.receiveInstruction(code);
        n.run();
    }
}
