# 概述
 ------
 &emsp;&emsp;**Spring Data** 是SpringSource基金会下的一个用于简化数据库访问，并支持云服务的开源框架。其主要目标是使得数据库的访问变得方便快捷，并支持map-reduce框架和云计算数据服务。对于拥有海量数据的项目，可以用Spring Data来简化项目的开发。
 
### 下面是Spring Data的组件

- [x]  **Spring Data Commons**  (Spring Data的核心包, 提供 Repository , CrudRepository PagingAndSortingRepository 等核心接口)

- [x]  **Spring Data JPA** (基于Hibernate开发的一个JPA框架)

- [x]  **Spring Data Elasticsearch**  

- [x]  **Spring Data Redis**

- [x]  **Spring Data REST**

- [x]  **Spring Data MongoDB**

- [ ]  Spring Data KeyValue

- [ ]  Spring Data LDAP

- [ ]  Spring Data Gemfire

- [ ]  Spring Data for Apache Cassandra

- [ ]  Spring Data for Apache Solr

- [ ]  Spring Data Couchbase (community module)

- [ ]  Spring Data Neo4j (community module)
 ------

## Spring Data JPA
> SpringData中最核心的概念就是Repository，Repository是一个抽象的接口，用户通过该接口来实现数据的访问

> * Repository：最顶层的接口，是一个空的接口，目的是为了统一所有Repository的类型，且能让组件扫描的时候自动识别。
> * CrudRepository ：是Repository的子接口，提供CRUD的功能
> * PagingAndSortingRepository：是CrudRepository的子接口，添加分页和排序的功能
> * JpaRepository：是PagingAndSortingRepository的子接口，增加了一些实用的功能，比如：批量操作等。
> * JpaSpecificationExecutor：用来做负责查询的接口
> * Specification：是Spring Data JPA提供的一个查询规范，要做复杂的查询，只需围绕这个规范来设置查询条件即可

###JPA 快速入门
> 第一步: 导依赖 注册Bean

```xml
  <!--spring-boot-starter-data-jpa包含spring-data-jpa、spring-orm 和 Hibernate 来支持 JPA
    SpringBoot 会根据导入的依赖通过 @EnableAutoConfiguration 注解自动注入 JPA 实例  -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
```
    
> 第二步: 继承 Repository 系列接口
```java
public interface UserRepostitory extends JpaRepository<User, Integer>, /*用户多条件查询*/ JpaSpecificationExecutor<User> {

    // 通过ID查询
    User findById(Integer id);
    
    // 通过年龄区间查询
    List<User> findByAgeBetween(int min,int max)

    // 用户名查询
    List<User> findByUsername(String username);

    //修改 (需要  @Modifying  配合 ,必须要有 事物, 不然报错)
    @Transactional //为方便起见,事物暂时写在 dao 层
    @Modifying //注解完成修改操作（注意：不支持新增）
    @Query(value = "update User  u set  u.age=?2 where u.id =?1")
    void updateAge(Integer id, Integer age);
}
 
```    
> 第三步: 调用方法
```java
    @Autowired
    private UserRepostitory userRepostitory;
```
>  **扩展查询**
   **CrudRepository**
     &emsp;&emsp;接口实现了save、delete、count、exists、findOne等方法，方法的意思从名字上很容易理解，继承这个接口时需要两个模板参数T和ID，T就是你的实体类（对应数据库表），ID就是主键。
    **PagingAndSortingRepository**
     &emsp;&emsp;除了CrudRepository提供的方法外还提供了分页和排序两种方法，T和ID的意思与CrudRepository相同。
    如果觉得curdrepository提供的查询不符合要求，可以继承该接口进行扩展，Spring Data JPA为此提供了一些表达条件查询的关键字，大致如下：

> * And--- 等价于SQL中的and 关键字，比如findByUsernameAndPassword(String user, Striang pwd)；

> * Or--- 等价于SQL中的or 关键字，比如findByUsernameOrAddress(String user, String addr)；

> * Between--- 等价于SQL中的between 关键字，比如 findBySalaryBetween(int max,int min)；

> * LessThan--- 等价于SQL中的"<"，比如 findBySalaryLessThan(int max)；

> * GreaterThan--- 等价于SQL中的">"，比如 findBySalaryGreaterThan(intmin)；

> * IsNull--- 等价于SQL中的"is null"，比如 findByUsernameIsNull()；

> * IsNotNull--- 等价于SQL中的"is not null"，比如 findByUsernameIsNotNull()；

> * NotNull--- 与IsNotNull等价；

> * Like--- 等价于SQL中的"like"，比如 findByUsernameLike(String user)；

> * NotLike--- 等价于SQL中的"not like"，比如 findByUsernameNotLike(Stringuser)；

> * OrderBy--- 等价于SQL中的"order by"，比如findByUsernameOrderBySalaryAsc(String user)；

> * Not--- 等价于SQL中的"！ ="，比如 findByUsernameNot(String user)；

> * In--- 等价于SQL中的"in"，比如findByUsernameIn(Collection<String> userList)，方法的参数可以是 Collection类型，也可以是数组或者不定长参数；

> * NotIn--- 等价于SQL中的"not in"，比如findByUsernameNotIn(Collection<String> userList)，方法的参数可以是 Collection类型，也可以是数组或者不定长参数；

 