package bigdata;

public class redis {
    
    /*
        mysql单表500万条
       
       第一代解决方式：缓存+mysql垂直拆分
       第二代解决方式：主从复制读写分离
       第三代解决方式：防火墙+负载均衡层+服务器专门化(缓存服务器、移动信息服务器、Hadoop集群、实时通信服务器、流媒体服务器)
    
       NoSQL：一般采用分布式系统，遵循CAP原理
       关系型数据库：高并发的更新操作
       多表关联的复杂查询
       
       Redis:缓存、消息中间件、数据库
       Redis：k-v结构的NoSQL数据库
       MongoDB：文档型数据库，自我描述型数据的结构，如json\html\xml
       Hbase:列式存储数据库
       Neo4j：图式存储计算数据库
       
       非关系型数据库：非结构化或半结构型数据
       redis:value以字节数组存储
       redis使用c语言编写，c适合跟底层硬件交互，每秒百万级OPS
       单线程(每次只能响应一次请求)，多路IO复用
       这个单线程是指redis接收客户端请求时是单线程的，但是redis内部有很多的读写线程，因此读写是多线程的
    
        安装gcc c++: yum install -y gcc-c++
        先编译再安装
        redis-benchmark:跑分工具
        redis-check-aof:
        redis-check-rdb:
        redis-cli: -h [host] -p [prot] 可以发送shutdown关闭server  quit/exit
        redis-server: + 配置文件
        redis-sentinel:哨兵模式
        
        0号库是默认库
        select 0
        flushdb/flushall
        dbsize
        
        key操作
        keys * : 查看所有的key
        keys ?2:正则匹配
        
        k-v结构： k一般就是String，value支持8种数据类型string|hash|zset|set|list 5种最常用
        
        type: 查看key的类型： type k1 -> string
        
        exists k1 -> 1表示存在，0表示不存在
        
        del k1 -> 删除k1, 返回1表示删除成功
        
        randomkey -> 随机返回一个key
        
        expire k2 30 -> 设置k2在30s后过期，设置成功返回true
        
        ttl k2  -> 查看k2还有多少秒过期， -2表示已经过期，-1表示永不过期
        
        renamenx k2 lkj ->重命名k2为lkj，nx后缀表示只有当lkj不存在时才能执行成功，如果不带nx，如果lkj存在则覆盖
      
        
        一.string：本质是bytes数组，对象、字符串都能存
        set k1 v1
        get k1
        
        append k1 new ->追加new到k1对应的value的后面: v1new
        
        strlen k1 -> 获取k1对应value的长度:5
        
        setnx k1 v1 -> 所有带nx的都要求新的不存在，否则返回0表示操作失败
        
        incr k1 -> k1对应的数字自增1，必须是数字
        decr k1 -> k1对应的数字自减1
        incrby key num -> key对应的value自增num
        decrby key num -> key对应的value自减num
        
        mset k1 v1 k2 v2 ... : set的批操作  建议使用的操作
        mget k1 k2 k3 ... : get的批操作  建议使用的操作
        
        msetnx k1 v1 ... : 不存在才能操作成功
        
        getrange k1 1 4: 获取k1对应的value的1-4位置
        setrange key 起始索引 value
        
        getset key value : 以旧换新，同时获取旧值
        
        二、list：类似双向链表,双向栈，从两边向中间压栈和弹栈
        有正序和逆序的概念，有压栈和弹栈的概念
        [0,-1]  闭区间
        
        lpush/rpush key value1 value2 ...
        lpop/rpop
        lrange key start stop : 查看 start到stop的值
        
        lrem key count value: 在key这个list下删除count个value
        
        lset key index value: 设置list的某个索引的位置的value
        
        ltrim key start stop : 从两边删除list，直至剩下start->stop的那些索引值
        
        三、set:不重复的单列集合
        sadd:向集合中添加元素
        
        smembers：查看所有元素
        
        scard: 查看元素个数
        
        srem: 删除指定元素
        
        srandmember:随机返回若干元素,有放回
        
        spop:随机返回若干元素,无放回
        
        s+交集inter/并集union/差集diff
        
        四、zset:可以排序的set(sorted set)，带排序的set
        zadd subject 100 math 90 chinese 95 music 75 english
        zscore subject math
        zrange subject 0 -1 withscores: 输出排序后的zset带分数
        zrangebyscore key min max withscores:
        zcard key:统计元素个数
        zcount key min max:
        zrem key member:删除指定元素
        zrank key member:返回指定元素的排名，默认asc
        
        五、hash操作:value存储的是field:value的格式
        hset key field value:
        hmset key field1 value1 field2 value2 ...:
        hlen key:查看长度
        hkeys key:查看所有的key
        hvals key:查看所有的valu
        hmget key field1 field2 ...:
        hexists key field:查看字段是否存在
        
        配置文件：
        bind 127.0.0.1 hadoop101:限定只能这个ip访问redis-server
        daemonize yes:守护线程运行
        loglevel notice:日志级别
        logfile "": daemonize模式下发到/dev/null
        requirepass foobared:设置密码
        
        rdb持久化和aof持久化：
        目的：redis在内存中工作，写的数据都是保存在内存中，一旦redis服务端进程崩溃，断电可能会丢失数据
            需要提供一种机制可以将内存中的数据定期保存到磁盘，保证数据安全
            
        rdb:快照备份（全盘备份）默认开启的模式
        备份：每间隔一段时间，将redis服务端内存中的所有数据以快照形式保存到磁盘
        单独创建一个fork子进程来进行持久化，复制一个子进程，会先将数据写入临时文件，待持久化过程都结束了，再用这个临时文件
        替换上次持久化好的文件，整个过程中主进程是不需要进行IO操作的，这就确保了极高的性能。如果需要进行大规模数据的恢复，且对
        数据恢复的完整性不是非常敏感，使用rdb比aof要更加高效。
        子进程和主进程共享CPU、内存、IO、程序计数器等所有资源
        子进程干活时会将主进程阻塞，阻塞的目的是为了让子进程更快地完成备份。
        快照文件存放位置：dir ./ 默认为redis-server启动的位置，在哪个路径启动redis服务，在哪存放rdb快照文件
        快照文件的名字： dbfilename: dump.rdb
        触发机制：每间隔一段时间触发一次
            主动触发： ①save(主进程不阻塞) \ bgsave(主进程不阻塞，更慢一些)
                      ②shutdown
                      ③flushall，触发备份为空文件，因为flushall本身清空所有数据
            被动触发： save <seconds> 且 <changes>
                      seconds 距离上一次备份的时间
                      changes 距离上一次备份的写操作次数
                      多个save之间是或的关系
        rdbcompression: 可以设置rdb文件的压缩，使用lzf算法
        rdbchecksum yes: 检查校验和，通过两次计算结果对比数据是否发生了变化，CRC64
        stop-writes-on-bgsave-error yes: 当bgsave备份出错时，禁止继续向redis写数据

        恢复：redis服务端在启动时，会从配置的指定目录中读取快照文件，将快照文件中存放的数据加载到内存中
        缺点：最后一次持久化后的数据有可能会丢失
    
        aof：日志备份(增量备份)，需要手动开启
        备份：aof将写操作的命令记录到一个日志文件中
        恢复：redis服务端进程启动，读取aof日志文件，从头到尾依次将所有记录的写操作命令执行一遍，重建数据！
        
        aof缺点：保存后的aof文件过大，占用过多的磁盘空间，日志文件过大时，恢复的速度慢
        
        相关参数：appendonly no:是否开启aof，默认no
                 appendfilename "appendonly.aof"
                 
        *3 $3 set $2 k1 $2 v2 : aof文件的格式
        如果手动修改aof文件错误，可以尝试redis-check-aof --fix appendonly.aof 尝试修复aof文件
        
        如果既开启rdb又开启aof，那么按照aof恢复数据，因为aof比rdb安全，rdb丢失的数据更多！
        
        备份机制：
        appendfsync always:  一旦buffer中来一条记录一条写操作
        appendfsync everysec:每秒都记录一下写操作，最多丢失一秒的数据！！！
        appendfsync no:      交给操作系统来判定是否记录写操作
        
        在重写时是否能够继续向aof文件中追加写记录
        no-appendfsync-on-rewrite no:在不影响数据安全的情况下，对写操作进行合并重写来尽可能减小aof文件大小来减少恢复数据的时间
        可以配置aof文件每翻一倍大小重写一次，或者达到64M重写一次
        
        rdb和aof对比
        优点：
        rdb：备份的文件紧凑，节省空间，备份的速度快，恢复速度快
        aof: 安全，默认最多丢失1s数据，aof日志人类可读可写，可以用来恢复一些致命操作！！！
        
        缺点：
        rdb: 不安全，有可能丢失最后一次备份后的数据
        aof: 日志冗余过多，占用磁盘空间多，数据过多时，恢复时间长！
        
        用redis做什么？
        redis可以用作数据库，缓存，消息中间件
        作为缓存来用，追求性能，因此开启rdb就行，或者连rdb都不需要开启
        作为数据库来用，追求安全，一般需要同时开启rdb和aof
        
        Redis中的事务，是将多个命令组队，一次性提交，防止有多个连续的命令被其他客户端发送的命令打断
        
        常用操作：multi:开启事务    exec:提交事务    discard:取消事务
        注意：redis中的事务不具有原子性，一个事务队列中每条命令才是原子性的，队列中一条命令是原子性，整个队列不是原子性的
        
        悲观锁：执行操作前假设当前操作有很大几率会被打断，基于这个假设，会在操作前将相关资源锁定，不允许操作期间的其他操作
        典型的如Synchronized就是悲观锁。对于共享数据的操作无论是读还是写都加锁，适合少读多写的场景
        
        乐观锁：执行操作前假设当前操作不会被打断。基于这个假设，在做操作之前不会加锁，万一发生了其他操作的干扰，放弃本次操作。
        基于版本号check and set,在写操作时检查版本号，在正式执行写操作之前比较一下版本号，如果版本号不一致就放弃本次写操作
        乐观锁适合多读少写或只读不写的场景。
        redis只提供乐观锁： watch key: 为key加锁  unwatch key:解除锁
        乐观锁也叫CAS锁，叫check and set
        
        redis主从复制：
        目的：分摊主机读压力；避免单点故障
        实现：配从不配主，在从机上使用slaveof 主机ip 主机port
        原理：一旦从机认主后，会向主机立刻发送sync命令，主机收到之后，执行rdb全盘备份，将备份后的快照发送给从机，从机
        加载快照，恢复数据。之后主机新收到的写命令也会同步给从机，从机通过执行命令保证和主机数据的一致性！
        原则：主从复制的机器，数据必须是一致的！
        常用命令：
            slaveof 主机ip 主机port：配置主从关系
            slaveof no one: 取消从机身份
            info replication：查看主从状态
            
        哨兵：哨兵是一个服务，不是用来存储数据的，用来监控主机和从机的状态，一旦主机宕机，从从机中选择一个提升为主机
        原理：
            sdown：当前哨兵认为机器宕机
            odown: 所有哨兵sdown的数量满足投票数，机器会标识为odown
        主从切换过程：
            ①当哨兵发现有主机sdown之后，向其他哨兵发送请求，请求确认主机的状态
            ②一旦sdown状态满足投票数，主机会标识为odown
            ③哨兵进行选举，选举哨兵的leader
            ④由哨兵的leader，选取从机提升为主机：a.slave-priority b.offset c.pid
            ⑤其他在线的从机，重新确认新的主机
            ⑥如果之前的主机恢复，必须认新的主机为主机，自己变为从机
        
        集群模式：
            目的：集群需要扩容，分摊主机的写压力
         实现：启动多个redis的实例（至少6个）,多个实例都开启集群模式(cluster-enabled yes)
         使用redis—trib.rb，将多个启动的实例组成一个集群！
         组成集群时，集群采取了slot（插槽）的设计，一共有16384个slot，每个slot需要平均分配到多个主机
         
         读写：
            写：每个key在写入集群之前，先通过crc16(key) % 16384,计算此key对应的slot号，根据slot号，找到映射的对应的主机，请求
            主机执行写入！
            读：先通过crc16(key) % 16384,计算此key对应的slot号，根据slot号，找到映射的对应的主机，请求主机执行读取操作！
            
            如果希望能够自动切换到对应的主机，使用客户端重定向模式: redis-cli -c
            
            多key操作： mset {xx}key1 {xx}key2 ... 需要加上前缀，并且前缀要一样
            
            集群可以自动进行主从切换，如果一段slot所对应的主机和从机都宕机，集群是否还可以提供服务取决于参数
            cluster-require-full-coverage,为true，集群不能继续提供服务，否则是可以的
         
         优缺点：配置简单
         缺点：3.0不支持多键操作，事务，Lua脚本，迁移成本高。
         
         
         补充：redis最普遍的应用场景还是用作缓存，将一些复杂查询的结果放到缓存中，使得这类请求能够快速响应。
         redis只支持乐观锁，因此适合频繁的读不适合写操作。
         在高并发场景下redis通常会用作高并发请求与数据库之间的缓冲。
         
         redis存在的问题：
         缓存穿透：是指查询一个一定不存在的数据，由于缓存没有命中时会去查询数据库，查不到数据则不写入缓存，这将导致每次这种查不到
         数据的请求都要再去数据库查询一遍，造成缓存穿透
         解决措施：①将空对象也缓存起来，并给他设置一个很短的过期时间，最长不超过5分钟
                  ②采用布隆过滤器，将所有可能存在的数据hash到一个足够大的bitmap中，一个一定不存在的数据会被这个bitmap拦截掉，从
                  而避免了对底层存储系统的查询压力
         缓存雪崩：如果缓存集中在一段很短的时间内过期，发生大量的缓存穿透，所有的查询都跑去数据库查询，就会造成数据库的访问压力短
         期内剧增。
         解决措施：将缓存过期的时间错开不要分布在一个时间点上。
         缓存击穿：如果某个key的热点非常高，不停地扛着高并发，当这个key过期瞬间，高并发操作就落到了数据库上，导致数据库瘫痪
         解决措施：可以设置key永不过期
         
         另外：数据库和缓存的双写一致性问题也需要关注
         
         
        
    * */
}
