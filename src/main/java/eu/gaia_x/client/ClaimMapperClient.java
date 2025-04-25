package eu.gaia_x.client;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.gaia_x.entity.RolesDto;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClaimMapperClient {
    private final String GETROLESURI = "/roles";
    private final String GETROLESCONTEXTURI = "/roles?context={X}";
    private HttpClient client = HttpClient.newHttpClient();

    public RolesDto getRoles(String baseUri, String jwt, String context)
      throws URISyntaxException, IOException, InterruptedException {
        String uri;
        if (context == null) {
            uri = baseUri + GETROLESURI;
        } else {
            uri = baseUri + GETROLESCONTEXTURI.replace("{X}", context);
        }
        var request = HttpRequest.newBuilder()
          .uri(new URI(uri))
          .GET()
          .setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
          .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response.body(), RolesDto.class);
    }

    protected void setClient(HttpClient client) {
        this.client = client;
    }
}
