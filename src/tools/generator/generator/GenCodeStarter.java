package tools.generator.generator;

import tools.generator.service.GenCodeService;

/**
 * ����������������
 * ˵����
 * 1.�����ɻ���Flow Power��ܵ�xml��Getter�ࡢUpdater�ࡢOP���ļ��Ĺ������룻
 * 2.�����ɻ��ڸ���ϵͳ���Ķ�ӦOracle���ݿ���ʵ�����ļ�����TLR_INFO��������TlrInfo��BaseTlrInfo����ʵ���ࣻ
 * 3.�����ɻ���Hibernate��ܵ����ݿ����ֶ�ӳ��hbm�ļ���DAO���ļ���
 * 4.�������ɵ��ļ��ڵ���IDE����ʱ���ֶ����е�����
 * 5.����ʵ�����serialVersionUID���ֶ�����
 * 6.�����߲�֧������Ϊ����������Oracle���ݿ�����ɶ�Ӧʵ�����hbm�ļ��������������ļ��������ֶ�������
 * 7.���������ɵ�hbm�ļ��������õ����������������������淶ΪSEQ_tableName������ѡ����������������ʽ���ֶ�������
 * 8.�����߲�֧����������Flow Power��ܵ�ftl�ļ�����Ϊ����FTLָ���ͻ��
 * 9.������δ����ʵ�����equals��hashCode��������������ӻ��Ż������ߣ�
 * 10.���ݿ���ֶ���������ΪNUMBER����ʱ�����ɵ�ʵ�����hbm�ļ�Ĭ��ֻ��Ӧһ��java�������ͣ��������Ҫ���е�����
 *
 * ��������������г����Ż�������������󳡾���������µ�ַ��https://github.com/chendongsheng30/PLoanCodeGenerator.git
 *
 * @author chendongsheng
 * @date 2020-11-22
 */
public class GenCodeStarter {

    public static void main(String[] args) throws Exception {
        
        GenCodeService genCodeService = new GenCodeService();
        genCodeService.genFile();
        
    }
}
