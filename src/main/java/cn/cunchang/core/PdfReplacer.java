
package cn.cunchang.core;

import cn.cunchang.rule.ReplaceRule;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpProcessor;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 替换PDF文件某个区域内的文本
 *
 * @author cunchang
 * @date 2020/9/4 上午10:45
 */
public class PdfReplacer implements Closeable {

    /**
     * 替换规则
     */
    private Map<String, ReplaceRule> replaceRuleMap;

    /**
     * 从pdf中查找到的文字的坐标
     */
    private List<ReplaceRegion> replaceRegionList = new ArrayList<>();

    /**
     * 覆盖文字的字体
     */
    private Font font;

    /**
     * 替换前的PDF输入流
     */
    private InputStream input;

    /**
     * 替换后的PDF输出流
     */
    private OutputStream output;

    /**
     * pdf解析时回调的监听器，通过监听器可以获取pdf的完整内容，并进行记录坐标
     */
    private ResultRenderListener resultRenderListener;

    /**
     * 包装PDF输入流
     */
    private PdfReader reader;

    /**
     * 根据listener解析
     */
    private PdfReaderContentParser parser;

    /**
     * 操作PDF内容
     */
    private PdfStamper stamper;

    public void setFont(Font font) {
        this.font = font;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public void setReplaceRuleMap(Map<String, ReplaceRule> replaceRuleMap) {
        this.replaceRuleMap = replaceRuleMap;
    }

    public void setResultRenderListener(ResultRenderListener resultRenderListener) {
        this.resultRenderListener = resultRenderListener;
    }

    public void init() throws IOException, DocumentException {
        if (input == null) {
            throw new NullPointerException("原PDF输入流不能为空");
        }
        if (output == null) {
            throw new NullPointerException("替换后PDF输出流不能为空");
        }
        if (font == null) {
            throw new NullPointerException("字体不能为空");
        }

        reader = new PdfReader(input);
        stamper = new PdfStamper(reader, output);
        parser = new PdfReaderContentParser(reader);
    }

    /**
     * 替换文本
     */
    public void process(int page) throws DocumentException, IOException {
        // 解析PDF，并找到需要替换的词组
        this.parseReplaceRegion(page);
        // 清理目标位置的内容
        this.cleanRegion(page);
        // 覆盖目标位置的内容
        this.coverageTextRegion(page);
    }

    public void process() throws DocumentException, IOException {
        int pages = reader.getNumberOfPages();
        // 对每一页PDF进行解析
        for (int page = 1; page <= pages; page++) {
            process(page);
        }
    }

    /**
     * 解析指定页数PDF下的文本的位置
     * 如果不知道待替换文字的坐标，可以使用该方法进行解析
     */
    public void parseReplaceRegion(int pageNumber) throws IOException {
        if (this.replaceRuleMap.size() == 0) {
            throw new IllegalArgumentException("请初始化指定替换的关键词");
        }
        parser.processContent(pageNumber, resultRenderListener);
        replaceRegionList = resultRenderListener.getResult();
        if (replaceRegionList == null || replaceRegionList.size() == 0) {
            throw new IllegalArgumentException("未在pdf中找到指定替换的关键词,请检查pdf文件(可能的情况:pdf格式的图片)");
        }
    }

    /**
     * 清理指定页码的PDF
     *
     * @param pageNumber 页码
     */
    public void cleanRegion(int pageNumber) throws IOException, DocumentException {
        List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<>(replaceRegionList.size());
        for (ReplaceRegion replaceRegion : replaceRegionList) {
            // 移除矩形中的内容，文字被消除无法复制
            // ll代表左下角，ur代表右上角
            // abs(llx-urx)=x，从左以llx起点，走x；llx>urx，往左
            // abs(lly-ury)=y，从下以lly起点，走y；lly>ury，往下
            Rectangle rectangle = new Rectangle(replaceRegion.getLlx(), replaceRegion.getLly(),
                    replaceRegion.getUrx(), replaceRegion.getUry());
            PdfCleanUpLocation pdfCleanUpLocation = new PdfCleanUpLocation(pageNumber, rectangle, BaseColor.WHITE);
            cleanUpLocations.add(pdfCleanUpLocation);
        }
        PdfCleanUpProcessor cleaner = new PdfCleanUpProcessor(cleanUpLocations, stamper);
        cleaner.cleanUp();
    }

    /**
     * 覆盖文本到指定页码的PDF
     *
     * @param pageNumber 页码
     */
    public void coverageTextRegion(int pageNumber) {
        PdfContentByte canvas = stamper.getOverContent(pageNumber);
        canvas.beginText();
        for (ReplaceRegion replaceRegion : replaceRegionList) {
            canvas.setFontAndSize(font.getBaseFont(), font.getSize());
            canvas.setTextMatrix(replaceRegion.getX(), replaceRegion.getY());
            ReplaceRule replaceRule = replaceRuleMap.get(replaceRegion.getPreKey());
            String desensitize = replaceRule.execute(replaceRegion.getValue(), replaceRegion.getW());
            canvas.showText(desensitize);
        }
        canvas.endText();
    }

    @Override
    public void close() {
        try {
            if (stamper != null) {
                stamper.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}