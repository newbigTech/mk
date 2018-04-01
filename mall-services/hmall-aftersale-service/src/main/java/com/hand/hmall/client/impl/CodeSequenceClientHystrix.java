package com.hand.hmall.client.impl;

import com.hand.hmall.client.CodeSequenceClient;
import org.springframework.web.bind.annotation.RequestParam;

public class CodeSequenceClientHystrix implements CodeSequenceClient {
    @Override
    public String nextCode(@RequestParam("type") String type) {
        return "null";
    }

    @Override
    public String nextServiceCode(String type) {
        return null;
    }
}
