package com.vz.share.dao.base;

import com.vz.share.context.Context;
import com.vz.share.context.ContextManager;
import com.vz.share.entity.PageData;
import com.vz.share.entity.base.BaseEntity;
import com.vz.share.exception.DaoException;
import com.vz.share.utils.IdWorkerUtil;
import org.hibernate.*;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.*;

/**
 * Created by VVz on 2017/3/16.
 *
 * @des base dao
 */
public class BaseDao<T> extends HibernateDaoSupport {

    public BaseDao() {

    }

    /**
     * 添加实体
     *
     * @param entity
     * @return
     */
    public String add(T entity) {
        String id = null;
        if (entity instanceof BaseEntity) {
            BaseEntity entityBase = (BaseEntity) entity;
            if (entityBase.getId() == null || entityBase.getId().equals("")) {
                id = String.valueOf(IdWorkerUtil.generateId());
                entityBase.setId(id);
            }
            if (entityBase.getCreateDate() == null) {
                entityBase.setCreateDate(new Date());
            }
            if (entityBase.getUpdateDate() == null) {
                entityBase.setUpdateDate(new Date());
            }
            if (entityBase.getCreateUser() == null) {
                Context context = ContextManager.getContext();
                if (context != null) {
                    entityBase.setCreateUser(context.getUserId());
                }
            }
            if (entityBase.getUpdateUser() == null) {
                Context context = ContextManager.getContext();
                if (context != null) {
                    entityBase.setUpdateUser(context.getUserId());
                }
            }
        }
        super.getHibernateTemplate().persist(entity);
        return id;
    }

    /**
     * 更新
     * @param entity
     */
    public void update(T entity){
        if (entity instanceof BaseEntity) {
            BaseEntity entityBase = (BaseEntity) entity;
            if (entityBase != null) {
                entityBase.setUpdateDate(new Date());

                Context context = ContextManager.getContext();
                if (context != null) {
                    entityBase.setUpdateUser(context.getUserId());
                }
            }
        }
        super.getHibernateTemplate().update(entity);
    }

    /**
     * 删除
     * @param entity
     */
    public void remove(T entity){
        super.getHibernateTemplate().delete(entity);
    }

    /**
     * 删除实体，根据主键
     *
     * @param clazz
     * @param id
     */
    public void remove(Class<?> clazz, Serializable id) {
        T object = this.get(clazz, id);
        if (object != null) {
            super.getHibernateTemplate().delete(object);
        }
    }

    /**
     * 获取实体，根据主键
     *
     * @param clazz
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<?> clazz, Serializable id) {
        return (T) this.getHibernateTemplate().get(clazz, id);
    }

    /**
     * 根据主键判断是否存在实体
     *
     * @param clazz
     * @param id
     * @return
     */
    public boolean exist(Class<?> clazz, Serializable id) {
        try {
            T object = this.get(clazz, id);
            return object != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否存在实体
     *
     * @param hql
     * @return
     */
    public boolean exist(String hql) {
        T object = this.findObject(hql);
        return object != null;
    }

    /**
     * 是否存在实体
     *
     * @param hql
     * @param arg
     * @return
     */
    public boolean exist(String hql, Object arg) {
        T object = this.findObject(hql, arg);
        return object != null;
    }

    /**
     * 是否存在实体
     *
     * @param hql
     * @param args
     * @return
     */
    public boolean exist(String hql, Object[] args) {
        Object object = this.findObject(hql, args);
        return object != null;
    }

    /**
     * 查找实体列表，通过实体类
     *
     * @param clazz
     * @return
     */
    public List<T> find(Class<?> clazz) {
        return find(clazz, null);
    }

    /**
     * 查找实体列表，通过某一属性
     *
     * @param clazz
     * @param propName
     * @param propValue
     * @return
     */
    public List<T> find(Class<?> clazz, String propName, Object propValue) {
        Map<String, Object> propValues = new HashMap<String, Object>();
        propValues.put(propName, propValue);
        return find(clazz, propValues);
    }

    /**
     * 查找实体列表，通过一些属性
     *
     * @param clazz
     * @param propValues
     * @return
     */
    public List<T> find(Class<?> clazz, Map<String, Object> propValues) {
        return find(clazz, propValues, null);
    }

    /**
     * 查找实体列表，通过一些属性及排序条件
     * @param clazz
     * @param propValues
     * @param orderBys
     * @return
     */
    public List<T> find(Class<?> clazz, Map<String, Object> propValues, String[] orderBys) {
        String hql = "from " + clazz.getSimpleName() + " where 1=1 ";
        //构建条件
        Object[] params = new Object[0];
        if (propValues != null && propValues.size() > 0) {
            params = new Object[propValues.size()];
            int i = 0;
            for (String key : propValues.keySet()) {
                hql += String.format(" and %1$s=? ", key);
                params[i] = propValues.get(key);
                i++;
            }
        }

        //构建排序
        if (orderBys != null && orderBys.length > 0) {
            hql += " order by ";
            int i = 0;
            for (String orderBy : orderBys) {
                if (i > 0) {
                    hql += ",";
                }
                hql += (" " + orderBy);
                i++;
            }
        }

        return find(hql, params);
    }

    /**
     * 查找实体列表，通过HQL
     *
     * @param hql
     * @return
     */
    public List<T> find(String hql) {
        return this.getHibernateTemplate().find(hql);
    }

    /**
     * 查找实体列表，通过HQL和单个参数
     *
     * @param hql
     * @param arg
     * @return
     */
    public List<T> find(String hql, Object arg) {
        return this.getHibernateTemplate().find(hql, new Object[] { arg });
    }

    /**
     * 查找实体列表，通过HQL和多个参数
     *
     * @param hql
     * @param args
     * @return
     */
    public List<T> find(String hql, Object[] args) {
        return this.getHibernateTemplate().find(hql, args);
    }

    /**
     * 查找实体列表，通过HQL和多个参数
     * 传参的格式为:id,:name,支持in
     *
     * @param hql
     * @param params
     * @return
     */
    public List<T> find(String hql, Map<String, Object> params) {
        Session session = null;
        try {
            session = super.getSession(true);
            Query query = session.createQuery(hql);
            if (params != null && params.size() > 0) {
                query.setProperties(params);
            }
            return query.list();
        } catch (Exception e) {
            throw new DaoException("get hql data error", e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }

    /**
     * 查找实体列表，通过名字命名的参数
     *
     * @param hql
     * @param args
     * @return
     */
    public List<T> findByNamedParam(String hql, Map<String, Object> args) {
        Session session = null;
        try {
            session = super.getSession(true);
            Query query = session.createQuery(hql);
            if (args != null && args.size() > 0) {
                for (String paramName : args.keySet()) {
                    Object paramValue = args.get(paramName);
                    Class<?> paramClass = paramValue.getClass();
                    if (Object[].class.isAssignableFrom(paramClass)) {
                        query.setParameterList(paramName, (Object[]) paramValue);
                    } else if (Collection.class.isAssignableFrom(paramClass)) {
                        query.setParameterList(paramName, (Collection<?>) paramValue);
                    } else if (List.class.isAssignableFrom(paramClass)) {
                        List<?> listParamValue = (List<?>) paramValue;
                        query.setParameterList(paramName, listParamValue.toArray());
                    } else {
                        query.setParameter(paramName, paramValue);
                    }
                }
            }
            return query.list();
        } catch (Exception e) {
            throw new DaoException("find by namedparam data error", e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }

    /**
     * 查找实体列表，通过SQL
     *
     * @param sql
     * @param clazz
     * @return
     */
    public List<T> findBySql(String sql, Class<?> clazz) {
        return this.findBySql(sql, clazz, new Object[] {});
    }

    /**
     * 查找实体列表，通过SQL和单个参数
     *
     * @param sql
     * @param clazz
     * @param arg
     * @return
     */
    public List<T> findBySql(String sql, Class<?> clazz, Object arg) {
        return this.findBySql(sql, clazz, new Object[] { arg });
    }

    /**
     * 查找实体列表，通过SQL和多个参数
     *
     * @param sql
     * @param clazz
     * @param args
     * @return
     */
    public List<T> findBySql(String sql, Class<?> clazz, Object[] args) {
        Session session = null;
        try {
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    query.setParameter(i, args[i]);
                }
            }
            query.addEntity(clazz);
            return query.list();
        } catch (Exception e) {
            throw new DaoException("get sql data error" + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }
    /**
     * 查找实体列表，通过SQL和多个参数
     * 传参的格式为:id,:name，支持in语句
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public List<T> findBySql(String sql, Class<?> clazz, Map<String, Object> params) {
        Session session = null;
        try {
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            if (params != null && params.size() > 0) {
                query.setProperties(params);
            }
            query.addEntity(clazz);
            return query.list();
        } catch (Exception e) {
            throw new DaoException("get sql data error" + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }

    /**
     * 查找数据列表，通过SQL
     *
     * @param sql
     * @return
     */
    public List<Object[]> findBySql(String sql) {
        return this.findBySql(sql, new Object[] {});
    }

    /**
     * 查找数据列表，通过SQL和单个参数
     *
     * @param sql
     * @param arg
     * @return
     */
    public List<Object[]> findBySql(String sql, Object arg) {
        return this.findBySql(sql, new Object[] { arg });
    }

    /**
     * 查找数据列表，通过SQL和多个参数
     *
     * @param sql
     * @param args
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findBySql(String sql, Object[] args) {
        Session session = null;
        try {
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    query.setParameter(i, args[i]);
                }
            }

            List<?> datas = query.list();
            if (datas != null && datas.size() > 0) {
                Object data = datas.get(0);
                if (!(data instanceof Object[])) {
                    // 需要处理的数据，列表内为单个对象，需转为对象组
                    List<Object[]> newDatas = new ArrayList<Object[]>();
                    for (Object item : datas) {
                        newDatas.add(new Object[] { item });
                    }
                    return newDatas;
                } else {
                    // 正常数据，列表内为对象组
                    return (List<Object[]>) datas;
                }
            } else {
                return new ArrayList<Object[]>();
            }
        } catch (Exception e) {
            throw new DaoException("get sql data error" + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }
    /**
     * 查找数据列表，通过SQL和多个参数，参数格式:id,:name,支持in(:ids)
     * @param sql
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findBySql(String sql, Map<String, Object> params) {
        Session session = null;
        try {
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            if (params != null && params.size() > 0) {
                query.setProperties(params);
            }

            List<?> datas = query.list();
            if (datas != null && datas.size() > 0) {
                Object data = datas.get(0);
                if (!(data instanceof Object[])) {
                    // 需要处理的数据，列表内为单个对象，需转为对象组
                    List<Object[]> newDatas = new ArrayList<Object[]>();
                    for (Object item : datas) {
                        newDatas.add(new Object[] { item });
                    }
                    return newDatas;
                } else {
                    // 正常数据，列表内为对象组
                    return (List<Object[]>) datas;
                }
            } else {
                return new ArrayList<Object[]>();
            }
        } catch (Exception e) {
            throw new DaoException("get sql data error" + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }
    /**
     * 查找带列名的数据列表，通过SQL
     *
     * @param sql
     * @return
     */
    public List<Map<String, Object>> findBySqlAlias(String sql) {
        return this.findBySqlAlias(sql, new Object[] {});
    }

    /**
     * 查找带列名的数据列表，通过SQL和单个参数
     *
     * @param sql
     * @param arg
     * @return
     */
    public List<Map<String, Object>> findBySqlAlias(String sql, Object arg) {
        return this.findBySqlAlias(sql, new Object[] { arg });
    }

    /**
     * 查找带列名的数据列表，通过SQL和多个参数
     *
     * @param sql
     * @param args
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findBySqlAlias(String sql, Object[] args) {
        Session session = null;
        try {
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    query.setParameter(i, args[i]);
                }
            }

            List<Map<String, Object>> datas = query.list();
            return datas;
        } catch (Exception e) {
            throw new DaoException("get sql data error" + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }
    /**
     * 查找带列名的数据列表，通过SQL和多个参数，参数格式:id,:name,支持in(:ids)
     *
     * @param sql
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findBySqlAlias(String sql, Map<String, Object> params) {
        Session session = null;
        try {
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            if (params != null && params.size() > 0) {
                query.setProperties(params);
            }

            List<Map<String, Object>> datas = query.list();
            //检测返回结果是不是为空，如果该行数据都为空则说明结果为空
            if (datas.size() == 1) {
                boolean isEmpty = true;
                for (Object item : datas.get(0).values()) {
                    if (item != null) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return new ArrayList<Map<String, Object>>();
                }
            }
            return datas;
        } catch (Exception e) {
            throw new DaoException("get sql data error" + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }
    /**
     * 查找对象，通过实体及某些属性
     * @param clazz
     * @param propName
     * @param propValue
     * @return
     */
    public T findObject(Class<?> clazz, String propName, Object propValue) {
        Map<String, Object> propValues = new HashMap<String, Object>();
        propValues.put(propName, propValue);
        return findObject(clazz, propValues);
    }
    /**
     * 查找对象，通过实体及一些属性
     *
     * @param clazz
     * @param propValues
     * @return
     */
    public T findObject(Class<?> clazz, Map<String, Object> propValues) {
        String hql = "from " + clazz.getName() + " where 1=1 ";
        Object[] params = new Object[0];
        if (propValues != null && propValues.size() > 0) {
            params = new Object[propValues.size()];
            int i = 0;
            for (String key : propValues.keySet()) {
                hql += String.format(" and %1$s=?", key);
                params[i] = propValues.get(key);
                i++;
            }
        }
        return findObject(hql, params);
    }
    /**
     * 查找对象，通过HQL
     *
     * @param hql
     * @return
     */
    public T findObject(String hql) {
        return this.findObject(hql, null);
    }

    /**
     * 查找对象，通过HQL和单个参数
     *
     * @param hql
     * @param arg
     * @return
     */
    public T findObject(String hql, Object arg) {
        return this.findObject(hql, new Object[] { arg });
    }

    /**
     * 查找对象，通过HQL和多个参数
     *
     * @param hql
     * @param args
     * @return
     */
    public T findObject(String hql, Object[] args) {
        Session session = null;
        try {
            session = this.getDefaultSession();
            Query query = session.createQuery(hql);
            query.setMaxResults(1);

            // set param
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    query.setParameter(i, args[i]);
                }
            }
            List<T> result = query.list();

            return this.getFirstOrDefault(result);
        } catch (Exception e) {
            throw new DaoException("find object error", e);
        } finally {
			/*if (session != null && session.isOpen()) {
				session.close();
			}*/
        }
    }

    /**
     * 查找对象，通过HQL
     *
     * @param hql
     * @return
     */
    public <T> T findObjectT(String hql) {
        return this.findObjectT(hql, null);
    }

    /**
     * 查找对象，通过HQL和单个参数
     *
     * @param hql
     * @param arg
     * @return
     */
    public <T> T findObjectT(String hql, Object arg) {
        return this.findObjectT(hql, new Object[] { arg });
    }

    /**
     * 查找对象，通过HQL和多个参数
     *
     * @param hql
     * @param args
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T findObjectT(String hql, Object[] args) {
        Object object = this.findObject(hql, args);
        if (object != null) {
            return (T) object;
        } else {
            return null;
        }
    }

    /**
     * 查找对象，通过SQL
     *
     * @param hql
     * @return
     */
    public T findObjectBySql(String hql) {
        return this.findObjectBySql(hql, new Object[0]);
    }

    /**
     * 查找对象，通过SQL和单个参数
     *
     * @param hql
     * @param arg
     * @return
     */
    public T findObjectBySql(String hql, Object arg) {
        return this.findObjectBySql(hql, new Object[] { arg });
    }

    /**
     * 查找对象，通过SQL和多个参数
     *
     * @param sql
     * @param args
     * @return
     */
    public T findObjectBySql(String sql, Object[] args) {
        Session session = null;
        try {
            session = this.getDefaultSession();
            SQLQuery query = session.createSQLQuery(sql);
            query.setMaxResults(1);
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    query.setParameter(i, args[i]);
                }
            }
            List<T> result = query.list();
            return this.getFirstOrDefault(result);
        } catch (Exception e) {
            throw new DaoException("get sql data error" + sql, e);
        } finally {
			/*if (session != null && session.isOpen()) {
				session.close();
			}*/
        }
    }

    /**
     * 查找对象，通过SQL和多个参数
     * 传参的格式为:id,:name,支持in
     *
     * @param sql
     * @param params
     * @return
     */
    public T findObjectBySql(String sql, Map<String, Object> params) {
        Session session = null;
        try {
            session = this.getDefaultSession();
            SQLQuery query = session.createSQLQuery(sql);
            query.setMaxResults(1);
            if (params != null && params.size() > 0) {
                query.setProperties(params);
            }
            List<T> result = query.list();
            return this.getFirstOrDefault(result);
        } catch (Exception e) {
            throw new DaoException("get sql data error" + sql, e);
        } finally {
			/*if (session != null && session.isOpen()) {
				session.close();
			}*/
        }
    }

    /**
     * 查找对象，通过SQL
     *
     * @param sql
     * @return
     */
    public <T> T findObjectTBySql(String sql) {
        return this.findObjectTBySql(sql, new Object[0]);
    }

    /**
     * 查找对象，通过SQL和单个参数
     *
     * @param sql
     * @param arg
     * @return
     */
    public <T> T findObjectTBySql(String sql, Object arg) {
        return this.findObjectTBySql(sql, new Object[] { arg });
    }

    /**
     * 查找对象，通过SQL和多个参数
     *
     * @param sql
     * @param args
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T findObjectTBySql(String sql, Object[] args) {
        Object object = this.findObjectBySql(sql, args);
        if (object != null) {
            return (T) object;
        } else {
            return null;
        }
    }

    /**
     * 查找对象，通过SQL和多个参数
     * 传参的格式为:id,:name,支持in
     *
     * @param sql
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T findObjectTBySql(String sql, Map<String, Object> params) {
        Object object = this.findObjectBySql(sql, params);
        if (object != null) {
            return (T) object;
        } else {
            return null;
        }
    }

//    /**
//     * 查找对象列表，通过SQL和多个参数
//     *
//     * @param sql
//     * @param args
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public List<T> findObjectListBySql(String sql, Object[] args) {
//        Session session = null;
//        try {
//            session = super.getSession(true);
//            SQLQuery query = session.createSQLQuery(sql);
//            if (args != null && args.length > 0) {
//                for (int i = 0; i < args.length; i++) {
//                    query.setParameter(i, args[i]);
//                }
//            }
//
//            List<T> datas = query.list();
//            if (datas != null && datas.size() > 0) {
//                T data = datas.get(0);
//                if (data instanceof Object[]) {
//                    // 需要处理的数据，列表内为对象组，需转为单个对象
//                    List<T> result = new ArrayList<T>();
//                    for (T item : datas) {
//                        T[] itemArray = (T[]) item;
//                        result.add(itemArray.length > 0 ? itemArray[0] : null);
//                    }
//                    return result;
//                } else {
//                    // 正常数据，列表内为对象组
//                    return (List<T>) datas;
//                }
//            } else {
//                return new ArrayList<T>();
//            }
//        } catch (Exception e) {
//            throw new DaoException("get sql data error" + sql, e);
//        } finally {
//			/*if (session != null) {
//				session.close();
//			}*/
//        }
//    }

    /**
     * 查找对象列表，通过SQL和多个参数，参数格式:id,:name,支持in(:ids)
     * @param sql
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object> findObjectListBySql(String sql, Map<String, Object> params) {
        Session session = null;
        try {
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            if (params != null && params.size() > 0) {
                query.setProperties(params);
            }

            List<?> datas = query.list();
            if (datas != null && datas.size() > 0) {
                Object data = datas.get(0);
                if (data instanceof Object[]) {
                    // 需要处理的数据，列表内为对象组，需转为单个对象
                    List<Object> result = new ArrayList<Object>();
                    for (Object item : datas) {
                        Object[] itemArray = (Object[]) item;
                        result.add(itemArray.length > 0 ? itemArray[0] : null);
                    }
                    return result;
                } else {
                    // 正常数据，列表内为对象组
                    return (List<Object>) datas;
                }
            } else {
                return new ArrayList<Object>();
            }
        } catch (Exception e) {
            throw new DaoException("get sql data error" + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }

    /**
     * 查询实体分页数据，通过HQL
     *
     * @param hql
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageData query(String hql, int pageIndex, int pageSize) {
        return this.query(hql, pageIndex, pageSize, new Object[0]);
    }

    /**
     * 查询实体分页数据，通过HQL和单个参数
     *
     * @param hql
     * @param pageIndex
     * @param pageSize
     * @param arg
     * @return
     */
    public PageData query(String hql, int pageIndex, int pageSize, Object arg) {
        return this.query(hql, pageIndex, pageSize, new Object[] { arg });
    }

    /**
     * 查询实体分页数据，通过HQL和多个参数
     *
     * @param hql
     * @param pageIndex
     * @param pageSize
     * @param args
     * @return
     */
    public PageData query(String hql, int pageIndex, int pageSize, Object[] args) {
        Session session = super.getSession(true);
        try {
            Query query = session.createQuery(hql);
            query.setFirstResult((pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);

            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    query = query.setParameter(i, args[i]);
                }
            }

            List<?> data = query.list();

            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
            ScrollableResults sr = query.scroll();
            long total = 0;
            if (sr.last()) {
                total = sr.getRowNumber() + 1;
            }

            PageData pd = new PageData();
            pd.setCurrentPageNumber(pageIndex);
            pd.setItems(data);
            pd.setPageSize(pageSize);
            pd.setTotal((int) total);

            return pd;
        } catch (Exception e) {
            throw new DaoException("get page data error," + hql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }
    /**
     * 查询实体分页数据，通过HQL和多个参数
     * 其中HQL的参数必须为:id,:name这种形式，支持in(:id)
     *
     * @param hql
     * @param pageIndex
     * @param pageSize
     * @param params
     * @return
     */
    public PageData query(String hql, int pageIndex, int pageSize, Map<String, Object> params) {
        Session session = super.getSession(true);
        try {
            Query query = session.createQuery(hql);
            query.setFirstResult((pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);

            if (params != null && params.size() > 0) {
                query.setProperties(params);
            }

            List<?> data = query.list();

            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
            ScrollableResults sr = query.scroll();
            long total = 0;
            if (sr.last()) {
                total = sr.getRowNumber() + 1;
            }

            PageData pd = new PageData();
            pd.setCurrentPageNumber(pageIndex);
            pd.setItems(data);
            pd.setPageSize(pageSize);
            pd.setTotal((int) total);

            return pd;
        } catch (Exception e) {
            throw new DaoException("get page data error," + hql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }

    /**
     * 执行语句，通过HQL
     *
     * @param hql
     */
    public void execute(String hql) {
        this.execute(hql, new Object[0]);
    }

    /**
     * 执行语句，通过HQL和单个参数
     *
     * @param hql
     * @param arg
     */
    public void execute(String hql, Object arg) {
        this.execute(hql, new Object[] { arg });
    }

    /**
     * 执行语句，通过HQL和多个参数
     *
     * @param hql
     * @param args
     */
    public void execute(String hql, Object[] args) {
        Session session = null;
        try {
            session = this.getDefaultSession();

            Query query = session.createQuery(hql);

            // seet param
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    query.setParameter(i, args[i]);
                }
            }

            query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException("execute error", e);
        } finally {
			/*if (session != null && session.isOpen()) {
				session.close();
			}*/
        }
    }

    /**
     * 执行语句，通过HQL和多个参数，支持key方式
     *
     * @param hql
     * @param params
     */
    public void executeByMap(String hql, Map<String, Object> params) {
        Session session = null;
        try {
            session = this.getDefaultSession();

            Query query = session.createQuery(hql);

            // set param
            if (params != null && params.size() > 0) {
                query.setProperties(params);
            }

            query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException("execute error", e);
        } finally {
			/*if (session != null && session.isOpen()) {
				session.close();
			}*/
        }
    }

    /**
     * 查询实体分页数据，通过SQL
     *
     * @param sql
     * @param entityClazz
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageData querySql(String sql, Class<?> entityClazz, int pageIndex, int pageSize) {
        return querySql(sql, entityClazz, pageIndex, pageSize, new Object[0]);
    }

    /**
     * 查询实体分页数据，通过SQL和单个参数
     *
     * @param sql
     * @param entityClazz
     * @param pageIndex
     * @param pageSize
     * @param arg
     * @return
     */
    public PageData querySql(String sql, Class<?> entityClazz, int pageIndex, int pageSize, Object arg) {
        return querySql(sql, entityClazz, pageIndex, pageSize, new Object[] { arg });
    }

    /**
     * 查询实体分页数据，通过SQL和多个参数
     *
     * @param sql
     * @param entityClazz
     * @param pageIndex
     * @param pageSize
     * @param args
     * @return
     */
    public PageData querySql(String sql, Class<?> entityClazz, int pageIndex, int pageSize, Object[] args) {
        Session session = null;
        try {
            PageData pageData = new PageData();
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            query.setFirstResult((pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    query = (SQLQuery) query.setParameter(i, args[i]);
                }
            }
            if (entityClazz != null) {
                query.addEntity(entityClazz);
            }
            List<?> result = query.list();

            // get total page
            SQLQuery countQuery = session.createSQLQuery("select count(1) from (" + sql + ") t");
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    countQuery.setParameter(i, args[i]);
                }
            }
            List<T> countResult = countQuery.list();
            Object totalObject = this.getFirstOrDefault(countResult);

            // set page
            pageData.setCurrentPageNumber(pageIndex);
            pageData.setPageSize(pageSize);
            pageData.setItems(result);
            pageData.setTotal(Integer.parseInt(totalObject.toString()));

            return pageData;
        } catch (Exception e) {
            throw new DaoException("get page data error," + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }

    /**
     * 查询实体分页数据，通过SQL和多个参数
     * 参数形式:id,:name,支持in(:id)
     *
     * @param sql
     * @param entityClazz
     * @param pageIndex
     * @param pageSize
     * @param params
     * @return
     */
    public PageData querySql(String sql, Class<?> entityClazz, int pageIndex, int pageSize, Map<String, Object> params) {
        Session session = null;
        try {
            PageData pageData = new PageData();
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            query.setFirstResult((pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);
            if (params != null && params.size() > 0) {
                query = (SQLQuery) query.setProperties(params);
            }
            if (entityClazz != null) {
                query.addEntity(entityClazz);
            }
            List<?> result = query.list();

            // get total page
            SQLQuery countQuery = session.createSQLQuery("select count(1) from (" + sql + ") t");
            if (params != null && params.size() > 0) {
                countQuery = (SQLQuery) countQuery.setProperties(params);
            }
            List<T> countResult = countQuery.list();
            Object totalObject = this.getFirstOrDefault(countResult);

            // set page
            pageData.setCurrentPageNumber(pageIndex);
            pageData.setPageSize(pageSize);
            pageData.setItems(result);
            pageData.setTotal(Integer.parseInt(totalObject.toString()));

            return pageData;
        } catch (Exception e) {
            throw new DaoException("get page data error," + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }

    /**
     * 查询实体分页数据，通过SQL
     *
     * @param sql
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageData querySql(String sql, int pageIndex, int pageSize) {
        return querySql(sql, pageIndex, pageSize, new Object[0]);
    }

    /**
     * 查询实体分页数据，通过SQL和单个参数
     *
     * @param sql
     * @param pageIndex
     * @param pageSize
     * @param arg
     * @return
     */
    public PageData querySql(String sql, int pageIndex, int pageSize, Object arg) {
        return querySql(sql, pageIndex, pageSize, new Object[] { arg });
    }

    /**
     * 查询实体分页数据，通过SQL和多个参数
     *
     * @param sql
     * @param pageIndex
     * @param pageSize
     * @param args
     * @return
     */
    public PageData querySql(String sql, int pageIndex, int pageSize, Object[] args) {
        return querySql(sql, null, pageIndex, pageSize, args);
    }

    /**
     * 查询实体分页数据，通过SQL和多个参数
     * 参数格式:id,:name,支持in(:ids)
     *
     * @param sql
     * @param pageIndex
     * @param pageSize
     * @param params
     * @return
     */
    public PageData querySql(String sql, int pageIndex, int pageSize, Map<String, Object> params) {
        return querySql(sql, null, pageIndex, pageSize, params);
    }

    /**
     * 查询带表头分页数据，通过SQL和多个参数，分页返回值List<Map<String, Object>>
     * @param sql
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageData querySqlAlias(String sql, int pageIndex, int pageSize) {
        return querySqlAlias(sql, pageIndex, pageSize, new Object[0]);
    }

    /**
     * 查询带表头分页数据，通过SQL和多个参数，分页返回值List<Map<String, Object>>
     * @param sql
     * @param pageIndex
     * @param pageSize
     * @param arg
     * @return
     */
    public PageData querySqlAlias(String sql, int pageIndex, int pageSize, Object arg) {
        return querySqlAlias(sql, pageIndex, pageSize, new Object[] { arg });
    }

    /**
     * 查询带表头分页数据，通过SQL和多个参数，分页返回值List<Map<String, Object>>
     * @param sql
     * @param pageIndex
     * @param pageSize
     * @param args
     * @return
     */
    public PageData querySqlAlias(String sql, int pageIndex, int pageSize, Object[] args) {
        Session session = null;
        try {
            PageData pageData = new PageData();
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            query.setFirstResult((pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    query = (SQLQuery) query.setParameter(i, args[i]);
                }
            }
            List<?> result = query.list();

            // get total page
            SQLQuery countQuery = session.createSQLQuery("select count(1) from (" + sql + ") t");
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    countQuery.setParameter(i, args[i]);
                }
            }
            List<T> countResult = countQuery.list();
            Object totalObject = this.getFirstOrDefault(countResult);

            // set page
            pageData.setCurrentPageNumber(pageIndex);
            pageData.setPageSize(pageSize);
            pageData.setItems(result);
            pageData.setTotal(Integer.parseInt(totalObject.toString()));

            return pageData;
        } catch (Exception e) {
            throw new DaoException("get page data error," + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }

    /**
     * 查询带表头分页数据，通过SQL和多个参数，分页返回值List<Map<String, Object>>
     * 参数格式:id,:name,支持in(:ids)
     * @param sql
     * @param pageIndex
     * @param pageSize
     * @param params
     * @return
     */
    public PageData querySqlAlias(String sql, int pageIndex, int pageSize, Map<String, Object> params) {
        Session session = null;
        try {
            PageData pageData = new PageData();
            session = super.getSession(true);
            SQLQuery query = session.createSQLQuery(sql);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            query.setFirstResult((pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);
            if (params != null && params.size() > 0) {
                query = (SQLQuery) query.setProperties(params);
            }
            List<?> result = query.list();

            // get total page
            SQLQuery countQuery = session.createSQLQuery("select count(1) from (" + sql + ") t");
            if (params != null && params.size() > 0) {
                countQuery = (SQLQuery) countQuery.setProperties(params);
            }
            List<T> countResult = countQuery.list();
            Object totalObject = this.getFirstOrDefault(countResult);

            // set page
            pageData.setCurrentPageNumber(pageIndex);
            pageData.setPageSize(pageSize);
            pageData.setItems(result);
            pageData.setTotal(Integer.parseInt(totalObject.toString()));

            return pageData;
        } catch (Exception e) {
            throw new DaoException("get page data error," + sql, e);
        } finally {
			/*if (session != null) {
				session.close();
			}*/
        }
    }

    /**
     * 执行语句，通过SQL
     *
     * @param sql
     * @return
     */
    public int executeSql(String sql) {
        return executeSql(sql, new Object[0]);
    }

    /**
     * 执行语句，通过SQL和单个参数
     *
     * @param sql
     * @param arg
     * @return
     */
    public int executeSql(String sql, Object arg) {
        return executeSql(sql, new Object[] { arg });
    }

    /**
     * 执行语句，通过SQL和多个参数
     *
     * @param sql
     * @param args
     * @return
     */
    public int executeSql(String sql, Object[] args) {
        if (sql == null || sql.equals(""))
            return -1;

        Session session = null;
        try {
            session = this.getDefaultSession();

            SQLQuery query = session.createSQLQuery(sql);

            // set parameter
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    query.setParameter(i, args[i]);
                }
            }

            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException("execute sql error," + sql, e);
        } finally {
			/*if (session != null && session.isOpen()) {
				session.close();
			}*/
        }
    }

    /**
     * 执行语句，通过SQL和多个参数
     * 参数形式:id,:name,支持in(:id)
     *
     * @param sql
     * @param params
     * @return
     */
    public int executeSql(String sql, Map<String, Object> params) {
        if (sql == null || sql.equals(""))
            return -1;

        Session session = null;
        try {
            session = this.getDefaultSession();

            SQLQuery query = session.createSQLQuery(sql);

            // set parameter
            if (params != null && params.size() > 0) {
                query = (SQLQuery) query.setProperties(params);
            }

            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException("execute sql error," + sql, e);
        } finally {
			/*if (session != null && session.isOpen()) {
				session.close();
			}*/
        }
    }

    /**
     * 获取默认SESSION。如果存在事务则返回事务中的SESSION，否则创建新的SESSION
     *
     * @return
     */
    public Session getDefaultSession() {
        //TODO 在有事务的时候获取事务的Session，事务处理中execute方法不被控制
        return this.getHibernateTemplate().getSessionFactory().getCurrentSession();
    }

    protected T getFirstOrDefault(List<T> objects) {
        if (objects != null && objects.size() > 0) {
            return objects.get(0);
        } else {
            return null;
        }
    }
    /**
     * 根据hql汇总数据
     * "select count(*) from 对象类"
     * @param hql
     * @param params
     * @return
     */
    public int selectCountHql(String hql,Object... params){
        return ((Number)getHibernateTemplate().find(hql,params).listIterator().next()).intValue();
    }
}
