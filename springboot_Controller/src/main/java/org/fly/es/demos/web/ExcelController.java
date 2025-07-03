package org.fly.es.demos.web;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExcelController {

    // http://127.0.0.1:8080/api/html
    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }

    @PostMapping("/upload-excel")
    public List<List<String>> uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空！");
        }

        // 解析 Excel
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // 读取第一个 Sheet
        List<List<String>> data = new ArrayList<>();

        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        rowData.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowData.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    default:
                        rowData.add("");
                }
            }
            data.add(rowData);
        }

        workbook.close();
        return data; // 返回解析后的数据（二维列表）
    }
}