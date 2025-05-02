package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.Feature;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureService {
    @Autowired
    FeatureRepository featureRepository;


    public List<Feature> getAllFeature() {

        return featureRepository.findAll();
    }
}
