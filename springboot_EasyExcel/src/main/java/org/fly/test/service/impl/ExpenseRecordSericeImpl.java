package org.fly.test.service.impl;

import jakarta.annotation.Resource;
import org.fly.test.entity.ExpenseRecord;
import org.fly.test.entity.ExpenseRecordExport;
import org.fly.test.mapper.ExpenseRecordMapper;
import org.fly.test.service.ExpenseRecordService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExpenseRecordSericeImpl implements ExpenseRecordService {

    @Resource
    private ExpenseRecordMapper expenseRecordMapper;



    @Override
    public void batchInsert(List<ExpenseRecord> records) {
        expenseRecordMapper.insertBatch(records);
    }

    @Override
    public List<ExpenseRecordExport> getExpenseRecordExportList() {
        return expenseRecordMapper.getExpenseRecordExportList();
    }


    @Override
    public List<ExpenseRecordExport> getExpenseRecordExportList2(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;  // 计算偏移量
        return expenseRecordMapper.getExpenseRecordExportList2(offset,pageSize);
    }
}
