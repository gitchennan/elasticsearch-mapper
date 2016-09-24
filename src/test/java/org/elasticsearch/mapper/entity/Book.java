package org.elasticsearch.mapper.entity;

import org.elasticsearch.mapper.annotations.*;

@MappingSetting(timestamp = true)
@Document(indexName = "my_store", type = "book")
public class Book {
    @Id
    private Long id;
    @Field(type = FieldType.Double)
    private Double price;
    @Field(type = FieldType.Byte)
    private Byte pageCount;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = "ik_max_word",
            searchAnalyzer = "ik",
            nullValue = "chennan",
            similarity = Similarity.BM25,
            termVector = TermVector.with_positions,
            ignoreAbove = 5,
            fielddata = Fielddata.disabled

    )
    private String author;

    @Field(type = FieldType.Binary)
    private byte[] pdf;

//    @MultiField(
//            mainField = @Field(index = FieldIndex.analyzed, analyzer = "ik_max_word", searchAnalyzer = "ik_smart"),
//            otherFields = {
//                    @NestedField(dotSuffix = "pinyin", index = FieldIndex.analyzed, indexAnalyzer = "lc_index", searchAnalyzer = "lc_search")
//            }
//    )
    private String name;

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public Byte getPageCount() {
        return pageCount;
    }

    public void setPageCount(Byte pageCount) {
        this.pageCount = pageCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
