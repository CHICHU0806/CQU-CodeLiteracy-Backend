package com.example.cqucodeliterary.repository;

import com.example.cqucodeliterary.entity.CheckInRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CheckInRecordRepository extends JpaRepository<CheckInRecord, Integer> {
    // 找出某个用户所有的签到日期，用来点亮月历
    List<CheckInRecord> findByUsername(String username);
}