package com.xboost.mapper;

import com.xboost.pojo.Activity;
import com.xboost.pojo.ModelArg;
import com.xboost.pojo.Route;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/5 0005.
 */
public interface SolutionMapper {

    //  添加Route信息  @param route
    void addRoute(Route route);

    //  添加Route信息  @param route
    void addActivity(Activity activity);



}

