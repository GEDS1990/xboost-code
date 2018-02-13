package com.xboost.service;

import com.xboost.exception.ForbiddenException;
import com.xboost.exception.NotFoundException;
import com.xboost.mapper.TaskMapper;
import com.xboost.pojo.Task;
import com.xboost.util.ShiroUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;

    /**
     * 添加新的待办任务
     * @param task
     */
    public void save(Task task) {
        task.setCreatetime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
        task.setUserid(ShiroUtil.getCurrentUserId());
        task.setDone(false);
        taskMapper.save(task);
    }

    /**
     * 获取当前登录用户的所有待办任务
     * @return
     */
    public List<Task> findAllByCurrentUser() {
        return taskMapper.findByUserId(ShiroUtil.getCurrentUserId());
    }

    /**
     * 改变任务的状态
     * @param taskId 任务ID
     * @param state 已完成 true,未完成 false
     */
    public void changeTaskState(String taskId, boolean state) {
        Task task = taskMapper.findById(taskId);
        if(task == null) {
            throw new NotFoundException();
        } else {
            if(task.getUserid().equals(ShiroUtil.getCurrentUserId())) {
                if(state) {
                    task.setDone(state);
                    task.setDonetime(DateTime.now().toString("yyyy-MM-dd HH:mm"));
                } else {
                    task.setDone(state);
                    task.setDonetime("");
                }
                taskMapper.update(task);
            } else {
                throw new ForbiddenException();
            }
        }
    }

    /**
     * 删除已完成任务
     * @param taskId 任务ID
     */
    public void delete(String taskId) {
        Task task = taskMapper.findById(taskId);
        if(task == null) {
            throw new NotFoundException();
        } else {
            if(task.getUserid().equals(ShiroUtil.getCurrentUserId())) {
                taskMapper.delete(task);
            } else {
                throw new ForbiddenException();
            }
        }
    }

    /**
     * 根据客户id查询相关未完成的待办任务
     * @param id
     * @return
     */
    public List<Task> findunDoneTaskByCustId(Integer id) {
        return taskMapper.findByCustIdAndState(id,false);
    }
}
