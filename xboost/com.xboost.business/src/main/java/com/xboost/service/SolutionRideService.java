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

    public List<Map> findByRide1(String openScenariosId,String rideId) {
        return rideMapper.findByRide1(openScenariosId,rideId);
    }

    public List<Map> findByRide2(String openScenariosId,String rideId) {
        return rideMapper.findByRide2(openScenariosId,rideId);
    }

    public List<Map> findAllRidesRelay(String openScenariosId) {
        return rideMapper.findAllRidesRelay(openScenariosId);
    }

    public List<Map> findAllRidesSeries(String openScenariosId) {
        return rideMapper.findAllRidesSeries(openScenariosId);
    }

    public List<Map> findByRideRelay(String openScenariosId,String rideId) {
        return rideMapper.findByRideRelay(openScenariosId,rideId);
    }

    public List<Map> findByRideSeries(String openScenariosId,String rideId) {
        return rideMapper.findByRideSeries(openScenariosId,rideId);
    }

    public Integer maxRideId(String openScenariosId) {
        return rideMapper.maxRideId(openScenariosId);
    }

    public Integer maxRouteId(String openScenariosId) {
        return rideMapper.maxRouteId(openScenariosId);
    }

    public Integer maxSequence(String openScenariosId,String rideId) {
        return rideMapper.maxSequence(openScenariosId,rideId);
    }

    public void delByScenariosId(String openScenariosId) {
        rideMapper.delByScenariosId(openScenariosId);
    }
}
