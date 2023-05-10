package jasyncapicmp.cli;

import jasyncapicmp.JAsyncApiCmpUserException;
import jasyncapicmp.config.Config;

import java.util.Stack;

public class CliParser {

    public Config parse(String[] args) {
        Config config = new Config();
        Stack<String> argStack = toStack(args);
        while (!argStack.empty()) {
            String arg = argStack.pop();
            if ("-o".equals(arg) || "--old".equals(arg)) {
                if (argStack.isEmpty()) {
                    throw new JAsyncApiCmpUserException("Missing argument for option -o/--old.");
                }
                String pop = argStack.pop();
                if (pop.startsWith("-")) {
                    throw new JAsyncApiCmpUserException("Missing argument for option -o/--old.");
                }
                config.setOldPath(pop);
            }
            if ("-n".equals(arg) || "--new".equals(arg)) {
                if (argStack.isEmpty()) {
                    throw new JAsyncApiCmpUserException("Missing argument for option -n/--new.");
                }
                String pop = argStack.pop();
                if (pop.startsWith("-")) {
                    throw new JAsyncApiCmpUserException("Missing argument for option -n/--new.");
                }
                config.setNewPath(pop);
            }
            if ("-h".equals(arg) || "--help".equals(arg)) {
                String message = "Usage: [-o <path> | --old <path>] [-n <path> | --new <path>]";
                throw new JAsyncApiCmpUserException(message);
            }
        }
        return config;
    }

    private static Stack<String> toStack(String[] args) {
        Stack<String> argStack = new Stack<>();
        for (int i = args.length - 1; i >= 0; i--) {
            argStack.push(args[i]);
        }
        return argStack;
    }
}
