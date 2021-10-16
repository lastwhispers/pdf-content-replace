package cn.cunchang;

import cn.cunchang.core.PdfReplacer;
import cn.cunchang.core.PdfReplacerBuilder;
import cn.cunchang.core.PositionRenderListener;
import cn.cunchang.rule.BankCardReplace;
import cn.cunchang.rule.ReplaceRule;
import com.itextpdf.text.DocumentException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author cunchang
 * @date 2020/9/3 上午2:06
 */
public class Client {

    static Map<String, ReplaceRule> bankRuleMap = new HashMap<>();

    static {
        ReplaceRule bankCardReplace = new BankCardReplace();
        // 招商银行
        bankRuleMap.put("付款账号：", bankCardReplace);
        bankRuleMap.put("收款人账号：", bankCardReplace);
    }

    public static void main(String[] args) throws IOException, DocumentException {
        String source = "/Users/cunchang/Downloads/receipt/source.pdf";
        String target = "/Users/cunchang/Downloads/receipt/target.pdf";

        // 第一步配置监听器
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

    public static void jpg() {
        BufferedImage image = null;
        try {
            File file = new File("/Users/cunchang/Desktop/76fcfdbca9091eb9e5b6cd563405acee.png");
            image = ImageIO.read(file);
            // byte[] bytes = toByteArray(file);
            // image = Imaging.getBufferedImage(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(image);
        System.out.println(image.getType());
    }


    /*读取文件的字节数组*/
    public static byte[] toByteArray(File file) throws IOException {
        if (Objects.isNull(file)) {
            return null;
        }
        if (!file.exists()) {
            throw new FileNotFoundException("file not exists");
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length())) {
            BufferedInputStream in;
            in = new BufferedInputStream(new FileInputStream(file));
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len;
            while (-1 != (len = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
