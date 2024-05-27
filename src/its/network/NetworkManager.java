package its.network;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import its.model.*;

public class NetworkManager {
    // Post with responseType
    public static <T> T post(String urlStr, Object request, Class<T> responseType) throws Exception {
        return post(urlStr, request, responseType, true);
    }

    // Post without responseType
    public static void post(String urlStr, Object request) throws Exception {
        post(urlStr, request, null, false);
    }

    // Post
    private static <T> T post(String urlStr, Object request, Class<T> responseType, boolean hasResponseType) throws Exception {
        HttpURLConnection connection = setupPostConnection(urlStr);
        sendRequest(connection, request);

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            return hasResponseType ? handleSuccessResponse(connection, responseType) : null;
        } else {
            handleErrorResponse(connection);
            return null; // This line will not be reached since handleErrorResponse will throw an exception
        }
    }

    private static HttpURLConnection setupPostConnection(String urlStr) throws IOException {
        URL url = URI.create(urlStr).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        return connection;
    }

    private static void sendRequest(HttpURLConnection connection, Object request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonInputString = objectMapper.writeValueAsString(request);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    private static <T> T handleSuccessResponse(HttpURLConnection connection, Class<T> responseType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return objectMapper.readValue(response.toString(), responseType);
        }
    }

    private static void handleErrorResponse(HttpURLConnection connection) throws IOException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream errorStream = connection.getErrorStream()) {
            if (errorStream != null) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }

                JsonNode errorJson = objectMapper.readTree(errorResponse.toString());
                String errorMessage = errorJson.get("message").asText();
                throw new Exception("Error: " + errorMessage);
            } else {
                throw new Exception("Error: No error response body available");
            }
        }
    }

    //User controller
    public static void resister(RegisterRequest registerRequest) throws Exception {
        post(SECRET.REGISTER_URL, registerRequest);
    }

    public static LoginResponse login(LoginRequest loginRequest) throws Exception {
        return post(SECRET.LOGIN_URL, loginRequest, LoginResponse.class);
    }

    public static List<UserResponse> getUsers() throws Exception {
        URL url = new URL(SECRET.USERS_URL);
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
            return objectMapper.readValue(response.toString(), new TypeReference<List<UserResponse>>() {});
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    //Project - Post
    public static void createProject(ProjectRequest project) throws Exception {
        URL url = new URL(SECRET.PROJECTS_URL);
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
        URL url = new URL(SECRET.PROJECTS_URL + "?userId=" + userId);
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
            int userId = 2; // 예시 userId
            List<ProjectResponse> projects = getProjectsByUserId(userId);
            for (ProjectResponse project : projects) {
                System.out.println("Project ID: " + project.getProjectId());
                System.out.println("Title: " + project.getTitle());
                System.out.println("Admin Name: " + project.getAdminName());
                System.out.println("Contributors: " + String.join(", ", project.getContributorNames()));
                System.out.println();
            }

            //User Controller : Register & Login
//            resister(new RegisterRequest("Minsop test - 102", "1234", "Dev"));
//            LoginResponse a = login(new LoginRequest("Minsop test - 102", "1234"));
//            System.out.println(a.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            String a = e.getMessage();
            System.out.println(a);
        }
    }
}
