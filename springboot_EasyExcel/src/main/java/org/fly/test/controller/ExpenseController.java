package org.fly.test.controller;


import com.alibaba.excel.EasyExcel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.fly.test.entity.ExpenseRecord;
import org.fly.test.entity.ExpenseRecordExport;
import org.fly.test.listener.ExpenseRecordListener;
import org.fly.test.service.ExpenseRecordService;
import org.github.jamm.MemoryMeter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@Slf4j
@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    @Autowired
    private ExpenseRecordListener expenseRecordListener;

    @Autowired
    private ExpenseRecordService expenseRecordService;


    //单线程解析，批量插入  数据量较小（如几万条或更少的数据）
    @PostMapping("/upload")
    @ResponseBody
    public List<ExpenseRecord> upload(@RequestParam("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), ExpenseRecord.class, expenseRecordListener)//自动入，交给spring容器
                .ignoreEmptyRow(true) // 忽略空行
                .sheet()
                .doRead();
        return expenseRecordListener.getAllData();
    }


    //多线程解析，每个sheet对应一个线程；异步线程批量插入   数据量较大（如几十万到百万条数据）
    @PostMapping("/upload2")
    @ResponseBody
    public void upload2(@RequestParam("file") MultipartFile file) throws IOException {
        // 1. 获取实际的 sheet 数量
        ExcelReader excelReader = EasyExcel.read(file.getInputStream()).build();
        int sheetCount = excelReader.excelExecutor().sheetList().size();
        excelReader.finish(); // 及时关闭资源

        // 2. 使用线程池（推荐自定义线程池，而不是全局共享的 executorService）
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                sheetCount,  //核心线程数
                20,          //线程池允许的最大线程数
                10,          //非核心线程闲置超时时长
                TimeUnit.SECONDS,                       // 时间单位
                new ArrayBlockingQueue(10),     // 有界队列
                Executors.defaultThreadFactory(),       // 默认线程工厂，可自定义线程名称等
                new ThreadPoolExecutor.AbortPolicy()    // 默认 拒绝策略

        );

        // 3. 提交任务
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < sheetCount; i++) {
            int sheetIndex = i;
            tasks.add(() -> {
                // 每个线程使用独立的 InputStream 和 Listener（避免并  。-发问题）
                try (InputStream inputStream = file.getInputStream()) {
                    ExpenseRecordListener listener = new ExpenseRecordListener(); // 每个线程独立实例
                    EasyExcel.read(inputStream, ExpenseRecord.class, listener)
                            .ignoreEmptyRow(true) // 忽略空行
                            .sheet(sheetIndex)
                            .doRead();
                } catch (IOException e) {
                    throw new RuntimeException("读取Sheet失败: " + sheetIndex, e);
                }
                return null;
            });
        }

        // 4. 执行并等待所有任务完成
        try {
            threadPool.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            throw new RuntimeException("任务被中断", e);
        } finally {
            threadPool.shutdown(); // 关闭线程池
        }
    }


    public void exportStudents(HttpServletResponse response) throws Exception {
        //EasyExcleUtilsExtend.exportExcleExtend(response,  TeachingExcel.class, null, "导入学员");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("导入学员", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        // 启用分块传输编码以支持进度跟踪
        response.setHeader("Transfer-Encoding", "chunked");

        // 2. 设置表头样式（匹配图片中的浅灰色底纹+黑字+不换行）
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setWrapped(false); // 关闭自动换行
        // 3. 应用样式策略
        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(
                headStyle, // 表头样式
                (List<WriteCellStyle>) null       // 内容样式（默认白底黑字）
        );
        List<ExpenseRecordExport> list = new ArrayList<>();

        EasyExcel.write(response.getOutputStream(), ExpenseRecordExport.class)
                .registerWriteHandler(styleStrategy) // 应用样式
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自动匹配最长内容
                .sheet("导入学员")
                .doWrite(list);
    }


    /**
     * 测试内存占用
     * @param response
     * @throws IOException
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        Instant start = Instant.now();

        //原始导出可以看到哪导致导不出
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<ExpenseRecordExport> list = expenseRecordService.getExpenseRecordExportList(); // 获取数据


        EasyExcel.write(response.getOutputStream(), ExpenseRecordExport.class)
                .sheet("测试数据")
                .doWrite(list);

        Duration duration = Duration.between(start, Instant.now());
        String formattedDuration = formatDuration(duration);
        log.info("导出操作耗时: {}", formattedDuration);
        MemoryMeter meter = MemoryMeter.builder()
                .withGuessing(MemoryMeter.Guess.UNSAFE)
                .build();

        // 测量整个列表及其元素的内存
        long totalSize = meter.measureDeep(list);

        log.info("List占用内存: {} bytes (约 {} MB)",
                totalSize,
                totalSize / (1024.0 * 1024.0));
        list.clear();

        long totalSize1 = meter.measureDeep(list);
        log.info("List占用内存: {} bytes (约 {} MB)",
                totalSize1,
                totalSize1 / (1024.0 * 1024.0));

    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        int minutes = (int) ((duration.getSeconds() % 3600) / 60);
        int seconds = (int) (duration.getSeconds() % 60);

        return String.format("%d小时%d分钟%d秒", hours, minutes, seconds);
    }


    /*
        使用流出来 可以避免oom，但是慢

     */
    @GetMapping("/export2")
    public void export2(HttpServletResponse response) throws IOException {
        Instant start = Instant.now();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        // 启用分块传输编码以支持进度跟踪
        response.setHeader("Transfer-Encoding", "chunked");

        // 2. 使用 try-with-resources 确保资源释放
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(),
                                                        ExpenseRecordExport.class).autoCloseStream(true).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet("数据").build();
            // 3. 分页参数（建议每页5000-10000条，根据实际数据量调整）
            int pageSize = 10000;
            int pageNum = 1;
            List<ExpenseRecordExport> batchList = new ArrayList<>();
            // 4. 分页查询 + 流式写入
            for (pageNum = 1; ; pageNum++) {
                batchList = expenseRecordService.getExpenseRecordExportList2(pageNum, pageSize);
                if (CollectionUtils.isEmpty(batchList)) {
                    break;
                }
                excelWriter.write(batchList, writeSheet);
                batchList.clear();
                System.out.println(pageNum);
            }
        }


        Duration duration = Duration.between(start, Instant.now());
        String formattedDuration = formatDuration(duration);
        log.info("导出操作耗时: {}", formattedDuration);

    }






}
