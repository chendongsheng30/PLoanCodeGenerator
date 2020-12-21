package ${classPackage};

import ${baseClassPackage}.${baseClassName};

<#include "../common/doAnnotation.ftl">
public class ${className} extends ${baseClassName} {
	
    private static final long serialVersionUID = /***/;

	public ${className} () {
		super();
	}

<#if pkType?exists>
	/**
	 * Constructor for primary key
	 */
	public ${className} (${pkType} ${pkName}) {
		super(${pkName});
	}
</#if>

}




