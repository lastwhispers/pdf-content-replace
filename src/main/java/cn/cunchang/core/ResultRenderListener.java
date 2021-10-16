package cn.cunchang.core;

import com.itextpdf.text.pdf.parser.RenderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录遍历到的结果集
 * @author cunchang
 * @date 2021/10/15 10:41 上午
 */
public abstract class ResultRenderListener implements RenderListener {

    /**
     * 结果集
     */
    protected final List<ReplaceRegion> result = new ArrayList<>();

    /**
     * 极有可能获取不到目标文本的高度，所以需要设置默认高度
     * <p>
     *     一般情况目标文本高度等于 cn.lastwisper.core.PdfReplacer的font size
     * </p>
     */
    protected float defaultH;


    public float getDefaultH() {
        return defaultH;
    }

    public void setDefaultH(float defaultH) {
        this.defaultH = defaultH;
    }

    public List<ReplaceRegion> getResult() {
        return this.result;
    }
}