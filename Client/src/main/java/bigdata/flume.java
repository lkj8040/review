package bigdata;

public class flume {
    
    /*
        0.Event:
        event = header + body 封装为一个一个bean对象
        header是kv结构
        body是字节数组
        
        1.Agent: 是什么？包含哪些组件？
        包含Source、Sink、Channel,put事务、take事务
        
        2.source:有哪些常见的source？
        常见的source有：netcat TCP Source 、 Exec Source、 Spooling Directory Source、Tailing Dir Source
        
        netCat TCP:启动一个nc -kl 端口服务，每行封装为一个event，客户端 nc 主机名 端口号
        
        Exec：这个source通过启动一个持续生成数据的进程来封装为event，如cat、tail -f ,exec是异步source，有丢失数据的风险
        
        Spooling Directory：指定一个目录进行监测，监测目录中的新文件，要求文件必须封闭，不能实时追加数据到文件，会标记哪些文件已经解析过
        两个注意点：目录中的文件必须是一成不变的，文件封闭放入目录后不能追加数据，文件不能重名，否则抛异常
        
        Tailing Dir:实时监控多个文件，与Spooling Directory不同，该Source文件中的内容可以是实时追加的，使用的linux的tail指令
        非常可靠：因为会以json格式记录已经读取文件的位置，offset，即便发生宕机下次重启会从offset位置开始读取
        
      
        3.sink：有哪些常见的sink？
        Logger Sink: 将event以info级别写出到指定的文件或者控制台！
        
        hdfs Sink:event的输出目的地是hdfs
        特点有哪些？提示有5点!可以设置压缩、可以配置文件滚动策略、可以配置格式化的转义序列（文件夹的名称是时间戳）、需要安装hadoop
        如果要使用转义序列，必须在header中添加对应的时间戳k-v，或者设置hdfs.useLocalTimeStamp = true
        
        Avro Sink：avro本身是一种数据格式，这种格式用于多个Agent之间的传递event，例如多个Agent之间的串联并联工作
        要求：和avro source配合使用，并且source和sink的配置要完全相同！
        
        
        4.channel:有哪几种channel？生产环境用的哪个channel？
        作用：缓存事件队列
        种类：3种，file channel 、 memory channel 、 bigdata.kafka channel
        
        
        5.channel processor：作用是什么？和selector以及Interceptor的区别
        作用：处理封装好的event，将event发送到channel中，processEventBatch中会进一步调用selector和interceptor的方法
     
        
        6.interceptor：作用是什么？如何自定义Interceptor？
        作用：会在channel processor中调用拦截器链，用于对事件进行预处理
        例如：在项目中就是用这个进行预处理，将错误日志过滤掉，封装event的header，让具有不同标签的事件进入不同的kafka channel中
        自定义步骤：实现Interceptor接口、重写init\intercept\intercept\close方法、创建Builder静态内部类、内部类中提供build方法
        其中，intercept方法一个用于处理单个事件，一个处理批事件，处理批事件的调用处理单个事件的intercept方法
        
        7.channel selector：作用是什么？有哪些selector？
        作用：决定event会发给哪个Channel
        种类：主要有两种 Replicating 和Multiplexing两种
        Replicating Channel Selector：默认的selector，会将所有event发送到配置了的channel中
        Multiplexing Channel Selector: 将event按照配置的header中的k-v信息发送到指定的channel中
        
        8.sink processor：作用是什么？各自的作用分别是什么？
        作用：决定将channel中的event分发到哪个sink中
        原理：sink processor从一个sink组中选择一个sink来取数据
        
        Default Sink Processor:默认的，只允许一个sink，一个sink就是一个sink组，不需要额外配置sink组
        Failover Sink Processor:故障转移，容错，出错就切换sink，故障sink进入cool pool
        Load Balancing Sink Processor:负载均衡,轮询的方式round_robin或者random算法
        
        9.如何启动一个flume-agent？
        bigdata.flume-ng agent -n agent1 -c 全局配置文件 -f agent配置文件
        
        
        10.source、channel、sink之间的配合关系？
        一个source可以向多个channel中写event（配置即可）、一个sink只能从一个channel中读event（从一个sink组中选取一个sink读数据）
        
        11. put事务和take事务
        Source在向Channel中put事务时，是一批一批的放入到putList中的，batchSize要小于transctionCapacity
        同时transctionCapacity不能大于Channel的Capacity
        几个关键步骤：doPut putList doCommit doRollBack | dotake takeList  doCommit doRollback
        
        12.flume监控
        JMXTrans（采集JMXMbean的属性）+InfluxDB（存属性）+Grafana（可视化和报警）
        
        
        flume的主要操作：除了javaAPI自定义source、sink、interceptor等以外，主要工作就是配置文件
        比如配置source用哪个、channel用哪个、sink用哪个、配置使用channel selector 、 Sink processor、Interceptor等信息
        
        然后启动flume的话就是 bigdata.flume-ng agent -n agentName -c conf/env.sh -f source_channel_sink_conf
        
    * */
}
