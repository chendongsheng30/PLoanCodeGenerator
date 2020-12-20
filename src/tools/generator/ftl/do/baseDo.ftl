package ${classPackage};

import java.io.Serializable;

<#include "../common/doAnnotation.ftl">
public abstract class ${className} implements Serializable {

	public static String REF = "${className}";

<#list columnNameList as columnName>
    <#assign javaName = javaNameMap[columnName] />
	public static String PROP_${columnName} = "${javaName}";
</#list>

	public ${className} () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
<#assign pkType = javaTypeMap[columnPK] />
<#assign pkName = javaNameMap[columnName] />
<#assign spkName = gsJavaNameMap[columnPK] />
	public ${className} (${pkType} ${pkName}) {
		this.set${spkName}(${pkName});
		initialize();
	}

	protected void initialize () {

    }

	// primary key
<#list columnNames as columnName>
    <#assign javaType = javaTypeMap[columnName] />
    <#assign javaName = javaNameMap[columnName] />
	private ${javaType} ${javaName};<#if commentsMap[columnName]?exists>//${columnCommentsMap[columnName]}</#if>
</#list>

<#list columnNames as columnName>
    <#assign javaType = javaTypeMap[columnName] />
    <#assign javaName = javaNameMap[columnName] />
    <#assign gsJavaName = gsJavaNameMap[columnName] />
	public ${javaType} get${gsJavaName} () {
		return ${javaName};
	}

	public void set${gsJavaName} (${javaType} ${javaName}) {
		this.javaName = javaName;
	}

</#list>
	public String toString () {
		return super.toString();
	}

}