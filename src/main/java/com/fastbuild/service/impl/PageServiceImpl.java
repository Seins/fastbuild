package com.fastbuild.service.impl;

import com.fastbuild.dao.BaseDao;
import com.fastbuild.entity.SimpleArticleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by dengfs on 2015/8/29.
 */
@Service("pageService")
public class PageServiceImpl {
    @Resource(name = "baseDao")
    BaseDao baseDao;


    public List<SimpleArticleEntity> getSimpleArticleByPage(Map param) throws SQLException {
        return baseDao.getList("page.getArticleByPage",param);
    }

    public SimpleArticleEntity getSimpleArticleById(Map param)throws SQLException {
        return (SimpleArticleEntity) baseDao.getObject("page.getArticleById", param);
    }
}
