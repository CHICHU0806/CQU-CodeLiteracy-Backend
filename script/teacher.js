/**
 * 教师端管理系统 - 统筹全功能版
 * 包含模块：初始化、统计板、学生管理、课程管理、资料管理、身份退出
 */

const USERNAME = localStorage.getItem('username') || '教师用户';
const API_BASE = "http://localhost:8080/api";

// ==========================================
// 1. 初始化页面：确保 DOM 加载后即刻读取
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    // 设置顶栏教师姓名和头像
    const nameEl = document.getElementById('teacher-name');
    if (nameEl) nameEl.textContent = USERNAME;

    const avatarEl = document.getElementById('teacher-avatar');
    if (avatarEl) avatarEl.src = `https://api.dicebear.com/7.x/avataaars/svg?seed=${encodeURIComponent(USERNAME)}`;

    // 默认进入统计板
    loadPage('dashboard');
});

// ==========================================
// 2. 页面导航逻辑：实现“进入即读取”
// ==========================================
function loadPage(page, element) {
    // A. 切换侧边栏高亮样式
    document.querySelectorAll('.sidebar-item').forEach(item => item.classList.remove('active'));
    if (element) {
        element.classList.add('active');
    } else {
        // 如果是通过代码调用（非点击），尝试手动匹配侧边栏项
        const items = document.querySelectorAll('.sidebar-item');
        items.forEach(item => {
            if (item.getAttribute('onclick')?.includes(`'${page}'`)) item.classList.add('active');
        });
    }

    // B. 更新页面标题
    const titleMap = {
        dashboard: '数据统计板',
        students: '学生学情管理',
        courses: '我的课程管理',
        materials: '教学资料上传'
    };
    document.getElementById('page-title').textContent = titleMap[page] || '管理系统';

    // C. 隐藏所有区域，显示目标区域
    document.querySelectorAll('.card').forEach(card => card.style.display = 'none');
    const targetContent = document.getElementById(`${page}-content`);
    if (targetContent) targetContent.style.display = 'block';

    // D. 触发各页面专用的读取逻辑
    if (page === 'dashboard') {
        refreshDashboard();
    } else if (page === 'students') {
        loadStudents();
    } else if (page === 'courses') {
        ensureCourseTableStructure(); // 恢复表格HTML架子
        loadCourses();               // 读取Course表并排列
    } else if (page === 'materials') {
        initMaterialPage();          // 初始化课程下拉框
    }
}

// ==========================================
// 3. 统计板逻辑 (Dashboard)
// ==========================================
function refreshDashboard() {
    fetch(`${API_BASE}/dashboard`)
        .then(res => res.json())
        .then(stats => {
            document.getElementById('course-count').textContent = stats.courses ?? 0;
            document.getElementById('student-count').textContent = stats.students ?? 0;
            document.getElementById('checkin-count').textContent = stats.totalCheckins ?? 0;
        })
        .catch(err => console.error('统计加载失败:', err));
}

// ==========================================
// 4. 学生管理逻辑 (Students)
// ==========================================
function loadStudents() {
    const tbody = document.getElementById('students-table-body');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">正在读取全校学生档案...</td></tr>';

    fetch(`${API_BASE}/students?role=STUDENT`)
        .then(res => res.json())
        .then(students => {
            tbody.innerHTML = '';
            if (students.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">暂无学生数据</td></tr>';
                return;
            }
            students.forEach(s => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${s.username}</td>
                    <td>${s.name || '未填真实姓名'}</td>
                    <td><span style="color:#2ecc71; font-weight:bold;">${s.checkinCount} 次</span></td>
                    <td><span style="color:#e67e22; font-weight:bold;">${s.totalScore} 分</span></td>
                    <td>
                        <button class="btn btn-outline" onclick="alert('查看学生详情功能正在接入...')">
                            <i class="fas fa-search"></i> 详情
                        </button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(err => {
            tbody.innerHTML = '<tr><td colspan="5" style="color:red; text-align:center;">加载失败，请检查后端接口</td></tr>';
        });
}

// ==========================================
// 5. 课程管理逻辑 (Courses) - 包含结构恢复
// ==========================================
function ensureCourseTableStructure() {
    const container = document.getElementById('courses-content');
    // 强制重置 HTML 结构，解决之前创建表单覆盖导致的问题
    container.innerHTML = `
        <div class="card-header" style="margin-bottom: 20px; display: flex; justify-content: flex-end;">
            <button class="btn btn-primary" onclick="showCourseForm()">
                <i class="fas fa-plus-circle"></i> 创建新课程
            </button>
        </div>
        <div id="courses-list">
            <table class="table" style="width:100%; border-collapse: collapse;">
                <thead>
                    <tr style="background:#f8f9fa;">
                        <th style="padding:12px; text-align:left;">课程名称</th>
                        <th style="padding:12px; text-align:left;">主讲教师</th>
                        <th style="padding:12px; text-align:left;">学生人数</th>
                        <th style="padding:12px; text-align:left;">资料数量</th>
                        <th style="padding:12px; text-align:left;">操作</th>
                    </tr>
                </thead>
                <tbody id="courses-table-body"></tbody>
            </table>
        </div>
    `;
}

function loadCourses() {
    const tbody = document.getElementById('courses-table-body');
    if (!tbody) return;

    fetch(`${API_BASE}/courses`)
        .then(res => res.json())
        .then(courses => {
            tbody.innerHTML = '';
            if (courses.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5" style="text-align:center; padding:20px;">还没有创建课程</td></tr>';
                return;
            }
            courses.forEach(course => {
                const row = document.createElement('tr');
                row.style.borderBottom = "1px solid #eee";
                row.innerHTML = `
                    <td style="padding:12px;"><strong>${course.name}</strong></td>
                    <td style="padding:12px;">${course.teacher}</td>
                    <td style="padding:12px;">${course.studentCount ?? 0}</td>
                    <td style="padding:12px;">${course.materialCount ?? 0}</td>
                    <td style="padding:12px;">
                        <button class="btn btn-outline" style="padding:5px 10px;" onclick="alert('正在进入课程详情...')">管理</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        });
}

function showCourseForm() {
    const container = document.getElementById('courses-content');
    container.innerHTML = `
        <div class="course-form" style="max-width:500px; margin:20px auto; background:#fff; padding:25px; border-radius:10px; box-shadow:0 4px 15px rgba(0,0,0,0.1);">
            <h3 style="margin-bottom:20px;">📚 开设新课程</h3>
            <div class="form-group" style="margin-bottom:15px;">
                <label style="display:block; margin-bottom:5px;">课程标题</label>
                <input type="text" id="course-name" style="width:100%; padding:10px; border:1px solid #ddd; border-radius:5px;" placeholder="例如: 高等数学 (2026春季)">
            </div>
            <div class="form-group" style="margin-bottom:20px;">
                <label style="display:block; margin-bottom:5px;">课程简介</label>
                <textarea id="course-description" style="width:100%; padding:10px; border:1px solid #ddd; border-radius:5px; height:80px;"></textarea>
            </div>
            <div style="display: flex; justify-content: flex-end; gap: 10px;">
                <button class="btn btn-outline" onclick="loadPage('courses')">取消</button>
                <button class="btn btn-primary" onclick="createCourse()">保存到数据库</button>
            </div>
        </div>
    `;
}

function createCourse() {
    const name = document.getElementById('course-name').value.trim();
    const desc = document.getElementById('course-description').value.trim();

    if (!name) { alert('请输入课程名称！'); return; }

    const courseData = { name: name, description: desc, teacher: USERNAME };

    fetch(`${API_BASE}/courses`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(courseData)
    })
        .then(res => {
            if (!res.ok) throw new Error('保存失败');
            return res.json();
        })
        .then(data => {
            alert(`成功！《${data.name}》已永久保存到数据库。`);
            loadPage('courses'); // 刷新并排列
        })
        .catch(err => alert('错误：' + err.message));
}

// ==========================================
// 6. 资料管理逻辑 (Materials)
// ==========================================
function initMaterialPage() {
    const select = document.getElementById('course-select');
    if (!select) return;

    fetch(`${API_BASE}/courses`)
        .then(res => res.json())
        .then(courses => {
            select.innerHTML = '<option value="">-- 请选择关联课程 --</option>';
            courses.forEach(c => {
                const opt = document.createElement('option');
                opt.value = c.id;
                opt.textContent = c.name;
                select.appendChild(opt);
            });
        });
}

function uploadMaterial() {
    const courseId = document.getElementById('course-select').value;
    const fileEl = document.getElementById('material-file');
    const file = fileEl.files[0];

    if (!courseId || !file) {
        alert('请确保已选择课程并选择了文件！');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    const btn = document.getElementById('upload-btn');
    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 正在上传...';

    fetch(`${API_BASE}/courses/${courseId}/materials`, {
        method: 'POST',
        body: formData
    })
        .then(res => {
            if (!res.ok) throw new Error('上传接口返回异常');
            return res.json();
        })
        .then(data => {
            alert(`上传成功！文件《${file.name}》已加入该课程。`);
            fileEl.value = ''; // 重置文件选择
        })
        .catch(err => alert('上传失败，请检查后端文件接口: ' + err.message))
        .finally(() => {
            btn.disabled = false;
            btn.innerHTML = '<i class="fas fa-cloud-upload-alt"></i> 上传资料';
        });
}

// ==========================================
// 7. 辅助功能 (Logout)
// ==========================================
function logout() {
    if (confirm('确定要退出管理系统吗？')) {
        localStorage.clear();
        window.location.href = 'login.html';
    }
}