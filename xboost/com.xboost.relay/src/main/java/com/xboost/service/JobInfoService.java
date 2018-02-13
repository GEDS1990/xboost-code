package com.xboost.service;

import com.mckinsey.sf.data.solution.ArrInfo;
import com.mckinsey.sf.data.solution.JobInfo;
import com.xboost.mapper.ArrInfoMapper;
import com.xboost.mapper.JobInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobInfoService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Autowired
    JobInfoMapper jobInfoMapper;
    public void saveJobInfo(JobInfo jobInfo) {
        jobInfoMapper.saveJobInfo(jobInfo);
    }
}
