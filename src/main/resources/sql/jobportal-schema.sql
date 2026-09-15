-- Create companies table
CREATE TABLE IF NOT EXISTS companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    logo VARCHAR(500),
    industry VARCHAR(100) NOT NULL,
    size VARCHAR(50) NOT NULL,
    rating DECIMAL(3,2) NOT NULL,
    locations VARCHAR(1000),
    founded INT NOT NULL,
    description TEXT,
    employees INT,
    website VARCHAR(500),
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by  VARCHAR(20)  NOT NULL,
    updated_at  TIMESTAMP   DEFAULT NULL,
    updated_by  VARCHAR(20) DEFAULT NULL
    );


create table contacts (
      id bigint primary key auto_increment,
      name varchar(50) not null,
      user_type varchar(20) not null,
      status varchar(20) not null,
      email varchar(100) not null,
      subject varchar(150) not null,
      message varchar(300) not null,
      created_at timestamp default current_timestamp,
      created_by varchar(100) not null,
      updated_at timestamp default null,
      updated_by varchar(100) default null
);


-- Create jobs table
CREATE TABLE IF NOT EXISTS jobs (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        company_id BIGINT NOT NULL,
        location VARCHAR(255) NOT NULL,
        work_type VARCHAR(50) NOT NULL, -- On-site, Remote, Hybrid
        job_type VARCHAR(50) NOT NULL, -- Full-time, Part-time, Contract, Freelance
        category VARCHAR(100) NOT NULL, -- Technology, Design, Marketing, Sales, Finance, Healthcare, Education, Operations
        experience_level VARCHAR(50) NOT NULL, -- Entry Level, Mid Level, Senior Level, Executive Level
        salary_min DECIMAL(12,2) NOT NULL,
        salary_max DECIMAL(12,2) NOT NULL,
        salary_currency VARCHAR(10) DEFAULT 'USD' NOT NULL,
        salary_period VARCHAR(20) DEFAULT 'year' NOT NULL,
        description TEXT NOT NULL,
        requirements TEXT, -- JSON array stored as text
        benefits TEXT, -- JSON array stored as text
        posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        application_deadline TIMESTAMP,
        applications_count INT DEFAULT 0,
        featured BOOLEAN DEFAULT FALSE,
        urgent BOOLEAN DEFAULT FALSE,
        remote BOOLEAN DEFAULT FALSE,
        status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL, -- ACTIVE, CLOSED, DRAFT
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        created_by VARCHAR(20) NOT NULL,
        updated_at TIMESTAMP DEFAULT NULL,
        updated_by VARCHAR(20) DEFAULT NULL,
        FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE
);


-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
     id     BIGINT AUTO_INCREMENT PRIMARY KEY,
     name        VARCHAR(50) NOT NULL UNIQUE,
     created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
     created_by VARCHAR(20) NOT NULL,
     updated_at TIMESTAMP   DEFAULT NULL,
     updated_by VARCHAR(20) DEFAULT NULL
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     email VARCHAR(255) NOT NULL UNIQUE,
     password_hash VARCHAR(500) NOT NULL,
     mobile_number VARCHAR(20) UNIQUE,
     role_id BIGINT NOT NULL,
     company_id BIGINT NULL,
     created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
     created_by    VARCHAR(20)  NOT NULL,
     updated_at    TIMESTAMP   DEFAULT NULL,
     updated_by    VARCHAR(20) DEFAULT NULL,
     CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id),
     CONSTRAINT fk_users_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE SET NULL
);