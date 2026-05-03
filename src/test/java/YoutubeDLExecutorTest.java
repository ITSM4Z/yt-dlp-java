import com.ytdlpjava.core.ProgressListener;
import com.ytdlpjava.core.YoutubeDLExecutor;
import com.ytdlpjava.core.YoutubeDLRequest;
import com.ytdlpjava.core.YoutubeDLResponse;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

public class YoutubeDLExecutorTest {
    @Test
    public void testSuccessfulDownload(){
        YoutubeDLRequest request = YoutubeDLRequest.builder(
                "https://www.youtube.com/watch?v=6AaKkGrTqjE",
                "C:\\Users\\mazen\\Desktop\\YT-DLP-JAVA-DOWNLOADS").build();

        ProgressListener listener = new ProgressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(float percentage, String speed, String eta) {
                System.out.printf("%.2f, %s, %s\n", percentage, speed, eta);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(String errorMessage) {
                System.out.printf("An error occurred: %s\n", errorMessage);
            }
        };

        var executor = new YoutubeDLExecutor();
        CompletableFuture<YoutubeDLResponse> future = executor.execute(request, listener);
        YoutubeDLResponse response = future.join();

        assertEquals(0, response.exitCode());
        assertNotNull(response.absoluteFilePath());
        assertFalse(response.absoluteFilePath().isEmpty());

        System.out.println("The file is saved at: " + response.absoluteFilePath());
    }
}
