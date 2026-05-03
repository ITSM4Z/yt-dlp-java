# YT-DLP Java Wrapper

This is a Java library I built to make it easier to use yt-dlp inside Java applications. Instead of manually messing around with command line arguments and process streams, this wrapper handles all that in the background. It uses a listener system so you can get real-time updates on things like download percentage, speed, and ETA without freezing your main program.

I designed this specifically to be used in GUI apps (like JavaFX), so it runs everything on separate threads to keep the interface responsive.

## How it works

The core of this project is a ProcessBuilder that talks to the yt-dlp executable. It parses the text output from the console using Regex to extract the numbers you actually care about. Everything is wrapped in a CompletableFuture, which is just a fancy way of saying the code will tell you when it's done without making your whole program wait around.

## Setup

Since this is just a wrapper, you need to have the actual yt-dlp tool on your machine.

1. Download the yt-dlp.exe from their GitHub releases.
2. Create a folder named bin in your project's root directory.
3. Drop the yt-dlp.exe inside that bin folder.

By default, the code looks for the executable in that specific spot. If you want to put it somewhere else, you can just pass the path to the constructor when you initialize the executor.

## Usage Example

Here is a quick look at how to trigger a download:
```java
// Create the request with the URL and where you want the file to go
YoutubeDLRequest request = YoutubeDLRequest.builder(
        "[MEDIA-URL]",
        "[FILE-OUTPUT-PATH]")
    .build();

// Initialize the engine
YoutubeDLExecutor executor = new YoutubeDLExecutor();

// Start the download
executor.execute(request, new ProgressListener() {
    @Override
    public void onProgress(float percentage, String speed, String eta) {
        System.out.println("Percentage: " + percentage + "%");
    }

    @Override
    public void onError(String errorMessage) {
        System.out.println("Something went wrong: " + errorMessage);
    }

    // You also have onStart and onComplete methods available
});
```
## Disclamer

This project is for educational purposes. I built it to learn more about process management and threading in Java.

You are responsible for how you use this tool. Make sure you aren't breaking any laws or the Terms of Service of the sites you are downloading from. I am not responsible for any copyright issues or misuse of this software. Use it fairly and respect the creators.

## Credits

A lot of great open-source tools made this possible:

* yt-dlp: The actual engine that does the hard work of finding and downloading the video streams.

* JUnit 5: Used for the unit tests to make sure the regex and process logic actually work.

* SLF4J: Used for the logging system so we can see what's happening behind the scenes.

## License

This project is under the MIT License. Feel free to use the code, change it, or do whatever you want with it, just keep the license file with it.
