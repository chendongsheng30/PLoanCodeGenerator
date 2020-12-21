package tools.generator.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import tools.generator.util.GenUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * ����freemarker���ɴ���
 *
 * @author chendongsheng
 * @date 2020-12-19
 */
public class GenOracleService {

    private static String FILE_ENCODE_GBK = "GBK";

    private Properties properties = new Properties();
    private String author;
    private String createDate;
    private String outPutDir;

    private Configuration genConfiguration;
    private Map<String, String> refMap;
    private DataSource dataSource;
    private QueryRunner queryRunner;

    private Map<String, Object> dataModel = new HashMap<String, Object>();
    private int count = 0;

    private GenCodeService genCodeService = new GenCodeService();

    private ArrayList<String> columnNameList = new ArrayList<String>();

    private String columnPK = "";
    // key=columnName, value=nullable
    protected Map<String, Boolean> nullableMap = new LinkedHashMap<String, Boolean>();
    // key=columnName, value=comments
    protected Map<String, String> commentsMap = new LinkedHashMap<String, String>();
    // key=columnName, value=javaName
    protected Map<String, String> javaNameMap = new LinkedHashMap<String, String>();
    // key=columnName, value=javaType
    protected Map<String, String> javaTypeMap = new LinkedHashMap<String, String>();
    // key=columnName, value=JavaName(get/set��������)
    protected Map<String, String> gsJavaNameMap = new LinkedHashMap<String, String>();
    // key=columnName, value=dataLength
    protected Map<String, String> dataLengthMap = new LinkedHashMap<String, String>();

    /**
     * �вι��췽��
     *
     * @param properties
     */
    public GenOracleService(Properties properties) throws Exception {
        this.properties = properties;
        initDataSource();
    }

    /**
     * ��ʼ�����ݿ�����
     *
     * @author chendongsheng
     */
    private void initDataSource() throws Exception {

        OracleDataSource ods = new OracleDataSource();
        ods.setURL(this.properties.getProperty("oracle.url"));
        ods.setUser(this.properties.getProperty("oracle.username"));
        ods.setPassword(this.properties.getProperty("oracle.password"));
        this.dataSource = ods;
        this.queryRunner = new QueryRunner(this.dataSource);

    }

    /**
     * �������ݿ����ģ���ļ�
     *
     * @param genConfiguration
     * @param createDate
     * @author chendongsheng
     */
    public int genOraclFiles(Configuration genConfiguration, String createDate) throws Exception {

        this.genConfiguration = genConfiguration;
        this.createDate = createDate;
        this.author = this.properties.getProperty("author");
        this.outPutDir = this.properties.getProperty("output.dir");

        this.refMap = getRef();

        // ��ȡҪ���ɵı�����
        String tableNames = this.properties.getProperty("tables.name");
        if (GenUtils.isBlank(tableNames)) {
            return 0;
        }

        String[] tables = tableNames.split("\\|");
        for (int i = 0; i < tables.length; i++) {
            String tableName = tables[i].toUpperCase();

            // ��ʼ�����ݿ��ֶ�תʵ�����ӳ������
            initClumnJavaInfo(tableName);

            // ����ʵ���ࣨdo���ļ�
            this.genDBFile("do", tableName);

            // ����ʵ���ࣨBaseDo���ļ�
            this.genDBFile("baseDo", tableName);

            // ����hbmӳ���ļ�
            this.genDBFile("hbm", tableName);

            // �������ݳ־ò�DAO�ļ�
            this.genDBFile("dao", tableName);

            // �������б������ݼ�������ÿ������һ�����������ݼ�
            this.columnPK = "";
            this.columnNameList.clear();
            this.nullableMap.clear();
            this.commentsMap.clear();
            this.javaNameMap.clear();
            this.javaTypeMap.clear();
            this.gsJavaNameMap.clear();
            this.dataLengthMap.clear();
        }

        return this.count;
    }

    /**
     * ����ģ���ļ�
     *
     * @param fileType  �ļ�����
     * @param tableName ����
     * @author chendongsheng
     */
    private void genDBFile(String fileType, String tableName) {
        //��ȡ�淶��Java����
        String className = GenUtils.TableNameToPojoName(tableName);
        Template template = null;
        String baseClassName = "Base" + className;
        if ("do".equals(fileType)) {
            try {
                // ��ȡģ��
                template = genConfiguration.getTemplate("do/do.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // ����ģ������
                dataModel.put("tableName", tableName);
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.do.package"));
                dataModel.put("baseClassPackage", this.properties.getProperty("output.base.do.package"));
                dataModel.put("className", className);
                dataModel.put("baseClassName", baseClassName);
                if (!GenUtils.isBlank(this.columnPK)) {
                    dataModel.put("pkType", this.javaTypeMap.get(this.columnPK));
                    dataModel.put("pkName", this.javaNameMap.get(this.columnPK));
                }
                dataModel.put("javaTypeMap", this.javaTypeMap);
                dataModel.put("javaNameMap", this.javaNameMap);
            }
        } else if ("baseDo".equals(fileType)) {
            String doName = className;
            className = baseClassName;
            try {
                // ��ȡģ��
                template = genConfiguration.getTemplate("do/baseDo.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // ����ģ������
                dataModel.put("tableName", tableName);
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.base.do.package"));
                dataModel.put("className", className);
                dataModel.put("doName", doName);
                dataModel.put("columnPK", this.columnPK);
                if (!GenUtils.isBlank(this.columnPK)) {
                    dataModel.put("columnPK", this.columnPK);
                    dataModel.put("pkType", this.javaTypeMap.get(this.columnPK));
                    dataModel.put("pkName", this.javaNameMap.get(this.columnPK));
                    dataModel.put("spkName", GenUtils.UpperFirstKeepOthers(this.javaNameMap.get(this.columnPK)));
                    //�������ֶδ��ֶμ������Ƴ�����ֹģ�����ʱ�ظ�
                    this.columnNameList.remove(this.columnPK);
                }
                dataModel.put("columnNames", this.columnNameList);
                dataModel.put("javaTypeMap", this.javaTypeMap);
                dataModel.put("javaNameMap", this.javaNameMap);
                dataModel.put("gsJavaNameMap", this.gsJavaNameMap);
                dataModel.put("commentsMap", this.commentsMap);
            }
        } else if ("hbm".equals(fileType)) {
            try {
                // ��ȡģ��
                template = genConfiguration.getTemplate("hibernate/hbm.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // ����ģ������
                dataModel.put("tableName", tableName);
                dataModel.put("classPackage", this.properties.getProperty("output.do.package"));
                dataModel.put("className", className);
                if (!GenUtils.isBlank(this.columnPK)) {
                    dataModel.put("columnPK", this.columnPK);
                    dataModel.put("pkType", this.javaTypeMap.get(this.columnPK));
                    dataModel.put("pkName", this.javaNameMap.get(this.columnPK));
                    //�������ֶδ��ֶμ������Ƴ�����ֹģ�����ʱ�ظ�
                    this.columnNameList.remove(this.columnPK);
                }
                dataModel.put("columnNames", this.columnNameList);
                dataModel.put("javaTypeMap", this.javaTypeMap);
                dataModel.put("javaNameMap", this.javaNameMap);
                dataModel.put("nullableMap", this.nullableMap);
                dataModel.put("dataLengthMap", this.dataLengthMap);

            }
        } else if ("dao".equals(fileType)) {
            String doName = className;
            className = className + "DAO";
            try {
                // ��ȡģ��
                template = genConfiguration.getTemplate("hibernate/dao.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // ����ģ������
                dataModel.put("classComment", "���ݿ��" + tableName + ")������");
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.dao.package"));
                dataModel.put("doClassPackage", this.properties.getProperty("output.do.package"));
                dataModel.put("className", className);
                dataModel.put("doName", doName);
                dataModel.put("sdoName", GenUtils.LowerFirstKeepOthers(doName));
                dataModel.put("tableName", tableName);

            }
        } else {
            System.out.println("��֧�����ɡ�" + fileType + "�����͵��ļ�");
            return;
        }

        // д���ļ�
        String suffix = "hbm".equals(fileType) ? ".hbm" : ".java";
        className = "hbm".equals(fileType) ? GenUtils.LowerFirstKeepOthers(className) : className;
        genCodeService.writerFile(template, this.dataModel, (outPutDir + className + suffix));

        // ��¼�����ļ�����
        count++;
        // ���������ļ�����һ�����ݼ�dataModel������ÿ����һ���ļ���������ݼ�
        this.dataModel.clear();

        System.out.println("�ɹ������ļ�:" + className + suffix);

    }

    /**
     * ��ȡ���ݿ��ֶ����ͺ�Java�������͵�ӳ���ϵ����
     *
     * @author chendongsheng
     */
    protected Map<String, String> getRef() {
        String ref = this.properties.getProperty("oracle.ref");
        Map<String, String> refMap = new HashMap<String, String>();

        String[] refArray = ref.split("\\|");
        String[] kv = null;
        for (String r : refArray) {
            kv = r.split("-");
            refMap.put(kv[0].toUpperCase(), kv[1]);
        }
        return refMap;
    }

    /**
     * ��ʼ�����ݿ��ֶ�תʵ�����ӳ������
     *
     * @param tableName
     * @author chendongsheng
     */
    private void initClumnJavaInfo(String tableName) throws SQLException {

        // ��ʼ���ֶ������Ϣ
        String sql = "select a.column_name COLUMN_NAME, a.data_type DATA_TYPE, a.data_length DATA_LENGTH, "
                + "a.data_precision DATA_PRECISION, a.DATA_SCALE DATA_SCALE, a.nullable NULLABLE, "
                + "a.data_default DATA_DEFAULT, b.comments COMMENTS "
                + "from user_tab_columns a, user_col_comments b "
                + "where a.table_name = b.table_name "
                + "and a.column_name = b.column_name "
                + "and a.table_name = '" + tableName + "' "
                + "order by a.column_id asc ";
        List<Map<String, Object>> dataList = queryRunner.query(sql, new MapListHandler());

        String columnName = null;
        String javaName = null;
        String gsJavaName = null;
        String dataType = null;
        String nullableStr = null;
        boolean nullable = false;
        String comments = null;
        String dataLength = null;
        if (dataList != null && dataList.size() > 0) {
            for (Map<String, Object> columnInfo : dataList) {
                // ���ݿ��ֶ�����
                columnName = columnInfo.get("COLUMN_NAME").toString();
                this.columnNameList.add(columnName);

                // ���ݿ��ֶζ�ӦJavaBean��������
                javaName = GenUtils.ColumnNameToJavaPropertyName(columnName);
                this.javaNameMap.put(columnName, javaName);

                // ���ݿ��ֶζ�ӦJavaBean������������
                dataType = columnInfo.get("DATA_TYPE").toString();
                this.javaTypeMap.put(columnName, refMap.get(dataType));

                // ���ݿ��ֶ��Ƿ��Ϊ�� 
                nullableStr = columnInfo.get("NULLABLE").toString();
                // ���ݿ���nullable��hbm�ļ���not-null
                if (nullableStr.equals("N")) {
                    nullable = true;
                } else {
                    nullable = false;
                }
                this.nullableMap.put(columnName, nullable);

                // ���ݿ��ֶ�ע��
                if (columnInfo.get("COMMENTS") != null) {
                    comments = columnInfo.get("COMMENTS").toString();
                    this.commentsMap.put(columnName, comments);
                }

                //get��set������������
                gsJavaName = GenUtils.UpperFirstKeepOthers(javaName);
                this.gsJavaNameMap.put(columnName, gsJavaName);

                //���ݳ���
                dataLength = columnInfo.get("DATA_LENGTH").toString();
                this.dataLengthMap.put(columnName, dataLength);
            }
        }

        // ��ʼ��������Ϣ
        sql = "select col.column_name COLUMN_NAME "
                + "from user_constraints con,user_cons_columns col "
                + "where con.constraint_name = col.constraint_name "
                + "and con.constraint_type = 'P' "
                + "and col.table_name = '" + tableName + "'";
        List<Map<String, Object>> pkList = queryRunner.query(sql, new MapListHandler());

        if (pkList != null && pkList.size() > 0) {
            for (Map<String, Object> pk : pkList) {
                // ���ݿ��ֶ�����
                this.columnPK = pk.get("COLUMN_NAME").toString();
            }
        }
    }

}
