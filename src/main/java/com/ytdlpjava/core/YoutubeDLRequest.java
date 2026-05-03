package com.ytdlpjava.core;

public class YoutubeDLRequest {
    private final String url;
    private final String outputDirectory;
    private final String format;
    private final boolean extractAudio;
    private final int videoQuality;
    private final int audioQuality;

    public YoutubeDLRequest(Builder builder){
        this.url = builder.url;
        this.outputDirectory = builder.outputDirectory;
        this.format = builder.format;
        this.extractAudio = builder.extractAudio;
        this.videoQuality = builder.videoQuality;
        this.audioQuality = builder.audioQuality;
    }

    public static Builder builder(String url, String outputDirectory) { return new Builder(url, outputDirectory); }

     public static final class Builder{
        // Required Parameters
        private final String url;
        private final String outputDirectory;

         // Optional Parameters
        private String format;
        private boolean extractAudio = false;
        private int videoQuality = 1080;
        private int audioQuality = 320;

        public Builder(String url, String outputDirectory){
            if(url == null || outputDirectory == null)
                throw new IllegalArgumentException("URL and Output Directory are mandatory.");

            this.url = url;
            this.outputDirectory = outputDirectory;
        }

         public Builder format(String format){
             this.format = format;
             return this;
         }

        public Builder extractAudio(boolean extractAudio){
            this.extractAudio = extractAudio;
            return this;
        }

        public Builder videoQuality(int videoQuality){
            this.videoQuality = videoQuality;
            return this;
        }

        public Builder audioQuality(int audioQuality){
            this.audioQuality = audioQuality;
            return this;
        }

        public YoutubeDLRequest build(){
            return new YoutubeDLRequest(this);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getOutputDirectory(){
        return outputDirectory;
    }

    public String getFormat() {
        return format;
    }

    public boolean isExtractAudio() {
        return extractAudio;
    }

    public int getVideoQuality() {
        return videoQuality;
    }

    public int getAudioQuality() {
        return audioQuality;
    }
}
