package com.shwotime.userservice.endpoint;

import com.shwotime.userservice.common.ApiResponse;
import com.shwotime.userservice.dto.AddressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressEndpoint {


    @GetMapping("/{keyword}")
    public ResponseEntity<ApiResponse<List<AddressDto>>> searchAddress(@PathVariable(value = "keyword") String keyword){


        return null;

    }



}
