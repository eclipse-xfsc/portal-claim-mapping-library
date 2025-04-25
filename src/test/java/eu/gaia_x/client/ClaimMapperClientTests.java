package eu.gaia_x.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.gaia_x.entity.GaiaxRole;
import eu.gaia_x.entity.RolesDto;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Optional;
import javax.net.ssl.SSLSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClaimMapperClientTests {

    private final String BASEURI = "http://iambaseuri.com";
    private final String JWT =
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    private final String CONTEXT = "RANDOMCONTEXT";
    ClaimMapperClient classUnderTest = new ClaimMapperClient();

    @Mock
    HttpClient client;

    @Test
    public void getRoles() throws URISyntaxException, IOException, InterruptedException {
        classUnderTest.setClient(client);
        when(client.send(any(), any())).thenReturn(createTestResponse());
        var result = classUnderTest.getRoles(BASEURI, JWT, CONTEXT);
        assertEquals(result.getRoles().get(0), GaiaxRole.TESTROLE.toString());
    }

    private RolesDto createTestRoleDTO() {
        var dto = new RolesDto();
        var stringRoles = new ArrayList<String>();
        stringRoles.add(GaiaxRole.TESTROLE.toString());
        dto.setRoles(stringRoles);
        return dto;
    }

    private HttpResponse<Object> createTestResponse() {
        return new HttpResponse<>() {
            @Override
            public int statusCode() {
                return 200;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<Object>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                Gson gson = new GsonBuilder().create();
                return gson.toJson(createTestRoleDTO());
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };
    }
}
