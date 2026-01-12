package com.example.cqucodeliterary.repository;

import com.example.cqucodeliterary.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    /**
     * 根据课程 ID 查找该课程下的所有资料
     * 只要方法名符合驼峰命名规范（findBy + 属性名），JPA 会自动生成 SQL：
     * SELECT * FROM materials WHERE course_id = ?
     */
    List<Material> findByCourseId(Integer courseId);

    /**
     * 按照上传时间倒序排列（让最新的资料排在前面）
     * SQL: SELECT * FROM materials ORDER BY upload_time DESC
     */
    List<Material> findAllByOrderByUploadTimeDesc();
}