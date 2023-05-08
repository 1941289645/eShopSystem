# 电商项目

## 技术栈

后端：springboot+mybatis
前端：vue2+element+vue-router



## 后台功能

- 管理员登录，带token加密

- 商品管理

  商品的增删查改，上架下架

- 订单管理（可发货）（待开发）

- 评论管理（待开发）

## 前台功能

- 普通用户登录，注册
- 首页：商品浏览，商品列表
- 商品详情
- 加购物车
- 选择数量计算价格下单
- 支付
- 订单列表（待支付，待发货，待评价，全部订单）
- 评论商品，收藏商品，点赞商品，我的收藏列表，我点赞过的商品列表（待开发）



## 数据库设计











## Record

此后记录开发过程

### 2023年5月8日15点37分

商品管理首页基本完成，但是图片上传功能还未开发。

修改增添商品页面可以按类别名称进行选择。



商品管理页的类别名称显示涉及到多表查询，下面是第一版多表查询功能：

首先需要在Products.java文件中添加以下字段：

~~~java
	@TableField(exist = false)
    private String categoryName;
~~~

让数据库忽略此条数据段，因为products表中没有此列，这是需要多表查询后在此存储数据的位置。

第一版多表查询是在ProductsController.java的分页查询函数下写入这些，最后返回page，这是每次查询一次分类表，将类别信息存储在每一个商品实体下，不推荐！

~~~java
        QueryWrapper<Products> queryWrapper= new QueryWrapper<>();
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByDesc("id");
        Page<Products> page=productsService.page(new Page<>(pageNum,pageSize),queryWrapper);
        List<Products> records=page.getRecords();
        for(Products record: records){
            Productcategories productcategories=productcategoriesService.getById(record.getProductCategoryId());
            if (productcategories!=null){
                record.setCategoryName(productcategories.getName());
            }
        }
~~~

第二版多表查询：

在ProductsController.java的分页查询函数下写入这些，调用productsService的接口函数，返回page。

~~~java
Page<Products> page=productsService.findPage(new Page<>(pageNum,pageSize),name);
~~~

![image-20230508154712468](README.assets/image-20230508154712468.png)

进而调用具体实现类的函数，在ProductsServiceImpl.java中：

![image-20230508154754024](README.assets/image-20230508154754024.png)

函数内部是调用mapper接口的数据库查询函数：

![image-20230508154834653](README.assets/image-20230508154834653.png)

内容是：在ProductsMapper.xml文件中，可以在Mapper中按contorl+enter自动创建sql查询语句

~~~sql
    <select id="findPage" resultType="com.example.springboot.entity.Products">
        select products.*,productcategories.name as categoryName from products,productcategories
        <where>
            products.product_category_id=productcategories.id
            <if test="name !=null and name != ''">
                and name like concat('%',#{name},'%')
            </if>
        </where>
    </select>
~~~

但是此时的sql语句并不规范，因为如果有的商品没有类别，将查询不出来，不会返回到前端，将sql语句改成以下形式：

~~~sql
    <select id="findPage" resultType="com.example.springboot.entity.Products">
        select products.*,productcategories.name as categoryName from products
            left join productcategories
            on products.product_category_id=productcategories.id
        <where>
            <if test="name !=null and name != ''">
                and name like concat('%',#{name},'%')
            </if>
        </where>
    </select>
~~~

采用左连接的方法，没有类别商品表作为主表也能查询出来，虽然商品可能必须有类别（可以在商品表中设置类别非空）。
