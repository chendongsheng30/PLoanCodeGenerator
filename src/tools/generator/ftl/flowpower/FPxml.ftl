<?xml version="1.0" encoding="UTF-8"?>
<!--  ����ֱ������SQL��䣬֧��ͬ�����첽�ύ -->
<!--  title:����
	  navigate:������Ϣ
	  type:��ʽ��sql|call)
	  interface:�Ƿ���ʾ��������ҳ��(true|false)
	  pagesize:ҳ����ʾ��¼��(ȱʡΪ10)
	  async:�첽�ύ��־
	  databusid:�������߱��
	  transdataactionurl:��������URL
-->
<CommQry
        title="${xmlTitle}"
        navigate="**һ��Ŀ¼** &gt; **����Ŀ¼** &gt; ${xmlTitle}"
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
        
        <!-- status :F-������Ϣ����ʾ N-����ʾ D-��ϸ��Ϣ����ʾ A-��������ϸ��Ϣ�ж���ʾ  -->
        <!-- primary : true-��Ϊ�����¼�ϴ���false-����Ϊ�����ϴ� -->
        <#--<Field id="tlrno" desc="����Ա��" status="N" method="None" readonly="true" primary="true" type="String"
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

