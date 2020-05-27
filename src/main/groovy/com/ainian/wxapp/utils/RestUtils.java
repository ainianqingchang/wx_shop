package com.ainian.wxapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RestUtils {

    private static class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
        final static Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            traceRequest(request, body);
            ClientHttpResponse response = execution.execute(request, body);
            traceResponse(response);
            return response;
        }

        private void traceRequest(HttpRequest request, byte[] body) throws IOException {
            logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>request begin>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            logger.debug("|URI         : {}", request.getURI());
            logger.debug("|Method      : {}", request.getMethod());
            logger.debug("|Headers     : {}", request.getHeaders() );
            logger.debug("|Request body: {}", new String(body, "UTF-8"));
            logger.debug("|-------------------------request end-----------------------------------");
        }

        private void traceResponse(ClientHttpResponse response) throws IOException {
            StringBuilder inputStringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
            String line = bufferedReader.readLine();
            while (line != null) {
                inputStringBuilder.append(line);
                inputStringBuilder.append('\n');
                line = bufferedReader.readLine();
            }
            logger.debug("|-------------------------response begin--------------------------------");
            logger.debug("|Status code  : {}", response.getStatusCode());
            logger.debug("|Status text  : {}", response.getStatusText());
            logger.debug("|Headers      : {}", response.getHeaders());
            logger.debug("|Response body: {}", inputStringBuilder.toString());
            logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<response end<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }


    public static RestTemplate restTemplate=new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

    static{
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
    }


    public static <T> T get(String url, Class<T> responseType, Object... uriVariables) throws RestClientException{
        return restTemplate.getForObject(url,responseType,uriVariables);
    }

    public static <T> T post(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException{
        return restTemplate.postForObject(url,request,responseType,uriVariables);
    }

    public static <T> T put(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException{

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<String> formEntity = new HttpEntity<String>(request.toString(), headers);

        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.PUT, formEntity, responseType,uriVariables);
        return result.getBody();
    }

    public static <T> T delete(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException{

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<String> formEntity = new HttpEntity<String>(request.toString(), headers);

        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.DELETE, formEntity, responseType,uriVariables);
        return result.getBody();
    }



}
