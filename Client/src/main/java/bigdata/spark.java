package bigdata;

public class spark {
    
    /*
    
    一、Spark_core:
    提交一个spark job:
    bin/spark-submit --class driver主类名 --master spark://hadoop101:7707 jar包 参数
    
    --master local[k]/ spark://hadoop101:7707 / yarn
    --master指定资源调度器
    分别对应3种启动模式：本地模式   standalone模式  yarn集群模式
    
    ./bin/spark-submit
    --class driver-class
    --master local/yarn/standalone
    --deploy-mode client/cluster
    --conf k=v
    <application-jar> args
    
    --deploy-mode决定driver在哪运行：client表示就在提交job的客户端运行   cluster表示由资源管理器决定在某一台机器上启动一个driver
    
    --conf: 如果值包含空格，可以加引号"key = value"
    
    application-jar: 打包好的jar包，包含依赖，这个url应该在集群中全局可见，可以给main()方法传递参数 args
    
    --executor-memory 1G 指定每个executor可用内存为1G
    --total-executor-cores 6 指定所有的executor使用的cpu总核心数为6
    --executor-cores 表示每个executor使用的cpu核心数
    
    repl: read executor print loop
    
    bin/spark-shell --master local[2]： 会自动提供一个sc(sparkcontext)用于测试、原型开发
    
    sc是spark的操作对象，相当于spark的入口
    
    spark中分为转换算子和行动算子，其实就是高阶函数
    
    Driver:初始化一个sc，做DAG任务划分，做任务调度，向资源管理器注册application
    资源管理器：启动executor进程来完成具体的计算任务
    Executor：一个分区启动一个task完成计算任务，向Driver进行反向注册，Driver可以得到当前任务的执行情况
    
    spark也有历史服务器：端口号是18080
    
    RDD：弹性、可分区、不可变、里面的元素可并行计算的弹性分布式数据集
    RDD的5个特点：
    a.里面被划分为多个分区，分区决定了并行计算的粒度，分区越多，并行计算任务越多，分区数可以指定，如果不指定，使用默认的cpu核心数
    每个分区的存储由blockmanager实现，一个分区对应一个block，每个block由一个task负责计算
    b.每个分区计算的函数(computing)
    每个rdd都会实现compute函数，以分区为单位进行并行计算。
    c.各个RDD之间的依赖(血缘关系)
    RDD的每次转换都会生成新的RDD，后一个RDD依赖于前一个RDD，当部分数据丢失时，Spark可以通过这种依赖关系重新计算丢失的分区数据，
    无需全部从头开始计算。
    d.对于key-value类型的数据，提供了分区器功能
    e.提供一个优先考虑在哪计算每个分区的列表(移动计算而不是移动数据)
    
    弹性：
    存储的弹性，内存与磁盘的自动切换；容错的弹性，数据丢失可以自动恢复；计算的弹性，计算出错可以重试；分片的弹性，可根据需要重新分片
    
    依赖：转换算子转换后得到新的RDD，该新的RDD同时也保留了从父RDD衍生所需要的信息，RDD之间的这种血缘关系称为依赖。
    宽依赖：父RDD的每个分区进入子RDD的多个分区中，多对多关系
    窄依赖：父RDD的每个分区只进入子RDD的一个分区，一一对应关系
    
    缓存：如果一个RDD在一个app中被应用多次，那么这个RDD中的数据会被缓存起来(cache)，而无需根据血缘关系从头计算多次
    
    checkpoint:如果血缘关系很长，则依赖血缘关系重新计算要花很长的时间，为此，RDD还支持通过ck将数据保存到磁盘，会切断之前的血缘关系，
    app中之后使用该RDD都无需再从头开始计算，而是直接从本地磁盘读取数据。
    
    RDD的延迟计算：只有遇到行动算子才会真正执行计算，转换算子就好比只是将图纸画好，行动算子是真正开始造汽车。
    
    RDD的创建：
    a. sc.parallelize(Array("zhangsan","lisi"), numSlice = 2)
    b. sc.makeRDD("zhangsan","lisi"), numSlice = 2)
    numSlice设定切片数量(分区数)
    c. 从外部存储创建RDD： sc.textFile("path")  外部文件可以是本地文件系统，HDFS，HBase，Amazon S3
    d. 从其他RDD转换得到
    
    RDD转换算子：
    map:对集合中的每个元素进行转换
    mapPartitions: 一次处理一个分区的数据，里面是迭代器类型！！！！建议开发中使用，有个小缺点是：一次取一个分区的数据，需要等该分区的
                   数据全部处理完，该分区的数据才能释放，如果数据倾斜可能导致OOM
    mapPartitionsWithIndex: 也是一次处理一个分区，给数据加上分区号！！！
    
    flatMap:可以增加数据，也可以减少数据 flatten+map操作，比map更加灵活
    
    glom: 将每个分区的数据合并成一个数组，每个分区聚合成一个Array
    
    groupBy(func(x)): 按照func的返回值作为key分组，对应的值放入一个迭代器中 RDD[(func(value), Iterable[classOf(value)])]
    会使用shuffle重新分区
    
    filter(func(x)): 过滤，返回经运算后返回true的那些元素
    
    sample(withReplacement, fraction, seed):
    withReplacement:是否放回抽样，true为放回抽样
    fraction: 抽样比例
    seed:随机数种子
    
    distinct([numTasks]):对RDD中的元素进行去重，参数表示任务的数量，默认值和分区数保持一致，会shuffle
    
    coalesce(numPartitions, shuffle = false): 默认只能减少分区，如果shuffle改为true，可以增加分区
    repartition(numPartitions):一般用来增加分区，一定会shuffle，底层调用的是coalesce(numPartitions, true)
    shuffle需要借助磁盘IO，性能会大大降低
    
    sortBy(func(x),ascending=true, numTasks=2):先转换，按转换后的结果排序，默认升序
    
    pipe("脚本")
    
    union: ++  并集，不去重
    
    intersection: 交集
    
    subtract: 差集
    
    zip: 拉链：对于每个RDD：每个分区内的元素个数必须要相等，分区数也必须要相等才能做拉链
    
    zipWithIndex: 元素和自己的索引做拉链 rdd1.zipWithIndex()
    
    zipPartitions: 分区数一样就可以做拉链 rdd1.zipPartitions(rdd2)((it1,it2)=>{it1.zipAll(it2,-1,-2)})
    
    cartesian: 笛卡尔积
    
    partitionBy: 分区  rdd2.partitionBy(new HashPartitioner(partitions = 2))
    
    reduceByKey: def reduceByKey(func: (V, V) => V): RDD[(K, V)] 分区内聚合(预聚合)和分区间聚合的逻辑相同，按key进行聚合
    
    groupByKey: def groupByKey(): RDD[(K, Iterable[V])] 按key进行分组，只对value进行操作，返回迭代器类型
    以下三种写法等价：
    val rdd2: RDD[(String, Iterable[Int])] = rdd1.groupByKey()
    val rdd3: RDD[(String, Iterable[Int])] = rdd1.groupBy(_._1).map(t => (t._1, t._2.map(_._2)))
    val rdd4: RDD[(String, Iterable[Int])] = rdd1.groupBy(_._1).map{ case (key, it) => (key, it.map(_._2)) }
    
    groupByKey按照key进行分组，直接进行shuffle，reduceByKey按照key进行聚合，先预聚合、再进行shuffle、然后分区间聚合
    
    aggregateByKey:def aggregateByKey[U: ClassTag](zeroValue: U)(seqOp: (U, V) => U, combOp: (U, U) => U): RDD[(K, U)]
    带零值(外部初始值)，分区内聚合和分区间聚合，注意 seqOp: (U, V) => U , combOp: (U, U) => U
    
    foldByKey: def foldByKey(zeroValue: V)(func: (V, V) => V): RDD[(K, V)]
    带零值，要求分区内聚合和分区间聚合类型一致
    
    combineByKey：def combineByKey[C](createCombiner: V => C, mergeValue: (C, V) => C, mergeCombiners: (C, C) => C): RDD[(K, C)]
    零值由RDD的第一个元素转换得到，同样存在分区内聚合和分区间聚合
    
    sortByKey: def sortByKey(ascending: Boolean = true, numPartitions: Int = self.partitions.length): RDD[(K, V)] 按key排序
    
    mapValues: key不变，只对value进行操作
    
    join：内连接 def join[W](other: RDD[(K, W)]): RDD[(K, (V, W))]
    leftOuterJoin: 左外连接
    rightOuterJoin: 右外连接
    fullOuterJoin: 全连接
    
    cogroup: 先进行groupByKey， 再进行join
    
    RDD行动算子：
    reduce: 聚合RDD中的所有元素，分区内聚合和分区间聚合
    collect:以数组的形式返回RDD中的所有元素到driver端
    count:  返回RDD中元素的个数
    take:   返回前N个元素，数据会被拉到driver端
    first:  返回RDD中第一个元素
    takeOrdered:返回排序后的前n个元素，数据会被拉到driver端
    aggregate:分区内聚合和分区间聚合，zeroValue会使用n+1次，n个分区每个分区都会使用一次，最后分区间聚合使用一次，合计n+1次
    fold:分区内聚合和分区间聚合的数据类型一致，zeroValue同样会使用n+1次
    countByKey:返回每个key对应的元素个数
    foreach:针对RDD中的每个元素都在executor上执行一次
    foreachPartition:每个分区都在executor上执行一次
    
    
    RDD中函数的传递
    Driver运行的位置和Executor运行的位置可能不在同一台机器上的进程，因此涉及到进程之间的通信，RPC框架是为解决进程间通信的，
    进程间通信涉及到数据的序列化
    传递参数或者函数必须要实现序列化，如果不想序列化，那么只能传递局部变量，并且局部变量所属的类也应该是序列化的。
    
    conf.registerKyroClasses(Array(classOf[User])) + User extends Serializable
    
    rdd.toDebugString: 查看stage和task划分
    
    rdd.dependencies: 查看血缘关系
    OneToOneDependency\ShuffleDependency
    
    RDD          -> blockmanager
    partition    -> block         -> task
    并行度、task、分区三者是同一个概念
    task和并行度从计算数据的角度划分
    分区从数据存储的角度划分
    
    如果需要shuffle，那么就是宽依赖，如果不shuffle，那么就是窄依赖
    常见的宽依赖：排序、reduceByKey、groupByKey、join和repartition，各种ByKey操作
    
    spark每遇到一个行动算子，才创建一个DAG执行图并启动一个job，每个job由多个stages组成，stage内部是窄依赖，stage之间是宽依赖
    通过窄依赖和宽依赖转换完成计算
    一个task就表示一个并行计算，有多少个task就有多少个分区，就有多少个并行计算
    
    有向无环图DAG: DAG为每个job构建一个stage组成的图表，从而确定运行每个task的位置，然后传递这些信息给TaskScheduler,
    由他们负责具体的task调度
    
    stage内部的task在各自executor中进行计算，不需要同其他executor或者driver通信，而stage之间需要进行网络通信
    是否需要进行网络通信是划分stage的标准
    
    task是spark中的最小执行单位，一个stage中的所有task会对不同的数据（分区）执行相同的代码，executor通过为task分配slots来执行tasks
    一个executor会启动多个task并行计算多个分区中数据
    
    RDD的持久化
    持久化分为内存持久化和磁盘持久化
    一个RDD如果被多次使用那么会自动被缓存起来(cache)
    
    persist和cache用于持久化一个rdd，rdd是弹性、不可变、可分区、里面的元素可并行计算的分布式数据集
    cache = persist(StorageLevel.MEMORY_ONLY)，底层调用的都是persist方法
    
    spark有很多自动cache的操作，如，在shuffle时会将中间操作数据缓存起来，目的是为了避免shuffle失败时需要全部重新计算（shuffle的代价太大）
    
    checkpoint：检查点机制，将rdd写入到磁盘，会消除rdd的血缘关系！！！
    使用步骤：sc.setCheckpointDir("path")----> rdd.checkpoint
    注意：检查点需要配合行动算子才会执行，但是会独立触发一个job，如果只想触发一个job，可以在设置ck的同时设置cache
    
    持久化和ck的区别：
    a.持久化只是将数据保存在BM中，执行计划不变。但是检查点机制会消除血缘关系，后续rdd的转换将会从checkpointRDD开始，改变了执行计划；
    b.持久化仍有可能丢失数据，checkpoint更难丢数据；
    c.设置了ck通常也要设置cache持久化，否则会出现重复计算。
    
    分区器：只有数据类型为k-v的rdd才需要使用分区器，spark有两种分区器：hash分区器和range分区器
    用户也可以自定义分区器:继承Partitioner
    
    HashPartitioner:根据key的hash值和分区数取模得到分区号,缺点是可能出现数据倾斜
    
    RangePartitioner: 将数据按key的大小尽量均匀划分到若干分区中，分区之间是有序的。抽样->排序->按分区数分区->生成边界数组
    rdd的每个数据根据这个边界数组确定自己的分区号
    注意：sortByKey使用的是rangePartitioner,为了确定采样边界数组，会collect启动一个job
    
    自定义分区器：需要重写numPartitions属性和getPartition(key)方法，另外为了提高效率建议重写equals和hashCode方法，用于判断两次分区
    方式是否相同，如果相同，则只执行一次分区
    
    读写文件：
    text格式：       sc.textFile        rdd.saveAsTextFile
    json格式：       sc.textFile        rdd.map(JSON.parseFull)  解析json文件
    Sequence格式：   sc.sequenceFile    rdd.saveAsSequenceFile
    ObjectFile格式： sc.objectFile      rdd.saveAsObjectFile
    
    //假设文件大小为5 5 6
    hdfs上读写：     sc.textFile("d:/input", minpartitions=4)
    minPartitions决定goalSize即每片大小=totalSize/numsplits=totalSize/minPartitions = 16 / 4 = 4;
    因此切分为6片
    
    累加器和广播变量：
    spark自带的累加器sc.longAccumulator(name="acc")   只提供一个add方法，这个add方法是原子性的，取值方法使用.value
    += 向map中添加一个tuple
    ++= 向map中添加一个map
    
    自定义累加器的步骤：
    1.继承AccumulatorV2[IN, OUT] : IN是累加器的输入值类型，OUT是累加器运算后的类型，OUT应该是线程安全的，或者原子性的
    2.实现6个方法：isZero\reset\copy\add\merge\value
    isZero:判断累加值是否为0
    reset:重置累加值
    copy:复制一个累加器
    add:分区内对外界提供的累加方法
    merge:分区间合并累加器的值
    value: 获取累加值
    累加器工作原理：Driver端copy累加器到每个分区，每个分区执行add方法，分区间合并时再执行merge方法
    
    广播变量：在每个节点上保存一个只读的变量的缓存，而无需每个分区/task拷贝一份
    创建：sc.broadcast(v)创建广播变量，封装一下v的值，如果想取v，使用.value方法，注意v要可以序列化
    广播变量是只读的，每个节点上只有一份！！！！
    
    
    二、Spark_SQL:
    1.DataFrame：
        是二维表结构;
        是特殊的DataSet; DataFrame = DataSet[Row]
        除了记录数据本身，还记录元数据schema;
        懒执行的，用到才执行;
        性能比rdd更高，因为会自动进行优化，比如将join操作放filter后面来执行
        弱类型的, 将一行数据封装为Row，取数据不太方便，不记录字段类型，不进行字段类型检查
    2.DataSet：
        是DataFrame的扩展
        支持编码解码器
        当需要访问非堆上的数据时，可以避免反序列化整个对象，提高了效率
        存储的是一个一个的样例类对象，样例类中的每个属性直接映射到DS的字段名上，读取非常方便
        强类型的，如： DataSet[People]
        DF只知道字段名不知道字段类型，取数据时无法进行类型检查，DS由于存储的是样例类对象，可以进行类型检查
        
    3.SparkSession:
        SparkSession内部封装了SC，实际计算是由SC完成
        SparkSession提供了执行SQL语句的API
        可以调用spark.newSession创建新的session对象
        如何创建一个SparkSession： SparkSession.builder().master("local[2]").appName("spark").getOrCreate()
        
    4.使用DF进行编程
    a.创建DF的三种方式：
        读取文件时直接创建DF：spark.read.json("path") spark.read.csv("path")
        从RDD转换为DF：rdd.toDF(column的字段名) <=> df.rdd   这种转换方式需要导入隐式转换 import spark.implicits._
        通过查询一个hive表来创建：spark.sql("select * from student") 返回DF对象
        
    b.如何使用DF：
        先获取一个DF
        df.createOrReplaceTempView("视图名") 创建一个临时视图， 甚至可以创建跨session的临时视图，只需要加上global_temp.xxx
        spark.sql("sql语句").show(false)
        
    5.使用DS编程
        DS和RDD比较类似，但是没有使用java或Kyro序列化，而是用了一种专门的编码器去序列化对象。
        StructType(Array(StructField("name", DoubleType))) ：专门的编码器序列化对象
        创建方式：rdd.toDS   df.toDS
        
    RDD\DF\DS三者的异同点：
    同：
    都是弹性分布式数据集；
    都有惰性机制；
    都会根据内存使用自动进行缓存计算，不用担心内存溢出；
    都有分区的概念；
    都有map、filter的概念；
    DF和DS都需要导入隐式转换 : import spark.implicits._
    都可以使用模式匹配
    
    异：
    RDD不支持SQL操作，一般和机器学习库一起使用
    DF中封装的数据类型是Row，每一列的值没法直接访问，不会对字段类型进行检查
    DS中封装的数据类型是样例类，强类型，会对字段类型进行检查，使用自己的编码器和解码器
    
    三者之间的转换：
    rdd->DF: toDF
    rdd->DS: toDS
    DF->rdd: rdd
    DF->DS:  as[case class]
    DS->rdd: rdd
    DS->DF: toDF
    
    6.自定义UDF和UDAF函数
    自定义UDF函数：直接spark.udf.register("name", 匿名函数)
    用于DF的自定义UDAF函数：
    1.继承UserDefinedAggregateFunction
    定义好输入元数据、缓冲区类型、输出数据类型、初始化缓冲区、分区内聚合、分区间聚合、获取值
    注册使用
    用于DS的自定义UDAF函数
    1.extends Aggregator[IN, BUFFER, OUT]
    2.zero\reduce\merge\finish\bufferEncoder\outputEncoder
    3.udaf class.toColumn.name
    
    7.Spark_SQL读写到OLAP系统、OLTP系统
    spark默认的数据存储格式是parquet，也可以通过spark.sql.sources.default这个属性来设置默认的数据源
    spark.read.format("格式").option("key","value").load：加载数据的通用方法
    df.write.mode(SaveMode.OverWrite).format("格式").option("key","value").save: 保存数据的通用方法
    
    spark.read.jdbc(url, dbtable, properties)
    df.write.mode("append").jdbc(url, table, properties)
    
    注意区别hive on spark(功能还在开发中 set hive.engine.spark.sql = true) 和 spark on hive(常用 )
    spark on hive 提供了三种使用SQL访问spark的方式：
    要使用spark on hive  需要conf文件夹下持有hive-site.xml, jars文件夹下持有jdbc驱动jar包
    bin/spark-shell     可以直接访问spark.sql("use gmall")
    bin/spark-sql --master yarn   界面不够友好
    bin/beeline -> !connect jdbc:hive2://hadoop102:10000  同样需要先启动服务： sbin/start-thriftserver.sh --master yarn
    
    在idea中访问hive表：
    持有hive-site.xml、mysql驱动、spark-hive依赖
    spark.enableHiveSupport()
    
    读：spark.sql("use gmall; select * from stud").show()
    写：df.write.mode("append").saveAsTable("stud")//看列名不看位置，这种写法更好一点，根据列名来确定插入
        df.write.insertInto("stud")//看位置不看列名

    除此以外，还可以直接写SQL插入到hive表中，注意，尽量不要在spark中创建数据库，因为默认在本地创建
    或者配置一下数据库的创建位置：spark.config("spark.sql.warehouse.dir","hdfs://hadoop101:9000/user/hive/warehouse")
    
    
    三、SparkStreaming
    一个批间隔的数据中包含一个RDD序列，不是真正的流式处理
    处理数据的单位是一批而不是单条，而数据采集却是逐条进行的，spark_streaming需要设置时间间隔将数据汇总到一定的量后再一并操作
    批处理间隔决定了提交作业的频率和数据处理的延迟，同时也影响着数据处理的吞吐量和性能。
    4种创建sparkStreaming的方法：
    1.socketTextStream
    2.queueStream
    3.自定义Receiver类来创建InputStream
    4.kafkaUtils.createDirectStream
            val ds: InputDStream[(String, String)] =
                KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
                ssc, //StreamingContext
                parms, //parameters : map
                Set("slkj") ) //topics : set
                
   DStream的无状态转换操作：map\flatMap\filter等作用到当前批次的每一个RDD上,为什么叫无状态的，因为计算的数据就是过去几秒钟的，不记录历史数据
   DStream <==> RDD: transform  和  forEachRDD都可以实现，区别：一个仍返回DStream且是转换算子，一个没有返回值且是行动算子
   DStream的有状态转换操作：a. updateStateByKey   b. reduceByKeyAndWindow   c. window
   
   //本次的计算结果和上一次的计算结果进行合并得到，只用value计算，自动关联key，上一次的结果用Option保存，本次的结果为Seq
   updateStateByKey((seq:Seq[Int],opt:Option[Int]) => Some(seq.sum + opt.getOrElse(0)))
   
   //依次为reduce处理逻辑、本批次新增-上批次移除、窗口长度、滑动步长
   reduceByKeyAndWindow(reduceFunc = _+_, invReduceFunc=_-_, windowDuration = Seconds(15), slideDuration = Seconds(10))
        
   注意：DStream编程中，可以使用累加器和广播变量、可以使用cache和persistence,
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    * */
}
