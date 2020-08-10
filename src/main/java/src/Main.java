package src;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Main {
    public static void main(String[] args) throws Exception {
        RestClient restClient = new RestClient("https://jsonplaceholder.typicode.com");
//        savePostsToFiles(restClient.getAllPosts());
        saveCommentsToFiles(restClient);
    }

    public static void saveCommentsToFiles(RestClient restClient) throws Exception {
        Stream<Post> posts = Arrays.stream(restClient.getAllPosts());

        Stream<Comment> allComments = posts.parallel().flatMap(post -> {
                    try {
                        return Arrays.stream(restClient.getCommentsByPost(post.getId()));
                    } catch (Exception e) {
                        return Stream.empty();
                    }
                });

        var groupedComments = allComments.collect(groupingBy(comment -> getDomain(comment.getEmail())));

        Gson gson = new GsonBuilder().create();
        groupedComments.forEach((domain, comments) -> {
            try (Writer writer = new FileWriter(domain + ".json")) {
                    gson.toJson(comments, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static String getDomain(String email) {
        return email.substring(email.indexOf('@') + 1);
    }

    public static void savePostsToFiles(Post[] posts) throws IOException {
        Gson gson = new GsonBuilder().create();
        for(Post post : posts) {
            try (Writer writer = new FileWriter(post.getId() + ".json")) {
                gson.toJson(post, writer);
            }
        }
    }
}
