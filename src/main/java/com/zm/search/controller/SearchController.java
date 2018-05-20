package com.zm.search.controller;

import com.google.common.collect.Lists;
import com.mysql.jdbc.StringUtils;
import com.zm.search.bean.DetailSearchRequest;
import com.zm.search.bean.DetailSearchResponse;
import com.zm.search.bean.HotKey;
import com.zm.search.bean.QAEntity;
import com.zm.search.common.CommonFunc;
import com.zm.search.service.SearchService;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    SearchService service;

    @RequestMapping(value = "/loadHot",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<HotKey>> loadHot() {
        List<HotKey> resultList = service.loadHot();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @RequestMapping(value = "/loadTitle",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<String>> loadTitle(HttpServletRequest request) {
        try {
            DetailSearchRequest req = CommonFunc.convertStream2Object(request.getInputStream());
            if (StringUtils.isNullOrEmpty(req.getKeyWords())) {
                return new ResponseEntity<>(Lists.newArrayList(), HttpStatus.OK);
            }
            if (null != req) {
                List<String> resultList = service.loadTitle(req.getKeyWords());
                return new ResponseEntity<>(resultList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Lists.newArrayList(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //log
            return new ResponseEntity<>(Lists.newArrayList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/loadDetail",method = RequestMethod.POST)
    @ResponseBody
    public DetailSearchResponse loadDetail(HttpServletRequest request) {
        try {
            DetailSearchRequest req = CommonFunc.convertStream2Object(request.getInputStream());
            if (null != req) {
                List<QAEntity> resultList = service.loadDetailResult(req.getKeyWords(), req.getFrom(), req.getTo());
                return new DetailSearchResponse(200, resultList, null);
            } else {
                return new DetailSearchResponse(500, Lists.newArrayList(), new IOException());
            }
        } catch (IOException e) {
            e.printStackTrace();
            //log
            return new DetailSearchResponse(500, Lists.newArrayList(), e);
        }
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public ResponseEntity getFromDBAndSave2ES() {
        try {
            if (service.loadDataFromDBAndSave2ES()) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/index.html")
    public String forwardIndex(Model model) {
        return "index";
    }

    @RequestMapping(value = "/detail.html")
    public String forwardDetail(HttpServletRequest req, Model model) {
        model.addAttribute("keyWords", req.getParameter("keyWords"));
        return "detail";
    }

}
