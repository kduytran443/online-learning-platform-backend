package com.hudsontran.courseservice.pdf;

import com.hudsontran.courseservice.dto.ClassReportData;
import com.hudsontran.courseservice.dto.CourseReportData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CoursePdfGenerator {

    private static final PDRectangle PAGE_SIZE = PDRectangle.A4;

    private static final float PAGE_WIDTH = PAGE_SIZE.getWidth();
    private static final float PAGE_HEIGHT = PAGE_SIZE.getHeight();

    private static final float MARGIN = 40F;
    private static final float CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2);

    private static final float TITLE_FONT_SIZE = 18F;
    private static final float SECTION_FONT_SIZE = 10F;
    private static final float BODY_FONT_SIZE = 9F;
    private static final float TABLE_FONT_SIZE = 7.5F;

    private static final float SECTION_HEIGHT = 30F;
    private static final float TABLE_HEADER_HEIGHT = 22F;
    private static final float TABLE_ROW_HEIGHT = 24F;

    /*
     * Template colors
     */
    private static final int BLUE_R = 20;
    private static final int BLUE_G = 78;
    private static final int BLUE_B = 171;

    private static final int LIGHT_GRAY_R = 235;
    private static final int LIGHT_GRAY_G = 237;
    private static final int LIGHT_GRAY_B = 240;

    private static final int BORDER_R = 110;
    private static final int BORDER_G = 110;
    private static final int BORDER_B = 110;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    public byte[] generate(CourseReportData report) {

        PDType1Font regularFont =
                new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        PDType1Font boldFont =
                new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

        try (
                PDDocument document = new PDDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {
            PdfContext context = new PdfContext(
                    document,
                    regularFont,
                    boldFont
            );

            drawReportTitle(context);

            drawSectionTitle(
                    context,
                    "COURSE INFORMATION"
            );

            drawCourseInformation(
                    context,
                    report
            );

            drawSectionTitle(
                    context,
                    "DESCRIPTION"
            );

            drawDescription(
                    context,
                    report
            );

            drawSectionTitle(
                    context,
                    "CLASSES"
            );

            drawClasses(
                    context,
                    report.classes()
            );

            context.finishPage();

            addPageNumbers(
                    document,
                    regularFont
            );

            document.save(output);

            return output.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to generate course PDF",
                    e
            );
        }
    }

    // =========================================================
    // HEADER
    // =========================================================

    private static void drawReportTitle(
            PdfContext context
    ) throws IOException {

        context.setFont(
                context.boldFont(),
                TITLE_FONT_SIZE
        );

        String title = "COURSE REPORT";

        float titleWidth =
                context.boldFont().getStringWidth(title)
                        / 1000F
                        * TITLE_FONT_SIZE;

        float x =
                (PAGE_WIDTH - titleWidth) / 2F;

        context.text(
                title,
                x,
                context.y()
        );

        context.moveDown(30F);

        context.setStrokeColor(
                0,
                0,
                0
        );

        context.line(
                MARGIN,
                context.y(),
                PAGE_WIDTH - MARGIN,
                context.y()
        );

        context.moveDown(20F);
    }

    // =========================================================
    // SECTION
    // =========================================================

    private void drawSectionTitle(
            PdfContext context,
            String title
    ) throws IOException {

        context.ensureSpace(
                SECTION_HEIGHT + 20F
        );

        float topY = context.y();

        context.setFillColor(
                BLUE_R,
                BLUE_G,
                BLUE_B
        );

        context.content().addRect(
                MARGIN,
                topY - SECTION_HEIGHT,
                CONTENT_WIDTH,
                SECTION_HEIGHT
        );

        context.content().fill();

        context.setFont(
                context.boldFont(),
                SECTION_FONT_SIZE
        );

        float textWidth =
                context.boldFont().getStringWidth(title)
                        / 1000F
                        * SECTION_FONT_SIZE;

        float x =
                MARGIN
                        + (CONTENT_WIDTH - textWidth) / 2F;

        float textY =
                topY
                        - SECTION_HEIGHT
                        + 10F;

        context.setTextColor(
                255,
                255,
                255
        );

        context.text(
                title,
                x,
                textY
        );

        context.resetTextColor();

        context.setY(
                topY - SECTION_HEIGHT - 12F
        );
    }

    // =========================================================
    // COURSE INFORMATION
    // =========================================================

    private void drawCourseInformation(
            PdfContext context,
            CourseReportData report
    ) throws IOException {

        float labelWidth = 100F;

        context.setFont(
                context.boldFont(),
                BODY_FONT_SIZE
        );

        context.keyValue(
                "Course ID",
                report.id(),
                labelWidth
        );

        context.keyValue(
                "Title",
                report.title(),
                labelWidth
        );

        context.keyValue(
                "Slug",
                report.slug(),
                labelWidth
        );

        context.keyValue(
                "Level",
                report.level(),
                labelWidth
        );

        context.keyValue(
                "Language",
                report.language(),
                labelWidth
        );

        context.keyValue(
                "Author ID",
                report.authorId(),
                labelWidth
        );

        context.keyValue(
                "Status",
                report.status(),
                labelWidth
        );

        context.keyValue(
                "Visibility",
                report.visibility(),
                labelWidth
        );

        context.keyValue(
                "Published At",
                formatDate(report.publishedAt()),
                labelWidth
        );

        context.keyValue(
                "Created At",
                formatDate(report.createdAt()),
                labelWidth
        );

        context.keyValue(
                "Updated At",
                formatDate(report.updatedAt()),
                labelWidth
        );

        context.moveDown(8F);
    }

    // =========================================================
    // DESCRIPTION
    // =========================================================

    private void drawDescription(
            PdfContext context,
            CourseReportData report
    ) throws IOException {

        context.setFont(
                context.boldFont(),
                BODY_FONT_SIZE
        );

        context.keyValue(
                "Short Description",
                report.shortDescription(),
                100F
        );

        context.moveDown(4F);

        context.setFont(
                context.regularFont(),
                BODY_FONT_SIZE
        );

        context.textBlock(
                report.description(),
                CONTENT_WIDTH
        );

        context.moveDown(8F);
    }

    // =========================================================
    // CLASSES
    // =========================================================

    private void drawClasses(
            PdfContext context,
            List<ClassReportData> classes
    ) throws IOException {

        if (classes == null || classes.isEmpty()) {

            context.setFont(
                    context.regularFont(),
                    BODY_FONT_SIZE
            );

            context.text("No classes found.");

            return;
        }

        float[] widths = {
                115F,
                100F,
                70F,
                70F,
                75F,
                85F
        };

        String[] headers = {
                "Class",
                "Instructor",
                "Price",
                "Students",
                "Status",
                "Visibility"
        };

        drawTableHeader(
                context,
                headers,
                widths
        );

        for (ClassReportData item : classes) {

            context.ensureSpace(
                    TABLE_ROW_HEIGHT
            );

            drawClassRow(
                    context,
                    item,
                    widths
            );
        }

        context.moveDown(10F);

        context.setFont(
                context.boldFont(),
                BODY_FONT_SIZE
        );

        context.text(
                "Total Classes: " + classes.size()
        );
    }

    private void drawTableHeader(
            PdfContext context,
            String[] headers,
            float[] widths
    ) throws IOException {

        float y = context.y();
        float x = MARGIN;

        context.setFillColor(
                LIGHT_GRAY_R,
                LIGHT_GRAY_G,
                LIGHT_GRAY_B
        );

        context.content().addRect(
                MARGIN,
                y - TABLE_HEADER_HEIGHT,
                CONTENT_WIDTH,
                TABLE_HEADER_HEIGHT
        );

        context.content().fill();

        context.setStrokeColor(
                BORDER_R,
                BORDER_G,
                BORDER_B
        );

        context.rect(
                MARGIN,
                y - TABLE_HEADER_HEIGHT,
                CONTENT_WIDTH,
                TABLE_HEADER_HEIGHT
        );

        context.setFont(
                context.boldFont(),
                TABLE_FONT_SIZE
        );

        for (int i = 0; i < headers.length; i++) {

            context.centerText(
                    headers[i],
                    x,
                    widths[i],
                    y - 15F,
                    TABLE_FONT_SIZE,
                    context.boldFont()
            );

            if (i < headers.length - 1) {

                float separatorX =
                        x + widths[i];

                context.line(
                        separatorX,
                        y,
                        separatorX,
                        y - TABLE_HEADER_HEIGHT
                );
            }

            x += widths[i];
        }

        context.moveDown(
                TABLE_HEADER_HEIGHT
        );
    }

    private void drawClassRow(
            PdfContext context,
            ClassReportData item,
            float[] widths
    ) throws IOException {

        float y = context.y();
        float x = MARGIN;

        context.setFont(
                context.regularFont(),
                TABLE_FONT_SIZE
        );

        context.setStrokeColor(
                BORDER_R,
                BORDER_G,
                BORDER_B
        );

        context.rect(
                MARGIN,
                y - TABLE_ROW_HEIGHT,
                CONTENT_WIDTH,
                TABLE_ROW_HEIGHT
        );

        String[] values = {
                safe(item.name()),
                safe(item.instructorName()),
                formatPrice(item.price()),
                formatStudents(
                        item.currentStudents(),
                        item.maxStudents()
                ),
                safe(item.status()),
                safe(item.visibility())
        };

        for (int i = 0; i < values.length; i++) {

            context.clipAndText(
                    values[i],
                    x,
                    y - 15F,
                    widths[i]
            );

            if (i < values.length - 1) {

                float separatorX =
                        x + widths[i];

                context.line(
                        separatorX,
                        y,
                        separatorX,
                        y - TABLE_ROW_HEIGHT
                );
            }

            x += widths[i];
        }

        context.moveDown(
                TABLE_ROW_HEIGHT
        );
    }

    // =========================================================
    // PAGE NUMBER
    // =========================================================

    private void addPageNumbers(
            PDDocument document,
            PDFont regularFont
    ) throws IOException {

        int pageNumber = 1;

        for (PDPage page : document.getPages()) {

            try (
                    PDPageContentStream content =
                            new PDPageContentStream(
                                    document,
                                    page,
                                    PDPageContentStream.AppendMode.APPEND,
                                    true,
                                    true
                            )
            ) {
                String value =
                        String.valueOf(pageNumber);

                float fontSize = 8F;

                float textWidth =
                        regularFont.getStringWidth(value)
                                / 1000F
                                * fontSize;

                float x =
                        PAGE_WIDTH
                                - MARGIN
                                - textWidth;

                content.setFont(
                        regularFont,
                        fontSize
                );

                content.beginText();

                content.newLineAtOffset(
                        x,
                        20F
                );

                content.showText(value);

                content.endText();
            }

            pageNumber++;
        }
    }

    // =========================================================
    // FORMAT
    // =========================================================

    private String formatDate(
            Instant value
    ) {

        if (value == null) {
            return "-";
        }

        return DATE_FORMATTER.format(value);
    }

    private String formatPrice(
            BigDecimal price
    ) {

        if (price == null || price.signum() == 0) {
            return "FREE";
        }

        return String.format(
                "%,.0f",
                price
        );
    }

    private String formatStudents(
            Integer current,
            Integer max
    ) {

        return (current != null ? current : 0)
                + "/"
                + (max != null ? max : "-");
    }

    private String safe(
            Object value
    ) {

        return value == null
                ? "-"
                : value.toString();
    }

    // =========================================================
    // PDF CONTEXT
    // =========================================================

    private static class PdfContext {

        private final PDDocument document;

        private final PDFont regularFont;
        private final PDFont boldFont;

        private PDPage page;
        private PDPageContentStream content;

        private float y;

        private PdfContext(
                PDDocument document,
                PDFont regularFont,
                PDFont boldFont
        ) throws IOException {

            this.document = document;
            this.regularFont = regularFont;
            this.boldFont = boldFont;

            createPage();
        }

        private void createPage()
                throws IOException {

            page = new PDPage(PAGE_SIZE);

            document.addPage(page);

            content =
                    new PDPageContentStream(
                            document,
                            page
                    );

            y =
                    PAGE_HEIGHT
                            - MARGIN;

            resetTextColor();

            setStrokeColor(
                    BORDER_R,
                    BORDER_G,
                    BORDER_B
            );
        }

        private void finishPage()
                throws IOException {

            if (content != null) {
                content.close();
                content = null;
            }
        }

        private void newPage()
                throws IOException {

            finishPage();

            createPage();
        }

        private void ensureSpace(
                float requiredHeight
        ) throws IOException {

            if (y - requiredHeight < MARGIN) {

                newPage();

                drawReportTitle(this);
            }
        }

        private void setFont(
                PDFont font,
                float size
        ) throws IOException {

            content.setFont(
                    font,
                    size
            );
        }

        private void text(
                String value
        ) throws IOException {

            text(
                    value,
                    MARGIN,
                    y
            );
        }

        private void text(
                String value,
                float x,
                float y
        ) throws IOException {

            content.beginText();

            content.newLineAtOffset(
                    x,
                    y
            );

            content.showText(
                    value == null
                            ? "-"
                            : value
            );

            content.endText();
        }

        private void textBlock(
                String value,
                float maxWidth
        ) throws IOException {

            if (value == null || value.isBlank()) {
                return;
            }

            String[] paragraphs =
                    value.split("\\R");

            for (String paragraph : paragraphs) {

                if (paragraph.isBlank()) {
                    moveDown(14F);
                    continue;
                }

                List<String> lines =
                        wrapText(
                                paragraph,
                                regularFont,
                                BODY_FONT_SIZE,
                                maxWidth
                        );

                for (String line : lines) {

                    ensureSpace(15F);

                    text(
                            line,
                            MARGIN,
                            y
                    );

                    moveDown(14F);
                }
            }
        }

        private void keyValue(
                String key,
                Object value,
                float labelWidth
        ) throws IOException {

            setFont(
                    boldFont,
                    BODY_FONT_SIZE
            );

            text(
                    key,
                    MARGIN,
                    y
            );

            float keyWidth =
                    boldFont.getStringWidth(
                            key
                    )
                            / 1000F
                            * BODY_FONT_SIZE;

            setFont(
                    regularFont,
                    BODY_FONT_SIZE
            );

            text(
                    safe(value),
                    MARGIN + Math.max(
                            labelWidth,
                            keyWidth + 10F
                    ),
                    y
            );

            moveDown(15F);
        }

        private void centerText(
                String value,
                float x,
                float width,
                float y,
                float fontSize,
                PDFont font
        ) throws IOException {

            float textWidth =
                    font.getStringWidth(value)
                            / 1000F
                            * fontSize;

            float textX =
                    x
                            + (width - textWidth) / 2F;

            text(
                    value,
                    textX,
                    y
            );
        }

        private void clipAndText(
                String value,
                float x,
                float y,
                float width
        ) throws IOException {

            String display =
                    truncateToWidth(
                            value,
                            regularFont,
                            TABLE_FONT_SIZE,
                            width - 6F
                    );

            text(
                    display,
                    x + 3F,
                    y
            );
        }

        private void rect(
                float x,
                float y,
                float width,
                float height
        ) throws IOException {

            content.addRect(
                    x,
                    y,
                    width,
                    height
            );

            content.stroke();
        }

        private void line(
                float x1,
                float y1,
                float x2,
                float y2
        ) throws IOException {

            content.moveTo(
                    x1,
                    y1
            );

            content.lineTo(
                    x2,
                    y2
            );

            content.stroke();
        }

        private void setFillColor(
                int r,
                int g,
                int b
        ) throws IOException {

            content.setNonStrokingColor(
                    r / 255F,
                    g / 255F,
                    b / 255F
            );
        }

        private void setTextColor(
                int r,
                int g,
                int b
        ) throws IOException {

            content.setNonStrokingColor(
                    r / 255F,
                    g / 255F,
                    b / 255F
            );
        }

        private void resetTextColor()
                throws IOException {

            setTextColor(
                    0,
                    0,
                    0
            );
        }

        private void setStrokeColor(
                int r,
                int g,
                int b
        ) throws IOException {

            content.setStrokingColor(
                    r / 255F,
                    g / 255F,
                    b / 255F
            );
        }

        private void moveDown(
                float amount
        ) {

            y -= amount;
        }

        private float y() {
            return y;
        }

        private void setY(
                float y
        ) {
            this.y = y;
        }

        private PDPageContentStream content() {
            return content;
        }

        private PDFont regularFont() {
            return regularFont;
        }

        private PDFont boldFont() {
            return boldFont;
        }

        private static List<String> wrapText(
                String text,
                PDFont font,
                float fontSize,
                float maxWidth
        ) throws IOException {

            String[] words =
                    text.trim().split("\\s+");

            List<String> lines =
                    new ArrayList<>();

            StringBuilder current =
                    new StringBuilder();

            for (String word : words) {

                String candidate =
                        current.isEmpty()
                                ? word
                                : current + " " + word;

                float width =
                        font.getStringWidth(candidate)
                                / 1000F
                                * fontSize;

                if (width <= maxWidth) {

                    current.setLength(0);
                    current.append(candidate);

                } else {

                    if (!current.isEmpty()) {
                        lines.add(
                                current.toString()
                        );
                    }

                    current.setLength(0);
                    current.append(word);
                }
            }

            if (!current.isEmpty()) {
                lines.add(
                        current.toString()
                );
            }

            return lines;
        }

        private static String truncateToWidth(
                String text,
                PDFont font,
                float fontSize,
                float maxWidth
        ) throws IOException {

            if (text == null) {
                return "-";
            }

            float width =
                    font.getStringWidth(text)
                            / 1000F
                            * fontSize;

            if (width <= maxWidth) {
                return text;
            }

            String result = text;

            while (
                    result.length() > 3
                            && font.getStringWidth(
                            result + "..."
                    )
                            / 1000F
                            * fontSize
                            > maxWidth
            ) {
                result =
                        result.substring(
                                0,
                                result.length() - 1
                        );
            }

            return result + "...";
        }

        private static String safe(
                Object value
        ) {
            return value == null
                    ? "-"
                    : value.toString();
        }
    }
}
