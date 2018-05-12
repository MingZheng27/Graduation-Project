package com.zm.search.service;

import com.google.common.collect.Lists;
import com.zm.search.bean.HotKey;
import com.zm.search.bean.QAEntity;
import com.zm.search.dao.DaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    DaoImpl dao;

    public List<HotKey> loadHot() {
        List<HotKey> resultList = Lists.newArrayList();
        for (Map.Entry entry : DaoImpl.cacheBuilder.asMap().entrySet()) {
            if ((int)entry.getValue() > 0) {
                resultList.add(new HotKey((String) entry.getKey(), (Integer) entry.getValue()));
            } else {
                DaoImpl.cacheBuilder.asMap().remove(entry.getKey());
            }
        }
        resultList.sort(new Comparator<HotKey>() {
            @Override
            public int compare(HotKey o1, HotKey o2) {
                return o2.getHotLevel() - o1.getHotLevel();
            }
        });
        return resultList;
    }

    public List<String> loadTitle(String keyWords) {
        return dao.queryTitle(keyWords);
    }

    public List<QAEntity> loadDetailResult(String keyWords, int from, int to) {
        return dao.detailQuery(keyWords, from, to);
    }

    public boolean loadDataFromDBAndSave2ES() throws IOException {
        List<QAEntity> list = dao.loadFromDatabase();
        return dao.save2ES(list);
    }

}
