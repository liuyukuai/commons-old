package com.itxiaoer.commons.poi;

import com.itxiaoer.commons.core.util.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ExcelUtil {

    private ExcelUtil() {

    }

    public static void apply(String path, RowsConsumer consumer) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        apply(new File(path), consumer);
    }

    public static void apply(File file, RowsConsumer consumer) {
        if (file == null || !file.exists()) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.info("file  = {} ", file.getAbsolutePath());
        }

        List<T> list = Lists.newArrayList();
        try (FileInputStream fis = new FileInputStream(file)) {
            // 兼容性处理
            Workbook workbook = WorkbookFactory.create(fis);
            // Sheet的数量
            int sheetCount = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetCount; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                //获取总行数
                int rowCount = sheet.getLastRowNum();
                for (int r = 0; r < rowCount + 1; r++) {
                    Row row = sheet.getRow(r);
                    consumer.accept(sheetCount, sheet, r, row);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    public static List<String> getRow(Row row) {
        if (!Objects.isNull(row)) {
            int cellCount = row.getPhysicalNumberOfCells();
            List<String> rows = new ArrayList<>(cellCount);
            for (int c = 0; c < cellCount; c++) {
                Cell cell = row.getCell(c);
                rows.add(getValue(cell));
            }
            return rows;
        }
        return Collections.emptyList();
    }


    public static String getValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();
        if (Objects.equals(cellType, CellType.NUMERIC)) {
            return String.valueOf(cell.getNumericCellValue());
        }

        if (Objects.equals(cellType, CellType.STRING)) {
            try {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    return sdf.format(date);
                }
            } catch (Exception e) {
                // ignore
            }
            return cell.getStringCellValue();
        }

        if (Objects.equals(cellType, CellType.FORMULA)) {
            return String.valueOf(cell.getCellFormula());
        }

        if (Objects.equals(cellType, CellType.ERROR)) {
            return String.valueOf(cell.getErrorCellValue());
        }

        if (Objects.equals(cellType, CellType.BOOLEAN)) {
            return String.valueOf(cell.getBooleanCellValue());
        }


        return "";
    }


    private static Map<String, XSSFPictureData> getPictures(XSSFSheet sheet) {
        Map<String, HSSFPictureData> map = new HashMap<>(16);
        XSSFDrawing drawingPatriarch = sheet.getDrawingPatriarch();
        List<XSSFShape> hssfShapes = Optional.ofNullable(sheet.getDrawingPatriarch()).map(XSSFDrawing::getShapes).orElse(Collections.emptyList());
        if (Lists.iterable(hssfShapes)) {
            return hssfShapes
                    .stream()
                    .filter(e -> e instanceof XSSFPicture)
                    .map(e -> (XSSFPicture) e)
                    .collect(Collectors.toMap(e -> {
                        XSSFClientAnchor preferredSize = e.getPreferredSize();
                        CTMarker from = preferredSize.getFrom();
                        return from.getRow() + "_" + from.getCol();
                    }, XSSFPicture::getPictureData));
        }
        return Collections.emptyMap();
    }


    private static List<Path> getPicture(Map<String, XSSFPictureData> pictures, Function<String, String> function) {
        //遍历写入图片
        return pictures.entrySet().stream().map((entry) -> {
            String key = entry.getKey();
            XSSFPictureData value = entry.getValue();
            // 图片后缀
            String ext = value.suggestFileExtension();
            try {
                // 文件路径
                String path = Optional.ofNullable(function).map(e -> e.apply(key)).orElse(key + "." + ext);
                return Files.write(Paths.get(path), value.getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(e -> !Objects.isNull(e)).collect(Collectors.toList());
    }

    /**
     * 获取sheet页面中的所有图片
     *
     * @param sheet sheet页
     * @return 图片路径
     */
    public static List<Path> getPictureFile(XSSFSheet sheet) {
        Map<String, XSSFPictureData> pictures = ExcelUtil.getPictures(sheet);
        return ExcelUtil.getPicture(pictures, null);
    }

    /**
     * 获取sheet页面中的所有图片
     *
     * @param sheet sheet页
     * @return 图片路径
     */
    public static List<Path> getPictureFile(XSSFSheet sheet, Function<String, String> function) {
        Map<String, XSSFPictureData> pictures = ExcelUtil.getPictures(sheet);
        return ExcelUtil.getPicture(pictures, function);
    }
}
