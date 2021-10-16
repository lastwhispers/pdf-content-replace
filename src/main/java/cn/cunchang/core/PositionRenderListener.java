
package cn.cunchang.core;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.util.Set;

/**
 * pdf渲染监听，当找到渲染的文本时，得到文本的坐标x,y,w,h
 *
 * @author cunchang
 * @date 2020/9/4 上午10:45
 */
public class PositionRenderListener extends ResultRenderListener {

    /**
     * 出现key的标识，说明下一个值就是替换的目标
     */
    private boolean keyFlag = false;

    /**
     * 目标关键字的，前一个关键字
     */
    private Set<String> preKeySet;

    /**
     * 目标关键字
     */
    private String preKey;

    public PositionRenderListener() {
    }

    public PositionRenderListener(Set<String> preKeySet) {
        this.preKeySet = preKeySet;
    }

    public PositionRenderListener(Set<String> preKeySet, float fontSize) {
        this.preKeySet = preKeySet;
        super.defaultH = fontSize;
    }

    @Override
    public void renderText(TextRenderInfo textInfo) {
        String text = textInfo.getText();
        System.out.println("listener text:"+text);
        if (keyFlag) {
            // bound 块内容
            Rectangle2D.Float bound = textInfo.getBaseline().getBoundingRectange();
            ReplaceRegion region = new ReplaceRegion();
            region.setPreKey(preKey);
            region.setValue(text);
            region.setH(bound.height == 0 ? this.defaultH : bound.height);
            region.setW(bound.width);
            region.setX(bound.x);
            region.setY(bound.y);
            result.add(region);
            keyFlag = false;
        }
        if (preKeySet.contains(text)) {
            keyFlag = true;
            preKey = text;
        }
    }


    @Override
    public void beginTextBlock() {
    }

    @Override
    public void endTextBlock() {
    }

    @Override
    public void renderImage(ImageRenderInfo imageInfo) {
    }
}