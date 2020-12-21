个贷系统代码生成器
  说明：
       1.可生成基于Flow Power框架的xml、Getter类、Updater类、OP类文件的公共代码；
       2.可生成基于个贷系统风格的对应Oracle数据库表的实体类文件，如TLR_INFO表，则生成TlrInfo和BaseTlrInfo两个实体类；
       3.可生成基于Hibernate框架的数据库表的字段映射hbm文件和DAO类文件；
       4.部分生成的文件在导入IDE工具时需手动进行导包；
       5.生成实体类的serialVersionUID需手动处理；
       6.本工具不支持主键为联合主键的Oracle数据库表生成对应实体类和hbm文件，但可在生成文件后自行手动调整；
       7.本工具生成的hbm文件主键采用的序列自增，且序列命名规范为SEQ_tableName，如想选用其它主键自增方式请手动调整；
       8.本工具不支持生成适配Flow Power框架的ftl文件，因为存在FTL指令冲突；
       9.本工具未生成实体类的equals和hashCode方法，需自行添加或优化本工具；
       10.数据库表字段数据类型为NUMBER类型时，生成的实体类和hbm文件默认只对应一种java数据类型，请根据需要自行调整。