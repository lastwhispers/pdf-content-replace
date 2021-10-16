package cn.cunchang.rule;


import cn.cunchang.util.DesensitizedUtils;

/**
 *
 * 密码替换规则
 *
 * @author cunchang
 *  @date 2020/9/3 上午12:30
 */
public class PasswordReplace implements ReplaceRule {

    @Override
    public String execute(String source, Float weight) {
        return DesensitizedUtils.password(source);
    }

}
