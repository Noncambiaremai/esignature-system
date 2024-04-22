package com.sys.esignature.controller;

import com.sys.esignature.service.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/face")
public class FaceController {
    @Autowired
    private FaceService faceService;
}


