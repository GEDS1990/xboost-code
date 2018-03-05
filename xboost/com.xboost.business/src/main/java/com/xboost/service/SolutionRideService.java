package com.xboost.service;

import com.xboost.mapper.RideMapper;
import com.xboost.pojo.Ride;
import com.xboost.pojo.Route;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@Transactional
public class SolutionRideService {
    @Inject
    public RideMapper rideMapper;
    public List<Ride> findAllRides(String openScenariosId) {
        return rideMapper.findAllRides(openScenariosId);
    }

    public void delByScenariosId(String openScenariosId) {
        rideMapper.delByScenariosId(openScenariosId);
    }
}
