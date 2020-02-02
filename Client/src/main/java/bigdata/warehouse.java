package bigdata;

public class warehouse {
    
    /*
        SKU: stock keeping unit  是比商品本身更小的描述单位，比如一件银色版、128G内存、支持移动网络的iphoneX
        SPU: standard product unit  商品本身，比如一部iPhoneX
        SPU表示的是一类商品，好处是不同的SKU可以使用相同的SPU描述信息，比如商品图片、海报、销售属性等
        
        订单表: order_info  只记录订单信息，订单状态、用户id、金额、创建时间、操作时间 周期型事实表
        订单详情表: order_detail  订单编号、订单号、用户id、商品id、创建时间  事务型事实表
        商品表：goods_info 描述商品本身，sku、spu、price、desc、三级品类id、创建时间等  实体表
        用户表：user_info  描述用户的信息  实体表
        商品分类表: 商品的品类描述 分一级、二级、三级品类   维度表
        订单状态表: 订单状态的描述信息   维度表
        支付流水表: 订单的支付信息 编号、 谁支付的、支付了多少钱、使用的什么支付渠道、支付时间  事务型事实表
        
        数据的同步策略: 全量表、增量表、新增及变化表、拉链表
        实体表：可以做每日全量表
        维度表：可以做每日全量表
        事务型事实表：可以采取每日增加一个分区的方式存储为每日增量表
        周期型事实表：可以使用新增及变化表对数据进行合并做拉链表
        
        
        范式理论：大多数关系型数据库都遵守3范式设计(3NF)
        使用范式的目的是：1)减少数据的冗余,尽量让每个数据只出现一次 2)保证数据的一致性
        缺点是：获取数据时，需要通过多表关联拼接出最后的结果
        
        函数依赖：
        完全函数依赖(AB=>C 但是 A=>C、B=>C不成立)
        部分函数依赖(AB是主键,AB=>C, 并且有 A=>C或B=>C)
        传递函数依赖(A=>B, B=>C，但是C=>B, B=>A不成立)
        
        第一范式: 属性不可分割
        第二范式: 不存在部分函数依赖
        第三范式: 不存在传递函数依赖
        
        关系模型：关系模型主要用于OLTP系统中，为了保证数据的一致性以及避免冗余，所以大部分业务系统的表都是遵循第三范式的。
        维度模型：维度模型主要应用于OLAP系统中，因为关系模型虽然冗余少，但是在大规模数据，跨表分析统计查询过程中，会使用多
        表关联，这会大大降低执行效率。
        在OLAP系统中，所有的表都划分为事实表和维度表两种，所有维度表围绕着事实表进行解释和描述。
        
        OLTP和OLAP的对比：
        读特性：OLTP每次查询只返回少量记录，OLAP对大量记录进行汇总
        写特性：OLTP支持随机、低延时写入用户的输入，OLAP采用批量导入的方式写入
        使用场景：OLTP用于用户、WEB项目，OLAP用于内部分析，为决策提供支持
        数据表征：OLTP总是在实时更新到最新的状态，OLAP则存储随时间变化的历史状态(每个自然日结束更新一次状态)
        数据规模：OLTP的数据在GB级别，OLAP的数据在TB到PB级别
        
        星型模型：维度表基于事实表进行描述，和雪花模型的主要区别在于维度只有一层
        雪花模型：维度表基于事实表进行描述，维度可以是多层的，和第三范式比较接近，但无法完全达到第三范式，因为性能成本太高
        星座模型：和星型模型及雪花模型的主要区别在于，维度表围绕多个事实表进行描述，这种模型是数仓的常态，因为很多数仓都是多个
        事实表，另外，各个事实表之间可能会共享一些维度表。
        数仓建模：选择星型模型还是雪花模型，取决于性能优先还是灵活更优先，星型模型由于维度只有一层，因此在多表关联的跨表查询中
        执行效率会相对更高一些，雪花模型则可以减少数据的冗余以及在数据的一致性方面更加突出。
        
        --sqoop将oltp系统的业务数据导入到olap系统中的数仓中
        sqoop用于hadoop和关系型数据库之间的导入和导出。
        原理：将导入和导出命令翻译为MR程序来实现
        由于sqoop的功能是传递数据，因此不需要reduce过程，sqoop翻译的mr程序只有map阶段，没有reduce阶段。
        sqoop翻译的mr程序主要是对inputformat和outputformat进行定制。
        
        样例：
        #导入到hdfs上
        bin/sqoop import
        --connetct jdbc:mysql://hadoop101:3306/company
        --username root
        --password L19940816
        --target-dir /user/bigdata.hive/bigdata.warehouse/gmall.db/student
        --delete-target-dir
        --fields-terminated-by '\t'
        --num-mappers 2
        --split-by id
        --table student
        --columns id, name
        --where 'id >=10 and id <= 20'
        或者：
        --query "select * from student where \$CONDITIONS and id >= 10 and id <= 20"
       
        
        #导入到hive表中
        bin/sqoop import
        --connect jdbc:mysql://hadoop101:3306/company
        --username root
        --password L19940816
        --table student
        --num-mappers 1
        --bigdata.hive-import
        --bigdata.hive-overwrite
        --fields terminated by '\t'
        --bigdata.hive-table stu_emp
        
        #hdfs上的文件导入到关系型数据库
        bin/sqoop export
        --connect jdbc:mysql://hadoop101:3306/company
        --username root
        --password L19940816
        --table student
        --num-mappers 1
        --export-dir /user/bigdata.hive/bigdata.warehouse/gmall.db/student
        --input-fields-terminated by '\t'
        --update-key id
        --update-mode allowinsert | updateonly
        注：allowinsert 如果key已经存在，就执行update，如果key不存在就执行插入，insert into xx on duplicate key update
            updateonly 只更新现有的行，不插入新行，执行SQL时，只执行update。如果不存在，就不做任何事情
        
            导入到HDFS上时：mysql底层的Null在底层就是Null，而Hive中的Null在底层是'\N'来存储
            --null-string '\\N'
            --null-non-string '\\N'
            导出到关系型数据库时：将字符串列和非字符串列的空串和"null"转义
            --input-null-string '\\N'
            --input-null-non-string '\\N'
        
        A.什么是拉链表：
        每条记录都有一个状态位标记当前状态，一个生效开始日期，一个生效结束日期，合计3个信息来表征每条记录的生命周期
        一旦一条记录的生命周期结束，就重新开始一条新的记录，并把生效结束日期+1后放入下一行生效开始日期
        最终的生效结束日期一定是9999-99-99，所对应的生效开始日期就是当前的最终状态日期！所对应的状态就是当前的最终状态！
        
        B.拉链表适合做什么，为什么要做拉链表：
        拉链表适合于状态位数据会发生变化，但是其余大部分是不变的。
        每日全量表每天都需要增加一次数据，保存效率很低，做拉链表的目的在于不用做每日全量表，如果状态多天没有变化，那么拉链表也不会有
        变化，就不会向拉链表中插入新的数据，拉链表只记录状态变化的数据，即只保留新增及变化数据
        
        C.如何使用拉链表：可以利用拉链表获取某个日期的全量切片数据
        通过   生效开始日期<=某个日期  且  生效结束日期 >= 某个日期，可以得到某个时间点的数据全量切片
        
        D.如何做拉链表：
        首先，业务系统中存储的是订单全量表，状态修改就修改了，不会存储修改的状态！！！全量表总是记录最终的当前的状态
        其次，初始的拉链表就等于初始的订单全量表，在该表的基础上增加生效开始日期为当前日期和生效结束日期为9999-99-99
        再次，第二天业务系统中的订单全量表肯定发生了变化(新增了数据[有新的订单]、前一天的交易记录修改了状态[从待支付变为了已支付])
              从全量订单表中找出前一天的新增及变化记录得到新增及变化表(订单变化表)
        最后，利用前一天的拉链表和当前的新增及变化表做合并操作
        
        主要操作有：
        首先创建一个临时表用来存储新的拉链表
        对于新增及变化表，可以增加生效开始日期和生效结束日期为9999-99-99后即可插入临时拉链表
        对于旧的拉链表，根据订单id做旧的拉链表与新增及变化表的leftjoin操作
        如果匹配上了就修改生效结束日期为当前日期前一天，如果没有匹配上，就保持原来的日期不变
        最后将临时拉链表的输入使用insert overwrite 写入旧的拉链表中作为新的拉链表
        

    * */
}
