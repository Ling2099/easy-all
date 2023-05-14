# 链式及 λ 表达式

---

::: tip 缘由
写这个类的前提条件是我觉得编码过程中的 if else 看起来太恶心了，而 JDK 原生的 [Optional](https://www.runoob.com/java/java8-optional-class.html) 类又只能判空，从某种意义上来说不符合现状；
当然网上也有许多例子可借鉴，或许还有更好的方式或工具来实现，只是我的水平还未达到。
:::

::: warning 注意
该类不适用嵌套 if，且做得不够完善的地方为返回泛型结果的强转，后面空了再调整；另外原生的 **Optional** 类在判空的使用上简直是妙笔生花
:::

<table>
  <tr>
    <td>类名</td>
    <td>方法名</td>
    <td>入参参数</td>
    <td>返回类型</td>
    <td>功能介绍</td>
  </tr>
  <tr>
    <td rowspan="9">ChainUtils&lt;T&gt;</td>
    <td>build</td>
    <td>无</td>
    <td>ChainUtils</td>
    <td>创建该实例的单例对象</td>
  </tr>
  <tr>
    <td>chainThrow</td>
    <td>BooleanSupplier</td>
    <td>ChainUtils</td>
    <td>传入一个 boolean 值且无返回值的函数式接口,，返回当前实例，判断是否抛出异常，<b>且自定义异常提示信息</b></td>
  </tr>
  <tr>
    <td>chainThrowNonMsg</td>
    <td>BooleanSupplier</td>
    <td>ChainUtils</td>
    <td>传入一个 boolean 值且无返回值的函数式接口，返回当前实例，判断是否抛出异常，<b>但不能自定义异常提示信息</b></td>
  </tr>
  <tr>
    <td>of</td>
    <td>BooleanSupplier</td>
    <td>ChainUtils</td>
    <td>传入一个 boolean 值且无返回值的函数式接口，返回当前实例，类比 if 代码块，作为后续执行链的条件判断</td>
  </tr>
  <tr>
    <td>map</td>
    <td>Function</td>
    <td>ChainUtils</td>
    <td>接受 Boolean 类型的值，返回你所执行的代码块结果；其中 Boolean 值由 of 方法提供</td>
  </tr>
  <tr>
    <td>nonMap</td>
    <td>Consumer</td>
    <td>ChainUtils</td>
    <td>接受 Boolean 类型的值，但不返回任何结果；同样，Boolean 值由 of 方法提供</td>
  </tr>
  <tr>
    <td>orElse</td>
    <td>Function</td>
    <td>ChainUtils</td>
    <td>区别于 map 方法, 类比 else 代码块，当 of 方法的条件不成立时执行；接受 of 方法提供的 Boolean 值，返回你所执行的代码块结果</td>
  </tr>
  <tr>
    <td>orNonElse</td>
    <td>Consumer</td>
    <td>ChainUtils</td>
    <td>同样类比 else 代码块，当 of 方法的条件不成立时执行；接受 of 方法提供的 Boolean 值，但不返回任何结果</td>
  </tr>
  <tr>
    <td>get</td>
    <td>无</td>
    <td>T</td>
    <td>返回调用链完成后你想要的结果</td>
  </tr>
</table>

+ 可能上面的表格看起来有点空洞，如下例子能够形象的描述

~~~Java
    public static int add() {
        return 1 + 1;
    }

    // 有时候，我们的业务代码可能有许多判断条件，类似于如下，或许还更复杂
    public static void main(String[] args) {
        int a = 10, b = 100, res = 0;

        if (a > b) {
            String msg = "小学生都会";
            System.out.println(msg);
        } else {
            System.out.println("a < b");
        }

        String name = "张三";

        if ("李四".equals(name)) {
            throw new BusinessException("是张三，不是李四");
        }

        if (true) {
            res = add();
        }

        System.out.println("1 + 1 = " + res);
    }
~~~

+ 执行结果为

~~~Java
a < b
1 + 1 = 2
~~~

+ 然后我们换种方式，执行结果相同

~~~Java
    public static int add() {
        return 1 + 1;
    }

    public static void main(String[] args) {
        int a = 10, b = 100, res = 0;
        String name = "张三";

        res = (int) ChainUtils.build()
            .of(() -> a > b)
            .nonMap(item -> {
                String msg = "小学生都会";
                System.out.println(msg);
            })
            .orNonElse(item -> System.out.println("a < b"))
            .chainThrow(() -> "李四".equals(name))
            .get("是张三，不是李四")
            .of(() -> true)
            .map(item -> add())
            .get();

        System.out.println("1 + 1 = " + res);
    }
~~~

+ 当然我们也可能会遇到类似以下的场景

~~~Java
    @Data
    private static class User {

        private String userName;

        private String passWord;
    }

    public void check(User user) {
        if (ObjectUtil.isEmpty(user)) {
            throw new BusinessException("用户对象不能为空");
        }

        if ("张三".equals(user.getUserName())) {
            throw new BusinessException("用户名不能是张三");
        }

        if (StrUtil.isBlank(user.getPassWord())) {
            throw new BusinessException("密码不能为空");
        }

        if (user.getPassWord().length() > 10) {
            throw new BusinessException("密码太长了");
        }

        if ("123456".equals(user.getPassWord())) {
            throw new BusinessException();
        }
    }
~~~

+ 换成链式后的样子，其结果相同

~~~Java
    @Data
    private static class User {

        private String userName;

        private String passWord;
    }

    public void check(User user) {
        ChainUtils.build()
            .chainThrow(() -> ObjectUtil.isEmpty(user))
            .get("用户对象不能为空")
            .chainThrow(() -> "张三".equals(user.getUserName()))
            .get("用户名不能是张三")
            .chainThrow(() -> StrUtil.isBlank(user.getPassWord()))
            .get("密码不能为空")
            .chainThrow(() -> user.getPassWord().length() > 10)
            .get("密码太长了")
            .chainThrowNonMsg(() -> "123456".equals(user.getPassWord()))
            .call();
        }
~~~

::: warning 总结
如果你搞不懂 Lambda 表达式这块语法糖，就当看个乐呵，同时也欢迎提出建议
:::