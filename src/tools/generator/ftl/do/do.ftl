package ${classPackage};

import ${baseClassPackage}e.${baseClassName};

<#include "../common/doAnnotation.ftl">
public class ${className} extends ${baseClassName} {
	
    private static final long serialVersionUID = /***/;

	public ${className} () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
<#assign pkType = javaTypeMap[columnPK] />
<#assign pkName = javaNameMap[columnName] />
	public ${className} (${pkType} ${pkName}) {
		super(pkName);
	}

}




