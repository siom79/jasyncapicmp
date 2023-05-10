package jasyncapicmp.config;

import jasyncapicmp.JAsyncApiCmpUserException;

public class ConfigValidator {
    public void validate(Config config) {
        if (config.getOldPath() == null || config.getOldPath().trim().length() == 0) {
            throw new JAsyncApiCmpUserException("Missing old path. Please provide the path to the old version. Try argument -h or --help.");
        }
        if (config.getNewPath() == null || config.getNewPath().trim().length() == 0) {
            throw new JAsyncApiCmpUserException("Missing new path. Please provide the path to the new version. Try argument -h or --help.");
        }
    }
}
