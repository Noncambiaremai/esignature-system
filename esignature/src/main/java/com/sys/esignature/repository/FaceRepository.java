package com.sys.esignature.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class FaceRepository {

    @PersistenceContext
    private EntityManager entityManager;
}
