CREATE TABLE Notificari (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action_url VARCHAR(500),
    category_id INT,
    FOREIGN KEY (user_id) REFERENCES Utilizatori(id)
);
