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
    public static <T> T post(String urlStr, Object request, TypeReference<T> responseType) throws Exception {
        return post(urlStr, request, responseType, true);
    }

    // Post without responseType
    public static void post(String urlStr, Object request) throws Exception {
        post(urlStr, request, null, false);
    }

    // Post
    private static <T> T post(String urlStr, Object request, TypeReference<T> responseType, boolean hasResponseType) throws Exception {
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

//        String jsonInputString = "{\"username\":\"가입되나\",\"password\":\"1234\",\"role\":\"Admin\"}";
//        System.out.println(jsonInputString);
        String jsonInputString = objectMapper.writeValueAsString(request);
        System.out.println(jsonInputString);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    private static <T> T handleSuccessResponse(HttpURLConnection connection, TypeReference<T> responseType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println("Response JSON: " + response.toString());
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
        return post(SECRET.LOGIN_URL, loginRequest, new TypeReference<LoginResponse>(){});
    }

    public static List<UserResponse> getContributors() throws Exception {
        URL url = new URL(SECRET.USERS_CONTRIBUTORS_URL);
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
    public static ProjectResponse createProject(ProjectRequest project) throws Exception {

        URL url = new URL(SECRET.PROJECTS_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("accept", "*/*");
        connection.setDoOutput(true);

        System.out.println("ㅇㅇㅇ: ");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonInputString = objectMapper.writeValueAsString(project);
        System.out.println("jsonInputString: " + jsonInputString);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        System.out.println("레스폰스: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return objectMapper.readValue(response.toString(), new TypeReference<ProjectResponse>() {});
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

    // Get Dev User in Project
    public static List<UserResponse> getDevUsersByProjectId(int projectId) throws Exception {
        URL url = new URL(SECRET.PROJECTS_URL + "/" + projectId + "/dev");
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

    //Issue

    //Issue Get
    public static List<IssueResponse> getIssuesByProjectId(int projectId) throws Exception {
        URL url = new URL(SECRET.ISSUES_URL + "?projectId=" + projectId);
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
            return objectMapper.readValue(response.toString(), new TypeReference<List<IssueResponse>>() {});
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    // Issue Get by Status
    public static List<IssueResponse> getIssuesByStatus(String stringStatus, int projectId) throws Exception {
        URL url = new URL(SECRET.ISSUES_WITH_SLAHSH_URL + stringStatus +"?projectId=" + projectId);
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
            return objectMapper.readValue(response.toString(), new TypeReference<List<IssueResponse>>() {});
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    //Issue Post
    public static List<IssueResponse> createIssuesByProjectId(IssueRequest issueRequest, int projectId) throws Exception {
        return post(SECRET.ISSUES_URL + "?projectId=" + projectId, issueRequest, new TypeReference<List<IssueResponse>>() {
        });

//        URL url = new URL(SECRET.ISSUES_URL + "?projectId=" + projectId);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestProperty("accept", "*/*");
//        connection.setDoOutput(true);
//
//        System.out.println("ㅇㅇㅇ: ");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//        String jsonInputString = objectMapper.writeValueAsString(issueRequest);
//        System.out.println("jsonInputString: " + jsonInputString);
//
//        try (OutputStream os = connection.getOutputStream()) {
//            byte[] input = jsonInputString.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//
//        int responseCode = connection.getResponseCode();
//        System.out.println("레스폰스: " + responseCode);
//
//        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String inputLine;
//            StringBuilder response = new StringBuilder();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            return objectMapper.readValue(response.toString(), new TypeReference<List<IssueResponse>>() {});
//        } else {
//            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
//        }
//        URL url = new URL(SECRET.ISSUES_URL + "?projectId=" + projectId);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestProperty("accept", "*/*");
//        connection.setDoOutput(true);
//        System.out.println("ㅇㅇㅇ: ");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//        String jsonInputString = objectMapper.writeValueAsString(issueRequest);
//        System.out.println("jsonInputString: " + jsonInputString);
//        try (OutputStream os = connection.getOutputStream()) {
//            byte[] input = jsonInputString.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//
//        int responseCode = connection.getResponseCode();
//        System.out.println("레스폰스: " + responseCode);
//
//        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String inputLine;
//            StringBuilder response = new StringBuilder();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            return objectMapper.readValue(response.toString(), new TypeReference<List<IssueResponse>>() {});
//        } else {
//            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
//        }
    }

    //Issue Detail
    // Issue Get by IssueId
    public static IssueResponse getIssueDetailByIssueId(int issuseId) throws Exception {
        URL url = new URL(SECRET.ISSUE_DETAILS_URL +"?issueId=" + issuseId);
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
            return objectMapper.readValue(response.toString(), new TypeReference<IssueResponse>() {});
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    // Issue update Status
    public static IssueResponse updateIssueStatus(int issueId, IssueRequest issueRequest) throws Exception {
        URL url = new URL(SECRET.ISSUES_WITH_SLAHSH_URL + "status?issueId=" + issueId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonInputString = objectMapper.writeValueAsString(issueRequest);
        System.out.println("url" + url.toString());
        System.out.println("jsonInputString: " + jsonInputString);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return objectMapper.readValue(response.toString(), new TypeReference<IssueResponse>() {});
        } else {
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
    }


    //Comment
    // Comment Get
    public static List<CommentResponse> getCommentsByIssueId(int issuseId) throws Exception {
        URL url = new URL(SECRET.COMMENTS_URL +"?issueId=" + issuseId);
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
            return objectMapper.readValue(response.toString(), new TypeReference<List<CommentResponse>>() {});
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    // Comment Post
    public static List<CommentResponse> postComment(CommentRequest comment, int issueId) throws Exception {
        return post(SECRET.COMMENTS_URL + "?issueId=" + issueId, comment, new TypeReference<List<CommentResponse>>() {
        });
//        URL url = new URL(SECRET.COMMENTS_URL + "?issueId=" + issueId);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//
//        int responseCode = connection.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String inputLine;
//            StringBuilder response = new StringBuilder();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.readValue(response.toString(), new TypeReference<List<CommentResponse>>() {});
//        } else {
//            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
//        }
    }

    //Comment Delete
    public static List<CommentResponse> deleteComment(int commentId, CommentRequest commentRequest) throws Exception {
        URL url = new URL(SECRET.COMMENTS_URL + "?commentId=" + commentId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonInputString = objectMapper.writeValueAsString(commentRequest);
        System.out.println("jsonInputString: " + jsonInputString);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return objectMapper.readValue(response.toString(), new TypeReference<List<CommentResponse>>() {});
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    // AssigneeSelection
    // Get candidates
    public static UserResponse getCandidate(int issueId) throws Exception {
        URL url = new URL(SECRET.ISSUE_CANDIDATE_URL +"?issueId=" + issueId);
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
            return objectMapper.readValue(response.toString(), new TypeReference<UserResponse>() {});
        } else if (responseCode == 404) {
            return null;
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    public static void applyAssignee(int issueId, IssueRequest issueRequest) throws Exception {
        post(SECRET.ISSUES_ASSIGNS_URL + "?issueId=" + issueId, issueRequest);
    }

    public static StatisticsResponse getStatistics(int projectId) throws Exception{
        URL url = new URL(SECRET.STATISTICS_URL +  projectId);
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
            return objectMapper.readValue(response.toString(), new TypeReference<StatisticsResponse>() {});
        } else if (responseCode == 404) {
            return null;
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    public static void main(String[] args) {
        try {
//            Project Post 테스트 코드
//            ProjectResponse projectResponse = createProject(new ProjectRequest("a minsop -created ㅂㅈㄷㅂㅈㄷㅈㅂㄷㅈㅂ1111ㄷ", 1, Arrays.asList(1, 3)));
//            System.out.println(Arrays.toString(projectResponse.getProjectContent()));

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

            //User Controller : Register & Login
//            resister(new RegisterRequest("여기서 가입되나??222", "1234", "Dev"));
//            LoginResponse a = login(new LoginRequest("여기서 가입되나??222", "1234"));
//            System.out.println(a.getUsername());

            // Issues Get 테스트 코드
//            int proejctId = 1; // 예시 userId
//            List<IssueResponse> issues = getIssuesByProjectId(proejctId);
//            for (IssueResponse issue : issues) {
//                System.out.println("issue ID: " + issue.getId());
//                System.out.println("issue name: " + issue.getTitle());
//                System.out.println("issue status: " + issue.getStatus().toString());
//                System.out.println("fixer: " + (issue.getFixer() == null));
//                System.out.println();
//            }

            //post Issues By Project Id 테스트 코드
//            IssueRequest issueRequest = new IssueRequest("ㅁㄴㅇㅁㄴㅇㄴㅇㅇ1", "테스트 이슈 설명", 0, Status.NEW, Priority.TRIVIAL, 4);
//
//            List<IssueResponse> issues = createIssuesByProjectId(issueRequest, 14);
//            for (IssueResponse issue : issues) {
//                System.out.println("issue ID: " + issue.getId());
//                System.out.println("issue Title: " + issue.getTitle());
//                System.out.println("issue status: " + issue.getStatus().toString());
//                System.out.println("issue priority: " + (issue.getPriority().toString()));
//                System.out.println();
//            }

            //Issue Get by Issue ID
//            IssueResponse issue = getIssueDetailByIssueId(1);
//            System.out.println(issue.getTitle());
//            System.out.println(issue.getAssignee());

            //Comment Get By Issue ID
//            List<CommentResponse> comments = getCommentsByIssueId(1);
//            for (CommentResponse comment : comments){
//                System.out.println(comment.getUsername());
//                System.out.println(comment.getRole());
//                System.out.println(comment.getMessage());
//                System.out.println(comment.getCreatedAt());
//            }

//            Post Comment
//            CommentRequest commentRequest = new CommentRequest("여기서 단s거", 4);
//            List<CommentResponse> comments = postComment(commentRequest, 1);
//            for (CommentResponse comment : comments){
//                System.out.println(comment.getUsername());
//                System.out.println(comment.getRole());
//                System.out.println(comment.getMessage());
//                System.out.println(comment.getCreatedAt());
//            }

            //Get Candidates
//            UserResponse user = getCandidate(3);
//            System.out.println(user.getUserId());
//            System.out.println(user.getUsername());

            //Get Statistics
//            StatisticsResponse sr = getStatistics(1);
//            System.out.println(sr.getAssigneeDistribution());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
