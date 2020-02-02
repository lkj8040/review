package bigdata;

public class git {
    
    /*
    顺便回顾一下shell编程：
    * $0:脚本名
    * $1-$n:第n个参数，${10}
    * $#:参数个数
    * $*和$@:参数列表，加了""， $*是"1 2 3"， $@是"1" "2" "3"
    * $?:上一条命令的执行返回值，返回0表示执行成功！
    *
    * 整数运算：$((运算式))  $[运算式]
    * 逻辑运算：[ 逻辑表达式 ] 非空即为true
    *       数值比较： -le -ge -lt -gt -eq -ne
    *       字符串比较： =
    *       是否有权限：-r -w -x
    *       文件类型判断：-f -d -L -e
    * if语句：
    * if [ 条件判断 ]
    * then  xxx
    * elif [ 条件判断 ]; then xxx
    * fi
    *
    * case语句：
    * case 变量 in
    * 值1) xxx;;
    * 值2) xxx;;
    * ...
    * *) xxx;;
    * esac
    *
    * for语句：
    * for(())
    * do
    *   xxx
    * done
    *
    * for 变量 in 集合
    * do
    *   xxx
    * done
    *
    * while语句：
    * while [ 条件判断 ]
    * do
    *   xxx
    * done
    *
    * 函数：
    * basename 路径：获取指定路径的基本名
    * dirname 路径：获取指定路径的父路径
    * read -p 控制台输出的内容 -t 时间 变量名：读取用户输入，赋值给变量
    * 自定义函数：
    * function 函数名[()]
    * {
    *   函数体
    * }
    * 调用： 函数名 参数列表
    *
    * 常用的文件处理工具：
    * wc:单词统计
    * 使用： wc [参数] 文件
    *           -l: 行数
    *           -w: 单词数
    *           -c: 字节数
    *           -m: 字符数
    * cut: 裁切指定内容的输出
    * 使用： cut [参数] 文件
    *              -d : 指定分隔符
    *              -f : 指定哪些列
    *              n- : 第n列到最后一列
    * sed: 读入文件，按行处理，输出到屏幕
    * 使用：sed [参数] 'command' 文件
    *           -e:如果在sed中同时进行多个动作，每个动作都需要跟-e参数
    *           常用的动作：
    *               a 添加
    *               d 删除
    *               s 替换
    * awk：强大的文本分析工具，读入文件，按行处理，指定对应的动作
    * 使用： awk [参数]  'parttern {command}' 文件
    *              -F: 指定分隔符
    *              -v: 定义一个变量
    *       内置的关键字：
    *               print：向屏幕输出
    *               $1-$n：第n列
    *               BEGIN：初始化代码块
    *               END：结束代码块
    *               FILENAME：文件名
    *               NR：行号
    *               NF：列号
    *
        
        git的三区设计：工作区、暂存区、本地库
        git config: git配置参数
        git status：查看git状态
        git add 文件：添加到暂存区
        git commit 文件：提交到本地库
        git commit -m "4th edition" 文件：提交到本地库，带版本信息
        git checkout -- 文件：从本地库中获取最新版本，会覆盖工作区中的版本
        git log --pretty=oneline: 查看所有版本的简洁信息
        git reset --hard HEAD~n: 回退n个版本
        git reflog: 查看所有的历史记录
        git reset --hard bfaeae4: 回退到某个具体的版本号
        git diff 文件：和暂存区对比
        git diff head 文件：和本地库对比
        
        分支的概念
        git branch -v: 查看分支
        git branch dev:创建分支dev
        git checkout dev:切换到分支dev
        git merge dev:合并分支dev
        分支冲突：需要解决冲突，再提交，提交时不能写文件名！！！
        
        
        上传本地仓库到github
        步骤：本地仓库；github上的仓库；配置本地和远程的网络连通和权限验证；执行上传命令
        SSH: 使用加密的秘钥对传输的数据进行加密和解密！
            特点：免输入密码进行登录！
        ssh-keygen -t rsa:A机器创建秘钥对，将公钥拷贝到B机器上，A机器可以远程访问B机器
        ssh -T git@github.com：测试是否网络连通
        
        git remote -v ： 查看有哪些代号可以使用
        git remote add <代号> <ssh地址> :相当于给地址起了个别名
        git push -u <ssh地址> <远程仓库的分支名> | git push -u <代号> <远程仓库的分支名>
        
        git fetch <代号> <远程仓库的分支名>:不合并
        git pull <代号> <远程仓库的分支名>: 合并 = fetch + merge
        
        git clone <远程url>
        
        github操作：
        fork
        pull request
        merge




*/
}
