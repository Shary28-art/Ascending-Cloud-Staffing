-- Ascending Cloud Staffing - MySQL 8 schema (matches the JPA entities; ddl-auto=validate)
-- For a NEW empty database:  mysql -u root -p ascending_cloud_staffing < schema.sql

CREATE DATABASE IF NOT EXISTS ascending_cloud_staffing CHARACTER SET utf8mb4;
USE ascending_cloud_staffing;

CREATE TABLE IF NOT EXISTS users (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  email VARCHAR(150) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(30) NOT NULL,
  phone VARCHAR(30),
  terms_accepted BIT(1),
  terms_accepted_at DATETIME(6),
  recruitment_consent BIT(1),
  created_at DATETIME(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS companies (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(150) NOT NULL,
  description TEXT,
  website VARCHAR(255),
  location VARCHAR(150),
  industry VARCHAR(100),
  company_size VARCHAR(50),
  logo_key VARCHAR(500),
  logo_content_type VARCHAR(100),
  logo_file_size BIGINT,
  created_at DATETIME(6),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS candidate_profiles (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  phone VARCHAR(30),
  location VARCHAR(150),
  city VARCHAR(100),
  state VARCHAR(100),
  country VARCHAR(100),
  bio TEXT,
  professional_headline VARCHAR(200),
  professional_summary TEXT,
  current_job_title VARCHAR(150),
  total_years_experience DECIMAL(4,1),
  desired_job_title VARCHAR(150),
  preferred_employment_type VARCHAR(50),
  preferred_work_arrangement VARCHAR(30),
  preferred_job_location VARCHAR(150),
  salary_expectation VARCHAR(100),
  availability_date DATE,
  work_authorization_status VARCHAR(100),
  sponsorship_required BIT(1),
  security_clearance_status VARCHAR(100),
  linkedin_portfolio_url VARCHAR(255),
  profile_visibility BIT(1) NOT NULL DEFAULT 1,
  share_with_employers BIT(1),
  PRIMARY KEY (id),
  UNIQUE KEY uk_candidate_user (user_id),
  CONSTRAINT fk_candidate_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS employer_profiles (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  phone VARCHAR(30),
  designation VARCHAR(100),
  company_id INT,
  primary_contact_name VARCHAR(150),
  contact_email VARCHAR(150),
  hiring_locations VARCHAR(255),
  employee_types_needed VARCHAR(255),
  preferred_work_arrangements VARCHAR(255),
  primary_roles VARCHAR(500),
  expected_hires INT,
  hiring_timeline VARCHAR(100),
  billing_address VARCHAR(500),
  selected_plan VARCHAR(100),
  preferred_interview_method VARCHAR(50),
  placement_terms_accepted BIT(1),
  notification_preferences VARCHAR(255),
  PRIMARY KEY (id),
  UNIQUE KEY uk_employer_user (user_id),
  CONSTRAINT fk_employer_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT fk_employer_company FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS skills (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_skills_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cloud_platforms (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_cloud_platforms_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS employment_types (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_employment_types_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS experience_levels (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_experience_levels_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS workplace_types (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_workplace_types_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS job_categories (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  PRIMARY KEY (id),
  UNIQUE KEY uk_job_categories_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS admin_roles (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(500),
  PRIMARY KEY (id),
  UNIQUE KEY uk_admin_roles_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS permissions (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  PRIMARY KEY (id),
  UNIQUE KEY uk_permissions_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS candidate_skills (
  candidate_id INT NOT NULL,
  skill_id INT NOT NULL,
  PRIMARY KEY (candidate_id, skill_id),
  CONSTRAINT fk_cs_candidate FOREIGN KEY (candidate_id) REFERENCES candidate_profiles (id) ON DELETE CASCADE,
  CONSTRAINT fk_cs_skill FOREIGN KEY (skill_id) REFERENCES skills (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS jobs (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(150) NOT NULL,
  company_id INT,
  location VARCHAR(150),
  salary VARCHAR(50),
  cloud_platform_id INT,
  employment_type_id INT,
  experience_level_id INT,
  workplace_type_id INT,
  job_category_id INT,
  description TEXT,
  responsibilities TEXT,
  required_qualifications TEXT,
  preferred_skills TEXT,
  education_requirements VARCHAR(255),
  certification_requirements VARCHAR(255),
  number_of_positions INT,
  application_deadline DATE,
  expected_start_date DATE,
  work_authorization VARCHAR(150),
  sponsorship_available BIT(1),
  security_clearance_required VARCHAR(100),
  travel_requirements VARCHAR(150),
  screening_questions TEXT,
  assigned_recruiter_id INT,
  status VARCHAR(20),
  approval_status VARCHAR(20),
  rejection_reason VARCHAR(500),
  approved_at DATETIME(6),
  approved_by_user_id INT,
  created_at DATETIME(6),
  PRIMARY KEY (id),
  KEY idx_jobs_public (approval_status, status),
  CONSTRAINT fk_jobs_company FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE SET NULL,
  CONSTRAINT fk_jobs_cloud FOREIGN KEY (cloud_platform_id) REFERENCES cloud_platforms (id) ON DELETE SET NULL,
  CONSTRAINT fk_jobs_emptype FOREIGN KEY (employment_type_id) REFERENCES employment_types (id) ON DELETE SET NULL,
  CONSTRAINT fk_jobs_explevel FOREIGN KEY (experience_level_id) REFERENCES experience_levels (id) ON DELETE SET NULL,
  CONSTRAINT fk_jobs_workplace FOREIGN KEY (workplace_type_id) REFERENCES workplace_types (id) ON DELETE SET NULL,
  CONSTRAINT fk_jobs_category FOREIGN KEY (job_category_id) REFERENCES job_categories (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS job_skills (
  job_id INT NOT NULL,
  skill_id INT NOT NULL,
  PRIMARY KEY (job_id, skill_id),
  CONSTRAINT fk_js_job FOREIGN KEY (job_id) REFERENCES jobs (id) ON DELETE CASCADE,
  CONSTRAINT fk_js_skill FOREIGN KEY (skill_id) REFERENCES skills (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS applications (
  id INT NOT NULL AUTO_INCREMENT,
  job_id INT NOT NULL,
  candidate_id INT NOT NULL,
  status VARCHAR(50) NOT NULL,
  applied_at DATETIME(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_application_job_candidate (job_id, candidate_id),
  CONSTRAINT fk_app_job FOREIGN KEY (job_id) REFERENCES jobs (id) ON DELETE CASCADE,
  CONSTRAINT fk_app_candidate FOREIGN KEY (candidate_id) REFERENCES candidate_profiles (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS application_status_history (
  id INT NOT NULL AUTO_INCREMENT,
  application_id INT NOT NULL,
  status VARCHAR(50) NOT NULL,
  changed_at DATETIME(6),
  PRIMARY KEY (id),
  CONSTRAINT fk_ash_application FOREIGN KEY (application_id) REFERENCES applications (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS interviews (
  id INT NOT NULL AUTO_INCREMENT,
  application_id INT NOT NULL,
  scheduled_at DATETIME(6),
  interview_type VARCHAR(50),
  status VARCHAR(50),
  meeting_link VARCHAR(500),
  notes TEXT,
  PRIMARY KEY (id),
  CONSTRAINT fk_interview_application FOREIGN KEY (application_id) REFERENCES applications (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS job_alerts (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  keyword VARCHAR(150),
  location VARCHAR(150),
  job_category_id INT,
  employment_type_id INT,
  experience_level_id INT,
  created_at DATETIME(6),
  PRIMARY KEY (id),
  CONSTRAINT fk_alert_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT fk_alert_category FOREIGN KEY (job_category_id) REFERENCES job_categories (id) ON DELETE SET NULL,
  CONSTRAINT fk_alert_emptype FOREIGN KEY (employment_type_id) REFERENCES employment_types (id) ON DELETE SET NULL,
  CONSTRAINT fk_alert_explevel FOREIGN KEY (experience_level_id) REFERENCES experience_levels (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS saved_jobs (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  job_id INT NOT NULL,
  saved_at DATETIME(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_saved_job_user_job (user_id, job_id),
  CONSTRAINT fk_saved_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT fk_saved_job FOREIGN KEY (job_id) REFERENCES jobs (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS resumes (
  id INT NOT NULL AUTO_INCREMENT,
  candidate_id INT NOT NULL,
  original_file_name VARCHAR(255) NOT NULL,
  storage_key VARCHAR(500) NOT NULL,
  content_type VARCHAR(100) NOT NULL,
  file_size BIGINT NOT NULL,
  uploaded_at DATETIME(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_resume_candidate (candidate_id),
  CONSTRAINT fk_resume_candidate FOREIGN KEY (candidate_id) REFERENCES candidate_profiles (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS conversations (
  id INT NOT NULL AUTO_INCREMENT,
  created_at DATETIME(6),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS conversation_participants (
  id INT NOT NULL AUTO_INCREMENT,
  conversation_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_conv_user (conversation_id, user_id),
  CONSTRAINT fk_cp_conversation FOREIGN KEY (conversation_id) REFERENCES conversations (id) ON DELETE CASCADE,
  CONSTRAINT fk_cp_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS messages (
  id INT NOT NULL AUTO_INCREMENT,
  conversation_id INT NOT NULL,
  sender_id INT NOT NULL,
  content TEXT NOT NULL,
  sent_at DATETIME(6),
  PRIMARY KEY (id),
  KEY idx_messages_conversation (conversation_id, sent_at),
  CONSTRAINT fk_msg_conversation FOREIGN KEY (conversation_id) REFERENCES conversations (id) ON DELETE CASCADE,
  CONSTRAINT fk_msg_sender FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notifications (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  title VARCHAR(200) NOT NULL,
  message TEXT,
  is_read BIT(1) NOT NULL DEFAULT 0,
  created_at DATETIME(6),
  PRIMARY KEY (id),
  KEY idx_notifications_user (user_id, is_read),
  CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS admin_role_permissions (
  role_id INT NOT NULL,
  permission_id INT NOT NULL,
  PRIMARY KEY (role_id, permission_id),
  CONSTRAINT fk_arp_role FOREIGN KEY (role_id) REFERENCES admin_roles (id) ON DELETE CASCADE,
  CONSTRAINT fk_arp_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user_admin_roles (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  admin_role_id INT NOT NULL,
  assigned_at DATETIME(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_admin_role (user_id, admin_role_id),
  CONSTRAINT fk_uar_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT fk_uar_role FOREIGN KEY (admin_role_id) REFERENCES admin_roles (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ---------------------------------------------------------------------------
-- Reference data (safe to re-run: INSERT IGNORE + unique name keys)
-- ---------------------------------------------------------------------------
INSERT IGNORE INTO employment_types (name) VALUES ('Full-time'),('Part-time'),('Contract'),('Contract-to-hire');
INSERT IGNORE INTO workplace_types (name) VALUES ('Remote'),('Hybrid'),('Onsite');
INSERT IGNORE INTO experience_levels (name) VALUES ('Entry level'),('Mid level'),('Senior level'),('Lead / Principal');
INSERT IGNORE INTO cloud_platforms (name) VALUES ('AWS'),('Azure'),('Google Cloud'),('Multi-cloud');
INSERT IGNORE INTO job_categories (name, description) VALUES
 ('Cloud Engineering','Cloud infrastructure and platform roles'),
 ('DevOps','CI/CD, automation and reliability'),
 ('Data & Analytics','Data engineering, BI and analytics'),
 ('Software Development','Application development'),
 ('Security','Cloud and application security');
INSERT IGNORE INTO admin_roles (name, description) VALUES
 ('SUPER_ADMIN','Full access'),('RECRUITER_ADMIN','Manages jobs and applications'),('SUPPORT_ADMIN','Read-only support access');
INSERT IGNORE INTO permissions (name, description) VALUES
 ('JOB_APPROVE','Approve or reject job postings'),('USER_MANAGE','Manage users'),
 ('APPLICATION_MANAGE','Manage applications'),('CONTENT_MANAGE','Manage reference data');

-- ---------------------------------------------------------------------------
-- Make an admin: register normally via POST /auth/register, then run:
--   UPDATE users SET role = 'ADMIN' WHERE email = 'you@example.com';
-- ---------------------------------------------------------------------------
