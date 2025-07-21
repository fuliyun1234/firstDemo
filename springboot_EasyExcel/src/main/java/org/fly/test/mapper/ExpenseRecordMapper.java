package org.fly.test.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.fly.test.entity.ExpenseRecord;
import org.fly.test.entity.ExpenseRecordExport;

import java.util.List;

@Mapper
public interface ExpenseRecordMapper {


    void insertBatch(List<ExpenseRecord> records);

    List<ExpenseRecordExport> getExpenseRecordExportList();
    List<ExpenseRecordExport> getExpenseRecordExportList2(int offset, int pageSize);
}
