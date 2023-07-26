package br.com.getnet.voucher_pool_manager.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeAll;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

/**
 * Base class for RestControllerReactiveTest classes.
 *
 * @author Thiago Oliveira</a>
 */
public class ControllerTestReactiveBase {

    protected static ObjectMapper objectMapper;
    protected static EasyRandom easyRandom;
    @Autowired
    protected WebTestClient webTestClient;
    @MockBean
    protected ModelMapper modelMapper;

    @BeforeAll
    protected static void beforeTests() {

        easyRandom = new EasyRandom();
        objectMapper = new ObjectMapper();
    }


    /**
     * Performs a request and returns the parsed response.
     *
     * @param method            the HTTP method
     * @param requestBody       the request body content
     * @param expectedHttpStatus the expected HttpStatus
     * @param responseClass     the expected response class
     * @param url               the endpoint url
     * @param urlArgs           the uri path variables
     * @return the parsed response
     */
    protected <RQ, RP> RP doRequest(HttpMethod method, RQ requestBody, HttpStatus expectedHttpStatus, Class<RP> responseClass, String url, Object... urlArgs) {
        return webTestClient.method(method)
                .uri(url, urlArgs)
                .headers(defaultHeaders())
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isEqualTo(expectedHttpStatus)
                .returnResult(responseClass)
                .getResponseBody()
                .blockFirst();
    }

    /**
     * Performs a POST and returns the parsed response.
     *
     * @param requestBody       the request body content
     * @param expectedHttpStatus the expected HttpStatus
     * @param responseClass     the expected response class
     * @param url               the endpoint url
     * @param urlArgs           the uri path variables
     * @return the parsed response
     */
    protected <RQ, RP> RP doPostWithResponse(
            RQ requestBody, HttpStatus expectedHttpStatus, Class<RP> responseClass, String url, Object... urlArgs) {
        return doRequest(HttpMethod.POST, requestBody, expectedHttpStatus, responseClass, url, urlArgs);
    }

    /**
     * Performs a GET and returns the parsed response.
     *
     * @param expectedHttpStatus the expected HttpStatus
     * @param responseClass     the expected response class
     * @param url               the endpoint url
     * @param urlArgs           the uri path variables
     * @return the parsed response
     */
    protected <RP> RP doGetWithResponse(
            HttpStatus expectedHttpStatus, Class<RP> responseClass, String url, Object... urlArgs) {
        return doRequest(HttpMethod.GET, null, expectedHttpStatus, responseClass, url, urlArgs);
    }

    /**
     * Performs a PUT and returns the parsed response.
     *
     * @param requestBody       the request body content
     * @param expectedHttpStatus the expected HttpStatus
     * @param responseClass     the expected response class
     * @param url               the endpoint url
     * @param urlArgs           the uri path variables
     * @return the parsed response
     */
    protected <RQ, RP> RP doPutWithResponse(
            RQ requestBody, HttpStatus expectedHttpStatus, Class<RP> responseClass, String url, Object... urlArgs) {
        return doRequest(HttpMethod.PUT, requestBody, expectedHttpStatus, responseClass, url, urlArgs);
    }

    /**
     * Returns a consumer that can add default headers to a request.
     *
     * @return a consumer that adds default headers to a request
     */
    public static Consumer<HttpHeaders> defaultHeaders() {
        return httpHeaders -> {
            httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        };
    }
}
