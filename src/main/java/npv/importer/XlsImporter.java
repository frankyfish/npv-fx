/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author nick
 */
public class XlsImporter {
    
    private final File file;
    private final String tag;
    
    int[] rPosition = new int[2];
    Double[] rValues;
    
    public XlsImporter(File selectedFile, String tagForSearch) {
        this.file = selectedFile;
        this.tag = tagForSearch; //current tag in files #Ri
    }
    
    public Double[] importXls(){
        try {
            return parseFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Double[] parseFile() throws IOException {
        InputStream inputStream = new FileInputStream(file);
        POIFSFileSystem fs = new POIFSFileSystem(inputStream);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        System.out.println("Testing.First row num=" + sheet.getFirstRowNum());
        findTag(sheet, tag);
        
        //reading an array of Ri values after '#Ri' tag
        HSSFRow row = sheet.getRow(rPosition[0]);
        ArrayList<Double> cellValues = new ArrayList<Double>();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getColumnIndex() >= rPosition[1] + 1) {
                cellValues.add(cell.getNumericCellValue());
            }
        }

        rValues = new Double[cellValues.size()];
        rValues = cellValues.toArray(new Double[rValues.length]);
        System.out.println("Values from sheet:");
        for (int i = 0; i < rValues.length; i++) {
            System.out.println(rValues[i]);
        }
        
        return this.rValues;
    }

    private void findTag(HSSFSheet sheet, String searchTag) {
        //looking for '#Ri' tag
        HSSFRow row = sheet.getRow(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Iterator<Cell> cellIterator;
        boolean isFound = false;

        while (rowIterator.hasNext()) {
            if (!isFound) {
                Row rRow = rowIterator.next();
                cellIterator = rRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell rCell = cellIterator.next();
                    if (rCell.getCellType() == Cell.CELL_TYPE_STRING
                            && rCell.getStringCellValue().equals(tag)) {
                        rPosition[0] = rRow.getRowNum();
                        rPosition[1] = rCell.getColumnIndex();
                        isFound = true;
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

}
