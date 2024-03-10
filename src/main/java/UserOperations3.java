import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserOperations3 {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";


    public void getOpenTasksForUser(int userId) throws IOException {

        String userTodosUrl = BASE_URL + "/users/" + userId + "/todos";
        URL url = new URL(userTodosUrl);
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


            String[] todos = response.toString().split("\\},\\{");
            StringBuilder openTasks = new StringBuilder("[");
            for (String todo : todos) {
                if (todo.contains("\"completed\": false")) {
                    openTasks.append("{").append(todo).append("},");
                }
            }
            openTasks.deleteCharAt(openTasks.length() - 1);
            openTasks.append("]");


            String fileName = "user-" + userId + "-open-tasks.json";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(openTasks.toString());
            }

            System.out.println("Open tasks for user " + userId + " have been written to " + fileName);
        }
    }
}

