package com.xboost.mapper;

import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.TimeWindow;
import com.xboost.pojo.CarLicence;
import com.xboost.pojo.Ride;
import com.xboost.pojo.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface RideMapper {

    //  添加运力信息  @param transport
    void save(Ride ride);

    //  update运力信息  @param transport
    void update(Map<String, Object> param);

    void delByScenariosId(String scenariosId);


    List<Map> findByRide1(@Param("openScenariosId") String openScenariosId,@Param("rideId")String rideId);

    List<Map> findByRide2(@Param("openScenariosId") String openScenariosId,@Param("rideId")String rideId);


    List<Map> findAllRidesRelay(String openScenariosId);

    List<Map> findAllRidesSeries(String openScenariosId);

    List<Map> findByRideRelay(@Param("openScenariosId") String openScenariosId,@Param("rideId")String rideId);

    List<Map> findByRideSeries(@Param("openScenariosId") String openScenariosId,@Param("rideId")String rideId);

    Integer maxRideId(String openScenariosId);
    Integer maxRouteId(String openScenariosId);

    Integer maxSequence(@Param("openScenariosId") String openScenariosId,@Param("rideId")String rideId);


}

