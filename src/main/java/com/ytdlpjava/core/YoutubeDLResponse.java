package com.ytdlpjava.core;

public record YoutubeDLResponse(String absoluteFilePath, int videoQuality, int exitCode, long executionTime) {

}