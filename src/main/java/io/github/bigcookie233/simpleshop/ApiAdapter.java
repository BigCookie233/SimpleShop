package io.github.bigcookie233.simpleshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ApiAdapter {
    private final RestTemplate restTemplate;
    private final ApiProperties apiProperties;

    @Autowired
    public ApiAdapter(RestTemplate restTemplate, ApiProperties apiProperties) {
        this.restTemplate = restTemplate;
        this.apiProperties = apiProperties;
    }

    public static String buildSortedQueryString(Map<String, String> params) {
        // 筛选参数并排序
        Map<String, String> sortedParams = new TreeMap<>(params);

        // 拼接参数
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return queryString.toString();
    }

    public static String getSign(Map<String, String> params) {
        String preSign = buildSortedQueryString(params);
        return calculateMD5(preSign);
    }

    public static String calculateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return "";
        }
    }

    public String getPayLink(String paymentId, String paymentMethod, String name, double amount) {
        Map<String, String> params = new TreeMap<>();
        params.put("pid", apiProperties.pid);
        params.put("type", paymentMethod);
        params.put("out_trade_no", paymentId);
        params.put("notify_url", apiProperties.hostUrl);
        params.put("return_url", apiProperties.hostUrl);
        params.put("name", name);
        params.put("money", String.valueOf(amount));
        params.put("sign", getSign(params));
        params.put("sign_type", "MD5");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "application/json");
        return getEncodedUrl(params);
    }

    private String getEncodedUrl(Map<String, String> params) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            multiValueMap.put(entry.getKey(), Collections.singletonList(entry.getValue()));
        }
        // Build the URL with encoded query parameters
        String url = UriComponentsBuilder.fromHttpUrl(apiProperties.apiUrl)
                .queryParams(multiValueMap)
                .encode() // Encode special characters
                .toUriString();
        return url;
    }
}
