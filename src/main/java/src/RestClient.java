package src;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RestClient {
    private static final String URI_GET_ALL_POSTS = "/posts";
    private String baseUrl;

    public RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Post[] getAllPosts() throws Exception {
        HttpResponse<String> response = get(baseUrl + URI_GET_ALL_POSTS);
        return new Gson().fromJson(response.body(), Post[].class);
    }

    public Comment[] getCommentsByPost(int postId) throws Exception {
        HttpResponse<String> response = get(baseUrl + "/posts/" + postId + "/comments");
        return new Gson().fromJson(response.body(), Comment[].class);
    }


    private HttpResponse<String> get(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
