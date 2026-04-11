# PawPal-Healing-Adopt


CREATE TABLE IF NOT EXISTS feedback (
    feedback_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '反馈ID',
    user_id VARCHAR(50) NOT NULL COMMENT '用户ID',
    username VARCHAR(100) COMMENT '用户名',
    content TEXT NOT NULL COMMENT '反馈内容',
    submit_time DATETIME NOT NULL COMMENT '提交时间',
    status VARCHAR(20) DEFAULT '待处理' COMMENT '状态：待处理/已回复',
    reply_content TEXT COMMENT '回复内容',
    reply_time DATETIME COMMENT '回复时间',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户反馈表';




CREATE TABLE adoption_request (
request_id INT PRIMARY KEY AUTO_INCREMENT,
user_id VARCHAR(50) NOT NULL,
pet_id INT NOT NULL,
applicant_name VARCHAR(100) NOT NULL,
applicant_phone VARCHAR(20) NOT NULL,
applicant_address VARCHAR(255) NOT NULL,
apply_reason TEXT,
apply_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
status VARCHAR(20) NOT NULL DEFAULT '待审核',
FOREIGN KEY (user_id) REFERENCES user(user_id),
FOREIGN KEY (pet_id) REFERENCES pet(id)
);