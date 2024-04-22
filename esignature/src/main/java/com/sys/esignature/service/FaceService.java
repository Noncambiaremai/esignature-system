package com.sys.esignature.service;

import com.sys.esignature.repository.FaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaceService {
    @Autowired
    private FaceRepository faceRepository;
}
