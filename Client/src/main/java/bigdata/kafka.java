package bigdata;

public class kafka {
    
    /*
        0. kafka是什么？用过没有？
        发布/订阅模式的分布式消息队列
        
        1. kafka和zk的关系，zk在kafka中扮演的什么角色？
        kafka中集群的管理、元数据的管理都依赖zk来实现
        zk中管理的信息有：kafka的topics、partitions、brokers_id、分区leader选举、整个kafka集群的controller选举、选举轮次
        有哪些消费者组，有哪些消费者等，0.9版本以前还维护了消费者消费的offset
        
        2.kafka为什么那么快？
        顺序写磁盘、零拷贝、磁盘分页技术
        kafka的特点：高性能、实时性、持久化、高吞吐、高容错、提供对流式数据的发布订阅
        
        3.kafka的扩展性体现在哪？
        kafka的主题与分区设计保证了扩展性，提高了吞吐量，如果想要扩展kafka，只需要增加broker
        
        4. broker、topic和partition
        broker：一台kafka服务器就是一个broker，一个集群由多个broker组成
        
        topic：是逻辑上的划分，一个topic会有多个分区，每个分区又会分布在多台broker上（有leader和follower的区别）
        
        partition：是物理上的划分，分区设计保证了kafka的扩展性，每个分区分布在多台broker上，分区设计可以使一个Consumer消费一个
        topic中的多个分区，但是要注意：每个Consumer只能消费同一个主题中的不同分区，达到了并行消费数据的目的。
        另外需要注意的是：kafka消费数据只保证分区内有序，分区间是无序的，如果想要整体有序，那么只能设置只有一个分区（单播模式）
        
        5. record和offset
        record：以ProductRecord的形式保存，一条record必须指定topic和value，可以选择指定partition、key、timestamp等
        如何识别一条record：每条record进入分区后会生成唯一的offset（id），offset是递增的
        需要注意：一条record是以key-value方式存储的，record默认保存7天时间
        
        offset是消费者消费到哪的偏移量，0.9版本以前记录在zk,之后的版本改为创建一个_consumer_offsets主题，专门用来存储offset
        
        
        6. replicas概念
        副本的作用：提高kafka数据的容错性，作为leader的备胎，partition的副本一般会负载均衡地分配在各个broker上
        每个分区都会有一台broker作为leader，0或多台机器作为followers,leader负责处理一切对partition的读写请求，follower
        只需要同步leader上的record，当leader发生故障，会从isr队列中选举出新的leader
        
        
        7. Producer和Consumer、Consumer Group？
        作用：生产数据、消费数据
        三者之间的关系：
        消费者和消费者组：每个消费者都属于一个消费者组，如果消费数据时没有指定消费者组，那么服务器会为消费者创建一个只有一个消费者的消费者组
        消费者分区分配策略：如果消费者客户端只指定了主题，没有指定消费者消费哪个分区的数据，那么服务器会自动为消费者分配分区消费，
        采用的策略有两种方式：range和round_robin，默认是range方式，轮询地分配分区，如果没有指定分区，好处是服务器自动维护消费者的offset
        
        kafka消费数据的特点：
        a)一个消费者可以消费一个主题的多个分区
        b)一个分区只能被消费者组中的某一个消费者消费
        c)根据a、b,同一个消费者组的不同消费者只能消费同一个主题中的不同分区
        
        
        8. kakfa文件存储机制
        kafka生产的数据究竟存储在哪：由于partition才是物理上存在的，因此，每个partition对应一个log文件，该log文件就存储了该分区的数据
        producer在生产数据时会将数据不断追加到log文件末尾。offset机制保证了下次消费时从最新的offset开始继续消费。
        分片和索引机制：如果一直往一个分区写入数据，那么文件会越来越大，为了保证消费数据的效率，kafka使用分片和索引机制，这个机制将每个
        partition分为多个segment文件(.index和.log文件),index记录物理偏移量，log文件记录offset，文件名记录文件的offset大小，支持顺序写操作
        
        9. kafka分区策略
        分区的目的是为了方便集群的水平扩展，支持并行消费数据，一个消费者组中的消费者可以同时消费多个分区的数据
        如果指定了producer的分区，那么将record发送到指定的分区
        如果没有指定producer的分区，但是有key，那么默认使用hash分区器分区
        如果既没有指定key也没有指定分区，那么采用轮询的方式决定进入哪个分区，轮询算法采用先随机访问一个区，后续依次轮询的算法
        
        
        10. kafka分区分配策略
        主要有round_robin和range算法
        默认使用range算法，将一个主题的所有分区汇总，除以消费者组的消费者数目，如果除不尽，排名靠前的消费者会多获得一个分区
        round_robin算法：将多个主题的所有分区汇总排序，以轮询的方式将各个分区分配给消费者
        
        
        11. kafka幂等性
        什么是幂等性：无论执行多少次同样的操作，结果都完全相同，称为具有幂等性
        kafka的幂等性仅仅在分区内是有效的！！！！
        分布式系统中的语义：
        at least once:最少一次，对应kafka最少保存一次数据，ack=-1
        at most once:最多一次，对应kafka最多保存一次数据,ack=0,1
        exactly once: 有且仅有一次，对应kafka保存且仅保存一次数据
        
        
        12. kafka发布数据的可靠性（安全性)和leader选举机制
        主要是通过副本数据同步策略和ISR同步队列保证数据的可靠性
        kafka默认使用的副本同步策略是ack=-1，这就要求所有的follower都完成同步才允许向producer发送ack确认消息，这样就确保了数据的可靠性
        另一方面，kafka为每个分区维护了一个isr队列，意为和leader保持同步的队列，kafka生产数据时，follower主动向leader同步数据，如果
        follower长时间未向leader同步数据，那么就将follower踢出isr，如果leader发生故障，那么会从isr中选举新的leader
        
        
        13.kafka应答机制
        0：表示不需要leader回复ack确认，只管发送，不管是否写成功，这种机制存在丢数据的可能
        1：表示只需要leader确认收到消息就回复ack，然后发送下一条数据，这种应答机制如果在leader刚发送ack后挂掉，并且其他follower都没有
        同步此条数据，那么会造成数据的漏消费。
        -1：表示leader和follower所有副本都确认收到数据才能发送下一条数据，这种机制如果在所有的follower同步完以后，leader返回ack之前挂掉，
        会造成数据的重复消费。
        
        
        14. kafka数据的重复性问题
        ack=-1可以保证数据最少保存一次，但是可能会造成数据的重复消费，如何实现exactly once 语义？
        kafka0.11版本以后引入enable.idempotence=true，可以保证数据只会写入一次
        开启上述参数后，kafka会对每个Record进行缓存，存储<ProducerId, Partition, SequenceNum(id)>作为该record的唯一标识
        确保已经发送的数据不会被重复写入分区中
        具体来说：idempotence + at least once = exactly once
        
        
        15.kafka消费者消费数据的一致性
        消费数据的一致性通过HW设计和LEO设计实现
        一致性：消费者无论从哪个副本消费，消费到的数据都是完全一致的，kafka保证所有的consumer可见的offset最大为HW（high watermark）
        LEO:log end offset 当前follower最近同步的offset
        HW：high watermark 是ISR队列中所有follower的最小的LEO，表示消费者所能消费到的最大offset
        0.11版本后改为leader_epoch
        HW的作用：①保证消费者消费数据的一致性，保证消费者最大只能消费到HW
                 ②当集群中的broker发生故障时，参考HW值对每个broker中存放的数据进行处理，删除超过HW的数据，进一步满足消费的一致性
        具体表现为：当follower故障时，会被踢出isr，follower重启后会读取磁盘记录的旧的HW，将log文件中高于HW的部分舍弃，然后从HW开始向
                   leader同步数据，直到追上当前的HW时重新进入ISR
                   当leader发生故障时，会从isr中选举新的leader，之后为了保证数据的一致性，各follower会将各自log文件中高于HW的部分舍弃，
                   然后从新的leader同步数据
                   
        
        16. kafka消息发送流程
        kafka生产数据时默认采用的是异步发送机制，含两个线程：main线程和sender线程，以及一个线程共享变量RecordAccumulator(记录缓冲区)
        main线程将消息发送给RecordAccumulator,Sender线程不断从RecordAccumulator中拉取消息发送到broker中
        
        生产数据流程： send(productRecord) ->拦截器->序列化器对key-value序列化->分区算法获取当前record分区  send()方法有返回结果！！
        返回Future对象，如果希望同步发送，调用Future.get()，使当前线程进入阻塞
        Future.get()返回值是一个RecordMetaData对象，记录了record的存储分区和offset
        
        消费数据流程：从topic的分区中拉取数据进行消费 consumer.poll(100)
        一堆配置参数配置：要消费哪个主题、消费者组信息、key-value的序列化器、自动提交offset
        
        commitSync同步提交会阻塞当前线程、commitAsync异步提交不会阻塞线程
        
        消费数据时可以自动提交offset也可以手动提交offset：但是都可能会出现漏漏消费和重复消费的情况
        
        如何避免漏消费和重复消费：将消费数据的操作和提交offset的操作变为一个原子操作！！！
        
        使用Consumer.assign()指定主题及分区，需要自己维护offset!
        使用Consumer.subscribe(Collection c)只指定主题，不指定分区，不需要自己维护offset!


        16. kafka监控
        bigdata.kafka Monitor  和 bigdata.kafka Manager
        
        
        17. 使用kafka要做哪些工作，如何使用kafka？
        操作包括：启动kafka server、启动kafka生产者、启动kafka消费者
                 创建主题、分区、修改主题、分区、删除主题、分区、查询主题、分区
        注意在指定副本数时，副本数不能超过broker数，因为将副本保存在同一台broker上是没有任何意义的
    * */
}
