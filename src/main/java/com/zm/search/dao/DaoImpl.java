package com.zm.search.dao;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.mysql.jdbc.PreparedStatement;
import com.zm.search.bean.QAEntity;
import com.zm.search.common.JDBCUtil;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Component
public class DaoImpl implements Dao {

    //K:keyWord V:HotLevel
    public static LoadingCache<String, Integer> cacheBuilder = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String s) throws Exception {
                    return null;
                }
            });

    static {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<String, Integer> entry : cacheBuilder.asMap().entrySet()) {
                    cacheBuilder.put(entry.getKey(),entry.getValue() - 1);
                }
            }
        },0,10 * 1000); //execute every 10s todo:config to properties file
    }

    @Autowired
    TransportClient client;

    @Override
    public List<QAEntity> detailQuery(String[] keyWords, int from, int to) {
        List<QAEntity> resultList = Lists.newArrayList();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (String s : keyWords) {
            if (!StringUtils.isEmpty(s)) {
                boolQuery.should(QueryBuilders.matchQuery("question_name", s));
                boolQuery.should(QueryBuilders.matchQuery("content", s));
                boolQuery.should(QueryBuilders.matchQuery("username", s));
            }
        }
        SearchRequestBuilder builder = client.prepareSearch("questionandanswer")
                .setTypes("qa")
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(from)
                .setSize(to - from);
        SearchResponse response = builder.get();
        for (SearchHit hit : response.getHits()) {
            Map<String,Object> map = hit.getSource();
            QAEntity entity = new QAEntity((int)map.get("id"),(String)map.get("child_topid_id"),(String)map.get("question_id"),
                    (String)map.get("question_name"),(String)map.get("answer_id"),(String)map.get("username"),(String)map.get("content")
            ,(boolean)map.get("is_article"));
            resultList.add(entity);
        }
        return resultList;
    }

    @Override
    public List<String> queryTitle(String keyWords) {
        List<String> resultList = Lists.newArrayList();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String[] keys = keyWords.split(" ");
        for (String s : keys) {
            if (!StringUtils.isEmpty(s)) {
                boolQuery.should(QueryBuilders.matchQuery("question_name", s));
            }
        }
        SearchRequestBuilder builder = client.prepareSearch("questionandanswer")
                .setTypes("qa")
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(7);
        SearchResponse response = builder.get();
        for (SearchHit hit : response.getHits()) {
            resultList.add((String) hit.getSource().get("question_name"));
        }
        return resultList;
    }

    @Override
    public List<QAEntity> loadFromDatabase() {
        List<QAEntity> resultList = Lists.newArrayList();
        Connection conn = JDBCUtil.getConn();
        String sql = "select * from question_and_answer";
        try {
            PreparedStatement state = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = state.executeQuery();
            while (rs.next()){
                QAEntity qa = new QAEntity(rs.getInt("id"),String.valueOf(rs.getInt("child_topic_id")),
                        rs.getString("question_id"),rs.getString("question_name"),
                        rs.getString("answer_id"),rs.getString("username"),
                        rs.getString("content"),
                        rs.getBoolean("is_article"));
                resultList.add(qa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public boolean save2ES(List<QAEntity> list) throws IOException {
        for (QAEntity entity : list) {
            XContentBuilder content = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("child_topic_id", entity.getChildTopicId())
                    .field("is_article", entity.isArtical())
                    .field("question_name", entity.getQuestionName())
                    .field("id", entity.getId())
                    .field("answer_id", entity.getAnswerId())
                    .field("question_id", entity.getQuestionId())
                    .field("content", entity.getExcerpt())
                    .field("username", entity.getUserName())
                    .endObject();
            IndexResponse result = client.prepareIndex("questionandanswer", "qa")
                    .setSource(content)
                    .get();
            if (null == result.getId()) {
                return false;
            }
        }
        return true;
    }

}
