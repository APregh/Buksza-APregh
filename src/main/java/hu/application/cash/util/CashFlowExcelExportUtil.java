package hu.application.cash.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Vetoed;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Util osztály, amely a cash-flow-riport.fxml-hez tartozó excel exportot végzi el
 *
 * @author Pregh András
 */
@Vetoed
public class CashFlowExcelExportUtil {

    /**
     * Létrehozza a cash-flow-riport oldalon lévő adatokból a megfelelő excelt.
     *
     * @param incomeMap map, amely a bevételeket tartalmazza
     * @param costMap   map, amely a kiadásokat tartalmazza
     * @param totalMap  map, amely a végösszegeket tartalmazza
     * @param year      év
     * @throws IOException
     */
    public static void generateCashFlowXls(Map<Integer, Long> incomeMap, Map<Integer, Long> costMap, Map<Integer, Long> totalMap, int year) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Cash Flow Riport");

        Row headerRow = sheet.createRow(0);

        List<String> cellHeaders = Arrays.asList("Dátum", "Bevétel", "Kiadás", "Összesen");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        for (int i = 0; i < 4; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(cellHeaders.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;

        for (int i = 1; i <= 12; i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(CashFlowUtil.toDateLabel(year, i));
            row.createCell(1).setCellValue(CashFlowUtil.toIncomeLabel(incomeMap.get(i)));
            row.createCell(2).setCellValue(CashFlowUtil.toCostLabel(incomeMap.get(i)));
            row.createCell(3).setCellValue(CashFlowUtil.toTotalLabel(totalMap.get(i)));
        }

        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("Összesen");
        row.getCell(0).setCellStyle(headerCellStyle);
        row.createCell(1).setCellValue(CashFlowUtil.toIncomeLabel(CashFlowUtil.countIncomeAmount(incomeMap)));
        row.createCell(2).setCellValue(CashFlowUtil.toCostLabel(CashFlowUtil.countCostAmount(costMap)));
        row.createCell(3).setCellValue(CashFlowUtil.toTotalLabel(CashFlowUtil.countIncomeAmount(totalMap)));

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }


        StringBuilder newLocalDateTime = new StringBuilder(String.valueOf(LocalDateTime.now()));
        newLocalDateTime.setCharAt(13, '-');
        newLocalDateTime.setCharAt(16, '-');
        newLocalDateTime.setCharAt(19, '-');
        FileOutputStream fileOut = new FileOutputStream("cash-flow-riport-" + newLocalDateTime + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        workbook.close();
    }

    private CashFlowExcelExportUtil() {

    }

}
