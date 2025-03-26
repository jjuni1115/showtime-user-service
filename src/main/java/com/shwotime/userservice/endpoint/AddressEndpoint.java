package com.shwotime.userservice.endpoint;

import com.shwotime.userservice.common.ApiResponse;
import com.shwotime.userservice.dto.AddressDto;
import com.shwotime.userservice.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressEndpoint {

    private final HttpServletRequest httpServletRequest;

    private final AddressService addressService;

    @GetMapping("/{keyword}")
    public ResponseEntity<ApiResponse<List<AddressDto>>> searchAddress(@PathVariable(value = "keyword") String keyword){


        List<AddressDto> res = addressService.searchAddress(keyword);

        return ResponseEntity.ok(ApiResponse.ok(res,httpServletRequest.getRequestURI()));

    }



}
