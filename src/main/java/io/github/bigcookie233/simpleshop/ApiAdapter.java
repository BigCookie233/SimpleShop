package io.github.bigcookie233.simpleshop;

import io.github.bigcookie233.simpleshop.entities.Action;
import io.github.bigcookie233.simpleshop.services.ConfigurablePropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ApiAdapter {
    private final ConfigurablePropertyService configurablePropertyService;

    @Autowired
    public ApiAdapter(ConfigurablePropertyService configurablePropertyService) {
        this.configurablePropertyService = configurablePropertyService;
    }

    public static String buildSortedQueryString(Map<String, String> params) {
        Map<String, String> sortedParams = new TreeMap<>(params);

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        return builder.build().toUriString().substring(1);
    }

    public static String getSign(Map<String, String> params, String key) {
        String preSign = buildSortedQueryString(params);
        return calculateMD5(preSign+key);
    }

    public static String calculateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));

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
        params.put("pid", configurablePropertyService.findPropertyByName("pid").value);
        params.put("type", paymentMethod);
        params.put("out_trade_no", paymentId);
        params.put("notify_url", configurablePropertyService.findPropertyByName("host_url").value+"/update-transaction");
        params.put("return_url", configurablePropertyService.findPropertyByName("host_url").value+"/complete-transaction");
        params.put("name", name);
        params.put("money", String.valueOf(amount));
        params.put("sign", getSign(params, configurablePropertyService.findPropertyByName("key").value));
        params.put("sign_type", "MD5");
        return getEncodedUrl(params);
    }

    private String getEncodedUrl(Map<String, String> params) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            multiValueMap.put(entry.getKey(), Collections.singletonList(entry.getValue()));
        }
        // Build the URL with encoded query parameters
        return UriComponentsBuilder.fromHttpUrl(configurablePropertyService.findPropertyByName("api_url").value)
                .queryParams(multiValueMap)
                .encode() // Encode special characters
                .toUriString();
    }

    public void executeCommand(Action action) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(configurablePropertyService.findPropertyByName("mcsm_url").value)
                .queryParam("remote_uuid", action.getRemoteUuid())
                .queryParam("uuid", action.getUuid())
                .queryParam("apikey", configurablePropertyService.findPropertyByName("api_key").value)
                .queryParam("command", action.getCommand())
                .encode()
                .toUriString().replace("%20", "+");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
    }
}
