package com.datascienceacademy.backenddatascienceacademy.controller;

import com.datascienceacademy.backenddatascienceacademy.service.CvParseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cv")
public class CvParseController {

    private final CvParseService cvParseService;

    public CvParseController(CvParseService cvParseService) {
        this.cvParseService = cvParseService;
    }

    @PostMapping("/parse")
    public ResponseEntity<Map<String, String>> parse(@RequestBody CvParseRequest request) {
        Map<String, String> result = cvParseService.parse(request.getCvBase64(), request.getFileType());
        return ResponseEntity.ok(result);
    }

    public static class CvParseRequest {
        private String cvBase64;
        private String fileType;

        public String getCvBase64() { return cvBase64; }
        public void setCvBase64(String cvBase64) { this.cvBase64 = cvBase64; }
        public String getFileType() { return fileType; }
        public void setFileType(String fileType) { this.fileType = fileType; }
    }
}
