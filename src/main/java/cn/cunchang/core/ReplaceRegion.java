
package cn.cunchang.core;

/**
 * 需要替换的区域
 *
 * @author cunchang
 * @date 2020/9/4 上午10:45
 */
public class ReplaceRegion {

    /**
     * 待替换关键词的前一个词组，用于找到value的替换规则
     */
    private String preKey;
    /**
     * 待替换文本
     */
    private String value;
    /**
     * pdf x坐标
     */
    private float x;
    /**
     * pdf y坐标
     */
    private float y;
    /**
     * pdf h坐标
     */
    private float h;
    /**
     * pdf w坐标
     */
    private float w;

    /**
     * pdf 页码
     */
    private Integer pageNum;

    /*******计算clean的矩阵位置*******/
    /*******千万别动*******/
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

    /*******计算clean的矩阵位置*******/

    public String getPreKey() {
        return preKey;
    }

    public void setPreKey(String preKey) {
        this.preKey = preKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    /**
     * 将文本居中
     */
    public Float getY() {
        return y - 1;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getH() {
        return h;
    }

    public void setH(Float h) {
        this.h = h;
    }

    public Float getW() {
        return w;
    }

    public void setW(Float w) {
        this.w = w;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}