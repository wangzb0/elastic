package com.elastic.es.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elastic.entity.ESPackage;
import com.elastic.entity.ESPackageAggDetailDTO;
import com.elastic.entity.ESPackageAggResultDTO;
import com.elastic.entity.ESPackageQueryDTO;
import com.elastic.entity.Package;
import com.elastic.entity.PackageDto;
import com.elastic.entity.PackageESSearchMetadata;
import com.elastic.entity.PackageStatusEnum;
import com.elastic.entity.ParamException;
import com.elastic.es.config.ESConfig;
import com.elastic.es.repository.ESPackageRepository;
import com.elastic.mapper.PackageMapper;
import com.elastic.util.EnumUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilters;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author wzb
 * @description TODO
 * @ClassName ESPackageService
 * @create: 2019-11-03 14:53
 */
@Service
@Slf4j
public class ESPackageService {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ESPackageRepository esPackageRepository;
    @Autowired
    private ESConfig esConfig;
    @Resource(name = "packageBucketNameMapping")
    private Map<String, PackageESSearchMetadata> packageBucketNameMapping;
    @Resource(name = "packageSearchFilterAggArray")
    private FiltersAggregationBuilder[] packageSearchFilterAggArray;
    @Autowired(required = false)
    private PackageMapper packageMapper;

    /**
     * 同步新操作的数据到ES
     *
     * @param cursorTimestamp 时间游标
     */
    public void syncMySqlToESByNewData(Long cursorTimestamp) {
        log.info("enter syncMySqlToESByNewData method; cursorTimestamp={}", cursorTimestamp);
        List<Package> packageList = packageMapper.getByNeedSyncData(cursorTimestamp);
        packageList.forEach(p -> esPackageRepository.save(ESPackage.getInstance(p)));
    }

    /**
     * ES 查询数据包
     *
     * @param pageSize 每页数量
     * @param pageNum  开始页
     * @param status   状态
     * @param userId   用户ID
     * @param serverId 服务ID
     * @param type     类型
     * @param disease  病种
     * @return
     */
    public Page<PackageDto> doGetPackagePage(Long pageSize, Long pageNum,
                                             Integer status, Long userId,
                                             Long serverId, Integer type,
                                             String disease) {
        log.info("enter doGetPackagePage method; pageSize={}， pageNum={}, status={}, userId={}, serverId={}, type={}, disease={}",
                pageSize, pageNum, status, userId, serverId, type, disease);
        // 构建 bool 查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 构建 group1Builder
        BoolQueryBuilder group1Builder = QueryBuilders.boolQuery();
        if (!ObjectUtils.isEmpty(serverId)) {
            group1Builder.must(QueryBuilders.matchPhraseQuery("serverId", serverId));
        }
        if (!ObjectUtils.isEmpty(status)) {
            group1Builder.must(QueryBuilders.matchPhraseQuery("status", status));
        }
        if (!ObjectUtils.isEmpty(userId)) {
            group1Builder.must(QueryBuilders.matchPhraseQuery("userId", userId));
            List<Long> packageIdList = CollectionUtil.newArrayList(1L, 2L, 3L, 4L);
            if (!CollectionUtils.isEmpty(packageIdList)) {
                // 构建 group2Builder
                TermsQueryBuilder group2Builder = QueryBuilders.termsQuery("id", packageIdList);
                // group2Builder 加入 queryBuilder
                queryBuilder.should(group2Builder);
            }
        }
        // TODO 这些 key 都写死了需要优化
        if (!ObjectUtils.isEmpty(type)) {
            group1Builder.must(QueryBuilders.matchPhraseQuery("tagJson.project_data_type", type));
        }
        if (!StringUtils.isEmpty(disease)) {
            group1Builder.must(QueryBuilders.matchPhraseQuery("tagJson.project_disease_type", disease));
        }
        // group1Builder 加入 queryBuilder
        queryBuilder.should(group1Builder);

        // 设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        Long from = (pageNum - 1) * pageSize;
        searchSourceBuilder.from(from.intValue());
        searchSourceBuilder.size(pageSize.intValue());
        searchSourceBuilder.sort("id", SortOrder.DESC);

        // 创建查询请求
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        searchRequest.indices("package_" + esConfig.getSuffix());
        log.info("searchSourceBuilder={}", searchSourceBuilder.toString());

        try {
            // 执行查询请求
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Page<PackageDto> page = wrapperPage(searchResponse, pageNum.intValue(), pageSize.intValue());
            log.debug("page={}", JSONUtil.parseObj(page));
            return page;
        } catch (Exception e) {
            log.error("doGetPackagePage error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 从ES查询数据
     *
     * @param pageSize    每页大小
     * @param pageNum     开始页
     * @param queryString 查询字符串
     * @return
     */
    public ESPackageQueryDTO doGetPackagePageByES(Long pageSize, Long pageNum, String queryString) {
        log.info("enter doGetPackagePageByES method: pageSize={}, pageNum={}, queryString={}", pageSize, pageNum, queryString);
        // 构建查询
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 构建 searchSource
        if (!StringUtils.isEmpty(queryString)) {
            searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
        } else {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        }
        for (FiltersAggregationBuilder filtersAggregationBuilder : packageSearchFilterAggArray) {
            searchSourceBuilder.aggregation(filtersAggregationBuilder);
        }

        Long from = (pageNum - 1) * pageSize;
        searchSourceBuilder.from(from.intValue());
        searchSourceBuilder.size(pageSize.intValue());
        searchSourceBuilder.sort("id", SortOrder.DESC);
        log.info("searchSourceBuilder={}", searchSourceBuilder.toString());

        // 构建 search request
        SearchRequest request = new SearchRequest();
        request.source(searchSourceBuilder);
        // TODO 替换索引名称
        request.indices("package_" + esConfig.getSuffix());
        // 发送查询请求
        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("request es error", e);
            throw new RuntimeException(e);
        } catch (ElasticsearchStatusException e) {
            log.error("request es error", e);
            if (e.status() == RestStatus.BAD_REQUEST) {
                throw new ParamException("检索公式错误，请检查后再次搜索", e);
            }
            throw new ParamException("ElasticSearch 查询错误", e);
        }

        // 封装查询结果
        Page<PackageDto> page = wrapperPage(response, pageNum.intValue(), pageSize.intValue());
        List<ESPackageAggResultDTO> aggList = wrapperAgg(response);

        ESPackageQueryDTO queryDTO = ESPackageQueryDTO.builder()
                .page(page)
                .aggList(aggList)
                .build();

        return queryDTO;

    }

    public Page<PackageDto> wrapperPage(SearchResponse searchResponse, Integer pageNum, Integer pageSize) {
        Page<PackageDto> page = new Page<>();
        List<PackageDto> records = CollectionUtil.newArrayList();

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            ESPackage esPackage = JSONUtil.toBean(hit.getSourceAsString(), ESPackage.class);
            PackageDto dto = new PackageDto();
            BeanUtils.copyProperties(esPackage, dto);
            if (!StringUtils.isEmpty(esPackage.getCreated())) {
                dto.setCreated(LocalDateTime.parse(esPackage.getCreated(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            dto.setStatusStr(EnumUtil.enumToMap(PackageStatusEnum.class).get(esPackage.getStatus()).toString());
            dto.setUserName("登录名称");
            dto.setUserId(1L);
            dto.setServerName("节点名称");
            records.add(dto);
        }
        long totalCount = searchResponse.getHits().getTotalHits();
        page.setTotal(totalCount);
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        page.setRecords(records);
        page.setPages((totalCount + pageSize - 1) / pageSize);
        return page;
    }

    public List<ESPackageAggResultDTO> wrapperAgg(SearchResponse searchResponse) {
        List<ESPackageAggResultDTO> aggList = CollectionUtil.newArrayList();
        if (!ObjectUtils.isEmpty(searchResponse) && !ObjectUtils.isEmpty(searchResponse.getAggregations())) {
            Map<String, Aggregation> originAggMap = searchResponse.getAggregations().getAsMap();
            for (String bucketName : originAggMap.keySet()) {
                if (originAggMap.get(bucketName) instanceof ParsedFilters) {
                    List<ParsedFilters.ParsedBucket> bucketList =
                            (List<ParsedFilters.ParsedBucket>) ((ParsedFilters) originAggMap.get(bucketName)).getBuckets();
                    List<ESPackageAggDetailDTO> detailDTOList = CollectionUtil.newArrayList();
                    ESPackageAggDetailDTO otherDetailDTO = null;
                    for (ParsedFilters.ParsedBucket parsedBucket : bucketList) {
                        ESPackageAggDetailDTO detailDTO = ESPackageAggDetailDTO.builder()
                                .subexpression(packageBucketNameMapping.get(bucketName).getSearchKeyed())
                                .value(parsedBucket.getKey())
                                .docCount(parsedBucket.getDocCount())
                                .valueDesc(packageBucketNameMapping.get(bucketName).getValueDescMap().get(parsedBucket.getKey()))
                                .build();
                        if (!parsedBucket.getKey().equals("_other_")) {
                            detailDTOList.add(detailDTO);
                        } else {
                            otherDetailDTO = detailDTO;
                        }
                    }
                    // 正常的数据根据 docCount 倒叙
                    detailDTOList.sort(Comparator.comparing(ESPackageAggDetailDTO::getDocCount).reversed());
                    // 将其他存入最后
                    detailDTOList.add(otherDetailDTO);
                    ESPackageAggResultDTO resultDTO = ESPackageAggResultDTO.builder()
                            .bucketName(bucketName)
                            .alias(packageBucketNameMapping.get(bucketName).getAlias())
                            .desc(packageBucketNameMapping.get(bucketName).getDescription())
                            .projectDataType(packageBucketNameMapping.get(bucketName).getProjectDataType())
                            .data(detailDTOList)
                            .build();
                    aggList.add(resultDTO);
                }
            }
        }
        return aggList;
    }
}
