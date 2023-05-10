package jasyncapicmp.loader;

import jasyncapicmp.JAsyncApiCmpUserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileLoader {

    public byte[] loadFileFromDisc(String file) {
        try {
            return Files.readAllBytes(Paths.get(file));
        } catch (IOException e) {
            throw new JAsyncApiCmpUserException("Failed to read file: " + file);
        }
    }
}
