package jasyncapicmp;

import jasyncapicmp.cli.CliParser;
import jasyncapicmp.cmp.AsyncApiComparator;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.config.Config;
import jasyncapicmp.config.ConfigValidator;
import jasyncapicmp.loader.FileLoader;
import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.output.StdoutYamlOutput;
import jasyncapicmp.parser.AsyncApiParser;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            CliParser cliParser = new CliParser();
            Config config = cliParser.parse(args);
            ConfigValidator configValidator = new ConfigValidator();
            configValidator.validate(config);
            FileLoader fileLoader = new FileLoader();
            byte[] oldFile = fileLoader.loadFileFromDisc(config.getOldPath());
            byte[] newFile = fileLoader.loadFileFromDisc(config.getNewPath());
            AsyncApiParser asyncApiParser = new AsyncApiParser();
            AsyncApi oldAsyncApi = asyncApiParser.parse(oldFile, config.getOldPath());
            AsyncApi newAsyncApi = asyncApiParser.parse(newFile, config.getNewPath());
            AsyncApiComparator comparator = new AsyncApiComparator();
            ObjectDiff diff = comparator.compare(oldAsyncApi, newAsyncApi);
            StdoutYamlOutput output = new StdoutYamlOutput();
            output.print(diff);
        } catch (JAsyncApiCmpUserException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Execution failed: " + e.getMessage(), e);
        }
    }
}
