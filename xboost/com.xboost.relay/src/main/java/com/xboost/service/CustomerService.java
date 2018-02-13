package com.xboost.service;

import com.xboost.exception.ForbiddenException;
import com.xboost.exception.NotFoundException;
import com.xboost.mapper.CustomerMapper;
import com.xboost.mapper.UserMapper;
import com.xboost.pojo.Customer;
import com.xboost.pojo.User;
import com.xboost.util.ShiroUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 保存新客户
     * @param customer
     */
    public void saveNewCustomer(Customer customer) {
        customer.setProgress(Customer.PROGRESS_NEW);
        customer.setUserid(ShiroUtil.getCurrentUserId());
        customer.setCreatetime(DateTime.now().toString("yyyy-MM-dd HH:mm"));

        customerMapper.save(customer);

        logger.info("{}-添加了新客户-{}",ShiroUtil.getCurrentUserName(),customer.getCustname());
    }

    /**
     * 更新客户
     * @param customer
     */
    public void updateCustomer(Customer customer) {
        customerMapper.update(customer);

        logger.info("{}-更新了客户-{}",ShiroUtil.getCurrentUserName(),customer.getCustname());
    }
    /**
     * 根据DataTables的查询方式获取 客户列表
     * @param param
     * @return
     */
    public List<Customer> findUserByParam(Map<String, Object> param) {
        //如果当前登录的用户是普通员工，那么只能看到自己的客户
        //如果当前登录的用户是经理，那么可以看到所有的客户
        if(!ShiroUtil.isManager()) {
            param.put("userid",ShiroUtil.getCurrentUserId());
        }
        return customerMapper.findByParam(param);
    }

    /**
     * 获取客户的总数量
     * @return
     */
    public Integer findCustomerCount() {
        return customerMapper.count().intValue();
    }

    /**
     * 根据DataTables的查询方式获取客户总数量
     * @param param
     * @return
     */
    public Integer findUserCountByParam(Map<String, Object> param) {
        //如果当前登录的用户是普通员工，那么只能看到自己的客户
        //如果当前登录的用户是经理，那么可以看到所有的客户
        if(!ShiroUtil.isManager()) {
            param.put("userid",ShiroUtil.getCurrentUserId());
        }
        return customerMapper.countByParam(param).intValue();
    }

    /**
     * 根据客户ID查询对应的客户
     * @param id
     * @return
     */
    public Customer findCustomerById(Integer id) {
        Customer customer = customerMapper.findById(id);
        if(customer == null) {
            throw new NotFoundException();
        } else {
            if(ShiroUtil.isManager()) {
                return customer;
            } else {
                if(customer.getUserid() == null || customer.getUserid().equals(ShiroUtil.getCurrentUserId())) {
                    return customer;
                } else {
                    throw new ForbiddenException();
                }
            }
        }
    }

    /**
     * 根据主键删除客户对象
     * @param id
     */
    public void delCustomer(Integer id) {
        if(ShiroUtil.isManager()) {
            //删除跟进记录

            //删除客户对象
            customerMapper.del(id);

            logger.info("{}删除了客户{}",ShiroUtil.getCurrentUserName(),id);
        } else {
            throw new ForbiddenException();
        }
    }

    /**
     * 公开客户（将客户投放公海）
     * @param id
     */
    public void publicCustomer(Integer id) {
        Customer customer = customerMapper.findById(id);
        if(customer == null) {
            throw new NotFoundException();
        } else {
            if(customer.getUserid().equals(ShiroUtil.getCurrentUserId())) {
                //公开客户
                customer.setUserid(null);
                customerMapper.update(customer);

                logger.info("{}将客户{}进行了公开",ShiroUtil.getCurrentUserName(),customer.getCustname());
            } else {
                throw new ForbiddenException();
            }
        }

    }

    /**
     * 添加公海客户(公开)到自己名下
     * @param id
     */
    public void getCustomer(Integer id) {
        Customer customer = customerMapper.findById(id);
        if(customer == null) {
            throw new NotFoundException();
        } else {
            //if(customer.getUserid().equals(null)) {
                //获取认证主体，如果主体已存在，则将当前的主体退出
//                Subject subject = SecurityUtils.getSubject();
//                User user = (User) subject.getPrincipal();
//                int userIdTemp = user.getId();
                //添加公海客户到自己名下
                customer.setUserid(ShiroUtil.getCurrentUserId());
//                customer.setUserid(userIdTemp);
                customerMapper.update4userid(customer);

                logger.info("{}将客户{}添加到了自己名下",ShiroUtil.getCurrentUserName(),customer.getCustname());
            //} else {
            //    throw new ForbiddenException();
            //}
        }

    }

    /**
     * 转交客户
     * @param custId 客户ID
     * @param userId 转入用户ID
     */
    public void tranCustomer(Integer custId, Integer userId) {
        Customer customer = customerMapper.findById(custId);
        if(customer == null) {
            throw new NotFoundException("客户不存在");
        } else {
            if(customer.getUserid().equals(ShiroUtil.getCurrentUserId())) {
                User user = userMapper.findById(userId);
                if(user == null) {
                    throw new NotFoundException("用户不存在");
                } else {
                    customer.setUserid(user.getId());
                    customer.setMark(customer.getMark() + "   " + ShiroUtil.getCurrentUserName()+"转交过来的客户");
                    customerMapper.update(customer);

                    logger.info("{}把客户{}转交给了{}",ShiroUtil.getCurrentUserName(),customer.getCustname(),user.getUsername());

                }
            } else {
                throw new ForbiddenException();
            }
        }
    }

    /**
     * 根据当前登录的用户获取对应的客户
     * @return
     */
    public List<Customer> findCustomerByCurrentUser() {
        return customerMapper.findByUserIdAndEmptyUserId(ShiroUtil.getCurrentUserId());
    }

    /**
     * 首页的统计柱状图
     * @return
     */
    public List<Map<String, Object>> homeTotal() {
        return customerMapper.findTotal();
    }

    /**
     * 获取userId对应客户
     * @param id
     * @return
     */
    public List<Customer> findCustomerByUserId(Integer id) {
        return customerMapper.findByUserIdAndEmptyUserId(id);
    }

    /**
     * 在微信中查询客户
     * @param id
     * @return
     */
    public Customer findCustById(Integer id) {
        return  customerMapper.findById(id);
    }
}
