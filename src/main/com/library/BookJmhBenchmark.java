package com.library;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

// JMH标准注解：平均耗时、毫秒单位、预热2轮、正式测试3轮、单进程
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 3)
@Fork(1)
@State(Scope.Benchmark)
public class BookJmhBenchmark {

    @Benchmark
    public void selectBookQuery() throws Exception {
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from book");
        ResultSet rs = ps.executeQuery();
        while (rs.next()){}
        rs.close();
        ps.close();

        conn.close();

    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BookJmhBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}