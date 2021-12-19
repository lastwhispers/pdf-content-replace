# 源码

源码：https://github.com/lastwhispers/pdf-content-replace


# 能干什么

基于java语言itext库，实现pdf内容替换。
比如将银行电子回执单PDF内的收款账号、付款账号进行脱敏替换。

<img src="https://github.com//lastwhispers/pdf-content-replace/raw/master/asset/pdf.png" alt="pdf" style="zoom: 50%;" />

目前我也仅仅在银行电子回执单中使用过，支持如下银行

- 网商银行、平安银行、光大银行、招商银行、民生银行、天津金城银行、建设银行、广发网上银行、中国建设银行

# 怎么使用

以招商银行的收付款账号脱敏为例

1. 把代码copy到你项目里面用，引入itext依赖，如果itext相关依赖下不下来就下源码本地打包

2. 配置收付款账号的脱敏规则上下文 bankRuleMap，比如`付款账号：123456999990503`，前一组关键词就是`付款账号：`，真正要替换的内容是`123456999990503`，bankRuleMap就设置为`bankRuleMap.put("付款账号：", new BankCardReplace());`
3. 其他代码照抄，运行即可

```java

/**
 * @author cunchang
 * @date 2020/9/3 上午2:06
 */
public class Client {

    static Map<String, ReplaceRule> bankRuleMap = new HashMap<>();

    static {
      	// 第一步：配置收付款账号的脱敏规则上下文 bankRuleMap
        ReplaceRule bankCardReplace = new BankCardReplace();
        // 招商银行
        bankRuleMap.put("付款账号：", bankCardReplace);
        bankRuleMap.put("收款人账号：", bankCardReplace);
    }

    public static void main(String[] args) throws IOException, DocumentException {
        String source = "/Users/cunchang/Downloads/receipt/source.pdf";
        String target = "/Users/cunchang/Downloads/receipt/target.pdf";

        // 第一步配置监听器，构建PdfReplacer
        PositionRenderListener positionRenderListener = new PositionRenderListener(bankRuleMap.keySet());
        PdfReplacer pdfReplacer = new PdfReplacerBuilder()
                .setReplaceRuleMap(bankRuleMap)
                .setSourcePdf(source)
                .setTargetPdf(target)
                .setResultRenderListener(positionRenderListener)
                .build();
        try {
            // 第二步执行
            pdfReplacer.process();
        } finally {
            // 第三步回收资源
            pdfReplacer.close();
        }
    }
}
```

# 怎么干的

1. 遍历pdf中每一页每一项内容
2. 找到替换的关键字，记录其在pdf文件中的所占内容矩形四个角坐标，
3. 最后先将内容抹除掉，然后写入脱敏的内容。

使用itextpdf5实现上述操作

第一步：主要通过com.itextpdf.text.pdf.parser.PdfReaderContentParser实现

第二步：主要通过com.itextpdf.text.pdf.parser.RenderListener实现，Parser遍历时会回调监听器，拿到内容

第三步：主要通过com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpProcessor实现，这一步比较关键，要对pdf中指定区域进行销毁。可以看一下itext的官方demo [How to remove text from a PDF?](https://kb.itextpdf.com/home/it7kb/faq/how-to-remove-text-from-a-pdf)



# 怎么扩展

**替换规则不满足需求**

实现cn.lastwisper.rule.ReplaceRule，加载自己的规则即可

**记录关键字的监听器不满足需求**

实现cn.lastwisper.core.ResultRenderListener，build时设置进去即可

**清除区域坐标不准**

cn.cunchang.core.ReplaceRegion类，修改Llx、Lly、Urx、Ury的偏移量(where `ll` stands for lower-left and `ur` stands for upper-right)

```java
public float getLlx() {
  return x;
}
public float getLly() {
  return y - 2;
}
public float getUrx() {
  return x + w - 2;
}
public float getUry() {
  return y + h - 2;
}
```

# 有点坑



## 已知bug

com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpRenderListener.processImage方法有bug，itext5依赖了apache image库，这个库已经十几年没更新了

```java
org.apache.commons.imaging.ImageReadException: Invalid marker found in entropy data
```

如果你也出现这样的问题，可以使用jdk自带的`ImageIO.read`做备选方案
![ImageIO.read](https://img-blog.csdnimg.cn/img_convert/1e3bb0830800d292923e5628cec9ef0a.png)

## itextpdf 的开源协议是AGPL！！！！！！

itextpdf 的协议是AGPL，这个需要注意！！！！！！

[企业内部的项目中引用了使用AGPL许可的软件,需要公开源代码么?](https://www.zhihu.com/question/20197892)

[itextpdf 协议和收费要求](https://kb.itextpdf.com/home/it5kb/faq/is-itext-java-library-free-of-charge-or-are-there-any-fees-to-be-paid)



# 参考
https://blog.csdn.net/sishenkankan/article/details/53107195







