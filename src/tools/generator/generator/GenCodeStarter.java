package tools.generator.generator;

import tools.generator.service.GenCodeService;

/**
 * 代码生成器启动类
 * 说明：
 * 1.可生成基于Flow Power框架的ftl、xml、Getter类、Updater类、OP类文件的公共代码；
 * 2.可生成基于个贷系统风格的对应Oracle数据库表的实体类文件，如TLR_INFO表，则生成TlrInfo和BaseTlrInfo两个实体类；
 * 3.可生成基于Hibernate框架的数据库表的字段映射hbm文件和DAO类文件；
 * 4.部分生成的文件在导入IDE工具上需手动进行导包；
 * 5.生成实体类的serialVersionUID需手动处理；
 * 6.本工具不支持主键为联合主键的Oracle数据库表生成对应实体类和hbm文件，但可在生成文件后自行手动调整。
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
