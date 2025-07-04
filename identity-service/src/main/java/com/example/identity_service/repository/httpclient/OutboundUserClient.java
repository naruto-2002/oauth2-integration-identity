package com.example.identity_service.repository.httpclient;

import com.example.identity_service.dto.request.ExchangeTokenRequest;
import com.example.identity_service.dto.response.ExchangeTokenResponse;
import com.example.identity_service.dto.response.OutboundUserResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "outbound-user-client", url = "https://www.googleapis.com")
public interface OutboundUserClient {
    @GetMapping(value = "/oauth2/v1/userinfo")
    OutboundUserResponse geetUserInfo(@RequestParam("alt") String alt,
                                      @RequestParam("access_token") String accessToken);
}
