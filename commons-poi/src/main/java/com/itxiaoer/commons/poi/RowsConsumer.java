package com.itxiaoer.commons.poi;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author : liuyk
 */
@FunctionalInterface
public interface RowsConsumer {

    /**
     * accept
     *
     * @param sheetIndex sheet index
     * @param sheet      sheet
     * @param rowIndex   rowIndex
     * @param row        row
     */
    void accept(int sheetIndex, Sheet sheet, int rowIndex, Row row);

}
