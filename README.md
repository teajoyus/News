# News
修改isUserLogin的bug、标签显示返回user的标签
@teajoyus
teajoyus committed on 8 Feb 2017
9a1b6e1  
Commits on Oct 25, 2016
为了配合Gson解析而定义的类，该类可自动解析出返回的json格式。可以解析出message、flag、data
@teajoyus
teajoyus committed on 25 Oct 2016
5165717  
单元测试时的工具类
@teajoyus
teajoyus committed on 25 Oct 2016
75037d1  
在类前面添加@Deprecated注解，表示此类不再被推荐使用
@teajoyus
teajoyus committed on 25 Oct 2016
7ec0de9  
吧无参构造方法自动将id和currentlabel设置为""。继承Message类来配合Gson的解析
@teajoyus
teajoyus committed on 25 Oct 2016
3c22fe9  
在Respone方法加入Gson框架自动解析json。
@teajoyus
teajoyus committed on 25 Oct 2016
bf1b4d3  
增加 User getUser(); 来为Model提供参数，才不用靠presenter传参数进来
@teajoyus
teajoyus committed on 25 Oct 2016
2e1cb03  
增加 User getUser(); 来为Model提供参数，才不用靠presenter传参数进来
@teajoyus
teajoyus committed on 25 Oct 2016
8b2da4c  
把相关方法变成无参数。吧请求参数放在View里面的getUser来获取
@teajoyus
teajoyus committed on 25 Oct 2016
d5974d3  
把相关方法变成无参数。吧请求参数放在View里面的getUser来获取
@teajoyus
teajoyus committed on 25 Oct 2016
7c0203e  
调用RegistPresenter的方法变成无参调用，除了有关验证码的方法
@teajoyus
teajoyus committed on 25 Oct 2016
389a189  
调用NewsPresenter的方法变成无参数的方法，除了调用搜索新闻的方法
@teajoyus
teajoyus committed on 25 Oct 2016
1687e85  
Commits on Oct 19, 2016
添加user-sdk min=14 max=23
@teajoyus
teajoyus committed on 19 Oct 2016
3caff3c  
删除掉图片的默认图test
@teajoyus
teajoyus committed on 19 Oct 2016
ef32b07  
修改imageload的初始化配置
@teajoyus
teajoyus committed on 19 Oct 2016
df41299  
修改和添加注释
@teajoyus
teajoyus committed on 19 Oct 2016
cfb80ae  
1、把getView拆分成几个封装方法，使得getview代码不会太多；  …
@teajoyus
teajoyus committed on 19 Oct 2016
50e2f6f  
Commits on Oct 16, 2016
1、修复新闻列表图片显示被压缩的问题  …
@teajoyus
teajoyus committed on 16 Oct 2016
789c860  
Commits on Oct 15, 2016
修改了一个测试
@teajoyus
teajoyus committed on 15 Oct 2016
b3e5ae4  
Initial commit
@teajoyus
teajoyus committed on 15 Oct 2016
