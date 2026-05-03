package com.ytdlpjava.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class YoutubeDLExecutor{
    private static final Logger log = LoggerFactory.getLogger(YoutubeDLExecutor.class);
    private final String executablePath;

    public YoutubeDLExecutor(){
        String root = System.getProperty("user.dir");

        Path path = Path.of(root, "bin", "yt-dlp.exe");

        if (!Files.exists(path)) {
            log.error("CRITICAL: yt-dlp.exe not found at {}", path.toAbsolutePath());
        }

        this.executablePath = path.toAbsolutePath().toString();
    }

    public YoutubeDLExecutor(String customPath){
        this.executablePath = customPath;
    }

    public CompletableFuture<YoutubeDLResponse> execute(YoutubeDLRequest request, ProgressListener listener){
        return CompletableFuture.supplyAsync(() -> {
            String regex = "\\[download\\]\\s+([0-9.]+).*at\\s+([0-9a-zA-Z./]+)\\s+ETA\\s+([0-9:]+)";
            Pattern pattern = Pattern.compile(regex);

            try{
                listener.onStart();

                var command = new ArrayList<>(List.of(
                        this.executablePath,
                        "--newline",
                        "--no-colors",
                        "-P", request.getOutputDirectory()
                ));
                if(request.isExtractAudio()) command.add("--extract-audio");
                command.add(request.getUrl());

                var processBuilder = new ProcessBuilder(command);
                processBuilder.redirectErrorStream(true);
                log.debug("Starting yt-dlp process...");
                var startingTime = System.currentTimeMillis();
                Process process = processBuilder.start();

                String absoluteFilePath = "";
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                    String line;
                    while ((line = reader.readLine()) != null){
                        var matcher = pattern.matcher(line);
                        if(matcher.find())
                            listener.onProgress(Float.parseFloat(matcher.group(1)), matcher.group(2), matcher.group(3));
                        else if(line.startsWith("[download] Destination: ") || line.contains("Merging formats into")){
                            String extractedPath = extractPathFromQuotes(line);
                            if(extractedPath != null)
                                absoluteFilePath = extractedPath;
                        }
                    }
                }

                int exitCode = process.waitFor();
                long executionTime = System.currentTimeMillis() - startingTime;
                if(exitCode == 0) {
                    listener.onComplete();
                    log.info("Download completed in {} ms.", executionTime);
                    return new YoutubeDLResponse(absoluteFilePath, request.getVideoQuality(), exitCode, executionTime);
                }
                else listener.onError("Process exited with code: " + exitCode);
            } catch (IOException | InterruptedException e) {
                listener.onError(e.getMessage());
            }
            return null;
        });
    }

    private String extractPathFromQuotes(String line){
        int firstQuote = line.indexOf('\"');
        int lastQuote = line.lastIndexOf('\"');

        if(firstQuote != -1 && lastQuote != -1 && firstQuote != lastQuote){
            return line.substring(firstQuote + 1, lastQuote);
        }

        return null;
    }
}
