package com.fastbuild.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Component
public class HibernateBaseDao {
    private HibernateTemplate hibernateTemplate;

    public void add(Object o) {
        hibernateTemplate.save(o);
    }

    public void update(Object o) {
        hibernateTemplate.update(o);
    }

    public void merge(Object o) {
        hibernateTemplate.merge(o);
    }

    public Object getById(Class<?> c, Serializable id) {
        return hibernateTemplate.get(c, id);
    }

    public void delete(Object o) {
        hibernateTemplate.delete(o);
    }

    public void deleteById(Class<?> c, Serializable id) {
        delete(getById(c, id));
    }

    public List<?> getAll(Class<?> c) {
        String hql = "from" + c.getName();
        return hibernateTemplate.find("from " + c.getName());
    }

    public void bulkUpdate(String hql, Object... objects) {
        hibernateTemplate.bulkUpdate(hql, objects);
    }

    public Object getUnique(final String hql, final Object... objects) {
        return hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                if (objects != null)
                    for (int i = 0; i < objects.length; i++)
                        query.setParameter(i, objects[i]);
                return query.uniqueResult();
            }
        });
    }

    public List<?> pageQuery(final String hql, final Integer page, final Integer size, final Object... objects) {
        return hibernateTemplate.executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                if (objects != null)
                    for (int i = 0; i < objects.length; i++) {
                        query.setParameter(i, objects[i]);
                    }
                if (page != null && size != null)
                    query.setFirstResult((page - 1) * size).setMaxResults(size);
                return query.list();
            }
        });
    }

    public List<?> pageQuery(String hql, Object... objects) {
        return pageQuery(hql, null, null, objects);
    }


    public void save(Object o) {
        if (o != null)
            hibernateTemplate.saveOrUpdate(o);
    }
    public void update(final String hql, final Object... objects) {
        hibernateTemplate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                if (objects != null)
                    for (int i = 0; i < objects.length; i++)
                        query.setParameter(i, objects[i]);
                return query.executeUpdate();
            }
        });
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Resource
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }


    public <T> List<T> queryBySql(String sql, Class<T> clzz) {

        SQLQuery sqlQuery = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());

        sqlQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> list = (List<Map<String, Object>>) sqlQuery
                .list();

        List<T> result = new ArrayList<T>();

        try {
            PropertyDescriptor[] props = Introspector.getBeanInfo(clzz)
                    .getPropertyDescriptors();

            for (Map<String, Object> map : list) {
                T t = clzz.newInstance();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String attrName = entry.getKey().toLowerCase();
                    for (PropertyDescriptor prop : props) {
                        if (!attrName.equals(prop.getName())) {
                            continue;
                        }
                        Method method = prop.getWriteMethod();
                        method.invoke(t, entry.getValue());
                    }
                }
                result.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
