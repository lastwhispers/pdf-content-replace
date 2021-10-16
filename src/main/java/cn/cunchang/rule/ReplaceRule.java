package cn.cunchang.rule;

/**
 * 文本替换规则接口
 *
 * @author cunchang
 * @date 2020/9/3 上午12:11
 */
public interface ReplaceRule {

    /**
     * 替换规则
     *
     * @param source 原文本
     * @param weight 原文本在PDF中的宽度
     * @return 替换后的文本
     */
    String execute(String source, Float weight);

}
