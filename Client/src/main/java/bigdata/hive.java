package bigdata;

public class hive {
    
    
    /*  只记录问答题，编程题另做练习训练
        
        关于hive的一些基本认识：
        hive本身不是数据库，只是一个数仓软件
        hive用于已经存储在hdfs上的结构化数据
        hive使用时需要先建表，表结构要和分析的数据的结构一致
        hive提供了cli和beeline两种方式访问数据库
        hive基于hadoop，用于分析和查询Hadoop中存储的数据
        
        hive不是关系型数据库，hive不是基于OLTP（online transaction processing）设计
        hive不能实时查询(hive用于批查询，查询效率在大数据环境下才有优势)和行级别更新(行级别更新指的就是update操作)
        
        hive需要先建表，元数据存储在数据库，要分析的数据存储在hdfs上
        元数据默认存储在derby数据库，但是一般不会用自带的数据库，一般会将元数据放到mysql上，这样可以多窗口访问hive
        一般会配置mysql来管理元数据
        hive是基于OLAP设计（Online Analytical Processing）
        hive使用类SQL语言进行查询
        hive的扩展性来自hdfs
        hive不支持update和delete操作，但支持追加写和覆盖写操作！！！（insert into 和 insert overwrite）
        
        OLTP：在线事务处理，强调事务的强一致性，要么写成功，要么写失败，任何时刻看到的数据都是相同的，时效性：出结果要快
        OLAP：在线分析处理，不支持随机写，没有事务，重视查询分析，不在乎时效性
        
        hive将hql转换成mr程序，将分析的语句通过hive的语法解析器翻译为mr，因此需要启动yarn（hive运行需要hdfs和yarn）
        
        hive的执行引擎：解析器、编译器、优化器、执行器 --->  不适合数据挖掘和迭代式计算，效率低
        
        
        
        0. hive和关系型数据库的区别
        数据存储的位置不同：hive的数据存储在hdfs上，关系型数据库的数据保存在本地文件系统或块设备上
        数据更新：hive不支持随机写操作，关系型数据库支持随机写
        索引：hive不支持建索引，查询需要暴力扫描全表，但是由于翻译为mr程序，可以并行访问数据，因此适合大数据的访问，数据库对列建立索引，
        查询效率很高，执行延迟低，在数据规模较小的情况下有优势
        执行：hive通过将hql语言翻译为mr程序运行，数据库通常有自己的执行引擎，如mysql的innodb
        执行延迟：数据规模较小时，数据库的执行效率高很多，但当数据规模大到一定规模时，hive的并行执行MR程序表现出一定的优势。
        可扩展性：hive的可扩展性来自hadoop，可以很容易实现水平扩展，关系型数据库由于有ACID限制，扩展性一般
        数据规模：hive适合大数据的分析，关系型数据库适合相对较小的数据规模
        
        
        1. hive窗口函数
        
        
        2. hive调优
        
        3.hive建表方式有哪些
        hive中的表在hdfs上就是一层一层的目录
        
        
        4.hive有哪几种表
        内部表：也叫管理表，管理表有删除数据的权限，删除管理表会连带数据一起删除，不适合和其他工具共享数据
        外部表：删除外部表不会删除表中的数据，只是将表的元数据删除，外部表是廉价的
        
        建表语句示例：
        songsong,bingbing_lili,xiao song:18_xiaoxiao song:19,hui long guan_beijing
        
        create table xxx(
        name varchar(20),
        friends array<string>, #array结构
        children map<string, int>, #map结构
        address struct<stress:string, city:string>) #struct结构，可以看作只有属性的类
        comment ""
        row format delimited
        fields terminated by ","
        collection items terminated by "_"
        map keys terminated by ":"
        lines terminated by "\n";
        
        库操作：
        默认库为 default库
        存放位置为/user/bigdata.hive/bigdata.warehouse
        如果不指定location属性，默认创建一个库生成/user/bigdata.hive/bigdata.warehouse/xx.db目录
        在HQL中不区分大小写，设置属性时有大小写
        
        创建库：
        create database if not exists mydb
        comment "database_comment"
        location "hdfs://hadoop101:9000/user/bigdata.hive/bigdata.warehouse"
        with dbproperties("create_user"="liukuijian");
        
        删除库：
        drop database mydb;#删除空库
        drop database mydb cascde;#删除非空库，顺便删除库中的表
        
        修改库：
        只能修改location、dbproperties、owner属性
        alter database mydb set dbproperties(""="")
        修改时同名的属性会覆盖，不存在的属性会增加
        
        查询库：
        show databases;
        desc database mydb;
        desc database extended mydb;
        use mydb;
        
        
        创建表：
        create external table if not exists mytable #创建外部表
        (name varchar(20) comment "", age int comment "") # 字段名
        comment ""
        partitioned by (date string comment "") #创建分区
        clustered by (age)  #创建分桶
        sorted by (name desc) into 4 buckets # 排序，抽样4桶
        row format delimited  #行的格式
        fields terminated by ""
        map keys terminated by ""
        collection items terminated by ""
        lines terminated by ""
        stored as textfile  #存储格式，默认为textfile 还可以指定为orc、parquet等格式
        location "hdfs://hadoop101:9000/user/bigdata.hive/bigdata.warehouse" #存储位置
        tblproperties("creater" = "liukuijian"); #表的属性
        
        修改外部表为管理表
        alter table mytable set tblproperties("EXTERNAL" = "FALSE");#固定写法，必须大写
        
        分区表：分区表会在当前表目录下再创建一层子目录，该子目录格式为 分区字段名=字段值
        数据库一层目录-->表一层子目录-->分区又一层子目录 -->数据放该子目录下
        
        mr的分区：指将数据分散到多个文件中，更多的是逻辑上的概念
        hive的分区：指将数据分散到多个分区子目录下，分区表会自动添加分区字段
        
        查看分区：show partitions mytable;
        
        创建分区的三种方式：会自动为表加上分区字段
        a. alter table mytable add partition(date="2020-02") partition(date="2020-01");
            生成分区目录、生成元数据
            
        b.如果hdfs事先建好了分区目录，那么可以使用msck repair table mytable 的方式自动根据分区目录名修复元数据！
            msck repair table mytable
            
        c. load data loal inpath "" into table mytable partition(xxx="")
        
        删除表： drop table mytable;
        
        查询表：
        show tables; desc mytable;desc extended mytable; desc formatted mytable;
        
        修改表：
        alter table mytable set tblproperties("EXTERNAL" = "TRUE");
        
        分桶操作：clustered by和mr中的分区操作类似，将表中的数据分散到多个文件中！
        在hive中clustered by 默认使用HashPartitioner作为分区器
        create table mytable(name string, age int) clustered by (age) into 2 buckets;
        分桶也是分散数据，分桶是为了按照某个字段进行聚簇，方便抽样查询！
        向分桶表导入数据，由于需要进行shuffle，所以肯定要启动一个mr程序
        
        导入数据的方式：
        load data local inpath
        hadoop fs -put
        insert into
        import
        
        create table stu_buck(name string, age int)
        partitioned by ()
        clustered by (name)
        sorted by(age desc)
        into 4 buckets
        row format delimited
        fields terminated by " "
        location "hdfs://hadoop101:9000/"
        stored as textfile
        tblproperties("key" = "value")
        
        抽样查询：
        select * from stu_bucks tablesample(buckets x out of y on name)
        
        
        清空表： truncate table mytable;
        
        修改表： alter table mytable change column name newName string comment "" after age;
        alter table mytable add columns (id int comment "", address struct<street:string, city:string> comment "");
        
        复制表:  非常有用！！！
        create table mytable like anothertable;(此种方式创建的表是带分区的)
        create table mytable as select 语句（此种方式无法创建分区表，就是一普通表）
        
        
        5.hive导入导出数据的方式有哪些
        
        load data local inpath "xxx" into table mytable partition(date="2020-02") #插入分区表
        使用local和不使用local的区别：
        使用local将数据从本地文件系统中拷贝到hdfs，使用的命令是hdfs -put
        不使用local,使用的命令是从hdfs 上mv 到另一个位置，hdfs -mv
        into表示追加写
        into 可以改为overwrite，表示覆盖写操作
        load方式不会启动mr程序！！！！

        insert into/overwrite：这种方式会启动mr程序，向表中加载数据时，需要分桶、排序、存储为非文本格式！！！
        insert into table mytable values(),()... | select 语句
        
        多插入模式：
        from mytable
            insert into table table1 select 语句1
            insert into table table2 select 语句2
            
        create table xxx like anotherxxx;
        create table xxx as select 语句;

        import：
        import [external] table 空表|不存在的表
        partition(date="2020-02") #导入哪个分区！！！
        from "hdfs://hadoop101:9000/" #要导入的数据在哪
        location "目标放置路径" #要将数据放置到哪块位置
        
        import external table mytable partition(""="") from '从哪导入' location '导入后存储到哪'
        
        import external table 空表、不存在表 partition() from "要导入的表的路径" location "将导入的数据存储在哪";
        a)导入时，如果表不存在，那么自动建表建分区表
        b)如果表已经存在，需要先检查元数据是否相同
        c)如果元数据相同，判断是否为分区表，如果不是分区表且为空表，那么允许导入数据
        d)如果是分区表，那么导入的分区必须不存在！！！（分区如果存在，不管空不空都不能导入！！！做项目的时候吃过大亏）
        
        导出方式：
        insert overwrite local directory 'path' row format delimited fields terminated by '' select 语句;
        
        
        
        export table mytable partition(''='') to '要导出的路径';
        export 可以将表或分区中的数据和元数据一并导出到hdfs的目录中，并且这种导出是和数据库无关的，意味着导出的数据和元数据可以被再次导入
        到任意数据库
        
        
        
        6.hive的函数有哪些
        查看函数：
            show functions :查看所有的函数
            desc function 函数名：查看某个函数的使用
            desc function extended 函数名：查看某个函数的使用
        函数的分类：
            UDF：用户定义函数，一进一出
            UDAF： 用户定义的聚集函数， 多进一出  max min sum count avg collect_set collect_list concat_ws
            UDTF： 用户定义的表生成函数，一进多出 explode(字段名)  lateral view explode(字段名) 表别名 as 字段名
            
        常用函数： nvl(value, default value) 空值转换函数
        case when :
               case 列名：
                    when 1 then 2
                    when 2 then 3
                    else 4
               end
        if(表达式，值1， 值2) 如果表达式返回true，值1，否则值2
        
        7.udf、UDAF的异同
        UDAF：聚集函数
        concat(str1, str2, ...,strN):字符串拼接函数，如果有一个为null，返回null
        concat_ws(seprator, [string| array<string>]):使用分隔符拼接传入的字符串和字符串数组中的字符串
        collect_list(列名)：将这列的数据返回为数组，不去重
        collect_set(列名)：将这列的数据返回为集合，去重
        
        UDTF：表函数 一进多出
        explode(列名)：炸裂函数将array炸裂为1列N行，将map炸裂为2列N行
        不能出现在select语句之外，必须紧挨着select，不能嵌套在表达式中，只能单独使用，不能和其他字段一起使用
        一般的用法是使用侧写：lateral view explode(字段名) 表别名 as 列字段名
        
        侧写将udtf炸裂的1列多行数据在逻辑上划分为1列1行
        
        窗口函数:在执行函数时，定义一个窗口作为函数的执行范围
        
        三类窗口函数：
        1)已经定义好的窗口函数：lead lag first_value last_value
        lead: lead (列名 , offset, default) 取后offset行数据作为窗口，如果没有就使用默认值default
        lag:  lag (列名, offset, default) 取前offset行数据作为窗口，如果没有就使用默认值default
        first_value: first_value(列名, true/false) 取指定列窗口中的第一个值，第二个布尔类型的参数表示是否跳过null，默认为false
        last_value: last_value(列名, 是否跳过null值) true表示跳过
        
        2)标准的聚集函数：
        MAX\MIN\AVG\COUNT\SUM
        
        3)统计函数：排名函数
        rank\row_number\dense_rank\cume_dist\percent_rank\ntile(n)
        rank:允许重复，并列跳号
        row_number:连续
        dense_rank:允许重复，并列不跳号
        cume_dist: 当前分数之前的总行数/数据集总行数  累计分布函数
        percent_dist:rank-1/数据集总行数-1
        ntile(n):将数据分为n组，返回当前的组号
        
        函数 over(partition by xx order by xx window_clause)
        
        window_clause: rows between unbounded preceding and current row following
        如果没有定义order by 和 window_clause: 窗口的默认大小为： 上无边界到下无边界
        如果定义了order by但没有window_clause: 窗口的默认大小为上无边界到当前行
        大多数函数不能跟window_clause:lead\lag\统计函数（rank\row_number\dense_rank\cume_dist\percent_dist\ntile(n)）
        主要是标准聚集函数可以使用window_clause：max\min\avg\count\sum
        
        
        8.4个by的区别
        order by：指定全表排序，只有一个reducer，生成一个结果文件，整个结果文件内部有序
        distribute by：指定在排序时按照哪个字段进行分区，一般配合sort by一起使用
        sort by:指定部分排序，多个reducer生成多个结果文件，每个结果文件内部有序，如果不指定distribute by那么会随机分区
        cluster by:如果sort by 和distribute by的字段相同，那么可以简写为cluster by，只能升序排asc

        9.一提到hive
        应该想到dml、ddl、分区表、管理表、导入导出数据、窗口函数、和关系型数据库的区别、如何优化hive
        
        10. hive优化
        1） MapJoin：如果不指定MapJoin或者不符合MapJoin的条件，那么hive解析器会将Join操作转换成Common Join，也即ReduceJoin
            容易发生数据倾斜。可以用MapJoin把小表全部加装到内存在map端进行Join操作，避免ReduceJoin处理。
        2） 行列过滤：
            列处理：在select中，只拿需要的列，如果存在分区，尽量使用分区过滤，不用select *;
            行处理： 如果有表的关联，尽量先过滤再关联，而不是全表关联
        3） 采用分区分桶技术：尽量将数据分散存储，分区技术将数据存储到多个目录下，并为表数据添加分区字段，分桶技术将数据分散到多个文件中
        4） 合理设置Map数：
            a) 一般地，map的数据取决于input目录下的文件数量以及各个文件的大小。
            b) 合理设置map数，map数不是越多越好，map数越多，各个map之间会互相竞争资源，如果map初始化的时间远远大于逻辑处理的时间，会造成资源浪费
            c) 小文件合并：数据源（滚动策略和滚动间隔）、归档、使用CombineHiveInputFormat等
            d) 合理设置reduce数，reduce的启动也是要消耗时间和资源的
            e) 压缩：
                ①map端压缩：
                1．开启hive中间传输数据压缩功能
                set bigdata.hive.exec.compress.intermediate=true;
                2．开启mapreduce中map输出压缩功能
                set mapreduce.map.output.compress=true;
                3．设置mapreduce中map输出数据的压缩方式
                set mapreduce.map.output.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;
                ②reduce端压缩：
                1．开启hive最终输出数据压缩功能
                set bigdata.hive.exec.compress.output=true;
                2．开启mapreduce最终输出数据压缩
                set mapreduce.output.fileoutputformat.compress=true;
                3．设置mapreduce最终数据输出压缩方式
                set mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;
            f) 数据倾斜：设置一个合理的分区器，让数据均衡化，空key过滤和空key转换也可以达到消除数据倾斜的目的
            g) 并行执行：对于含有多个阶段的job，如果某些阶段可以并行执行，那么可以缩短job的执行时间
            h) JVM重用：一个JVM实例可以在同一个job中重复使用多次，避免了重启JVM。
            i) 任务推测执行：对于执行速度非常慢的job会启动一个备份job，最后哪个先执行完就采用哪个的结果。不适用于某些本身计算时间就
                非常长、非常吃资源的任务。
            j) fetch抓取：可以在某些情况下不使用mr计算。
            k) 开启动态分区：需要先开启非严格模式。
            

            
    * */
}
