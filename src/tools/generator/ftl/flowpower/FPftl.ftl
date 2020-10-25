<#import "/templets/commonQuery/CommonQueryTagMacro.ftl" as CommonQueryMacro>

<@CommonQueryMacro.page title="${ftlTitle}">
<table align="left">
    <tr>
        
        <td valign="top" rowspan="1" valign="top">
            <@CommonQueryMacro.CommonQuery id="**数据集ID**" init="true" navigate="false" parameters="action=query" >
                <table align="left">
                    <tr>
                        <td valign="top" valign="top">
         		            <@CommonQueryMacro.DataTable id ="datatable1"  fieldStr="" width="200" />
                        </td>
                    </tr>
                </table>
            </@CommonQueryMacro.CommonQuery>
        </td>

        <td valign="top" rowspan="1" valign="top">
            <@CommonQueryMacro.CommonQuery id="**数据集ID**" init="true" navigate="false" submitMode="all" readOnly="true">
                <table align="left">
                    <tr>
                        <td rowspan="1" valign="top" width="500">
        		            <@CommonQueryMacro.Group id="group1" label="**本组标签名称**" fieldStr="" colNm=4/>
                        </td>
                    </tr>
                </table>
            </@CommonQueryMacro.CommonQuery>
        </td>

    </tr>
</table>
</@CommonQueryMacro.page>










