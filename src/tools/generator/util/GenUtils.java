package tools.generator.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * ������
 *
 * @author:chendongsheng
 * @date:2020-10-24
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

    // ����main����
    public static void main(String[] args) throws IOException {
        /*String outputDir = "F:\\TemplateFiles";
        if (!outputDir.endsWith(File.separatorChar + "")) {
            outputDir += File.separatorChar;
        }
        checkDir(outputDir);*/

        String fileName = "TlrInfoGetter.java";
        fileName = getFileNameWithoutSuffix(fileName);
        System.out.println("----fileName:" + fileName);
    }


}
