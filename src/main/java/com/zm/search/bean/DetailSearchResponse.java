package com.zm.search.bean;

import java.util.List;

public class DetailSearchResponse {

    private int resultCode;
    private List<QAEntity> resultList;
    private Exception ex;

    public DetailSearchResponse() {
    }

    public DetailSearchResponse(int resultCode, List<QAEntity> resultList, Exception ex) {
        this.resultCode = resultCode;
        this.resultList = resultList;
        this.ex = ex;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<QAEntity> getResultList() {
        return resultList;
    }

    public void setResultList(List<QAEntity> resultList) {
        this.resultList = resultList;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }
}
