package com.xboost.interceptor;

import com.xboost.util.RedisUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;


public class MethodCacheInterceptor implements MethodInterceptor {
    private Logger logger = Logger.getLogger(MethodCacheInterceptor.class);
    private RedisUtil redisUtil;
    private Long defaultCacheExpireTime = 1000*60*60*12L; // 缓存默认的过期时间 一天

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object value = null;
        long s = System.currentTimeMillis();//start time
        String targetName = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();

        Object[] arguments = invocation.getArguments();
        String key = getCacheKey(targetName, methodName, arguments);
        System.out.println(targetName + "." + methodName + "-----SQL-----"+arguments[0].toString());
        try {
            // 判断是否有缓存
            if (redisUtil.exists(key)) {
                value = redisUtil.get(key);
                System.out.println("----获取缓存---key="+key + "----耗时："+(System.currentTimeMillis()-s)+"ms");
                return value;
            }
            // 写入缓存
            value = invocation.proceed();
            if (value != null) {
                final String tkey = key;
                final Object tvalue = value;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        redisUtil.set(tkey, tvalue, defaultCacheExpireTime);
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (value == null) {
                return invocation.proceed();
            }
        }
        System.out.println("----数据库查询----耗时："+(System.currentTimeMillis()-s)+"ms");

        return value;
    }

    /**
     * 是否加入缓存
     *
     * @return
     */
//    private boolean isAddCache(String targetName, String methodName) {
//        boolean flag = true;
//        if (targetNamesList.contains(targetName)
//                || methodNamesList.contains(methodName)) {
//            flag = false;
//        }
//        return flag;
//    }

    /**
     * 创建缓存key
     *
     * @param targetName
     * @param methodName
     * @param arguments
     */
    private String getCacheKey(String targetName, String methodName,
                               Object[] arguments) {
        StringBuffer sbu = new StringBuffer();
        if(targetName.contains("JdbcTemplate") && methodName.contains("query") && arguments.length == 1 ){
//            if(arguments[0] instanceof String){
//                return MD5Utils.encryptMD5((String)arguments[0]);
//            }
        }
        sbu.append(targetName).append("_").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sbu.append("_").append(arguments[i]);
            }
        }
        return sbu.toString();
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}
