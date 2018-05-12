package com.zm.search.dao;

import com.zm.search.bean.QAEntity;

import java.io.IOException;
import java.util.List;

public interface Dao {

    List<QAEntity> detailQuery(String keyWords, int from, int to);

    List<String> queryTitle(String keyWords);

    List<QAEntity> loadFromDatabase();

    boolean save2ES(List<QAEntity> list) throws IOException;

}
