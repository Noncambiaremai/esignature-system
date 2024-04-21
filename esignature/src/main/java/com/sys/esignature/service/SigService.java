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

    public void uploadSigs(String sig_name, String sig_path, long timeStamp, String userId) {
        sigRepository.uploadSigs(sig_name, sig_path, timeStamp, userId);
    }

    public List<Signature> selectAllByUserId(String userId) {
        return sigRepository.selectAllByUserId(userId);
    }

    public boolean deleteSigBySigId(Integer sig_id) {
        return sigRepository.deleteSigBySigId(sig_id);
    }
}
