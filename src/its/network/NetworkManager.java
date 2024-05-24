package its.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import its.model.*;

public class NetworkManager {
    //Project - Post
    public static void createProject(ProjectRequest project) throws Exception {
        URL url = new URL(SECRET.BASE_URL + SECRET.PROJECTS_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(project);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }
    //Project - Get
    public static List<ProjectResponse> getProjectsByUserId(int userId) throws Exception {
        URL url = new URL(SECRET.BASE_URL + SECRET.PROJECTS_ENDPOINT + "?userId=" + userId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.toString(), new TypeReference<List<ProjectResponse>>() {});
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    public static void main(String[] args) {
        try {


//            Project Post 테스트 코드
//             createProject(new ProjectRequest("a minsop -created - swing5", 2, Arrays.asList(1, 3)));


             //Project Get 테스트 코드
//            int userId = 2; // 예시 userId
//            List<ProjectResponse> projects = getProjectsByUserId(userId);
//            for (ProjectResponse project : projects) {
//                System.out.println("Project ID: " + project.getProjectId());
//                System.out.println("Title: " + project.getTitle());
//                System.out.println("Admin Name: " + project.getAdminName());
//                System.out.println("Contributors: " + String.join(", ", project.getContributorNames()));
//                System.out.println();
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
