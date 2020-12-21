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
 * ����freemarker���ɴ���
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
     * ����ģ���ļ�
     *
     * @author chendongsheng
     */
    public void genFile() throws Exception {

        // У�����ò�������ʼ��
        this.checkAndInit();

        // ����xml�ļ�
        String xmlName = this.properties.getProperty("output.xml.name");
        if (!GenUtils.isBlank(xmlName)) {
            this.genCommonFile("xml", xmlName);
        }
        // ����Getter�ļ�
        String getterName = this.properties.getProperty("output.getter.name");
        if (!GenUtils.isBlank(getterName)) {
            this.genCommonFile("getter", getterName);
        }
        // ����GetterOperation�ļ�
        String getterOpName = this.properties.getProperty("output.getter.operation.name");
        if (!GenUtils.isBlank(getterOpName)) {
            this.genCommonFile("getterOp", getterOpName);
        }
        // ����Updater�ļ�
        String updaterName = this.properties.getProperty("output.updater.name");
        if (!GenUtils.isBlank(updaterName)) {
            this.genCommonFile("updater", updaterName);
        }
        // ����UpdaterOperation�ļ�
        String updaterOpName = this.properties.getProperty("output.updater.operation.name");
        if (!GenUtils.isBlank(updaterOpName)) {
            this.genCommonFile("updaterOp", updaterOpName);
        }

        // �������ݿ���ص��ļ�
        GenOracleService genOracleService = new GenOracleService(this.properties);
        int dbCount = genOracleService.genOraclFiles(this.genConfiguration, createDate);

        System.out.println("�ܹ�����" + (count + dbCount) + "���ļ���");

    }

    /**
     * У�����ò�������ʼ��
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

        // ��֤�������������
        this.checkParams(this.properties);

        // ����Configuration����ָ��Freemarker�汾
        this.genConfiguration = new Configuration(Configuration.VERSION_2_3_23);
        // ����ģ��·��
        this.genConfiguration.setClassForTemplateLoading(this.getClass(), "/tools/generator/ftl");
        // ָ�������ļ��ı���ΪGBK
        this.genConfiguration.setDefaultEncoding(FILE_ENCODE_GBK);

        // �ļ�������
        this.author = this.properties.getProperty("author");
        if (GenUtils.isBlank(author)) {
            this.author = "CodeGenerator";
        }
        // �ļ���������
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.createDate = df.format(Calendar.getInstance().getTime());

    }

    /**
     * ����ģ���ļ�
     *
     * @param fileType �ļ�����
     * @param fileName �ļ�����
     * @author chendongsheng
     */
    private void genCommonFile(String fileType, String fileName) {

        Template template = null;

        if ("xml".equals(fileType)) {
            try {
                // ��ȡģ��
                template = genConfiguration.getTemplate("flowpower/FPxml.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // ����ģ������
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
                // ��ȡģ��
                template = genConfiguration.getTemplate("flowpower/FPGetter.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // ����ģ������
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.getter.package"));
                dataModel.put("className", GenUtils.getFileNameWithoutSuffix(fileName));
                dataModel.put("classComment", this.properties.getProperty("output.class.getter.comment"));

            }
        } else if ("getterOp".equals(fileType)) {
            try {
                // ��ȡģ��
                template = genConfiguration.getTemplate("flowpower/FPGetterOpration.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // ����ģ������
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.getter.operation.package"));
                dataModel.put("className", GenUtils.getFileNameWithoutSuffix(fileName));
                dataModel.put("classComment", this.properties.getProperty("output.class.getter.operation.comment"));

            }
        } else if ("updater".equals(fileType)) {
            try {
                // ��ȡģ��
                template = genConfiguration.getTemplate("flowpower/FPUpdater.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // ����ģ������
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.updater.package"));
                dataModel.put("className", GenUtils.getFileNameWithoutSuffix(fileName));
                dataModel.put("classComment", this.properties.getProperty("output.class.updater.comment"));

            }
        } else if ("updaterOp".equals(fileType)) {
            try {
                // ��ȡģ��
                template = genConfiguration.getTemplate("flowpower/FPUpdaterOpration.ftl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (template != null) {
                // ����ģ������
                dataModel.put("author", this.author);
                dataModel.put("createDate", this.createDate);
                dataModel.put("classPackage", this.properties.getProperty("output.updater.operation.package"));
                dataModel.put("className", GenUtils.getFileNameWithoutSuffix(fileName));
                dataModel.put("classComment", this.properties.getProperty("output.class.updater.operation.comment"));

            }
        } else {
            System.out.println("��֧�����ɡ�" + fileType + "�����͵��ļ�");
            return;
        }

        // д���ļ�
        this.writerFile(template, dataModel, outPutDir + fileName);

        // ��¼�����ļ�����
        count++;
        // ���������ļ�����һ�����ݼ�dataModel������ÿ����һ���ļ���������ݼ�
        this.dataModel.clear();

        System.out.println("�ɹ������ļ�:" + fileName);

    }

    /**
     * д���ļ�
     *
     * @param template
     * @param dataModel
     * @param file
     * @author chendongsheng
     */
    public void writerFile(Template template, Map<String, Object> dataModel, String file) {

        Writer writer = null;
        try {
            // ����һ���ļ���д����Writer
            writer = new OutputStreamWriter(new FileOutputStream(file), FILE_ENCODE_GBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            // ʹ��ģ������������ָ��·��
            template.process(dataModel, writer);
            writer.flush();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // �ر���Դ
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
     * ������֤
     *
     * @param properties ���ò���
     * @author chendongsheng
     */
    private void checkParams(Properties properties) throws Exception {

        this.outPutDir = this.properties.getProperty("output.dir");
        if (GenUtils.isBlank(this.outPutDir)) {
            System.out.println("------δ�����ļ������������ļ��ڱ��ص����·����");
            return;
        }
        if (!this.outPutDir.endsWith(File.separator)) {
            this.outPutDir += File.separator;
        }
        this.properties.setProperty("output.dir", this.outPutDir);
        // �������ļ����Ƿ��Ѵ���
        GenUtils.checkDir(this.outPutDir);

        String fileName = "";
        String filePath = "";

        fileName = this.properties.getProperty("output.getter.name");
        filePath = this.properties.getProperty("output.getter.package");
        if (!GenUtils.isBlank(fileName) && GenUtils.isBlank(filePath)) {
            throw new Exception("������Getter�ļ��İ�·����");
        }

        fileName = this.properties.getProperty("output.getter.operation.name");
        filePath = this.properties.getProperty("output.getter.operation.package");
        if (!GenUtils.isBlank(fileName) && GenUtils.isBlank(filePath)) {
            throw new Exception("������GetterOperation�ļ��İ�·����");
        }

        fileName = this.properties.getProperty("output.updater.name");
        filePath = this.properties.getProperty("output.updater.package");
        if (!GenUtils.isBlank(fileName) && GenUtils.isBlank(filePath)) {
            throw new Exception("������Updater�ļ��İ�·����");
        }

        fileName = this.properties.getProperty("output.updater.operation.name");
        filePath = this.properties.getProperty("output.updater.operation.package");
        if (!GenUtils.isBlank(fileName) && GenUtils.isBlank(filePath)) {
            throw new Exception("������UpdaterOperation�ļ��İ�·����");
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
                throw new Exception("������ʵ�����ļ��İ�·����");
            } else if (GenUtils.isBlank(DAOPath)) {
                throw new Exception("������DAO���ļ��İ�·����");
            } else if (GenUtils.isBlank(oracleUrl) || GenUtils.isBlank(oracleUsername)
                    || GenUtils.isBlank(oraclePassword) || GenUtils.isBlank(oracleRef)) {
                throw new Exception("Oracle���ݿ���Ϣ���ò�������");
            }

        }

    }

}
