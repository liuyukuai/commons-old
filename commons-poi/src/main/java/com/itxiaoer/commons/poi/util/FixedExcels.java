
package com.itxiaoer.commons.poi.util;

import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.core.util.UUIDUtils;
import com.itxiaoer.commons.poi.FixedSheets;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
public final class FixedExcels {
    /**
     * 写入数据
     *
     * @param path       模板路径
     * @param sheetsList excel数据
     * @return 写入数据的文件
     */
    public static Optional<File> write(String path, List<FixedSheets> sheetsList) {
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
    public static Optional<File> write(ClassPathResource resource, List<FixedSheets> sheetsList) {
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
    public static Optional<File> write(InputStream fis, List<FixedSheets> sheetsList) {

        if (Lists.iterable(sheetsList) && Objects.nonNull(fis)) {
            // 创建workbook对象
            File file = new File(System.getProperty("user.dir"), UUIDUtils.guid());
            try (Workbook workbook = WorkbookFactory.create(fis)) {
                IOUtils.copy(fis, file);
                sheetsList.forEach(sheets -> FixedExcels.write(workbook, sheets));
                workbook.write(new FileOutputStream(file));
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
    static void write(Workbook workbook, FixedSheets sheets) {

        // 创建sheet页面
        Sheet sheet = workbook.getSheet(sheets.getName());
        Drawing patriarch = sheet.createDrawingPatriarch();
        // 写入数据
        Lists.empty(sheets.getCells()).forEach(e -> {
            if (e.isDrawable()) {
                try (InputStream is = new FileInputStream((File) e.getValue())) {
                    byte[] bytes = IOUtils.toByteArray(is);

                    //anchor主要用于设置图片的属性
                    XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255, (short) e.getCols(), (short) e.getRows(), (short) e.getCols2(), (short) e.getRows2());
                    anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
                    //插入图片
                    patriarch.createPicture(anchor, workbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                Row row = sheet.getRow(e.getRows());
                Cell cell = row.getCell(e.getCols());
                cell.setCellValue(Objects.isNull(e.getValue()) ? "" : String.valueOf(e.getValue()));
            }
        });
    }
}
