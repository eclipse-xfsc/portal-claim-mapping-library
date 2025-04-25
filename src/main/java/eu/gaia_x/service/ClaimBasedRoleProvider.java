package eu.gaia_x.service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import eu.gaia_x.client.ClaimMapperClient;
import eu.gaia_x.entity.GaiaxRole;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClaimBasedRoleProvider {
    private final String serviceMapperUri;
    private final String serviceContext;
    private ClaimMapperClient client;

    private final LoadingCache<String, List<String>> cache;

    public ClaimBasedRoleProvider(String serviceMapperBaseUri) {
        this(serviceMapperBaseUri, null);
    }

    public ClaimBasedRoleProvider(String serviceMapperBaseUri, String context) {
        client = new ClaimMapperClient();
        serviceMapperUri = serviceMapperBaseUri;
        serviceContext = context;
        CacheLoader<String, List<String>> loader;
        loader = new CacheLoader<>() {
            @Override
            public List<String> load(String key) throws URISyntaxException, IOException, InterruptedException {
                var rolesDto = client.getRoles(serviceMapperUri, key, serviceContext);
                return rolesDto.getRoles();
            }
        };
        cache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build(loader);
    }

    public boolean hasGaiaxRole(String jwt, List<GaiaxRole> roles) {
        var result = false;
        var serverRoles = cache.getUnchecked(jwt);
        var stringRoles = new ArrayList<String>();
        for (GaiaxRole role : roles) {
            stringRoles.add(role.toString());
        }
        for (String serverRole : serverRoles) {
            for (String requestRole : stringRoles) {
                if (serverRole.equals(requestRole)) {
                    return true;
                }
            }
        }
        return result;
    }

    protected void setClient(ClaimMapperClient client) {
        this.client = client;
    }
}
