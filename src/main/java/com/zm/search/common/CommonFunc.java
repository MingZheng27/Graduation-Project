package com.zm.search.common;

import com.alibaba.fastjson.JSON;
import com.zm.search.bean.DetailSearchRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommonFunc {

    public static DetailSearchRequest convertStream2Object(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        BufferedReader bfr = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while (null != (line = bfr.readLine())) {
                sb.append(line);
            }
        } catch (IOException ex) {
            return null;
        }
        DetailSearchRequest req = JSON.parseObject(sb.toString(), DetailSearchRequest.class);
        return req;
    }

}
