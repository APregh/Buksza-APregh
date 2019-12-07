package hu.application.cash.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.Vetoed;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import hu.application.cash.model.pojo.TransactionPojo;

/**
 * Util osztály, amely a tranzakciók exportálásában segít.
 *
 * @author Pregh András
 */
@Vetoed
public class TransactionExcelExportUtil {

    /**
     * Létrehozza a tranzakciók oldalon lévő adatokból a megfelelő excelt.
     *
     * @param transactions a tranzakciók listája
     * @throws IOException
     */
    public static void generateTransactionXls(List<TransactionPojo> transactions) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        CreationHelper createHelper = workbook.getCreationHelper();

        Sheet sheet = workbook.createSheet("Tranzakciók");

        Row headerRow = sheet.createRow(0);

        List<String> cellHeaders =
                Arrays.asList("Tranzakció neve", "Fiók neve", "Tranzakció kategória", "Tranzakció típusa", "Összeg", "Létrehozás dátuma", "Pénzmozgás dátuma");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        for (int i = 0; i < 7; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(cellHeaders.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy. MM. dd."));

        int rowNum = 1;

        for (TransactionPojo transaction : transactions) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(transaction.getTransactionName());
            row.createCell(1).setCellValue(transaction.getAccountName());
            row.createCell(2).setCellValue(transaction.getTransactionCategory());
            row.createCell(3).setCellValue(transaction.getTransactionType());
            row.createCell(4).setCellValue(transaction.getAmount()); // + " Ft");

            Cell creationDateCell = row.createCell(5);
            creationDateCell.setCellValue(transaction.getCreationDate());
            creationDateCell.setCellStyle(dateCellStyle);

            Cell paymentDeadlineCell = row.createCell(6);
            paymentDeadlineCell.setCellValue(transaction.getPaymentDeadline());
            paymentDeadlineCell.setCellStyle(dateCellStyle);
        }

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        StringBuilder newLocalDateTime = new StringBuilder(String.valueOf(LocalDateTime.now()));
        newLocalDateTime.setCharAt(13, '-');
        newLocalDateTime.setCharAt(16, '-');
        newLocalDateTime.setCharAt(19, '-');
        FileOutputStream fileOut = new FileOutputStream("transactions-" + newLocalDateTime + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        workbook.close();
    }

    private TransactionExcelExportUtil() {

    }

}
