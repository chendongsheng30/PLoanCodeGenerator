package tools.generator.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 工具类
 *
 * @author chendongsheng
 * @date 2020-11-22
 */
public class GenUtils {

    // 判断字符串是否为空
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

    // 判断文件夹是否存在，存在则清空，不存在则新建
    public static void checkDir(String dirPath) throws IOException {
        File dirPojo = new File(dirPath);
        if (!dirPojo.exists()) {
            dirPojo.mkdirs();
        } else {
            FileUtils.cleanDirectory(dirPojo);
        }
    }

    // 去掉Java文件名的.java后缀
    public static String getFileNameWithoutSuffix(String fileName) {
        if (fileName.endsWith(".java")) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName;
    }

    // 数据库表名转换为规范的Java类名,如：TLR_INFO -> TlrInfo
    public static String TableNameToPojoName(String tableName) {
        String javaName = "";
        String[] nameArray = tableName.split("\\_");
        for (int i = 0; i < nameArray.length; i++) {
            javaName += UpperFirstLowerOthers(nameArray[i]);
        }
        return javaName;
    }

    // 将表的字段名，转换为JavaBean属性名
    public static String ColumnNameToJavaPropertyName(String columnName) {
        // 将任意的空白符用空字符串替换
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

    // 首字母大写，其它字符保持不变
    public static String UpperFirstKeepOthers(String str) {
        if (null == str || str.length() == 0) {
            return str;
        } else if (str.length() == 1) {
            return str.toUpperCase();
        }
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    // 首字母大写，其它字符保持不变
    public static String LowerFirstKeepOthers(String str) {
        if (null == str || str.length() == 0) {
            return str;
        } else if (str.length() == 1) {
            return str.toLowerCase();
        }
        str = str.substring(0, 1).toLowerCase() + str.substring(1);
        return str;
    }

    // 首字母小写，其它字符转换成大写
    public static String UpperFirstLowerOthers(String str) {
        if (null == str || str.length() == 0) {
            return str;
        } else if (str.length() == 1) {
            return str.toUpperCase();
        }

        str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        return str;
    }

    // 测试main方法
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
