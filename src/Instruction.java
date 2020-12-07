import java.util.List;

public class Instruction {

    private Command command;
    private List<String> arguments;
    private List<Instruction> instructions;

    public Instruction(Command command, List<String> arguments, List<Instruction> instructions) {
        super();
        this.command = command;
        this.arguments = arguments;
        this.instructions = instructions;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }
}
