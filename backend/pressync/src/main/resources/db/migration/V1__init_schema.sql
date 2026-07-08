CREATE TABLE Utilizatori (
    id INT AUTO_INCREMENT NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    active BIT(1) NOT NULL,
    failed_login_attempts INT NOT NULL,
    account_locked_until DATETIME(6) NULL,
    mfa_enabled BIT(1) NOT NULL,
    mfa_code VARCHAR(255) NULL,
    mfa_expiry DATETIME(6) NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE Utilizatori ADD CONSTRAINT uk_utilizatori_email UNIQUE (email);

CREATE TABLE ConfigurariEveniment (
    id INT AUTO_INCREMENT NOT NULL,
    repeatableType VARCHAR(255) NULL,
    repeatsOnSpecificDay VARCHAR(255) NULL,
    base_date DATE NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE CategoriiEvenimente (
    id INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    startingTime TIME(6) NULL,
    endTime TIME(6) NULL,
    attendanceTimeStart TIME(6) NULL,
    attendanceDuration INT NULL,
    repeatable BIT(1) NULL,
    date DATE NULL,
    created_by INT NULL,
    config_id INT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE CategoriiEvenimente ADD CONSTRAINT fk_categorie_created_by FOREIGN KEY (created_by) REFERENCES Utilizatori(id);
ALTER TABLE CategoriiEvenimente ADD CONSTRAINT fk_categorie_config FOREIGN KEY (config_id) REFERENCES ConfigurariEveniment(id);

CREATE TABLE Evenimente (
    id INT AUTO_INCREMENT NOT NULL,
    event_category_id INT NULL,
    active BIT(1) NULL,
    archived BIT(1) NULL,
    date DATE NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE Evenimente ADD CONSTRAINT fk_eveniment_categorie FOREIGN KEY (event_category_id) REFERENCES CategoriiEvenimente(id);

CREATE TABLE Prezente (
    id INT AUTO_INCREMENT NOT NULL,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    joined_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE Prezente ADD CONSTRAINT fk_prezenta_utilizator FOREIGN KEY (user_id) REFERENCES Utilizatori(id);
ALTER TABLE Prezente ADD CONSTRAINT fk_prezenta_eveniment FOREIGN KEY (event_id) REFERENCES Evenimente(id);
ALTER TABLE Prezente ADD CONSTRAINT uk_prezenta_user_event UNIQUE (user_id, event_id);

CREATE TABLE Notificari (
    id INT AUTO_INCREMENT NOT NULL,
    user_id INT NOT NULL,
    type VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NULL,
    is_read BIT(1) NOT NULL,
    timestamp DATETIME(6) NOT NULL,
    action_url VARCHAR(255) NULL,
    category_id INT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE Notificari ADD CONSTRAINT fk_notificare_utilizator FOREIGN KEY (user_id) REFERENCES Utilizatori(id);
