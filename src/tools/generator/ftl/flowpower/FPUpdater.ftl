package ${classPackage};

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huateng.common.err.Module;
import com.huateng.common.err.Rescode;
import com.huateng.commquery.result.MultiUpdateResultBean;
import com.huateng.commquery.result.UpdateResultBean;
import com.huateng.commquery.result.UpdateReturnBean;
import com.huateng.ebank.framework.operation.OPCaller;
import com.huateng.ebank.framework.operation.OperationContext;
import com.huateng.ebank.framework.web.commQuery.BaseUpdate;
import com.huateng.exception.AppException;

<#include "../common/annotation.ftl">
public class ${className} extends BaseUpdate {

    public UpdateReturnBean saveOrUpdate(MultiUpdateResultBean multiUpdateResultBean,
            HttpServletRequest request, HttpServletResponse response) throws AppException {
        
        try {
            UpdateReturnBean updateReturnBean = new UpdateReturnBean();

            UpdateResultBean updateResultBean = multiUpdateResultBean.getUpdateResultBeanByID(**数据集ID**);
            
            **此处增加updateResultBean转换成Object的逻辑**

            OperationContext oc = new OperationContext();
            oc.setAttribute(**Operation**.IN_LIST, **Object**);
            OPCaller.call(**OP类ID**, oc);

            return updateReturnBean;
        } catch (AppException appEx) {
            throw appEx;
        } catch (Exception ex) {
            throw new AppException(Module.SYSTEM_MODULE,
                    Rescode.DEFAULT_RESCODE, ex.getMessage(), ex);
        }
    }

}