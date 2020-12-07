public enum Command {
    INC("inc"), DEC("dec"), SETZERO("set0"), ISZERO("is0"), GOTO("goto"), SET("set"), ADD("add"), IF("if"),
    WHILE("while"), DOISPONTOS(":"), ELSE("else");

    private String command;

    Command(String command) {
        this.command = command;
    }

    public static Command deString(String texto) {
        for (Command c : Command.values()) {
            if (c.command.equalsIgnoreCase(texto)) {
                return c;
            }
        }
        return null;
    }
}
