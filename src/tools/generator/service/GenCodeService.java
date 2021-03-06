package tools.generator.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import tools.generator.util.GenUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 利用freemarker生成代码
 *
 * @author chendongsheng
 * @date 2020-11-22
 */
public class GenCodeService {

    private static String FILE_ENCODE_GBK = "GBK";

    private Properties properties = new Properties();
    private String author;
    private String createDate;
    private Configuration genConfiguration;
    private String outPutDir;
    private Map<String, Object> dataModel = new HashMap<String, Object>();
    private int count = 0;

    /**
     * 生成模板文件
     *
     * @author chendongsheng
     */
    public void genFile() throws Exception {

        // 校验配置参数并初始化
        this.checkAndInit();

        // 生成xml文件
        String xmlName = this.properties.getProperty("output.xml.name");
        if (!GenUtils.isBlank(xmlName)) {
            this.genCommonFile("xml", xmlName);
        }
        // 生成Getter文件
        String getterName = this.properties.getProperty("output.getter.name");
        if (!GenUtils.isBlank(getterName)) {
            this.genCommonFile("getter", getterName);
        }
        // 生成GetterOperation文件
        String getterOpName = this.properties.getProperty("output.getter.operation.name");
        if (!GenUtils.isBlank(getterOpName)) {
            this.genCommonFile("getterOp", getterOpName);
        }
        // 生成Updater文件
        String updaterName = this.properties.getProperty("output.updater.name");
        if (!GenUtils.isBlank(updaterName)) {
            this.genCommonFile("updater", updaterName);
        }
        // 生成UpdaterOperation文件
        String updaterOpName = this.properties.getProperty("output.updater.operation.name");
        if (!GenUtils.isBlank(updaterOpName)) {
            this.genCommonFile("updaterOp", updaterOpName);
        }

        // 生成数据库相关的文件
        GenOracleService genOracleService = new GenOracleService(this.properties);
        int dbCount = genOracleService.genOraclFiles(this.genConfiguration, createDate);

        System.out.println("总共生成" + (count + dbCount) + "个文件！");

    }

    /**
     * 校验配置参数并初始化
     *
     * @author chendongsheng
     */
    private void checkAndInit() throws Exception {

        String defaultConfig = "config.properties";
        InputStream configInputStream = ClassLoader.getSystemResourceAsStream(defaultConfig);
        if (configInputStream == null) {
            try {
                configInputStream = new FileInputStream(defaultConfig);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            this.properties.load(configInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 验证传入参数完整性
        this.checkParams(this.properties);

        // 创建Configuration对象指定Freemarker版本
        this.genConfiguration = new Configuration(Configuration.VERSION_2_3_23);
        // 设置模版路径
        this.genConfiguration.setClassForTemplateLoading(this.getClass(), "/tools/generator/ftl");
        // 指定生成文件的编码为GBK
        this.genConfiguration.setDefaultEncoding(FILE_ENCODE_GBK);

        // 文件创建者
        this.author = this.properties.getProperty("author");
        if (GenUtils.isBlank(author)) {
            this.author = "CodeGenerator";
        }
        // 文件创建日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.createDate = df.format(Calendar.getInstance().getTime());

    }

    /**
     * 生成模板文件
     *
     * @param fileType 文件类型
     * @param fileName 文件名称
     * @author chendongsheng
     */
    private void genCommonFile(String fileType, String fileName) {

        Template template = null;

        if ("xml".equals(fileType)) {
            try {
                // 获取模板
                template = genConfiguration.getTemplate("flowpower/FPxml.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // 插入模板数据
                dataModel.put("xmlTitle", this.properties.getProperty("output.xml.title"));
                String getterPackag = this.properties.getProperty("output.getter.package");
                String getterName = this.properties.getProperty("output.getter.name");
                String getterClassName = getterPackag + GenUtils.getFileNameWithoutSuffix(getterName);
                dataModel.put("getterClassName", getterClassName);
                String updaterPackag = this.properties.getProperty("output.updater.package");
                String updaterName = this.properties.getProperty("output.updater.name");
                String updaterClassName = updaterPackag + GenUtils.getFileNameWithoutSuffix(updaterName);
                dataModel.put("updaterClassName", updaterClassName);

            }
        } else if ("getter".equals(fileType)) {
            try {
                // 获取模板
                template = genConfiguration.getTemplate("flowpower/FPGetter.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // 插入模板数据
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.getter.package"));
                dataModel.put("className", GenUtils.getFileNameWithoutSuffix(fileName));
                dataModel.put("classComment", this.properties.getProperty("output.class.getter.comment"));

            }
        } else if ("getterOp".equals(fileType)) {
            try {
                // 获取模板
                template = genConfiguration.getTemplate("flowpower/FPGetterOpration.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // 插入模板数据
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.getter.operation.package"));
                dataModel.put("className", GenUtils.getFileNameWithoutSuffix(fileName));
                dataModel.put("classComment", this.properties.getProperty("output.class.getter.operation.comment"));

            }
        } else if ("updater".equals(fileType)) {
            try {
                // 获取模板
                template = genConfiguration.getTemplate("flowpower/FPUpdater.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // 插入模板数据
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.updater.package"));
                dataModel.put("className", GenUtils.getFileNameWithoutSuffix(fileName));
                dataModel.put("classComment", this.properties.getProperty("output.class.updater.comment"));

            }
        } else if ("updaterOp".equals(fileType)) {
            try {
                // 获取模板
                template = genConfiguration.getTemplate("flowpower/FPUpdaterOpration.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // 插入模板数据
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.updater.operation.package"));
                dataModel.put("className", GenUtils.getFileNameWithoutSuffix(fileName));
                dataModel.put("classComment", this.properties.getProperty("output.class.updater.operation.comment"));

            }
        } else {
            System.out.println("不支持生成【" + fileType + "】类型的文件");
            return;
        }

        // 写入文件
        this.writerFile(template, dataModel, outPutDir + fileName);

        // 记录生成文件个数
        count++;
        // 由于所有文件共用一个数据集dataModel，所以每生成一个文件后清空数据集
        this.dataModel.clear();

        System.out.println("成功生成文件:" + fileName);

    }

    /**
     * 写入文件
     *
     * @param template
     * @param dataModel
     * @param file
     * @author chendongsheng
     */
    public void writerFile(Template template, Map<String, Object> dataModel, String file) {

        Writer writer = null;
        try {
            // 创建一个文件编写对象Writer
            writer = new OutputStreamWriter(new FileOutputStream(file), FILE_ENCODE_GBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            // 使用模板和数据输出到指定路径
            template.process(dataModel, writer);
            writer.flush();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 参数验证
     *
     * @param properties 配置参数
     * @author chendongsheng
     */
    private void checkParams(Properties properties) throws Exception {

        this.outPutDir = this.properties.getProperty("output.dir");
        if (GenUtils.isBlank(this.outPutDir)) {
            System.out.println("------未生成文件，请先配置文件在本地的输出路径！");
            return;
        }
        if (!this.outPutDir.endsWith(File.separator)) {
            this.outPutDir += File.separator;
        }
        this.properties.setProperty("output.dir", this.outPutDir);
        // 检查输出文件夹是否已存在
        GenUtils.checkDir(this.outPutDir);

        String fileName = "";
        String filePath = "";

        fileName = this.properties.getProperty("output.getter.name");
        filePath = this.properties.getProperty("output.getter.package");
        if (!GenUtils.isBlank(fileName) && GenUtils.isBlank(filePath)) {
            throw new Exception("请配置Getter文件的包路径！");
        }

        fileName = this.properties.getProperty("output.getter.operation.name");
        filePath = this.properties.getProperty("output.getter.operation.package");
        if (!GenUtils.isBlank(fileName) && GenUtils.isBlank(filePath)) {
            throw new Exception("请配置GetterOperation文件的包路径！");
        }

        fileName = this.properties.getProperty("output.updater.name");
        filePath = this.properties.getProperty("output.updater.package");
        if (!GenUtils.isBlank(fileName) && GenUtils.isBlank(filePath)) {
            throw new Exception("请配置Updater文件的包路径！");
        }

        fileName = this.properties.getProperty("output.updater.operation.name");
        filePath = this.properties.getProperty("output.updater.operation.package");
        if (!GenUtils.isBlank(fileName) && GenUtils.isBlank(filePath)) {
            throw new Exception("请配置UpdaterOperation文件的包路径！");
        }

        String tablesName = this.properties.getProperty("tables.name");
        if (!GenUtils.isBlank(tablesName)) {
            String DOPath = this.properties.getProperty("output.do.package");
            String baseDOPath = this.properties.getProperty("output.base.do.package");
            String DAOPath = this.properties.getProperty("output.dao.package");
            String oracleUrl = this.properties.getProperty("oracle.url");
            String oracleUsername = this.properties.getProperty("oracle.username");
            String oraclePassword = this.properties.getProperty("oracle.password");
            String oracleRef = this.properties.getProperty("oracle.ref");

            if (GenUtils.isBlank(DOPath) || GenUtils.isBlank(baseDOPath)) {
                throw new Exception("请配置实体类文件的包路径！");
            } else if (GenUtils.isBlank(DAOPath)) {
                throw new Exception("请配置DAO类文件的包路径！");
            } else if (GenUtils.isBlank(oracleUrl) || GenUtils.isBlank(oracleUsername)
                    || GenUtils.isBlank(oraclePassword) || GenUtils.isBlank(oracleRef)) {
                throw new Exception("Oracle数据库信息配置不完整！");
            }

        }

    }

}
