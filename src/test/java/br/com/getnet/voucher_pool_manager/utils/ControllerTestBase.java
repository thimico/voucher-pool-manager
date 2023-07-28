package br.com.getnet.voucher_pool_manager.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeAll;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Base class for RestControllerTest classes.
 *
 * @author Thiago Oliveira</a>
 */
public class ControllerTestBase {

    protected static ObjectMapper objectMapper;
    protected static EasyRandom easyRandom;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ModelMapper modelMapper;


    @BeforeAll
    protected static void beforeTests() {

        easyRandom = new EasyRandom();
        objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }




    /**
     * Executes the specified request, tests the response status without parsing the response.
     *
     * @param requestBuilder the request builder
     * @param expectedStatus the expected response status
     * @return the response action
     * @throws Exception
     */
    protected ResultActions doRequest(RequestBuilder requestBuilder, HttpStatus expectedStatus)
            throws Exception {
        return mockMvc
                .perform(requestBuilder)
                .andExpect(status().is(expectedStatus.value()));
    }

    /**
     * Executes the specified request, tests the response status and returns the parsed response.
     *
     * @param requestBuilder the request builder
     * @param expectedStatus the expected response status
     * @param responseType   the expected returned type reference
     * @param <T>            the response type
     * @return the actual response
     * @throws Exception
     */
    protected <T> T doRequest(RequestBuilder requestBuilder, HttpStatus expectedStatus, TypeReference<T> responseType)
            throws Exception {
        byte[] bytes = doRequest(requestBuilder, expectedStatus)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(bytes, responseType);
    }

    /**
     * Performs a GET and returns the ResultActions.
     *
     * @param url            the endpoint url
     * @param expectedStatus the expected HttpStatus
     * @param uriVars        the uri path variables
     * @return the ResultActions
     * @throws Exception if something goes wrong
     */
    protected ResultActions doGet(String url, HttpStatus expectedStatus, Object... uriVars) throws Exception {
        try {
            return doRequest(get(url, uriVars)
                            .header("Authorization", "Bearer " + "token")
                            .contentType(MediaType.APPLICATION_JSON),
                    expectedStatus);
        } catch (Exception e) {
            // If any exception occurs, fail the test with a specific message.
            // This will help you identify the cause of the failure.
            String message = "Exception occurred during doGet: " + e.getMessage();
            fail(message);
            // Return null to satisfy the method signature. The code will never reach here if the test fails.
            return null;
        }
    }


    /**
     * Performs a GET and returns the parsed response object.
     *
     * @param url            the endpoint url
     * @param expectedStatus the expected status
     * @param responseType   the expected response type
     * @param <T>            the class of the expected response
     * @param uriVars        the uri path variables
     * @return the parsed response
     * @throws Exception if something goes wrong
     */
    protected <T> T doGet(String url, HttpStatus expectedStatus, TypeReference<T> responseType, Object... uriVars) throws Exception {
        byte[] bytes = doGet(url, expectedStatus, uriVars)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(bytes, responseType);
    }

    /**
     * Performs a POST and returns ResultActions.
     *
     * @param url            the endpoint url
     * @param body           the request body content
     * @param expectedStatus the expected HttpStatus
     * @param uriVars        the uri path variables
     * @return the ResultActions
     * @throws Exception if something goes wrong
     */
    protected ResultActions doPost(String url, Object body, HttpStatus expectedStatus, Object... uriVars) throws Exception {
        return doRequest(
                post(url, uriVars)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(body)),
                expectedStatus);
    }

    protected <T> T doPostWithResponse(String url, Object body, HttpStatus expectedStatus, Class<T> responseClass, Object... uriVars) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(url, uriVars)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().is(expectedStatus.value()))
                .andReturn();

        T response = modelMapper.map(mvcResult.getResponse().getContentAsString(), responseClass);

        return response;
    }



    /**
     * Performs a POST and returns ResultActions.
     *
     * @param url            the endpoint url
     * @param body           the request body content
     * @param expectedStatus the expected HttpStatus
     * @param uriVars        the uri path variables
     * @return the ResultActions
     * @throws Exception if something goes wrong
     */
    protected <T> T doPost(String url, Object body, HttpStatus expectedStatus, TypeReference<T> responseType,
                           Object... uriVars) throws Exception {
        byte[] bytes = doPost(url, body, expectedStatus, uriVars)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(bytes, responseType);
    }

    protected <T> T doPost(String url, Object body, HttpStatus expectedStatus, Class<T> responseClass, Object... uriVars) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(url, uriVars)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().is(expectedStatus.value()))
                .andReturn();

        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), responseClass);
    }



    public static Consumer<HttpHeaders> defaultHeaders() {
        return httpHeaders -> {
            httpHeaders.add(ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        };
    }


    /**
     * Performs a POST and returns the parsed response.
     *
     * @param url            the endpoint url
     * @param body           the request body content
     * @param expectedStatus the expected HttpStatus
     * @param uriVars        the uri path variables
     * @return the parsed response
     * @throws Exception if something goes wrong
     */
    protected ResultActions doPut(String url, Object body, HttpStatus expectedStatus, Object... uriVars) throws Exception {
        return doRequest(put(url, uriVars)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(body)),
                expectedStatus);
    }

    /**
     * Performs a POST and returns the parsed response.
     *
     * @param url            the endpoint url
     * @param body           the request body content
     * @param expectedStatus the expected HttpStatus
     * @param responseType   the expected response type
     * @param <T>            the class of the expected response
     * @param uriVars        the uri path variables
     * @return the parsed response
     * @throws Exception if something goes wrong
     */
    protected <T> T doPut(String url, Object body, HttpStatus expectedStatus, TypeReference<T> responseType,
                          Object... uriVars) throws Exception {
        byte[] bytes = doPut(url, body, expectedStatus, uriVars)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(bytes, responseType);
    }
}
