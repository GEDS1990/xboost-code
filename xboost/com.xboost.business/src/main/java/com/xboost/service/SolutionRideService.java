package com.xboost.service;

import com.xboost.mapper.RideMapper;
import com.xboost.pojo.Ride;
import com.xboost.pojo.Route;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@Transactional
public class SolutionRideService {
    @Inject
    public RideMapper rideMapper;
    public List<Map> findAllRidesRelay(String openScenariosId) {
        return rideMapper.findAllRidesRelay(openScenariosId);
    }

    public List<Map> findAllRidesSeries(String openScenariosId) {
        return rideMapper.findAllRidesSeries(openScenariosId);
    }

    public void delByScenariosId(String openScenariosId) {
        rideMapper.delByScenariosId(openScenariosId);
    }
}
