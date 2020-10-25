package tools.generator.generator;

import tools.generator.service.GenCodeService;

/**
 * 代码生成器启动类
 * 说明：
 *      1.可生成基于Flow Power框架的ftl、xml、Getter类、Updater类、OP类文件的公共代码；
 *      2.可生成基于个贷系统风格的对应Oracle数据库表的实体类文件，如TLR_INFO表，则生成TlrInfo和BaseTlrInfo两个实体类；
 *      3.可生成基于Hibernate框架的数据库表的字段映射hbm文件和DAO类文件。
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
