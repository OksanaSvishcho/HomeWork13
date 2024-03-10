import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserOperations2 {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";


    public void getUserLatestPostComments(int userId) throws IOException {

        String userPostsUrl = BASE_URL + "/users/" + userId + "/posts";
        URL url = new URL(userPostsUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            String[] posts = response.toString().split("\\},\\{");
            String lastPostId = posts[posts.length - 1].split(",")[0].split(":")[1];
            String lastPostCommentsUrl = BASE_URL + "/posts/" + lastPostId + "/comments";

            // Отримання коментарів до останнього поста
            URL commentsUrl = new URL(lastPostCommentsUrl);
            HttpURLConnection commentsConnection = (HttpURLConnection) commentsUrl.openConnection();
            commentsConnection.setRequestMethod("GET");

            try (InputStream commentsInputStream = commentsConnection.getInputStream();
                 InputStreamReader commentsInputStreamReader = new InputStreamReader(commentsInputStream);
                 BufferedReader commentsBufferedReader = new BufferedReader(commentsInputStreamReader)) {
                StringBuilder commentsResponse = new StringBuilder();
                while ((line = commentsBufferedReader.readLine()) != null) {
                    commentsResponse.append(line);
                }

                String fileName = "user-" + userId + "-post-" + lastPostId + "-comments.json";
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                    writer.write(commentsResponse.toString());
                }

                System.out.println("Comments for the latest post of user " + userId + " have been written to " + fileName);
            }
        }
    }
}

