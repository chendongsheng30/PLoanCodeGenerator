package ${classPackage};

import com.huateng.common.err.Module;
import com.huateng.common.err.Rescode;
import com.huateng.commquery.result.Result;
import com.huateng.commquery.result.ResultMng;
import com.huateng.ebank.business.common.PageQueryResult;
import com.huateng.ebank.framework.operation.OperationContext;
import com.huateng.ebank.framework.web.commQuery.BaseGetter;
import com.huateng.exception.AppException;

<#include "../common/annotation.ftl">
public class ${className} extends BaseGetter {

	public Result call() throws AppException {

        try {
            BeanUtilsEx(**Object**, getCommQueryServletRequest().getParameterMap());
            PageQueryResult pageResult = new PageQueryResult();
            
            **���ַ�ʽ��ȡ���ݣ�1.����OP�ࣻ2.���ñ����getData()�������˴��������ѡ������һ�ּ��ɣ�**
            // ����OP��
            OperationContext oc = new OperationContext();
			oc.setAttribute(**Operation**.IN_LIST, **Object**);
			OPCaller.call(**OP��ID**, oc);

            // ����getData()����
            //pageResult = getData(**Object**);

            ResultMng.fillResultByList(
                    getCommonQueryBean(),
                    getCommQueryServletRequest(),
                    pageResult.getQueryResult(),
                    getResult());
            result.setContent(pageResult.getQueryResult());
            if (pageResult.getQueryResult().size() == 0) {
                result.getPage().setTotalPage(0);
            } else {
                result.getPage().setTotalPage(1);
            }

            result.init();
            return result;

        } catch (AppException appEx) {
            throw appEx;
        } catch (Exception ex) {
            throw new AppException(Module.SYSTEM_MODULE,
                    Rescode.DEFAULT_RESCODE, ex.getMessage(), ex);
        }

    }

    // ��ȡ����
    protected PageQueryResult getData(**Object** object) throws Exception {

        PageQueryResult pageQueryResult = new PageQueryResult();

        **���ݻ�ȡ�߼�����**

        pageQueryResult.setTotalCount(**ObjectList.size()**);
        pageQueryResult.setQueryResult(**ObjectList**);

        return pageQueryResult;
    }

}



















