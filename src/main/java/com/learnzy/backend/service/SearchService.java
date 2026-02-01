package com.learnzy.backend.service;

import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.learnzy.backend.entity.Course;
import com.learnzy.backend.entity.LearningSearchDocument;
import com.learnzy.backend.entity.Topic;
import com.learnzy.backend.enums.ContentType;
import com.learnzy.backend.models.search.Match;
import com.learnzy.backend.models.search.SearchResponse;
import com.learnzy.backend.models.search.SearchResult;
import com.learnzy.backend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


@Service
public class SearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private CourseRepository courseRepository;
    private HashMap<String, Course> courseHashMap = new HashMap<>();


    public SearchResponse search(String query){
        query = query.toLowerCase();
      return searchWithRanking(query);
    }

    public SearchResponse searchWithRanking(String keyword)  {

        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s.multiMatch(m -> m
                                        .query(keyword)
                                        .fields("title^3", "content^2", "fieldId")
                                        .type(TextQueryType.BestFields)   // supports fuzziness
                                        .fuzziness("AUTO")
                                ))
                                .should(s -> s.multiMatch(m -> m
                                        .query(keyword)
                                        .fields(
                                                "title", "title._2gram", "title._3gram",
                                                "content", "content._2gram", "content._3gram",
                                                "fieldId"
                                        )
                                        .type(TextQueryType.BoolPrefix)  // prefix/partial matching
                                ))
                        )
                ).build();


        SearchHits<LearningSearchDocument> hits =
                    elasticsearchOperations.search(query, LearningSearchDocument.class);


            HashMap<String, SearchResult> matchHashMap = new LinkedHashMap<>();

            List<LearningSearchDocument> documents = hits.getSearchHits().stream().map(hit -> hit.getContent()).collect(Collectors.toList());

            for (LearningSearchDocument document : documents) {
                if (document.getContentType().equals(ContentType.COURSE)) {
                    if (matchHashMap.get(document.getFieldId()) == null) {
                        SearchResult searchResult = SearchResult.builder()
                                .courseId(document.getFieldId())
                                .courseTitle(document.getTitle())
                                .matches(new ArrayList<>())
                                .build();
                        matchHashMap.put(document.getFieldId(), searchResult);
                    }
                } else {
                    String[] levels = document.getPath().split("\\$");
                    String courseId = levels[0];
                    String topidId = levels[1];
                    String subtopic = levels.length >= 3 ? levels[2] : "";
                    if (courseHashMap.get(courseId) == null) {
                        courseHashMap.put(courseId, courseRepository.findByCourseCodeIgnoreCase(courseId).get());
                    }
                    if (matchHashMap.get(courseId) == null) {
                        SearchResult searchResult = SearchResult.builder()
                                .courseId(courseId)
                                .courseTitle(courseHashMap.get(courseId).getTitle().toLowerCase())
                                .matches(new ArrayList<>())
                                .build();
                        matchHashMap.put(courseId, searchResult);
                    }
                    Topic topic = courseHashMap.get(courseId).getTopicList().stream().filter(currtopic -> currtopic.getTopicCode().equalsIgnoreCase(topidId)).findFirst().get();
                    matchHashMap.get(courseId).getMatches().add(
                            Match.builder()
                                    .type(document.getContentType().toString().toLowerCase())
                                    .topicTitle(topic.getTitle())
                                    .subtopicId(topic.getTopicCode())
                                    .subtopicTitle(subtopic)
                                    .build()
                    );
                }
            }

            SearchResponse searchResponse = SearchResponse.builder()
                    .query(keyword)
                    .results(new ArrayList<>())
                    .build();

            for (Map.Entry<String, SearchResult> match : matchHashMap.entrySet()) {
                searchResponse.getResults().add(match.getValue());
            }

            return searchResponse;

    }

}
