package com.elastic.es.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.elastic.entity.PackageESSearchMetadata;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class PackageMetadataConfig {
    // package 查询时用于构建 聚合 条件时使用
    private static List<PackageESSearchMetadata> packageESSearchMetadataList;

    static {
        packageESSearchMetadataList = CollectionUtil.newArrayList();

        // 相关领域
        Map<String, String> metadata1ValueDescMap = CollectionUtil.newHashMap();
        metadata1ValueDescMap.put("Agricultural", "农业");
        metadata1ValueDescMap.put("Medical", "医学");
        metadata1ValueDescMap.put("Industrial", "医疗");
        metadata1ValueDescMap.put("Environmental", "环境");
        metadata1ValueDescMap.put("Evolution", "进化");
        metadata1ValueDescMap.put("Model Organism", "模式生物");
        metadata1ValueDescMap.put("Unknown", "未知");
        PackageESSearchMetadata metadata1 = PackageESSearchMetadata.builder()
                .bucketName("project_relevant_area")
                .alias("相关领域")
                .description("项目隶属哪个领域")
                .searchKeyed("tagJson.project_relevant_area")
                .searchKeyedArray(new String[]{"Agricultural", "Medical", "Industrial", "Environmental", "Evolution", "Model Organism", "Unknown"})
                .valueDescMap(metadata1ValueDescMap)
                .projectDataType("基因")
                .build();
        packageESSearchMetadataList.add(metadata1);

        // 项目类别
        Map<String, String> metadata2ValueDescMap = CollectionUtil.newHashMap();
        metadata2ValueDescMap.put("Whole Genome sequencing", "全部基因组测序项目");
        metadata2ValueDescMap.put("Clone ends", "克隆末端测序项目");
        metadata2ValueDescMap.put("Epigenomics DNA", "甲基化，组蛋白修饰，染色质数据集项目");
        metadata2ValueDescMap.put("Exome", "转录组测序项目");
        metadata2ValueDescMap.put("Map", "无测序数据的图位数据项目，图位克隆测序项目，辐射杂种细胞图项目");
        metadata2ValueDescMap.put("Random Survey", "从收集到的样品的随机取样的测序项目，即不打算对材料进行全面取样");
        metadata2ValueDescMap.put("Targeted Locus (Loci)", "特定位点的测序项目，如 16S rRNA 测序");
        metadata2ValueDescMap.put("Targeted loci cultured", "来自培养样本的靶向位点测序项目");
        metadata2ValueDescMap.put("Targeted loci environmental", "来自环境样本（未培养）的靶向位点测序项目");
        metadata2ValueDescMap.put("Transcriptome or Gene Expression", "大规模 RNA 测序或表达分析。包括基因的 EST，RNA-seq，和芯片");
        metadata2ValueDescMap.put("Variation", "项目的主要目标是识别人群中的序列变异");
        metadata2ValueDescMap.put("Genome sequencing and assembly", "全部基因组测序和组装项目");
        metadata2ValueDescMap.put("Raw sequence reads", "原始组学数据项目");
        metadata2ValueDescMap.put("Genome sequencing", "基因组测序项目");
        metadata2ValueDescMap.put("Assembly", "基因组组装项目");
        metadata2ValueDescMap.put("Metagenomic assembly", "由环境样品测序产生的基因组组装测序项目");
        metadata2ValueDescMap.put("Proteome", "大规模蛋白质组学项目，包括质谱分析");
        metadata2ValueDescMap.put("Other", "其他数据类型，需要用户做文字描述");
        PackageESSearchMetadata metadata2 = PackageESSearchMetadata.builder()
                .bucketName("project_type")
                .alias("项目类别")
                .description("项目的类别")
                .searchKeyed("tagJson.project_type")
                .searchKeyedArray(new String[]{"Whole Genome sequencing", "Clone ends", "Epigenomics DNA", "Exome", "Map",
                        "Random Survey", "Targeted Locus (Loci)", "Targeted loci cultured", "Targeted loci environmental",
                        "Transcriptome or Gene Expression", "Variation", "Genome sequencing and assembly",
                        "Raw sequence reads", "Genome sequencing", "Assembly", "Metagenomic assembly", "Proteome", "Other"})
                .valueDescMap(metadata2ValueDescMap)
                .projectDataType("基因")
                .build();
        packageESSearchMetadataList.add(metadata2);

        // 样本类型
        Map<String, String> metadata3ValueDescMap = CollectionUtil.newHashMap();
        metadata3ValueDescMap.put("Clinical or host-associated pathogen", "临床或宿主相关病原体");
        metadata3ValueDescMap.put("Environmental, food or other pathogen", "环境、食物或其他病原体");
        metadata3ValueDescMap.put("Microbe", "微生物");
        metadata3ValueDescMap.put("Model organism or animal sample", "模型生物或动物样本");
        metadata3ValueDescMap.put("Human", "人类");
        metadata3ValueDescMap.put("Plant", "植物");
        metadata3ValueDescMap.put("Virus", "病毒");
        PackageESSearchMetadata metadata3 = PackageESSearchMetadata.builder()
                .bucketName("project_sample_type")
                .alias("样本类型")
                .description("样本的类型")
                .searchKeyed("tagJson.project_sample_type")
                .searchKeyedArray(new String[]{"Clinical or host-associated pathogen", "Environmental, food or other pathogen", "Microbe",
                        "Model organism or animal sample", "Human", "Plant", "Virus"})
                .valueDescMap(metadata3ValueDescMap)
                .projectDataType("基因")
                .build();
        packageESSearchMetadataList.add(metadata3);

        // 技术平台
        Map<String, String> metadata4ValueDescMap = CollectionUtil.newHashMap();
        metadata4ValueDescMap.put("Illumina Hiseq 2000", "");
        metadata4ValueDescMap.put("Ion torrent PGM", "");
        PackageESSearchMetadata metadata4 = PackageESSearchMetadata.builder()
                .bucketName("project_tech_platform")
                .alias("技术平台")
                .description("样本数据生成的技术平台")
                .searchKeyed("tagJson.project_tech_platform")
                .searchKeyedArray(new String[]{"Illumina Hiseq 2000", "Ion torrent PGM"})
                .valueDescMap(metadata4ValueDescMap)
                .projectDataType("基因")
                .build();
        packageESSearchMetadataList.add(metadata4);

        // 参考基因组
        Map<String, String> metadata5ValueDescMap = CollectionUtil.newHashMap();
        metadata5ValueDescMap.put("hg19", "");
        metadata5ValueDescMap.put("GRCh38", "");
        PackageESSearchMetadata metadata5 = PackageESSearchMetadata.builder()
                .bucketName("project_genome")
                .alias("参考基因组")
                .description("研究所用的参考基因组版本号")
                .searchKeyed("tagJson.project_genome")
                .searchKeyedArray(new String[]{"hg19", "GRCh38"})
                .valueDescMap(metadata5ValueDescMap)
                .projectDataType("基因")
                .build();
        packageESSearchMetadataList.add(metadata5);

        // 疾病类型
        Map<String, String> metadata6ValueDescMap = CollectionUtil.newHashMap();
        metadata6ValueDescMap.put("肺结节", "");
        metadata6ValueDescMap.put("脑卒中", "");
        PackageESSearchMetadata metadata6 = PackageESSearchMetadata.builder()
                .bucketName("project_disease_type")
                .alias("疾病类型")
                .description("疾病名称")
                .searchKeyed("tagJson.project_disease_type")
                .searchKeyedArray(new String[]{"肺结节", "脑卒中"})
                .valueDescMap(metadata6ValueDescMap)
                .projectDataType("影像")
                .build();
        packageESSearchMetadataList.add(metadata6);

        // 标注方式
        Map<String, String> metadata7ValueDescMap = CollectionUtil.newHashMap();
        metadata7ValueDescMap.put(".nii", "");
        metadata7ValueDescMap.put(".vcf", "");
        metadata7ValueDescMap.put(".csv", "");
        PackageESSearchMetadata metadata7 = PackageESSearchMetadata.builder()
                .bucketName("project_label_format")
                .alias("标注方式")
                .description("标注文件的格式信息 如 .nii .xml .csv等")
                .searchKeyed("tagJson.project_label_format")
                .searchKeyedArray(new String[]{".nii", ".vcf", ".csv"})
                .valueDescMap(metadata7ValueDescMap)
                .projectDataType("影像")
                .build();
        packageESSearchMetadataList.add(metadata7);

        // 数据格式
        Map<String, String> metadata8ValueDescMap = CollectionUtil.newHashMap();
        metadata8ValueDescMap.put(".dicom", "");
        metadata8ValueDescMap.put(".jpg", "");
        metadata8ValueDescMap.put(".png", "");
        PackageESSearchMetadata metadata8 = PackageESSearchMetadata.builder()
                .bucketName("project_data_format")
                .alias("数据格式")
                .description("数据文件的格式信息 如 .dicom .jpg .png")
                .searchKeyed("tagJson.project_data_format")
                .searchKeyedArray(new String[]{".dicom", ".jpg", ".png"})
                .valueDescMap(metadata8ValueDescMap)
                .projectDataType("影像")
                .build();
        packageESSearchMetadataList.add(metadata8);

        // 检测手段
        Map<String, String> metadata9ValueDescMap = CollectionUtil.newHashMap();
        metadata9ValueDescMap.put("CT", "");
        metadata9ValueDescMap.put("MRI", "");
        metadata9ValueDescMap.put("眼底镜", "");
        PackageESSearchMetadata metadata9 = PackageESSearchMetadata.builder()
                .bucketName("project_testing_method")
                .alias("检测手段")
                .description("影像文件的检测方式 如 CT MRI 眼底镜 等")
                .searchKeyed("tagJson.project_testing_method")
                .searchKeyedArray(new String[]{"CT", "MRI", "眼底镜"})
                .valueDescMap(metadata9ValueDescMap)
                .projectDataType("影像")
                .build();
        packageESSearchMetadataList.add(metadata9);
    }

    /**
     * 数据包聚合查询的时ES桶名与中文映射关系
     *
     * @return
     */
    /**
     {
         "project_type": {
         "bucketName": "project_type",
         "searchKeyedArray": ["Whole Genome sequencing", "Clone ends", "Epigenomics DNA", "Exome", "Map", "Random Survey", "Targeted Locus (Loci)", "Targeted loci cultured", "Targeted loci environmental", "Transcriptome or Gene Expression", "Variation", "Genome sequencing and assembly", "Raw sequence reads", "Genome sequencing", "Assembly", "Metagenomic assembly", "Proteome", "Other"],
         "alias": "项目类别",
         "description": "项目的类别",
         "projectDataType": "基因",
         "searchKeyed": "tagJson.project_type",
         "valueDescMap": {
                 "Whole Genome sequencing": "全部基因组测序项目",
                 "Genome sequencing": "基因组测序项目",
                 "Targeted Locus (Loci)": "特定位点的测序项目，如 16S rRNA 测序",
                 "Raw sequence reads": "原始组学数据项目",
                 "Clone ends": "克隆末端测序项目",
                 "Targeted loci cultured": "来自培养样本的靶向位点测序项目",
                 "Proteome": "大规模蛋白质组学项目，包括质谱分析",
                 "Random Survey": "从收集到的样品的随机取样的测序项目，即不打算对材料进行全面取样",
                 "Exome": "转录组测序项目",
                 "Transcriptome or Gene Expression": "大规模 RNA 测序或表达分析。包括基因的 EST，RNA-seq，和芯片",
                 "Genome sequencing and assembly": "全部基因组测序和组装项目",
                 "Epigenomics DNA": "甲基化，组蛋白修饰，染色质数据集项目",
                 "Targeted loci environmental": "来自环境样本（未培养）的靶向位点测序项目",
                 "Variation": "项目的主要目标是识别人群中的序列变异",
                 "Map": "无测序数据的图位数据项目，图位克隆测序项目，辐射杂种细胞图项目",
                 "Metagenomic assembly": "由环境样品测序产生的基因组组装测序项目",
                 "Assembly": "基因组组装项目",
                 "Other": "其他数据类型，需要用户做文字描述"
            }
         }
        ...
     }
     **/
    @Bean
    public Map<String, PackageESSearchMetadata> packageBucketNameMapping() {
        Map<String, PackageESSearchMetadata> resultMap = CollectionUtil.newHashMap();
        packageESSearchMetadataList.forEach(metadata -> resultMap.put(metadata.getBucketName(), metadata));
        return resultMap;

    }

    /**
     * 数据包搜索聚合集合
     *
     * @return
     */
/**   {
        "project_testing_method": {
            "filters": {
                "filters": {
                    "CT": {
                        "match": {
                            "tagJson.project_testing_method": {
                                "query": "CT",
                                "operator": "AND",
                                "prefix_length": 0,
                                "max_expansions": 50,
                                "fuzzy_transpositions": true,
                                "lenient": false,
                                "zero_terms_query": "NONE",
                                "auto_generate_synonyms_phrase_query": true,
                                "boost": 1.0
                            }
                        }
                    },
                    "MRI": {
                        "match": {
                            "tagJson.project_testing_method": {
                                "query": "MRI",
                                "operator": "AND",
                                "prefix_length": 0,
                                "max_expansions": 50,
                                "fuzzy_transpositions": true,
                                "lenient": false,
                                "zero_terms_query": "NONE",
                                "auto_generate_synonyms_phrase_query": true,
                                "boost": 1.0
                            }
                        }
                    },
                    "眼底镜": {
                        "match": {
                            "tagJson.project_testing_method": {
                                "query": "眼底镜",
                                "operator": "AND",
                                "prefix_length": 0,
                                "max_expansions": 50,
                                "fuzzy_transpositions": true,
                                "lenient": false,
                                "zero_terms_query": "NONE",
                                "auto_generate_synonyms_phrase_query": true,
                                "boost": 1.0
                            }
                        }
                    }
                },
                "other_bucket": true,
                "other_bucket_key": "_other_"
            }
        }
        ...
    }
 */
    @Bean
    public FiltersAggregationBuilder[] packageSearchFilterAggArray() {
        List<FiltersAggregationBuilder> resultList = CollectionUtil.newArrayList();
        packageESSearchMetadataList.forEach(metadata -> resultList.add(createFiltersAggregationBuilder(metadata)));
        return resultList.toArray(new FiltersAggregationBuilder[resultList.size()]);
    }

    /**
     * 创建 FiltersAggregationBuilder 对象
     *
     * @param metadata 元数据
     * @return FiltersAggregationBuilder 实例
     */
    private static FiltersAggregationBuilder createFiltersAggregationBuilder(PackageESSearchMetadata metadata) {
        List<FiltersAggregator.KeyedFilter> keyedFilterList = CollectionUtil.newArrayList();
        for (String key : metadata.getSearchKeyedArray()) {
            keyedFilterList.add(new FiltersAggregator.KeyedFilter(key,
                    QueryBuilders.matchQuery(metadata.getSearchKeyed(), key).operator(Operator.AND)));
        }
        FiltersAggregationBuilder builder =
                new FiltersAggregationBuilder(metadata.getBucketName(),
                        keyedFilterList.toArray(new FiltersAggregator.KeyedFilter[keyedFilterList.size()]));
        // 开启other统计
        builder.otherBucket(Boolean.TRUE);
        return builder;
    }

}
