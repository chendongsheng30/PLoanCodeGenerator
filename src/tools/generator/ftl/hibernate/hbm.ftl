<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd" >

<hibernate-mapping package="${classPackage}">
    <class name="${className}" table="${tableName}" lazy="false">
        
        <meta attribute="sync-DAO">false</meta>

        <#if pkType?exists>
        <id name="${pkName}" column="${columnPK}" type="${pkType}">
            <generator class="sequence">
                <param name="sequence">SEQ_${tableName}</param>
            </generator>
        </id>
        </#if>

        <#list columnNames as columnName>
            <#assign javaType = javaTypeMap[columnName] />
            <#assign javaName = javaNameMap[columnName] />
            <#assign nullable = nullableMap[columnName] />
            <#assign dataLength = dataLengthMap[columnName] />
        <property name="${javaName}" column="${columnName}" type="${javaType}" not-null="${nullable?c}" length="${dataLength}" unique="false" optimistic-lock="true" lazy="false" generated="never"/>
        </#list>

    </class>
</hibernate-mapping>