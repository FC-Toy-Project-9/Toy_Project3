package com.fc.toy_project3.domain.itinerary.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fc.toy_project3.domain.itinerary.dto.response.ItinerarySearchResponseDTO;
import com.fc.toy_project3.domain.itinerary.service.ItineraryService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ItineraryKeywordServiceTest {

    @Autowired
    private ItineraryService itineraryService;

    @Nested
    @DisplayName("getPlaceByKeyword()는")
    class Context_getPlaceByKeyword {

        @Test
        @DisplayName("키워드로 query를 톻해 장소를 조회할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            itineraryService.init();

            // when
            List<ItinerarySearchResponseDTO> result = itineraryService.getPlaceByKeyword("카카오프렌즈");

            // then
            assertThat(result.get(0)).isNotNull();
        }
    }
}
