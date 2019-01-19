package com.itxiaoer.commons.poi;

import org.apache.poi.ss.usermodel.Row;

/**
 * @author : liuyk
 */
@FunctionalInterface
public interface RowsConsumer {

    /**
     * accept
     *
     * @param sheetIndex sheet index
     * @param sheetName  sheetName
     * @param rowIndex   rowIndex
     * @param row        row
     */
    void accept(int sheetIndex, String sheetName, int rowIndex, Row row);

}
