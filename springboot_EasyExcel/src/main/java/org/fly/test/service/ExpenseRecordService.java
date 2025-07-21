package org.fly.test.service;

import org.fly.test.entity.ExpenseRecord;
import org.fly.test.entity.ExpenseRecordExport;
import org.springframework.stereotype.Service;

import java.util.List;




public interface ExpenseRecordService {


    void batchInsert(List<ExpenseRecord> records);

    List<ExpenseRecordExport> getExpenseRecordExportList();
    List<ExpenseRecordExport> getExpenseRecordExportList2(int pageNum, int pageSize);
}
