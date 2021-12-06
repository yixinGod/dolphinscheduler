package org.apache.dolphinscheduler.dao;

import org.apache.dolphinscheduler.dao.datasource.ConnectionFactory;
import org.apache.dolphinscheduler.dao.entity.Project;
import org.apache.dolphinscheduler.dao.mapper.AlertMapper;
import org.apache.dolphinscheduler.dao.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProjectDao extends AbstractBaseDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    protected void init() {
        projectMapper = ConnectionFactory.getInstance().getMapper(ProjectMapper.class);
    }

    public Project selectById(int id){
        return projectMapper.selectById(id);
    }
}