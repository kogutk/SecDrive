package pl.kkogut;

import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


//https://rbaskets.in/web/4igzwne?token=dg6W6a51vVlJzw38MHRh59w2R3KHtu1kf8YWhxn4OAaj
public class APIcaller {
    static String getUrl = "https://rbaskets.in/api/baskets/4igzwne";
    static String postUrl = "https://rbaskets.in/4igzwne";
    static String apiKey = "dg6W6a51vVlJzw38MHRh59w2R3KHtu1kf8YWhxn4OAaj";
    enum method {GET, POST, DELETE;}


    public static HttpResponse<String> getAllRequests() {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString("");
        String url = getUrl+"/requests";
        HttpResponse<String> response = apiCall(url,method.GET,bodyPublisher);
        return response;
    }
    public static JSONObject getJasonFromResponse(HttpResponse<String> response){
        JSONObject json = new JSONObject(response.body().toString());
        return json;
    }

    public static void post(String body) {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(body);
        HttpResponse<String> response = apiCall(postUrl,method.POST,bodyPublisher);
    }
    public static void post(JSONObject json) {
        post(json.toString());
    }
    public static void deleteAllCalls() {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString("");
        String url = getUrl+"/requests";
        HttpResponse<String> response = apiCall(url,method.DELETE,bodyPublisher);
    }

    public static HttpResponse<String> apiCall(String url, method met, HttpRequest.BodyPublisher bodyPublisher){
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .method(met.toString(), bodyPublisher)
                .header("Authorization", apiKey)
                .build();
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println(responseToString(response));
            return response;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String responseToString(HttpResponse<String> response){
        int status = response.statusCode();
        boolean success = status >=200 && status <= 299;
        String body = response.body();
        return String.format("Response for: %s -> %s (status: %s)", response.uri(), success,status);
    }
}
