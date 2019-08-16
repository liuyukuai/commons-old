package com.itxiaoer.commons.poi;

import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.core.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

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


    /**
     * 遍历文件路径
     *
     * @param path     excel文件路径
     * @param consumer 回调函数
     */
    public static void apply(String path, RowsConsumer consumer) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        apply(new File(path), consumer);
    }

    /**
     * 遍历文件
     *
     * @param file     excel文件
     * @param consumer 回调函数
     */
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

    /**
     * 获取某行的所有值
     *
     * @param row 行
     * @return 值集合
     */
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

    /**
     * 获取单元格值
     *
     * @param cell 单元格
     * @return 单元格值
     */
    public static String getValue(Cell cell) {
        if (Objects.isNull(cell)) {
            return "";
        }

        CellType cellType = cell.getCellType();
        if (Objects.equals(cellType, CellType.NUMERIC)) {
            return String.valueOf(cell.getNumericCellValue());
        }

        if (Objects.equals(cellType, CellType.STRING)) {
            try {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    return sdf.format(date);
                }
            } catch (Exception e) {
                // ignore
            }
            return cell.getStringCellValue();
        }

        if (Objects.equals(cellType, CellType.FORMULA)) {
            CellType cachedFormulaResultType = cell.getCachedFormulaResultType();

            if (Objects.equals(cachedFormulaResultType, CellType.NUMERIC)) {
                return String.valueOf(cell.getNumericCellValue());
            }

            if (Objects.equals(cachedFormulaResultType, CellType.STRING)) {
                return cell.getStringCellValue();
            }

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

    /**
     * 获取sheet页中的所有合并单元格
     *
     * @param sheet sheet页
     * @return 合并单雅戈尔地址
     */
    public static List<CellRangeAddress> getCombines(Sheet sheet) {
        return sheet.getMergedRegions();
    }

    /**
     * 判断单元是否为合并单元格，并返回单元格占用的行数和列数
     *
     * @param combines sheet页所有的合并单元格式
     * @param cell     具体某个单元格
     * @param sheet    sheet页
     * @return 单元是否是合并单元格
     */
    public static Optional<CombineCell> isCombineCell(List<CellRangeAddress> combines, Cell cell, Sheet sheet) {
        for (CellRangeAddress ca : combines) {
            //获得合并单元格的起始行, 结束行, 起始列, 结束列
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            //判断cell是否在合并区域之内，在的话返回true和合并行列数
            if (cell.getRowIndex() >= firstRow && cell.getRowIndex() <= lastRow) {
                if (cell.getColumnIndex() >= firstColumn && cell.getColumnIndex() <= lastColumn) {
                    int mergedRow = lastRow - firstRow + 1;
                    int mergedCol = lastColumn - firstColumn + 1;
                    return Optional.of(new CombineCell(true, mergedRow, mergedCol));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 获取sheet页的所有图片
     *
     * @param sheet sheet页
     * @return 图片集合
     */
    private static List<XSSFPictureData> getPictures(XSSFSheet sheet) {
        Map<String, HSSFPictureData> map = new HashMap<>(16);
        XSSFDrawing drawingPatriarch = sheet.getDrawingPatriarch();
        List<XSSFShape> hssfShapes = Optional.ofNullable(sheet.getDrawingPatriarch()).map(XSSFDrawing::getShapes).orElse(Collections.emptyList());
        if (Lists.iterable(hssfShapes)) {
            return hssfShapes
                    .stream()
                    .filter(e -> e instanceof XSSFPicture)
                    .map(e -> (XSSFPicture) e)
                    .map(XSSFPicture::getPictureData)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 所有图片路径
     *
     * @param pictures 图片
     * @param function 回调函数
     * @return 图片路径
     */
    private static List<Path> getPicture(List<XSSFPictureData> pictures, Function<String, String> function) {
        //遍历写入图片
        return pictures.stream().map((v) -> {
            // 图片后缀
            String ext = v.suggestFileExtension();
            try {
                // 文件路径
                String path = Optional.ofNullable(function).map(e -> e.apply(v.toString())).orElse(UUIDUtils.guid() + "." + ext);
                return Files.write(Paths.get(path), v.getData());
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
        List<XSSFPictureData> pictures = ExcelUtil.getPictures(sheet);
        return ExcelUtil.getPicture(pictures, null);
    }

    /**
     * 获取sheet页面中的所有图片
     *
     * @param sheet sheet页
     * @return 图片路径
     */
    public static List<Path> getPictureFile(XSSFSheet sheet, Function<String, String> function) {
        List<XSSFPictureData> pictures = ExcelUtil.getPictures(sheet);
        return ExcelUtil.getPicture(pictures, function);
    }
}
