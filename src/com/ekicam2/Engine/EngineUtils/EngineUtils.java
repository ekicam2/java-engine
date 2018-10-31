package com.ekicam2.Engine.EngineUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EngineUtils {
    public static String ReadFromFile(String PathToFile) {
        try {
            return new String(Files.readAllBytes(Paths.get(PathToFile)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return "";
    }
}
