
package com.itxiaoer.commons.poi.util;

import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.core.util.UUIDUtils;
import com.itxiaoer.commons.poi.Sheets;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * excel工作类
 *
 * @author liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Excels {

    /**
     * 写入数据
     *
     * @param path       模板路径
     * @param sheetsList excel数据
     * @return 写入数据的文件
     */
    public static Optional<File> write(String path, List<Sheets> sheetsList) {
        try (InputStream fis = new FileInputStream(new File(path))) {
            return write(fis, sheetsList);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 写入数据
     *
     * @param resource   模板文件
     * @param sheetsList excel数据
     * @return 写入数据的文件
     */
    public static Optional<File> write(ClassPathResource resource, List<Sheets> sheetsList) {
        try (InputStream fis = resource.getInputStream()) {
            return write(fis, sheetsList);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    /**
     * 写入数据
     *
     * @param fis        模板文件流
     * @param sheetsList excel数据
     * @return 写入数据的文件
     */
    public static Optional<File> write(InputStream fis, List<Sheets> sheetsList) {

        if (Lists.iterable(sheetsList) && Objects.nonNull(fis)) {
            // 创建workbook对象
            File file = new File(System.getProperty("user.dir"), UUIDUtils.guid());
            try (HSSFWorkbook workbook = new HSSFWorkbook()) {
                IOUtils.copy(fis, file);
                sheetsList.forEach(sheets -> write(workbook, sheets));
                workbook.write(file);
                return Optional.of(file);
            } catch (Exception ignored) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    /**
     * 写入数据
     *
     * @param workbook workbook对象
     * @param sheets   数据
     */
    private static void write(HSSFWorkbook workbook, Sheets sheets) {
        // 创建sheet页面
        HSSFSheet sheet = workbook.createSheet(sheets.getName());
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

    private static void writeHeaders(HSSFSheet sheet, List<String> headers) {
        Optional.ofNullable(headers)
                .filter(Lists::iterable)
                .ifPresent(e -> {
                    // 创建sheet页面
                    HSSFRow row = sheet.createRow(0);
                    for (int i = 0; i < e.size(); i++) {
                        HSSFCell cell = row.createCell(i);
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
    private static void writeRows(HSSFSheet sheet, List<List<String>> rows) {
        Optional.ofNullable(rows)
                .filter(Lists::iterable)
                .ifPresent(e -> {
                    for (int i = 0; i < e.size(); i++) {
                        HSSFRow row = sheet.createRow(i + 1);
                        List<String> values = e.get(i);
                        for (int j = values.size() - 1; j >= 0; j--) {
                            HSSFCell cell = row.createCell(j);
                            String s = values.get(j);
                            cell.setCellValue(StringUtils.isBlank(s) ? "" : s);
                        }
                    }
                });
    }

}
