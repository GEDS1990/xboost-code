package com.xboost.mapper;

import com.xboost.pojo.Scenarios;
import com.xboost.pojo.ScenariosCategory;
import com.xboost.util.ShiroUtil;
import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface MyScenariosMapper {

    //  添加场景信息  @param scenario
    void save(Scenarios scenario);


     // 查询所有场景信息  @return
    List<Scenarios> findAll(Integer userId);

    Long findAllCount(Integer userId);

    Long findAllScenariosCount();

    // 根据参数查询场景信息  @param param @return
    List<Scenarios> findByParam(Map<String, Object> param);
    Long findCountByParam(Map<String, Object> param);

    /**
     * 根据ID查询场景信息
     * @param id
     * @return
     */
    Scenarios findById(Integer id);

    void saveScenariosIduserId(@Param("scenariosId") String scenariosId,@Param("userId")Integer userId);

    // 根据id编辑场景信息  @param scenario
    void edit(Scenarios scenario);


    //根据id删除场景信息   * @param id
    void delById(@Param("id") Integer id,@Param("userId") String userId);

    /**
     *send场景信息to user
     * @param scenariosId
     * @param userIdTemp
     */
    void sendToUserByScenariosId(@Param("scenariosId") String scenariosId,@Param("userIdTemp")int userIdTemp);

    /**
     *
     * @param openScenariosId
     * @return
     */
    void updateOpenTime(@Param("openScenariosId") String openScenariosId,@Param("openTime") String openTime);



    //  添加场景类别 @param category
    void addCategory(ScenariosCategory category);

    // 根据用户id查询场景类别  @return
    List<ScenariosCategory> findCategory(Integer userId);

    void updateFinishTime(@Param("finishTime") String finishTime, @Param("openScenariosId") String scenariosId);

    void updateStatus(@Param("status") String status,@Param("openScenariosId") String scenariosId);

}

