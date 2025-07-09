CREATE TABLE IF NOT EXISTS todos (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    priority ENUM('高', '中', '低') NOT NULL DEFAULT '中',
    status ENUM('未着手', '着手中', '完了') NOT NULL DEFAULT '未着手'
);