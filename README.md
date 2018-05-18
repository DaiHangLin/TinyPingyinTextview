# TinyPingyinTextview
<p>用于简单显示拼音加汉字的TextView</p>

### bug
* 暂时不支持width的wrap_content
* 渲染颜色自动滑动时，内容可见时应该不需要滑动

### 数据格式
* 汉字 List<String> 换行时以 “ ” 替换
* 拼音 List<String> 换行时以 “newLine” 作为flag

### 实现效果
* 拼音与汉字相对应
* 拼音长时，下行汉字居中
* 汉字长时，上行拼音居中
* 逐字渲染颜色，支持暂停

<img src="https://images2018.cnblogs.com/blog/596306/201804/596306-20180408165904481-1853356808.png" width="200" height="300">
</p>
<img src="https://images2018.cnblogs.com/blog/596306/201804/596306-20180408165908618-1894707176.png" width="200" height="300">

## 2018-05-18

+ 本地删除MyLibrary库
+ 通过compile 'com.dlin:TinyPingyinTextview:0.0.1'加载已经上传到jcenter的MyLibrary库
