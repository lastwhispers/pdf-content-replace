package cn.cunchang.rule;

/**
 * 符合等比例替换
 *
 * @author cunchang
 * @date 2020/9/3 上午1:20
 */
public class SymbolReplace implements ReplaceRule {

    /**
     * 填充符
     */
    private String symbol;

    /**
     * 填充比例scale，每隔scale填充一个symbol
     * 1.比如source=571911791710403，source在pdf的weight=150
     * 2.将source替换成target=*************** (15个星)
     * 3.scale=10=150/15计算得来
     */
    private float scale;

    public SymbolReplace() {
        symbol = "*";
        scale = 4.2F;
    }

    public SymbolReplace(String symbol, float scale) {
        this.symbol = symbol;
        this.scale = scale;
    }

    @Override
    public String execute(String source, Float weight) {
        // 向下取整
        double len = Math.floor(weight / scale);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(symbol);
        }
        return sb.toString();
    }

}
