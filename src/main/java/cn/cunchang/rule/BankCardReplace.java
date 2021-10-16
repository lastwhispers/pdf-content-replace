package cn.cunchang.rule;

import cn.cunchang.util.DesensitizedUtils;

/**
 * 银行卡替换规则
 *
 * @author cunchang
 * @date 2020/9/3 上午12:30
 */
public class BankCardReplace implements ReplaceRule {

    private final int shortBankCard = 10;

    @Override
    public String execute(String source, Float weight) {
        // 特殊银行卡长度 630442905 =》 630****05
        if (source.length() <= shortBankCard) {
            return DesensitizedUtils.around(source, 3, 2);
        }
        return DesensitizedUtils.bankCard(source);
    }

    public static void main(String[] args) {
        BankCardReplace bankCardReplace = new BankCardReplace();
        System.out.println(bankCardReplace.execute("630442905", 0.0f));
    }
}
