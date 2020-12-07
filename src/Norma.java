import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Norma {
    private int[] registrations;
    private List<Instruction> instructionList;
    private int skipBlockIfWhile;
    private Integer valueGoToIfWHile;

    public Norma(int numberRegistrations) {
        this.registrations = new int[numberRegistrations];
        this.instructionList = new ArrayList<>();
    }

    public void run() throws Exception {
        for (int i = 0; i < instructionList.size(); i++) {
            Instruction instruction = instructionList.get(i);
            Command command = instruction.getCommand();

            switch (command) {
                case ISZERO:

                    break;

                case GOTO:
                    i = goTo(instruction);
                    break;

                case IF:
                    checkConditionIfElse(instruction);
                    if(valueGoToIfWHile != null) {
                        i = valueGoToIfWHile;
                        valueGoToIfWHile = null;
                    }
                    break;

                default:
                    runInstruction(instruction);
            }
        }
    }

    public int goTo(Instruction instruction) {
        for (int i = 0; i < instructionList.size(); i++) {
            Instruction internal = instructionList.get(i);

            if (internal.getArguments().get(0).equals(":" + instruction.getArguments().get(0))) {
                return i;
            }
        }
        return 0;
    }

    public void checkConditionWhile(Instruction instruction) throws Exception {
        int registration = registrations[Integer.parseInt(instruction.getArguments().get(1))];
        System.out.println(" WHILE ");

        while (registration == 0) {
            if (valueGoToIfWHile == null) {
                runConditionIfElseWhile(instruction);
                registration = registrations[Integer.parseInt(instruction.getArguments().get(1))];
            } else {
                registration = 1;
            }
        }
    }

    public void checkConditionIfElse(Instruction instruction) throws Exception {
        int registration = registrations[Integer.parseInt(instruction.getArguments().get(1))];

        if (registration == 0) {
            System.out.println("IF " + instruction.getArguments().get(0) + " " + instruction.getArguments().get(1));

            runConditionIfElseWhile(instruction);
        } else {
            Instruction instructionElse = findInstructionElse(instruction);

            if (instructionElse != null) {
                System.out.println("ELSE");
                runConditionIfElseWhile(instructionElse);
            }
        }
    }

    public void runConditionIfElseWhile(Instruction instruction) throws Exception {
        List<Instruction> instructionsInternal = instruction.getInstructions();

        for (int i = 0; i < instructionsInternal.size(); i++) {
            Instruction instructionInternal = instructionsInternal.get(i);

            if (instructionInternal.getCommand() == Command.IF) {
                checkConditionIfElse(instructionInternal);
            } else if (instructionInternal.getCommand() == Command.WHILE) {
                checkConditionWhile(instructionInternal);
            } else {

                if (instructionInternal.getCommand() == Command.ELSE) {
                    continue;
                }
                if (instructionInternal.getCommand() == Command.GOTO) {
                    valueGoToIfWHile = goTo(instructionInternal);
                    break;
                }
                runInstruction(instructionInternal);
            }
        }
    }

    public Instruction findInstructionElse(Instruction instruction) {
        List<Instruction> instructionsInternalIf = instruction.getInstructions();

        for (int i = 0; i < instructionsInternalIf.size(); i++) {
            Instruction instructionInternalIf = instructionsInternalIf.get(i);

            if (instructionInternalIf.getCommand() == Command.ELSE) {
                return instructionInternalIf;
            }
        }
        return null;
    }

    public void runInstruction(Instruction instruction) throws Exception {
        Command command = instruction.getCommand();

        switch (command) {
            case INC:
                System.out.println("INC " + instruction.getArguments().get(0));
                registrations[Integer.parseInt(instruction.getArguments().get(0))]++;
                printRegistrations();
                break;

            case DEC:
                System.out.println("DEC " + instruction.getArguments().get(0));
                registrations[Integer.parseInt(instruction.getArguments().get(0))]--;
                printRegistrations();
                break;

            case SETZERO:
                System.out.println("SET0 " + instruction.getArguments().get(0));
                registrations[Integer.parseInt(instruction.getArguments().get(0))] = 0;
                printRegistrations();
                break;

            case SET:
                System.out.println("SET " + instruction.getArguments().get(0) + " " + instruction.getArguments().get(1));
                registrations[Integer.parseInt(instruction.getArguments().get(0))] = Integer.parseInt(instruction.getArguments().get(1));
                printRegistrations();
                break;

            case ADD:
                System.out.println("ADD " + instruction.getArguments().get(0) + " " + instruction.getArguments().get(1));
                registrations[Integer.parseInt(instruction.getArguments().get(0))] = registrations[Integer.parseInt(instruction.getArguments().get(0))]
                        + registrations[Integer.parseInt(instruction.getArguments().get(1))];
                printRegistrations();
                break;

            default:
                break;
        }
    }

    public void printRegistrations() {
        try {
            Thread.sleep( 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("R0:" + registrations[0] + " " + "R1:" + registrations[1] + " " + "R2:" + registrations[2]
                + " " + "R3:" + registrations[3] + " " + "R4:" + registrations[4] + "\n" + "R5:" + registrations[5]
                + " " + "R6:" + registrations[6] + " " + "R7:" + registrations[7] + " " + "R8:" + registrations[8] + " "
                + "R9:" + registrations[9] + " ");
    }

    public void receiveInstruction(String code) throws Exception {
        List<String> instructionsString = new ArrayList<String>(Arrays.asList(code.split(";")));

        for (int i = 0; i < instructionsString.size(); i++) {
            String instruction = instructionsString.get(i);
            skipBlockIfWhile = 0;

            List<String> line = new ArrayList<String>(Arrays.asList(instruction.split(" ")));

            validateCommand(instruction);
            validateArguments(line);

            Instruction inst = generateInstruction(line, instructionsString, i);

            if (inst != null) {
                if (inst.getCommand() == Command.IF || inst.getCommand() == Command.WHILE || inst.getCommand() == Command.ELSE) {
                    skipBlockIfWhile = 0;
                    skipBlocksIfWhile(instructionsString, i);
                    i = i + skipBlockIfWhile;
                }
                instructionList.add(inst);
            }
        }
    }

    public Instruction generateInstruction(List<String> line, List<String> instructionsString, int i) {
        String command = line.get(0);

        if(command.contains(":")) {
            command = ":";
        }

        switch (command) {
            case "inc":
                line.remove(0);
                return new Instruction(Command.INC, line, null);

            case "dec":
                line.remove(0);
                return new Instruction(Command.DEC, line, null);

            case "set0":
                line.remove(0);
                return new Instruction(Command.SETZERO, line, null);

            case "is0":
                line.remove(0);
                return new Instruction(Command.ISZERO, line, null);

            case "goto":
                line.remove(0);
                return new Instruction(Command.GOTO, line, null);

            case "if":
                return generateInstructionIfWhile(instructionsString, i, line);

            case "while":
                return generateInstructionIfWhile(instructionsString, i, line);

            case "set":
                line.remove(0);
                return new Instruction(Command.SET, line, null);

            case "add":
                line.remove(0);
                return new Instruction(Command.ADD, line, null);

            case ":":
                return new Instruction(Command.DOISPONTOS, line, null);

            default:
        }

        return null;
    }

    public Instruction generateInstructionIfWhile(List<String> instructionsString, int i, List<String> line) {
        List<Instruction> instructionsInternal = new ArrayList<Instruction>();

        for (int j = 0; j < instructionsInternal.size(); j++) {
            String instruction = instructionsString.get(j);

            List<String> lineInternal = new ArrayList<String>(Arrays.asList(instruction.split(" ")));

            String command = lineInternal.get(0);

            if(command.equals("if") || command.equals("while") || command.equals("else")) {
                instructionsInternal.add(generateInstructionIfWhile(instructionsString, j, lineInternal));
                skipBlocksIfWhile(instructionsString, j);
                j = j + skipBlockIfWhile;
                skipBlockIfWhile = 0;
            } else {
                if (command.equals("endif") || command.equals("endwhile") || command.equals("endelse")) {
                    break;
                } else {
                    instructionsInternal.add(generateInstruction(lineInternal, instructionsString, 0));
                }
            }
        }

        String command = line.get(0);
        line.remove(0);

        if (command.equals("if")) {
            return new Instruction(Command.IF, line, instructionsInternal);
        } else if (command.equals("while")) {
            return new Instruction(Command.WHILE, line, instructionsInternal);
        } else {
            return new Instruction(Command.ELSE, line, instructionsInternal);
        }
    }

    public int skipBlocksIfWhile(List<String> instructionsString, int i) {
        int skipBlocks = 0;

        for (int j = 0; j < instructionsString.size(); j++) {
            String instructions = instructionsString.get(j);

            List<String> lineInternal = new ArrayList<String>(Arrays.asList(instructions.split(" ")));

            String command = lineInternal.get(0);

            if(command.equals("if") || command.equals("while") || command.equals("else")) {
                skipBlockIfWhile++;
                skipBlocks++;
                j = j + skipBlocksIfWhile(instructionsString, j);
            } else {
                if(command.equals("endif") || command.equals("endwhile") || command.equals("endelse")) {
                    skipBlockIfWhile++;
                    skipBlocks++;
                    break;
                } else {
                    skipBlocks++;
                    skipBlockIfWhile++;
                }
            }
        }
        return skipBlocks;
    }

    public void validateCommand(String instruction) throws Exception {
        if (instruction.split(" ")[0] == null) {
            throw new Exception("Error ao encontrar comando da instrução: " + instruction);
        }
    }

    public void validateArguments(List<String> arguments) throws Exception {
        if (arguments.size() == 0) {
            throw new Exception("Erro ao encontrar argumentos");
        }
    }
}