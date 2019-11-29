package com.elastic.es.repository;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.elastic.entity.ESPackage;
import com.elastic.es.config.ESConfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ESPackageRepository implements ESRepository<ESPackage, Long> {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ESConfig esConfig;
    public static final String index = "package_";
    public static final String type = index + "type";

    @Override
    public ESPackage save(ESPackage esPackage) {
        log.info("enter save method; esPackage={}", JSONUtil.parse(esPackage));

        IndexRequest request = new IndexRequest(index + esConfig.getSuffix(), type, esPackage.getId() + "");
        request.source(JSONUtil.parseObj(esPackage), XContentType.JSON);
        IndexResponse indexResponse = null;
        try {
            indexResponse = client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("save error", e);
            throw new RuntimeException(e);
        }
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED
                || indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            return esPackage;
        }
        return null;
    }

    @Override
    public ESPackage findById(Long id) {
        log.info("enter findById method; id={}", id);

        GetRequest getRequest = new GetRequest(index + esConfig.getSuffix(), type, id + "");
        GetResponse getResponse = null;
        try {
            getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("findById error", e);
            throw new RuntimeException(e);
        }
        if (getResponse.isExists()) {
            return JSONUtil.toBean(getResponse.getSourceAsString(), ESPackage.class);
        }

        return null;
    }

    @Override
    public Long deleteById(Long id) {
        log.info("enter deleteById method; id={}", id);
        DeleteRequest deleteRequest = new DeleteRequest(index + esConfig.getSuffix(), type, id + "");
        try {
            client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("deleteById error", e);
            throw new RuntimeException(e);
        }
        return id;
    }
}
