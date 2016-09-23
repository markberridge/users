CREATE TABLE users (
    id            NVARCHAR(64) PRIMARY KEY,
    username      NVARCHAR(255),
    password      NVARCHAR(255),
    status        NVARCHAR(64),
    updated_date  DATETIME,
    created_date  DATETIME
);

CREATE TABLE user_event (
    id            INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tag_uri       NVARCHAR(100) NOT NULL,
    users_id      NVARCHAR(64) NOT NULL,
    updated_date  DATETIME,
    created_date  DATETIME
);

-- TODO add primary keys, constraints and indexes 