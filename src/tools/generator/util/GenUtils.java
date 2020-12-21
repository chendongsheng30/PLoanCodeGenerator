package tools.generator.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * ������
 *
 * @author chendongsheng
 * @date 2020-11-22
 */
public class GenUtils {

    // �ж��ַ����Ƿ�Ϊ��
    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    // �ж��ļ����Ƿ���ڣ���������գ����������½�
    public static void checkDir(String dirPath) throws IOException {
        File dirPojo = new File(dirPath);
        if (!dirPojo.exists()) {
            dirPojo.mkdirs();
        } else {
            FileUtils.cleanDirectory(dirPojo);
        }
    }

    // ȥ��Java�ļ�����.java��׺
    public static String getFileNameWithoutSuffix(String fileName) {
        if (fileName.endsWith(".java")) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName;
    }

    // ���ݿ����ת��Ϊ�淶��Java����,�磺TLR_INFO -> TlrInfo
    public static String TableNameToPojoName(String tableName) {
        String javaName = "";
        String[] nameArray = tableName.split("\\_");
        for (int i = 0; i < nameArray.length; i++) {
            javaName += UpperFirstLowerOthers(nameArray[i]);
        }
        return javaName;
    }

    // ������ֶ�����ת��ΪJavaBean������
    public static String ColumnNameToJavaPropertyName(String columnName) {
        // ������Ŀհ׷��ÿ��ַ����滻
        columnName = columnName.replaceAll("\\s", "");
        columnName = columnName.toLowerCase();
        String[] segs = columnName.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segs.length; i++) {
            if (i == 0) {
                sb.append(segs[i]);
            } else {
                sb.append(UpperFirstLowerOthers(segs[i]));
            }
        }
        return sb.toString();
    }

    // ����ĸ��д�������ַ����ֲ���
    public static String UpperFirstKeepOthers(String str) {
        if (null == str || str.length() == 0) {
            return str;
        } else if (str.length() == 1) {
            return str.toUpperCase();
        }
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    // ����ĸ��д�������ַ����ֲ���
    public static String LowerFirstKeepOthers(String str) {
        if (null == str || str.length() == 0) {
            return str;
        } else if (str.length() == 1) {
            return str.toLowerCase();
        }
        str = str.substring(0, 1).toLowerCase() + str.substring(1);
        return str;
    }

    // ����ĸСд�������ַ�ת���ɴ�д
    public static String UpperFirstLowerOthers(String str) {
        if (null == str || str.length() == 0) {
            return str;
        } else if (str.length() == 1) {
            return str.toUpperCase();
        }

        str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        return str;
    }

    // ����main����
    public static void main(String[] args) throws IOException {
        /*String outputDir = "F:\\TemplateFiles";
        if (!outputDir.endsWith(File.separatorChar + "")) {
            outputDir += File.separatorChar;
        }
        checkDir(outputDir);*/

        /*String fileName = "TlrInfoGetter.java";
        fileName = getFileNameWithoutSuffix(fileName);*/

        String tableName = "tmp_Tlr_InFo";
        tableName = tableName.toUpperCase();
        System.out.println("----tableName:" + tableName);
        tableName = TableNameToPojoName(tableName);

        System.out.println("----tableName:" + tableName);

    }


}
