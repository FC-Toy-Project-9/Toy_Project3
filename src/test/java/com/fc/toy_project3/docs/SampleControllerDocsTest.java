package com.fc.toy_project3.docs;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;

import com.fc.toy_project3.docs.SampleController.SampleRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;

public class SampleControllerDocsTest extends RestDocsSupport {

    @Override
    public Object initController() {
        return new SampleController();
    }

    private final ConstraintDescriptions sampleDescriptions = new ConstraintDescriptions(
        SampleRequestDTO.class);

    @Test
    void common() throws Exception {
        // when
        SampleRequestDTO request = new SampleRequestDTO("testName");

        // given, then
        this.mockMvc.perform(post("/docs").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andDo(restDoc.document(
            requestFields(fieldWithPath("name").description("이름").attributes(
                key("constraints").value(sampleDescriptions.descriptionsForProperty("name")))),
            responseFields(this.responseCommon()).and(
                fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                    .description("응답 데이터"))));
    }
}
