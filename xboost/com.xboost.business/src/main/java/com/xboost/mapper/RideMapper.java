package com.xboost.mapper;

import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.TimeWindow;
import com.xboost.pojo.CarLicence;
import com.xboost.pojo.Ride;
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


}

