package com.budaos.modules.core.common.handler;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

/**
 * 错误处理类
 * @author huj
 * @create 2022-08-15 15:45
 * @email hujun@budaos.com
 * @company 创蓝科技 www.budaos.com
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    /**
     * Indicate whether the given response has any errors.
     * <p>Implementations will typically inspect the
     * {@link ClientHttpResponse#getStatusCode() HttpStatus} of the response.
     *
     * @param response the response to inspect
     * @return {@code true} if the response indicates an error; {@code false} otherwise
     * @throws IOException in case of I/O errors
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false;
    }

    /**
     * Handle the error in the given response.
     * <p>This method is only called when {@link #hasError(ClientHttpResponse)}
     * has returned {@code true}.
     *
     * @param response the response with the error
     * @throws IOException in case of I/O errors
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // 对请求头的处理
        List<String> customHeader = response.getHeaders().get("x-app-err-id");

        String svcErrorMessageID = "";
        if (customHeader != null) {
            svcErrorMessageID = customHeader.get(0);
        }
        // 对body 的处理 (inputStream)
        String body = convertStreamToString(response.getBody());

        try {
            errorHandler.handleError(response);
        } catch (RestClientException scx) {

            throw new CustomException(scx.getMessage(), scx, body);
        }

    }

    /**
     * Alternative to {@link #handleError(ClientHttpResponse)} with extra
     * information providing access to the request URL and HTTP method.
     *
     * @param url      the request URL
     * @param method   the HTTP method
     * @param response the response with the error
     * @throws IOException in case of I/O errors
     * @since 5.0
     */
    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {

    }

    /**
     * inputStream 装换为 string
     * @param is
     * @return
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
