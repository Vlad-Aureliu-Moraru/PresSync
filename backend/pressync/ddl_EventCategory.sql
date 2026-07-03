CREATE TABLE categorii_evenimente
(
    id                      INT AUTO_INCREMENT NOT NULL,
    name                    VARCHAR(255)       NULL,
    starting_time           time               NULL,
    end_time                time               NULL,
    attendance_time_start   time               NULL,
    attendance_duration     INT                NULL,
    repeatable              BIT(1)             NULL,
    repeatable_type         VARCHAR(255)       NULL,
    repeats_after_finished  BIT(1)             NULL,
    repeats_on_specific_day VARCHAR(255)       NULL,
    CONSTRAINT pk_categorieeveniment PRIMARY KEY (id)
);