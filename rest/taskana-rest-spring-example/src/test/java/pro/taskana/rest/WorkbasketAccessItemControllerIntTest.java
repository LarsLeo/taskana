package pro.taskana.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pro.taskana.rest.resource.WorkbasketAccesItemExtendedResource;

import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"devMode=true"})
public class WorkbasketAccessItemControllerIntTest {

    String url = "http://127.0.0.1:";
    RestTemplate template;
    HttpEntity<String> request;
    @LocalServerPort
    int port;

    @Before
    public void before() {
        template = getRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic dGVhbWxlYWRfMTp0ZWFtbGVhZF8x");
        request = new HttpEntity<String>(headers);
    }

    @Test
    public void testGetAllWorkbasketAccessItems() {
        ResponseEntity<PagedResources<WorkbasketAccesItemExtendedResource>> response = template.exchange(
                url + port + "/v1/workbasket-access", HttpMethod.GET, request,
                new ParameterizedTypeReference<PagedResources<WorkbasketAccesItemExtendedResource>>() {
                });
        assertNotNull(response.getBody().getLink(Link.REL_SELF));
    }

    @Test
    public void testGetWorkbasketAccessItemsKeepingFilters() {
        String parameters = "/v1/workbasket-access/?sort-by=workbasket-key&order=asc&page=1&page-size=9&access-ids=user_1_1";
        ResponseEntity<PagedResources<WorkbasketAccesItemExtendedResource>> response = template.exchange(
                url + port + parameters, HttpMethod.GET, request,
                new ParameterizedTypeReference<PagedResources<WorkbasketAccesItemExtendedResource>>() {
                });
        assertNotNull(response.getBody().getLink(Link.REL_SELF));
        assertTrue(response.getBody()
                .getLink(Link.REL_SELF)
                .getHref()
                .endsWith(parameters));
    }

    @Test
    public void testThrowsExceptionIfInvalidFilterIsUsed() {
        try {
            template.exchange(
                    url + port + "/v1/workbasket-access/?sort-by=workbasket-key&order=asc&page=1&page-size=9&invalid=user_1_1", HttpMethod.GET, request,
                    new ParameterizedTypeReference<PagedResources<WorkbasketAccesItemExtendedResource>>() {
                    });
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("[invalid]"));
        }
    }

    @Test
    public void testGetSecondPageSortedByWorkbasketKey() {
        String parameters = "/v1/workbasket-access/?sort-by=workbasket-key&order=asc&page=2&page-size=9&access-ids=user_1_1";
        ResponseEntity<PagedResources<WorkbasketAccesItemExtendedResource>> response = template.exchange(
                url + port + parameters, HttpMethod.GET, request,
                new ParameterizedTypeReference<PagedResources<WorkbasketAccesItemExtendedResource>>() {
                });
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("user_1_1", response.getBody().getContent().iterator().next().accessId);
        assertNotNull(response.getBody().getLink(Link.REL_SELF));
        assertTrue(response.getBody()
                .getLink(Link.REL_SELF)
                .getHref()
                .endsWith(parameters));
        assertNotNull(response.getBody().getLink(Link.REL_FIRST));
        assertNotNull(response.getBody().getLink(Link.REL_LAST));
        assertEquals(9, response.getBody().getMetadata().getSize());
        assertEquals(1, response.getBody().getMetadata().getTotalElements());
        assertEquals(1, response.getBody().getMetadata().getTotalPages());
        assertEquals(1, response.getBody().getMetadata().getNumber());
    }

    /**
     * Return a REST template which is capable of dealing with responses in HAL format
     *
     * @return RestTemplate
     */
    private RestTemplate getRestTemplate() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
        converter.setObjectMapper(mapper);

        RestTemplate template = new RestTemplate(Collections.<HttpMessageConverter<?>>singletonList(converter));
        return template;
    }
}
