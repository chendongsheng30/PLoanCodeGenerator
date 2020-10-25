package ${classPackage};

import com.huateng.ebank.framework.exceptions.CommonException;
import com.huateng.ebank.framework.operation.BaseOperation;
import com.huateng.ebank.framework.operation.OperationContext;

<#include "../common/annotation.ftl">
public class ${className} extends BaseOperation{

    //public final static String Object = **Object**;

    public void afterProc(OperationContext context) throws CommonException {

    }

    public void execute(OperationContext context) throws CommonException {

        try {

            **²¹³äÂß¼­´úÂë**

        } catch (Exception e) {
            ExceptionUtil.throwCommonException(e.getLocalizedMessage(), 
                ErrorCode.ERROR_CODE_ACCUM_FUND_INFO_SELECT, e);
        }
    }

    public void beforeProc(OperationContext context) throws CommonException {

    }

}

