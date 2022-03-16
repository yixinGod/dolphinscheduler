package org.apache.dolphinscheduler.dao;

import org.apache.dolphinscheduler.dao.datasource.ConnectionFactory;
import org.apache.dolphinscheduler.dao.entity.ProcessInstance;
import org.apache.dolphinscheduler.dao.mapper.ProcessInstanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProcessInstanceDao extends AbstractBaseDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProcessInstanceMapper processInstanceMapper;

    @Override
    protected void init() {
        processInstanceMapper = ConnectionFactory.getInstance().getMapper(ProcessInstanceMapper.class);
    }

    public ProcessInstance selectById(int id){
        return processInstanceMapper.selectById(id);
    }

    public void updateGlobalParams(String globalParams,int id){
        processInstanceMapper.updateGlobalParamsById(globalParams,id);
    }
}