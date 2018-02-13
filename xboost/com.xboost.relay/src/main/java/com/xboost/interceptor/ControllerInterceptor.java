//package com.xboost.interceptor;
//
//import com.xboost.util.RedisUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class ControllerInterceptor extends HandlerInterceptorAdapter {
//    @Autowired
//    private RedisUtil redisUtil;
//    /**
//     * 在Controller方法前进行拦截
//     */
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("ControllerInterceptor.preHandle()");
//        try {
//            // 判断是否有缓存
//            if (redisUtil.exists(key)) {
//                value = redisUtil.get(key);
//                System.out.println("----获取缓存---key="+key + "----耗时："+(System.currentTimeMillis()-s)+"ms");
//                return value;
//            }
//            // 写入缓存
//            value = invocation.proceed();
//            if (value != null) {
//                final String tkey = key;
//                final Object tvalue = value;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        redisUtil.set(tkey, tvalue, defaultCacheExpireTime);
//                    }
//                }).start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (value == null) {
//                return invocation.proceed();
//            }
//        }
//        System.out.println("----数据库查询----耗时："+(System.currentTimeMillis()-s)+"ms");
//
////        return value;
//        return true;
//    }
//
//    public void postHandle(HttpServletRequest request,
//                           HttpServletResponse response, Object handler,
//                           ModelAndView modelAndView) throws Exception {
//        System.out.println("ControllerInterceptor.postHandle()");
//    }
//    /**
//     * 在Controller方法后进行拦截
//     */
//    public void afterCompletion(HttpServletRequest request,
//                                HttpServletResponse response, Object handler, Exception ex)
//            throws Exception {
//        System.out.println("ControllerInterceptor.afterCompletion()");
//    }
//}