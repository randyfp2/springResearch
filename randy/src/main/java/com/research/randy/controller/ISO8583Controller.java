package com.research.randy.controller;

import com.research.randy.model.ISO8583.ISO8583Request;
import com.research.randy.model.ISO8583.ISO8583Response;
import com.research.randy.service.ISO8583Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restApi/esb/ISO")
public class ISO8583Controller {
    @Autowired
    private ISO8583Service iso8583Service;

    @PostMapping("/transferIntraBank")
    public ISO8583Response convertAndSend(@RequestBody ISO8583Request request) {
        return iso8583Service.processRequest(request);
    }
}
