package cn.cunchang.util;

import org.apache.commons.lang3.StringUtils;

public class DesensitizedUtils {

    /**
     * @param value 脱敏字段
     * @param index 前面保留n位明文
     */
    public static String left(String value, int index) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        String name = StringUtils.left(value, index);
        return StringUtils.rightPad(name, StringUtils.length(value), "*");
    }

    /**
     * @param value 脱敏字段
     * @param index 前面保留n位明文
     * @param hide  保留n位密文
     */
    public static String left(String value, int index, int hide) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        int length = StringUtils.length(value);
        int size = length;
        if (length > index) {
            if (index > 0) {
                size = index + hide;
            } else {
                size = hide;
            }
        }
        String name = StringUtils.left(value, index);
        return StringUtils.rightPad(name, size, "*");
    }

    /**
     * @param value 脱敏字段
     * @param end   后面保留n位明文
     */
    public static String right(String value, int end) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(value, end), StringUtils.length(value), "*");
    }

    /**
     * @param value 脱敏字段
     * @param end   后面保留n位明文
     * @param hide  保留n位密文
     */
    public static String right(String value, int end, int hide) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        int length = StringUtils.length(value);
        int size = length;
        if (length > end) {
            if (end > 0) {
                size = end + hide;
            } else {
                size = hide;
            }
        }

        String name = StringUtils.right(value, end);
        return StringUtils.leftPad(name, size, "*");
    }

    /**
     * @param value 脱敏字段
     * @param index 前面保留n位明文
     * @param end   后面保留n位明文
     */
    public static String around(String value, int index, int end) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        return around(value, index, end, StringUtils.length(value));
    }

    /**
     * @param value 脱敏字段
     * @param index 前面保留n位明文
     * @param end   后面保留n位明文
     * @param hide  保留n位密文
     */
    public static String around(String value, int index, int end, int hide) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        if (index <= 0 && end <= 0) {
            return value;
        } else if (index > 0 && end <= 0) {
            return left(value, index, hide);
        } else if (index <= 0 && end > 0) {
            return right(value, end, hide);
        } else {
            int length = StringUtils.length(value);
            if (index + end >= length) {
                return value;
            } else {
                int size = length - index - end;
                if (size > hide) {
                    size = hide;
                }
                String left = StringUtils.left(value, index);
                String right = StringUtils.right(value, end);
                String repeat = StringUtils.repeat("*", size);
                return StringUtils.join(left, repeat, right);
            }
        }
    }

    /**
     * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
     */
    public static String chineseName(String fullName) {
        return left(fullName, 1, 2);
    }

    /**
     * 【身份证号】显示最后四位，其他隐藏。共计18位或者15位，比如：*************1234
     */
    public static String idCardNum(String id) {
        return right(id, 4);
    }

    /**
     * 【固定电话】后四位，其他隐藏，比如1234
     */
    public static String fixedPhone(String num) {
        return right(num, 4);
    }

    /**
     * 【手机号码】前三位，后四位，其他隐藏，比如135****6810
     */
    public static String mobilePhone(String num) {
        return around(num, 3, 4);
    }

    /**
     * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****
     *
     * @param sensitiveSize 敏感信息长度
     */
    public static String address(String address, int sensitiveSize) {

        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);
        return left(address, length - sensitiveSize);
    }

    /**
     * 【电子邮箱】邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com>
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1) {

            return email;
        } else {
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*")
                    .concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }
    }

    /**
     * 【银行卡号】前六位，后四位，其他用星号隐藏每位1个星号，比如：6222600**********1234>
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return around(cardNum, 6, 4);
    }

    /**
     * 【密码】密码的全部字符都用*代替，比如：******
     */
    public static String password(String password) {
        if (StringUtils.isBlank(password)) {
            return "";
        }
        String pwd = StringUtils.left(password, 0);
        return StringUtils.rightPad(pwd, StringUtils.length(password), "*");
    }
}