package com.fc.toy_project3.docs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fc.toy_project3.global.common.ResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * SampleController는 Spring REST Docs를 테스트 하기 위한 컨트롤러로, 실제로 작동하지 않습니다.
 */
@RestController
public class SampleController {

    @PostMapping("/docs")
    public ResponseEntity<ResponseDTO<Void>> sample(
        @RequestBody @Valid SampleRequestDTO sampleRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseDTO.res(HttpStatus.OK, sampleRequestDTO.getName() + "님, 성공!"));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SampleRequestDTO {

        @NotEmpty(message = "이름을 입력하세요.")
        @JsonProperty("name")
        private String name;
    }
}
