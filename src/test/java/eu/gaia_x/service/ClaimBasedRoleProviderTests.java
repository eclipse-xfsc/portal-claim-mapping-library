package eu.gaia_x.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import eu.gaia_x.client.ClaimMapperClient;
import eu.gaia_x.entity.GaiaxRole;
import eu.gaia_x.entity.RolesDto;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClaimBasedRoleProviderTests {

    private final String BASEURI = "http://iambaseuri.com";
    private final String JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    ClaimBasedRoleProvider classUnderTest = new ClaimBasedRoleProvider(BASEURI);

    @Mock
    ClaimMapperClient client;

    @Test
    public void hasGaiaxRoleNoCache() throws URISyntaxException, IOException, InterruptedException {
        classUnderTest.setClient(client);
        Mockito.when(client.getRoles(any(),any(), any())).thenReturn(createTestRoleDTO());
        var roles = new ArrayList<GaiaxRole>();
        roles.add(GaiaxRole.TESTROLE);
        var result = classUnderTest.hasGaiaxRole(JWT, roles);
        assertTrue(result);
        verify(client, times(1)).getRoles(any(),any(), any());
    }

    @Test
    public void hasGaiaxRoleWithCache() throws URISyntaxException, IOException, InterruptedException {
        classUnderTest.setClient(client);
        Mockito.when(client.getRoles(any(),any(), any())).thenReturn(createTestRoleDTO());
        var roles = new ArrayList<GaiaxRole>();
        roles.add(GaiaxRole.TESTROLE);
        var result = classUnderTest.hasGaiaxRole(JWT, roles);
        assertTrue(result);
        result = classUnderTest.hasGaiaxRole(JWT, roles);
        assertTrue(result);
        verify(client, times(1)).getRoles(any(),any(), any());
    }

    @Test
    public void doesNotHaveGaiaxRole() throws URISyntaxException, IOException, InterruptedException {
        classUnderTest.setClient(client);
        Mockito.when(client.getRoles(any(),any(), any())).thenReturn(createTestRoleDTO());
        var roles = new ArrayList<GaiaxRole>();
        roles.add(GaiaxRole.ADMIN);
        var result = classUnderTest.hasGaiaxRole(JWT, roles);
        assertFalse(result);
        verify(client, times(1)).getRoles(any(),any(), any());
    }

    private RolesDto createTestRoleDTO(){
        var dto = new RolesDto();
        var stringRoles = new ArrayList<String>();
        stringRoles.add(GaiaxRole.TESTROLE.toString());
        dto.setRoles(stringRoles);
        return dto;
    }

}
