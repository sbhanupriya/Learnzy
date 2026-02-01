package com.learnzy.backend.entity;


import com.learnzy.backend.enums.ContentType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "learningsearchindex")
@Builder
@Data
public class LearningSearchDocument {
    @Id
    public String id;
    @Field(type= FieldType.Search_As_You_Type)
    private String fieldId;
    @Field(type= FieldType.Search_As_You_Type)
    private String title;
    @Field(type= FieldType.Search_As_You_Type)
    private String content;
    @Field(type = FieldType.Keyword)
    private ContentType contentType;
    @Field(type = FieldType.Text)
    private String path;
}
