package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.models.Feature;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feature")
public class FeatureController {
    @Autowired
    FeatureService featureService;

    @GetMapping("/getallfeature")
    public List<Feature> getAllFeature() {
        return  featureService.getAllFeature();
    }
}
