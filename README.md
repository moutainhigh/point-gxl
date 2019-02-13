# 汉德森积分-德分项目

## 项目用途

用于汉薇商城后台及DTS服务提供相关的积分、德分及虚拟货币的底层调用

## 实现方式

数据源从外部传入, 通过Spring动态加载外部数据源实现, 暴露Service为 `PointContextInitialize`

主要的Service实现类有:

* PointCommService 最原始的实现, 可以同时提供积分、德分的操作， 目前仅用于DTS包，汉薇商城已经不再使用
* PointServiceApi 实现自 `TokenServiceApi`，德分操作实现类
* CommissionServiceApi 实现自 `TokenServiceApi`，积分操作实现类

各个实现方法文档请查看 `TokenServiceApi` 注释

基础的业务操作封装到 `modify` 方法中

### 业务参数解释

* cpId 三网唯一用户Id
* bizId 积分、德分业务操作Id, 大部分情况下为订单Id
* FunctionCode 对应 `gradecode` 表, 实现业务操作
* Trancd 业务描述, 见 `Trancd` 枚举

#### 基本操作

GradeNumber 目前总共有5种基本操作，分别为 **GRANT**, **CONSUME**, **FREEZE_GRANT**, **ROLLBACK**, **RELEASE** 

对应新增, 消费, 新增冻结, 回滚, 解冻5中类型, 新的业务如果可以归为这5类则可以复用代码, 代码中这5种操作分别对应5种实现

核心业务逻辑抽象为 `BasePointCommOperator`


| 操作代码     | 说明     | 代码实现                       |
|--------------|----------|--------------------------------|
| GRANT        | 新增     | GrantPointCommOperator         |
| CONSUME      | 消费     | ConsumePointCommOperator       |
| FREEZE_GRANT | 新增冻结 | FreezeGrantPointCommOperator   |
| ROLLBACK     | 回退     | RollbackPointCommOperator      |
| RELEASE      | 解冻     | FreezeReleasePointCommOperator |

**** 由于历史原因, 实现类实现不区分德分积分, 都可以支持****

#### 扣减实现

业务上扣减操作比较复杂, 需要按平台顺序扣减, 实现方式为定义扣减链, 递归扣减,
如果要修改扣减顺序, 则只需要调整扣减链顺序, 代码可以参考 `PointCommCalHelper:minus`

`detailMap` 中的Key目前没有意义, **请不要再使用**

### 单元测试

代码调整后请运行完整的单元测试, 确保老功能正常, 如果添加了新的业务, 请在 `PointOperationTest/CommissionOperationTest`
中添加新的单元测试

运行单元测试: `mvn test`

### 发布及更新代码

调试环境请使用后缀为SNAPSHOT格式, 修改 `pom.xml` 中version部分代码

```xml
<groupId>com.hds.xquark</groupId>
<artifactId>xquark-point-core</artifactId>
<!--<version>3.4.6-api</version>-->
<version>1.1-SNAPSHOT</version>
```

正式环境修改为数字+ -api格式

打包命令:

```sh
mvn clean compile
mvn deploy
```

### 附录

GradeCode表数据

```
id | categoryId | gradeNumber | gradeName    | description      | accountCodeStatus | createdDate         |
+----+------------+-------------+--------------+------------------+-------------------+---------------------+
| 1  | 100        | 1001        | GRANT        | 德分发放         | 0                 | 2018-09-22 13:44:00 |
| 2  | 100        | 1002        | CONSUME      | 德分消费（扣减） | 0                 | 2018-09-22 13:44:00 |
| 3  | 100        | 1003        | FREEZE_GRANT | 新增冻结德分     | 1                 | 2018-09-22 13:44:00 |
| 4  | 100        | 1004        | ROLLBACK     | 取消订单德分回退 | 2                 | 2018-09-22 13:44:00 |
| 5  | 100        | 1005        | ROLLBACK     | 退货订单德分回退 | 3                 | 2018-09-22 13:44:00 |
| 6  | 100        | 1006        | RELEASE      | 新增可用德分     | 0                 | 2018-09-22 13:44:00 |
| 7  | 200        | 2001        | GRANT        | 收益发放         | 0                 | 2018-09-22 13:44:00 |
| 8  | 200        | 2002        | CONSUME      | 收益消费（扣减） | 0                 | 2018-09-22 13:44:00 |
| 9  | 200        | 2003        | FREEZE_GRANT | 新增冻结收益     | 1                 | 2018-09-22 13:44:00 |
| 10 | 200        | 2004        | ROLLBACK     | 取消订单收益回退 | 2                 | 2018-09-22 13:44:00 |
| 11 | 200        | 2005        | ROLLBACK     | 退货订单收益回退 | 3                 | 2018-09-22 13:44:00 |
| 12 | 200        | 2006        | RELEASE      | 新增可用收益     | 0                 | 2018-09-22 13:44:00 |
| 13 | 200        | 2007        | CONSUME      | 收益提现         | 0                 | 2018-09-22 13:44:00 |
| 14 | 100        | 1007        | CONSUME      | 德分红包         | 4                 | 2018-12-12 20:07:01 |
| 15 | 100        | 1008        | GRANT        | 红包退回         | 5                 | 2018-12-12 20:07:01 |
| 16 | 200        | 2008        | GRANT        | 退货订单收益补偿 | 6                 | 2018-12-13 09:32:39
```
