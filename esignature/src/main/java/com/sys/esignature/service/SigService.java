package com.sys.esignature.service;

import com.sys.esignature.entity.Signature;
import com.sys.esignature.repository.SigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SigService {
    @Autowired
    private SigRepository sigRepository;

    public void uploadSigs(String sig_name, String sig_path, long timeStamp) {
        sigRepository.uploadSigs(sig_name, sig_path, timeStamp);
    }

    public List<Signature> selectAllByUserId() {
        return sigRepository.selectAllByUserId();
    }
}
