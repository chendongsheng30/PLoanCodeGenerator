package ${classPackage};

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ${doClassPackage}.${doName};

import com.huateng.ebank.business.common.ErrorCode;
import com.huateng.ebank.business.common.SystemConstant;
import com.huateng.ebank.framework.exceptions.CommonException;
import com.huateng.ebank.framework.util.DataFormat;
import com.huateng.ebank.framework.util.ExceptionUtil;

<#include "../common/annotation.ftl">
public class ${className} extends HibernateDaoSupport {
	
    /**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(${className}.class);

	public ${className}() {
		super();
	}

	/**
	 * 根据Hibernate ID查询记录，如果没有找到记录，则抛出异常
	 *
	 * @param id
	 * @return ${doName}
	 * @throws CommonException
	 */
	public ${doName} query(String id) throws CommonException {
		if (logger.isDebugEnabled()) {
			logger.debug("query(String) - start"); 
		}

		try {
            ${doName} ${sdoName} = (${doName}) this.getHibernateTemplate().get(${doName}.class, id);
			if (logger.isDebugEnabled()) {
				logger.debug("query(String) - end"); 
			}
			return ${sdoName};
		} catch (Exception e) {
			logger.error("query(String)", e); 

			ExceptionUtil.throwCommonException(e.getMessage(),
					ErrorCode.ERROR_CODE_${tableName}_SELECT, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("query(String) - end"); 
		}
		return null;
	}

	/**
	 * 根据Hibernate ID查询记录，如果没有找到记录，返回null,不抛出异常
	 *
	 * @param id
	 * @return ${doName}
	 * @throws CommonException
	 * @author yjw add
	 */
	public ${doName} queryById(String id) throws CommonException {
		
		if(id == null)
		{
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("query(String) - start"); 
		}

		try {
            ${doName} ${sdoName} = (${doName}) this.getHibernateTemplate().get(${doName}.class, id);
			if (logger.isDebugEnabled()) {
				logger.debug("query(String) - end"); 
			}
			return ${sdoName};
		} catch (Exception e) {
			logger.error("query(String)", e); 

			ExceptionUtil.throwCommonException(e.getMessage(),
					ErrorCode.ERROR_CODE_${tableName}_SELECT, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("query(String) - end"); 
		}
		return null;
	}

	/**
	 * 根据输入的条件查询所有符合条件的记录
	 *
	 * @param whereString
	 * @param objArray
	 * @param typeArray
	 * @return List
	 * @throws CommonException
	 */
	public List queryByCondition(String whereString, Object[] objArray,
			Type[] typeArray) throws CommonException {
		if (logger.isDebugEnabled()) {
			logger.debug("queryByCondition(String, Object[], Type[]) - start"); 
		}

		try {
			List list = this.getHibernateTemplate().find(
					"from ${doName} po where " + whereString, objArray);

			if (logger.isDebugEnabled()) {
				logger.debug("queryByCondition(String, Object[], Type[]) - end"); 
			}
			return list;
		} catch (Exception e) {
			logger.error("queryByCondition(String, Object[], Type[])", e); 

			ExceptionUtil.throwCommonException(e.getMessage(),
					ErrorCode.ERROR_CODE_${tableName}_SELECT, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("queryByCondition(String, Object[], Type[]) - end"); 
		}
		return null;
	}

	/**
	 * 根据输入的条件查询所有符合条件的记录
	 *
	 * @param whereString
	 * @return List
	 * @throws CommonException
	 */
	public List queryByCondition(String whereString) throws CommonException {
		if (logger.isDebugEnabled()) {
			logger.debug("queryByCondition(String) - start"); 
		}

		try {
			List list = this.getHibernateTemplate().find(
					"from ${doName} po where " + whereString);

			if (logger.isDebugEnabled()) {
				logger.debug("queryByCondition(String) - end"); 
			}
			return list;
		} catch (Exception e) {
			logger.error("queryByCondition(String)", e); 

			ExceptionUtil.throwCommonException(e.getMessage(),
					ErrorCode.ERROR_CODE_${tableName}_SELECT, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("queryByCondition(String) - end"); 
		}
		return null;
	}

	/**
	 * 更新记录
	 *
	 * @param po
	 * @throws CommonException
	 */
	public void update(${doName} po) throws CommonException {
		if (logger.isDebugEnabled()) {
			logger.debug("update(${doName}) - start"); 
		}

		try {
			this.getHibernateTemplate().update(po);
		} catch (Exception e) {
			logger.error("update(${doName})", e); 

			ExceptionUtil.throwCommonException(e.getMessage(),
					ErrorCode.ERROR_CODE_${tableName}_UPDATE, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("update(${doName}) - end"); 
		}
	}

	/**
	 * 插入记录
	 *
	 * @param po
	 * @throws CommonException
	 */
	public void insert(${doName} po) throws CommonException {
		if (logger.isDebugEnabled()) {
			logger.debug("insert(${doName}) - start"); 
		}

		try {
			this.getHibernateTemplate().save(po);
		} catch (Exception e) {
			logger.error("insert(${doName})", e); 

			ExceptionUtil.throwCommonException(e.getMessage(),
					ErrorCode.ERROR_CODE_${tableName}_INSERT, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("insert(${doName}) - end"); 
		}
	}


	/**
	 * 删除记录
	 *
	 * @param po
	 * @throws CommonException
	 */
	public void delete(${doName} po) throws CommonException {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(${doName}) - start"); 
		}

		try {
			this.getHibernateTemplate().delete(po);
		} catch (Exception e) {
			logger.error("delete(${doName})", e); 

			ExceptionUtil.throwCommonException(e.getMessage(),
					ErrorCode.ERROR_CODE_${tableName}_DELETE, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("delete(${doName}) - end"); 
		}
	}

}
