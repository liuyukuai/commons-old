
package com.itxiaoer.commons.poi.util;

import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.poi.FixedSheets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * excel工作类
 *
 * @author liuyk
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public final class FixedExcels {
    /**
     * 写入数据
     *
     * @param file       模板文件流
     * @param sheetsList excel数据
     */
    public static void write(File file, List<FixedSheets> sheetsList) {

        String path = file.getAbsolutePath();

        if (Lists.iterable(sheetsList)) {
            // 创建workbook对象
            File dest = new File(System.getProperty("user.dir"), file.getName() + ".new");
            try (Workbook workbook = WorkbookFactory.create(file)) {
                sheetsList.forEach(sheets -> FixedExcels.write(workbook, sheets));
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
    static void write(Workbook workbook, FixedSheets sheets) {

        // 创建sheet页面
        Sheet sheet = workbook.getSheet(sheets.getName());
//        if (Objects.isNull(sheet)) {
//            sheet = workbook.createSheet(sheets.getName());
//        }
        Drawing patriarch = sheet.createDrawingPatriarch();
        // 写入数据
        Lists.empty(sheets.getCells()).forEach(e -> {
            if (e.isDrawable() && Objects.nonNull(e.getValue()) && StringUtils.isNotBlank(e.getValue().toString())) {
                try (InputStream is = new FileInputStream((File) e.getValue())) {
                    byte[] bytes = IOUtils.toByteArray(is);

                    //anchor主要用于设置图片的属性
                    XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255, (short) e.getCols(), (short) e.getRows(), (short) e.getCols2(), (short) e.getRows2());
                    anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
                    //插入图片
                    patriarch.createPicture(anchor, workbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
                } catch (Exception e1) {
//                    e1.printStackTrace();
                    log.error(e1.getMessage());
                }
            } else {
                Row row = sheet.getRow(e.getRows());
                if (Objects.isNull(row)) {
                    row = sheet.createRow(e.getRows());
                }
                Cell cell = row.getCell(e.getCols());
                if (Objects.isNull(cell)) {
                    cell = row.createCell(e.getCols());
                    CellStyle style = workbook.createCellStyle();
                    cell.setCellStyle(style);
                }
                cell.setCellValue(Objects.isNull(e.getValue()) ? "" : String.valueOf(e.getValue()));
            }
        });
    }
}
