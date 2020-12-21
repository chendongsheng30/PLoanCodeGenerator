package ${classPackage};

import java.io.Serializable;

<#include "../common/doAnnotation.ftl">
public class ${className} implements Serializable {

    public static String REF = "${doName}";

<#if pkType?exists>
    public static String PROP_${columnPK} = "${pkName}";
</#if>
<#list columnNames as columnName>
    <#assign javaName = javaNameMap[columnName] />
    public static String PROP_${columnName} = "${javaName}";
</#list>

<#if pkType?exists>
    // primary key
    private ${pkType} ${pkName};<#if commentsMap[pkName]?exists>//${commentsMap[pkName]}</#if>
</#if>

<#list columnNames as columnName>
    <#assign javaType = javaTypeMap[columnName] />
    <#assign javaName = javaNameMap[columnName] />
    private ${javaType} ${javaName};<#if commentsMap[columnName]?exists>//${commentsMap[columnName]}</#if>
</#list>

    public ${className} () {
        initialize();
    }

<#if pkType?exists>
    /**
    * Constructor for primary key
    */
    public ${className} (${pkType} ${pkName}) {
        this.${pkName} = ${pkName};
        initialize();
    }
</#if>

    protected void initialize () {

    }

<#if pkType?exists>
    public ${pkType} get${spkName} () {
        return ${pkName};
    }

    public void set${spkName} (${pkType} ${pkName}) {
        this.${pkName} = ${pkName};
    }
</#if>

<#list columnNames as columnName>
    <#assign javaType = javaTypeMap[columnName] />
    <#assign javaName = javaNameMap[columnName] />
    <#assign gsJavaName = gsJavaNameMap[columnName] />
    public ${javaType} get${gsJavaName} () {
        return ${javaName};
    }

    public void set${gsJavaName} (${javaType} ${javaName}) {
        this.${javaName} = ${javaName};
    }

</#list>
    public String toString () {
        return super.toString();
    }

}