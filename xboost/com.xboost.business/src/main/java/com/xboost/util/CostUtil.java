package com.xboost.util;

/**
 * 计费工具类
 */
public class CostUtil {
    /**
     * 车辆计费规则
     * @param dist
     * @param time
     * @param cartype
     * @return
     */
    public double vehiclesCost(double dist,double time, String cartype){
        double total = 0;
        if("baidu".equalsIgnoreCase(cartype)||"百度".equalsIgnoreCase(cartype)){
            total = 11;
        }else if("didi".equalsIgnoreCase(cartype)||"滴滴".equalsIgnoreCase(cartype)){
            if(dist<=15){
                total = 8+2*dist+0.55*time;
            }else{
                total = 8+2*dist+0.55*time+(dist-15)*1.5;
            }
        }else if("truck".equalsIgnoreCase(cartype)||"货车".equalsIgnoreCase(cartype)){
            if(dist<=5){
                total = 30;
            }else{
                total = 30+(dist-5)*5;
            }
        }else{
            System.err.println("no such car");
        }
        return total;

    }
}
