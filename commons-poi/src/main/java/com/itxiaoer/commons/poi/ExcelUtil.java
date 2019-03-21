package com.itxiaoer.commons.poi;

import com.itxiaoer.commons.core.util.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Objects;

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
                int rowCount = sheet.getPhysicalNumberOfRows();
                //遍历第一行，因为第一行，也就是索引为0的那一行是标题，所以这里从第二行也就是索引为1的行开始遍历
                for (int r = 1; r < rowCount; r++) {
                    Row row = sheet.getRow(r);
                    if (!Objects.isNull(row)) {
                        consumer.accept(sheetCount, sheet.getSheetName(), r, row);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String getValue(Cell cell) {
        CellType cellType = cell.getCellType();
        if (Objects.equals(cellType, CellType.NUMERIC)) {
            return String.valueOf(cell.getNumericCellValue());
        }

        if (Objects.equals(cellType, CellType.STRING)) {
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
}
