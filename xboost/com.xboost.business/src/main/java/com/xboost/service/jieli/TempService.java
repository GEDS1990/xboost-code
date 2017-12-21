package com.xboost.service.jieli;

import com.mckinsey.sf.data.solution.ArrInfo;
import com.xboost.mapper.ArrInfoMapper;
import com.xboost.mapper.jieli.TempMapper;
import com.xboost.pojo.jieli.Temp;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@Transactional
public class TempService {
    /**
     * 根据查询条件获取模
     * @param param
     * @return
     */
    @Inject
    TempMapper tempMapper;
    public void saveArrInfo(Temp temp) {
        tempMapper.saveTemp(temp);
    }
    /**
     * 查询所有运力信息
     * param
     * @return
     */
    public List<Temp> findAll(String scenariosId) {
        return tempMapper.findAll(scenariosId);
    }
}
