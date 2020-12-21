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
 * 利用freemarker生成代码
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
    // key=columnName, value=JavaName(get/set方法名中)
    protected Map<String, String> gsJavaNameMap = new LinkedHashMap<String, String>();
    // key=columnName, value=dataLength
    protected Map<String, String> dataLengthMap = new LinkedHashMap<String, String>();

    /**
     * 有参构造方法
     *
     * @param properties
     */
    public GenOracleService(Properties properties) throws Exception {
        this.properties = properties;
        initDataSource();
    }

    /**
     * 初始化数据库连接
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
     * 生成数据库相关模板文件
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

        // 获取要生成的表数组
        String tableNames = this.properties.getProperty("tables.name");
        if (GenUtils.isBlank(tableNames)) {
            return 0;
        }

        String[] tables = tableNames.split("\\|");
        for (int i = 0; i < tables.length; i++) {
            String tableName = tables[i].toUpperCase();

            // 初始化数据库字段转实体类的映射数据
            initClumnJavaInfo(tableName);

            // 生成实体类（do）文件
            this.genDBFile("do", tableName);

            // 生成实体类（BaseDo）文件
            this.genDBFile("baseDo", tableName);

            // 生成hbm映射文件
            this.genDBFile("hbm", tableName);

            // 生成数据持久层DAO文件
            this.genDBFile("dao", tableName);

            // 由于所有表共用数据集，所以每处理完一个表后清空数据集
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
     * 生成模板文件
     *
     * @param fileType  文件类型
     * @param tableName 表名
     * @author chendongsheng
     */
    private void genDBFile(String fileType, String tableName) {
        //获取规范的Java类名
        String className = GenUtils.TableNameToPojoName(tableName);
        Template template = null;
        String baseClassName = "Base" + className;
        if ("do".equals(fileType)) {
            try {
                // 获取模板
                template = genConfiguration.getTemplate("do/do.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // 插入模板数据
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
                // 获取模板
                template = genConfiguration.getTemplate("do/baseDo.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // 插入模板数据
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
                    //将主键字段从字段集合中移除，防止模板填充时重复
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
                // 获取模板
                template = genConfiguration.getTemplate("hibernate/hbm.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // 插入模板数据
                dataModel.put("tableName", tableName);
                dataModel.put("classPackage", this.properties.getProperty("output.do.package"));
                dataModel.put("className", className);
                if (!GenUtils.isBlank(this.columnPK)) {
                    dataModel.put("columnPK", this.columnPK);
                    dataModel.put("pkType", this.javaTypeMap.get(this.columnPK));
                    dataModel.put("pkName", this.javaNameMap.get(this.columnPK));
                    //将主键字段从字段集合中移除，防止模板填充时重复
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
                // 获取模板
                template = genConfiguration.getTemplate("hibernate/dao.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // 插入模板数据
                dataModel.put("classComment", "数据库表（" + tableName + ")访问类");
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
            System.out.println("不支持生成【" + fileType + "】类型的文件");
            return;
        }

        // 写入文件
        String suffix = "hbm".equals(fileType) ? ".hbm" : ".java";
        className = "hbm".equals(fileType) ? GenUtils.LowerFirstKeepOthers(className) : className;
        genCodeService.writerFile(template, this.dataModel, (outPutDir + className + suffix));

        // 记录生成文件个数
        count++;
        // 由于所有文件共用一个数据集dataModel，所以每生成一个文件后清空数据集
        this.dataModel.clear();

        System.out.println("成功生成文件:" + className + suffix);

    }

    /**
     * 获取数据库字段类型和Java数据类型的映射关系集合
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
     * 初始化数据库字段转实体类的映射数据
     *
     * @param tableName
     * @author chendongsheng
     */
    private void initClumnJavaInfo(String tableName) throws SQLException {

        // 初始化字段相关信息
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
                // 数据库字段名称
                columnName = columnInfo.get("COLUMN_NAME").toString();
                this.columnNameList.add(columnName);

                // 数据库字段对应JavaBean属性名称
                javaName = GenUtils.ColumnNameToJavaPropertyName(columnName);
                this.javaNameMap.put(columnName, javaName);

                // 数据库字段对应JavaBean属性数据类型
                dataType = columnInfo.get("DATA_TYPE").toString();
                this.javaTypeMap.put(columnName, refMap.get(dataType));

                // 数据库字段是否可为空 
                nullableStr = columnInfo.get("NULLABLE").toString();
                // 数据库是nullable，hbm文件是not-null
                if (nullableStr.equals("N")) {
                    nullable = true;
                } else {
                    nullable = false;
                }
                this.nullableMap.put(columnName, nullable);

                // 数据库字段注释
                if (columnInfo.get("COMMENTS") != null) {
                    comments = columnInfo.get("COMMENTS").toString();
                    this.commentsMap.put(columnName, comments);
                }

                //get、set方法名中名称
                gsJavaName = GenUtils.UpperFirstKeepOthers(javaName);
                this.gsJavaNameMap.put(columnName, gsJavaName);

                //数据长度
                dataLength = columnInfo.get("DATA_LENGTH").toString();
                this.dataLengthMap.put(columnName, dataLength);
            }
        }

        // 初始化主键信息
        sql = "select col.column_name COLUMN_NAME "
                + "from user_constraints con,user_cons_columns col "
                + "where con.constraint_name = col.constraint_name "
                + "and con.constraint_type = 'P' "
                + "and col.table_name = '" + tableName + "'";
        List<Map<String, Object>> pkList = queryRunner.query(sql, new MapListHandler());

        if (pkList != null && pkList.size() > 0) {
            for (Map<String, Object> pk : pkList) {
                // 数据库字段名称
                this.columnPK = pk.get("COLUMN_NAME").toString();
            }
        }
    }

}
