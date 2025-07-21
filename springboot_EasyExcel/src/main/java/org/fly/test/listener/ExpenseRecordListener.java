package org.fly.test.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.fly.test.entity.ExpenseRecord;
import org.fly.test.service.ExpenseRecordService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ExpenseRecordListener implements ReadListener<ExpenseRecord> {

    @Resource
    ExpenseRecordService expenseRecordService;

    private static final int BATCH_SIZE = 1000; // 每1000条批量插入一次
    private List<ExpenseRecord> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_SIZE);
    private List<ExpenseRecord> allDataList = new ArrayList<>();//存储所有数据，

    /*
        每解析一行数据，一行数据复制给实体类 ExpenseRecord ，就调用 invoke方法，
        然后将数据放到 cachedDataList里面，当cachedDataList 的数据大于1000条时候，进行插库，并重置缓存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invoke(ExpenseRecord data, AnalysisContext context) {
        cachedDataList.add(data);
        allDataList.add(data); // 同步添加到全量列表
        if (cachedDataList.size() >= BATCH_SIZE) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_SIZE); //重置缓存
        }
    }

    /*
        处理最后一批不足 BATCH_SIZE 的数据
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("Excel 数据导入完成！");
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveData() {
        if (!cachedDataList.isEmpty()) {
            expenseRecordService.batchInsert(cachedDataList); // 调用 MyBatis 批量插入
            log.info("已保存 {} 条数据", cachedDataList.size());
        }
    }


    //获取完整 excel 数据
    public List<ExpenseRecord> getAllData() {
        return allDataList;
    }



}