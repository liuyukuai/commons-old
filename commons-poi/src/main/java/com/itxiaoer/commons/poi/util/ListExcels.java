
package com.itxiaoer.commons.poi.util;

import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.poi.Sheets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * excel工作类
 *
 * @author liuyk
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ListExcels {


    /**
     * 写入数据
     *
     * @param file       模板文件流
     * @param sheetsList excel数据
     */
    public static void write(File file, List<Sheets> sheetsList) {

        String path = file.getAbsolutePath();
        if (Lists.iterable(sheetsList)) {
            File dest = new File(System.getProperty("user.dir"), file.getName() + ".new");
            // 创建workbook对象
            try (Workbook workbook = WorkbookFactory.create(file)) {
                sheetsList.forEach(sheets -> ListExcels.write(workbook, sheets));
                workbook.write(new FileOutputStream(dest));
                boolean delete = file.delete();
                if (delete) {
                    Files.move(Paths.get(dest.getAbsolutePath()), Paths.get(path));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 写入数据
     *
     * @param workbook workbook对象
     * @param sheets   数据
     */
    static void write(Workbook workbook, Sheets sheets) {
        // 创建sheet页面
        Sheet sheet = workbook.getSheet(sheets.getName());
        if (Objects.isNull(sheet)) {
            sheet = workbook.createSheet(sheets.getName());
        }
        // 写入表头
        writeHeaders(sheet, sheets.getHeaders());
        // 写入数据
        writeRows(sheet, sheets.getRows());
    }

    /**
     * 写入数据
     *
     * @param sheet   sheet对象
     * @param headers 表头数据
     */

    static void writeHeaders(Sheet sheet, List<String> headers) {

        Optional.ofNullable(headers)
                .filter(Lists::iterable)
                .ifPresent(e -> {
                    Row row = sheet.createRow(0);
                    // 创建sheet页面
                    for (int i = 0; i < e.size(); i++) {
                        Cell cell = row.createCell(i);
                        cell.setCellValue(e.get(i));
                    }
                });
    }

    /**
     * 写入数据
     *
     * @param sheet sheet对象
     * @param rows  数据
     */
    static void writeRows(Sheet sheet, List<List<String>> rows) {
        Optional.ofNullable(rows)
                .filter(Lists::iterable)
                .ifPresent(e -> {
                    for (int i = 0; i < e.size(); i++) {
                        List<String> values = e.get(i);
                        if (!Lists.iterable(values)) {
                            continue;
                        }
                        Row row = sheet.createRow(i + 1);

                        for (int j = values.size() - 1; j >= 0; j--) {
                            Cell cell = row.createCell(j);
                            String s = values.get(j);
                            cell.setCellValue(StringUtils.isBlank(s) ? "" : s);
                        }
                    }
                });
    }

}
