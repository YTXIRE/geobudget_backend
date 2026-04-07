package com.geobudget.geobudget.service;

import com.geobudget.geobudget.validator.GeoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoIpServiceTest {
    @Mock
    private RestTemplate restTemplate;

    private GeoIpService service;

    @BeforeEach
    void setUp() {
        service = new GeoIpService(restTemplate, new GeoValidator());
    }

    @Test
    void getExternalIp_usesForwardedHeaderWhenItIsPublic() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "91.198.174.192");
        request.setRemoteAddr("127.0.0.1");

        String ip = service.getExternalIp(request);

        assertEquals("91.198.174.192", ip);
        verify(restTemplate, never()).getForObject(eq("https://api4.ipify.org?format=json"), eq(String.class));
    }

    @Test
    void getExternalIp_usesPublicIpServiceWhenRequestIsLocalhost() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        when(restTemplate.getForObject(eq("https://api4.ipify.org?format=json"), eq(String.class)))
                .thenReturn("{\"ip\":\"91.198.174.192\"}");

        String ip = service.getExternalIp(request);

        assertEquals("91.198.174.192", ip);
    }

    @Test
    void getExternalIp_fallsBackToRemoteAddrWhenPublicIpServiceFails() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        when(restTemplate.getForObject(eq("https://api4.ipify.org?format=json"), eq(String.class)))
                .thenThrow(new RuntimeException("ipify unavailable"));

        String ip = service.getExternalIp(request);

        assertEquals("127.0.0.1", ip);
    }
}
