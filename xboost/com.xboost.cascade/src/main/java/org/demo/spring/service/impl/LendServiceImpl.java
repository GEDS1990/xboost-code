package org.demo.spring.service.impl;

import org.demo.spring.dao.LendDao;
import org.demo.spring.model.Lend;
import org.demo.spring.service.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Created by Administrator on 2017/6/29 0029.
 */
@Service
public class LendServiceImpl implements LendService {
    @Autowired
    LendDao lendDao;
    @Override
    public Long saveLend(Lend lend) {
        return lendDao.saveLend(lend);
    }

    @Override
    public List<Lend> queryLends(String lendName) {
        return null;
    }

    @Override
    public Lend getLendById(String id) {
        return null;
    }
}
