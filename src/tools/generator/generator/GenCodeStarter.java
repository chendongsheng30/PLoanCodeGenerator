package tools.generator.generator;

import tools.generator.service.GenCodeService;

/**
 * ����������������
 * ˵����
 *      1.�����ɻ���Flow Power��ܵ�ftl��xml��Getter�ࡢUpdater�ࡢOP���ļ��Ĺ������룻
 *      2.�����ɻ��ڸ���ϵͳ���Ķ�ӦOracle���ݿ���ʵ�����ļ�����TLR_INFO��������TlrInfo��BaseTlrInfo����ʵ���ࣻ
 *      3.�����ɻ���Hibernate��ܵ����ݿ����ֶ�ӳ��hbm�ļ���DAO���ļ���
 *
 * @author:chendongsheng
 * @date:2020-10-24
 */
public class GenCodeStarter {

    public static void main(String[] args) throws Exception {
        GenCodeService genCodeService = new GenCodeService();
        genCodeService.genFile();
    }
}
