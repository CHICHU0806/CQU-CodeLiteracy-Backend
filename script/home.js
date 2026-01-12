function getFilePreviewLink(fileUrl) {
    const ext = fileUrl.split('.').pop().toLowerCase();
    const previewTypes = ['pdf', 'mp4', 'mp3', 'flac', 'txt', 'jpg', 'png'];

    if (previewTypes.includes(ext)) {
        return `<iframe src="${fileUrl}" style="width:100%; height:200px; border:1px solid #eee;"></iframe>`;
    }

    return `<a href="${fileUrl}" target="_blank" style="color:#F2876A; text-decoration:none;">下载讲义</a>`;
}

// 在课程渲染中使用
html += `<div class="course-link">${getFilePreviewLink(`${API_BASE.replace('/api','')}/materials/${c.id}.pdf`)}</div>`;