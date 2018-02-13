package com.xboost.mapper.jieli;


import com.xboost.pojo.jieli.Temp;

import java.util.List;
import java.util.Map;

public interface TempMapper {
    // 根据查询条件获取模型整体参数  @param param @return
    void saveTemp(Temp tmep);

    void saveRouteOpt(Map map);

    void saveOD_demand(Map map);

    void savedistance_ref(Map map);

    void savetemp_list(Map map);


    void delAllOD_demandByScenariosId(String OpenScenariosId);

    void delAlldistance_refByScenariosId(String OpenScenariosId);

    void delAlltemp_listByScenariosId(String OpenScenariosId);

    List<Map> findthree_points_route(String scenariosId);

    List<Map> findfour_points_route(String scenariosId);

    List<Map> findAllTwoPointsRoute(String scenariosId);
    //findAll
    List<Map> findAll01(String scenariosId);

    //findRouteTempBycode1
    List<Map> findRouteTempBycode1(String route_temp_sql);

    //findAll
    List<Map> findAll02(String scenariosId);
    //findAll
    List<Map> findAll03(String scenariosId);
    //findAll
    List<Map> findAll04(String scenariosId);
    //findAll
    List<Map> findAll05(String scenariosId);
    //findAll
    List<Map> findAll06(String scenariosId);
    //findAll
    List<Map> findAll07(String scenariosId);
    //findAll
    List<Map> findAll08(String scenariosId);

    //findAllFlowLim
    List<Map> findAllFlowLim(String scenariosId);


}
