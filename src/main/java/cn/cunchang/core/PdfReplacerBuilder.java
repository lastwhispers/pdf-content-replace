package cn.cunchang.core;

import cn.cunchang.rule.ReplaceRule;
import cn.cunchang.util.CollectionUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cunchang
 * @date 2020/9/4 上午10:45
 */
public class PdfReplacerBuilder {
    /**
     * 关键字与替换规则映射
     */
    private final Map<String, ReplaceRule> replaceRuleMap = new HashMap<>();

    /**
     * pdf解析时回调的监听器，通过监听器可以获取pdf的完整内容，并进行记录坐标
     */
    private ResultRenderListener resultRenderListener;

    /**
     * 输入pdf 输入流
     */
    private InputStream sourcePdfInput;
    /**
     * 输出pdf 输出流
     */
    private OutputStream targetPdfOutput;
    /**
     * 替换的字体
     */
    private Font font;

    public PdfReplacer build() throws IOException, DocumentException {
        if (CollectionUtils.isEmpty(this.replaceRuleMap)) {
            throw new IllegalArgumentException("初始化替换规则映射不能为空!");
        }
        PdfReplacer pdfReplacer = new PdfReplacer();
        pdfReplacer.setReplaceRuleMap(this.replaceRuleMap);
        pdfReplacer.setInput(this.sourcePdfInput);
        pdfReplacer.setOutput(this.targetPdfOutput);
        if (this.font == null) {
            setDefaultFont();
            System.out.println("未设置字体，使用默认字体");
        }
        pdfReplacer.setFont(this.font);
        if (this.resultRenderListener.getDefaultH() == 0F) {
            this.resultRenderListener.setDefaultH(font.getSize());
            System.out.println("未设置关键字默认高度，使用字体高度");
        }
        pdfReplacer.setResultRenderListener(this.resultRenderListener);

        pdfReplacer.init();
        return pdfReplacer;
    }

    public PdfReplacerBuilder setSourcePdf(String sourcePdf) throws FileNotFoundException {
        this.sourcePdfInput = new FileInputStream(sourcePdf);
        return this;
    }

    public PdfReplacerBuilder setTargetPdf(String targetPdf) throws FileNotFoundException {
        this.targetPdfOutput = new FileOutputStream(targetPdf);
        return this;
    }

    public PdfReplacerBuilder setSourcePdfFile(File sourcePdfFile) throws FileNotFoundException {
        this.sourcePdfInput = new FileInputStream(sourcePdfFile);
        return this;
    }

    public PdfReplacerBuilder setTargetPdfFile(File targetPdfFile) throws FileNotFoundException {
        this.sourcePdfInput = new FileInputStream(targetPdfFile);
        return this;
    }

    public PdfReplacerBuilder setSourcePdfInput(InputStream sourcePdfInput) {
        this.sourcePdfInput = sourcePdfInput;
        return this;
    }

    public PdfReplacerBuilder setTargetPdfOutput(OutputStream targetPdfOutput) {
        this.targetPdfOutput = targetPdfOutput;
        return this;
    }

    public PdfReplacerBuilder setReplaceRule(String name, ReplaceRule replaceRule) {
        this.replaceRuleMap.put(name, replaceRule);
        return this;
    }

    public PdfReplacerBuilder setReplaceRuleMap(Map<String, ReplaceRule> replaceRuleMap) {
        this.replaceRuleMap.putAll(replaceRuleMap);
        return this;
    }

    /**
     * 默认字体
     *
     * @param size 字体大小
     * @return PdfReplacerBuilder
     */
    public PdfReplacerBuilder setFont(float size) {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            System.err.println("缺少 itext-asian 类库，无法加载字体");
            e.printStackTrace();
        }
        this.font = new Font(bf, size, Font.BOLD);
        return this;
    }

    public PdfReplacerBuilder setFont(Font font) {
        this.font = font;
        return this;
    }

    public PdfReplacerBuilder setDefaultFont() {
        setFont(10F);
        return this;
    }

    public PdfReplacerBuilder setResultRenderListener(ResultRenderListener resultRenderListener) {
        this.resultRenderListener = resultRenderListener;
        return this;
    }

}
