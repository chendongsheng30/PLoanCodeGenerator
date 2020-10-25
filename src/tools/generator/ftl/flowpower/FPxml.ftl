<?xml version="1.0" encoding="UTF-8"?>
<!--  用于直接生成SQL语句，支持同步和异步提交 -->
<!--  title:标题
	  navigate:导航信息
	  type:方式（sql|call)
	  interface:是否显示输入条件页面(true|false)
	  pagesize:页面显示记录数(缺省为10)
	  async:异步提交标志
	  databusid:数据总线编号
	  transdataactionurl:数据整理URL
-->
<CommQry
        title="${xmlTitle}"
        navigate="**一级目录** &gt; **二级目录** &gt; ${xmlTitle}"
        type="call"
        interface="false"
        pagesize="10"
        async="true"
        databusid="DEMO"
        transdataactionurl="/trans/TransDataAction.do"
        getterclassname="${getterClassName}"
>
    <Include id="BankParam"/>

    <Fields>
        
        <!-- status :F-基本信息中显示 N-不显示 D-详细信息中显示 A-基本、详细信息中都显示  -->
        <!-- primary : true-作为表单项记录上传　false-不作为表单项上传 -->
        <#--<Field id="tlrno" desc="操作员号" status="N" method="None" readonly="true" primary="true" type="String"
               size="20" xpath="/tlrno" default="false"/>-->

    </Fields>

    <Where>

    </Where>

    <Operations>
        updaterclassname="${updaterClassName}"
    </Operations>

    <Sql>

    </Sql>

</CommQry>

