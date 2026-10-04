-- Ascending Cloud Staffing - MySQL 8 schema (matches the JPA entities; ddl-auto=validate)
-- For your EXISTING database. Safe to run more than once.
--   mysql -u root -p ascending_cloud_staffing < upgrade_existing_db.sql
-- 1) creates any missing tables  2) adds every missing column  3) back-fills old jobs.
-- It does NOT change the type of a column that already exists, and new foreign keys on
-- newly-added columns are not added (the app works without them).
-- BACK UP YOUR DATABASE FIRST.

USE ascending_cloud_staffing;

DROP PROCEDURE IF EXISTS add_column_if_missing;
DELIMITER //
CREATE PROCEDURE add_column_if_missing(IN tbl VARCHAR(64), IN col VARCHAR(64), IN ddl VARCHAR(255))
BEGIN
  IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                 WHERE table_schema = DATABASE() AND table_name = tbl AND column_name = col) THEN
    SET @sql = CONCAT('ALTER TABLE `', tbl, '` ADD COLUMN `', col, '` ', ddl);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END//
DELIMITER ;

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

-- Add columns that are missing from tables that already existed
CALL add_column_if_missing('users', 'name', 'VARCHAR(100)');
CALL add_column_if_missing('users', 'first_name', 'VARCHAR(100)');
CALL add_column_if_missing('users', 'last_name', 'VARCHAR(100)');
CALL add_column_if_missing('users', 'email', 'VARCHAR(150)');
CALL add_column_if_missing('users', 'password', 'VARCHAR(255)');
CALL add_column_if_missing('users', 'role', 'VARCHAR(30)');
CALL add_column_if_missing('users', 'phone', 'VARCHAR(30)');
CALL add_column_if_missing('users', 'terms_accepted', 'BIT(1)');
CALL add_column_if_missing('users', 'terms_accepted_at', 'DATETIME(6)');
CALL add_column_if_missing('users', 'recruitment_consent', 'BIT(1)');
CALL add_column_if_missing('users', 'created_at', 'DATETIME(6)');
CALL add_column_if_missing('companies', 'name', 'VARCHAR(150)');
CALL add_column_if_missing('companies', 'description', 'TEXT');
CALL add_column_if_missing('companies', 'website', 'VARCHAR(255)');
CALL add_column_if_missing('companies', 'location', 'VARCHAR(150)');
CALL add_column_if_missing('companies', 'industry', 'VARCHAR(100)');
CALL add_column_if_missing('companies', 'company_size', 'VARCHAR(50)');
CALL add_column_if_missing('companies', 'logo_key', 'VARCHAR(500)');
CALL add_column_if_missing('companies', 'logo_content_type', 'VARCHAR(100)');
CALL add_column_if_missing('companies', 'logo_file_size', 'BIGINT');
CALL add_column_if_missing('companies', 'created_at', 'DATETIME(6)');
CALL add_column_if_missing('candidate_profiles', 'user_id', 'INT');
CALL add_column_if_missing('candidate_profiles', 'phone', 'VARCHAR(30)');
CALL add_column_if_missing('candidate_profiles', 'location', 'VARCHAR(150)');
CALL add_column_if_missing('candidate_profiles', 'city', 'VARCHAR(100)');
CALL add_column_if_missing('candidate_profiles', 'state', 'VARCHAR(100)');
CALL add_column_if_missing('candidate_profiles', 'country', 'VARCHAR(100)');
CALL add_column_if_missing('candidate_profiles', 'bio', 'TEXT');
CALL add_column_if_missing('candidate_profiles', 'professional_headline', 'VARCHAR(200)');
CALL add_column_if_missing('candidate_profiles', 'professional_summary', 'TEXT');
CALL add_column_if_missing('candidate_profiles', 'current_job_title', 'VARCHAR(150)');
CALL add_column_if_missing('candidate_profiles', 'total_years_experience', 'DECIMAL(4,1)');
CALL add_column_if_missing('candidate_profiles', 'desired_job_title', 'VARCHAR(150)');
CALL add_column_if_missing('candidate_profiles', 'preferred_employment_type', 'VARCHAR(50)');
CALL add_column_if_missing('candidate_profiles', 'preferred_work_arrangement', 'VARCHAR(30)');
CALL add_column_if_missing('candidate_profiles', 'preferred_job_location', 'VARCHAR(150)');
CALL add_column_if_missing('candidate_profiles', 'salary_expectation', 'VARCHAR(100)');
CALL add_column_if_missing('candidate_profiles', 'availability_date', 'DATE');
CALL add_column_if_missing('candidate_profiles', 'work_authorization_status', 'VARCHAR(100)');
CALL add_column_if_missing('candidate_profiles', 'sponsorship_required', 'BIT(1)');
CALL add_column_if_missing('candidate_profiles', 'security_clearance_status', 'VARCHAR(100)');
CALL add_column_if_missing('candidate_profiles', 'linkedin_portfolio_url', 'VARCHAR(255)');
CALL add_column_if_missing('candidate_profiles', 'profile_visibility', 'BIT(1) NOT NULL DEFAULT 1');
CALL add_column_if_missing('candidate_profiles', 'share_with_employers', 'BIT(1)');
CALL add_column_if_missing('employer_profiles', 'user_id', 'INT');
CALL add_column_if_missing('employer_profiles', 'phone', 'VARCHAR(30)');
CALL add_column_if_missing('employer_profiles', 'designation', 'VARCHAR(100)');
CALL add_column_if_missing('employer_profiles', 'company_id', 'INT');
CALL add_column_if_missing('employer_profiles', 'primary_contact_name', 'VARCHAR(150)');
CALL add_column_if_missing('employer_profiles', 'contact_email', 'VARCHAR(150)');
CALL add_column_if_missing('employer_profiles', 'hiring_locations', 'VARCHAR(255)');
CALL add_column_if_missing('employer_profiles', 'employee_types_needed', 'VARCHAR(255)');
CALL add_column_if_missing('employer_profiles', 'preferred_work_arrangements', 'VARCHAR(255)');
CALL add_column_if_missing('employer_profiles', 'primary_roles', 'VARCHAR(500)');
CALL add_column_if_missing('employer_profiles', 'expected_hires', 'INT');
CALL add_column_if_missing('employer_profiles', 'hiring_timeline', 'VARCHAR(100)');
CALL add_column_if_missing('employer_profiles', 'billing_address', 'VARCHAR(500)');
CALL add_column_if_missing('employer_profiles', 'selected_plan', 'VARCHAR(100)');
CALL add_column_if_missing('employer_profiles', 'preferred_interview_method', 'VARCHAR(50)');
CALL add_column_if_missing('employer_profiles', 'placement_terms_accepted', 'BIT(1)');
CALL add_column_if_missing('employer_profiles', 'notification_preferences', 'VARCHAR(255)');
CALL add_column_if_missing('skills', 'name', 'VARCHAR(100)');
CALL add_column_if_missing('cloud_platforms', 'name', 'VARCHAR(50)');
CALL add_column_if_missing('employment_types', 'name', 'VARCHAR(50)');
CALL add_column_if_missing('experience_levels', 'name', 'VARCHAR(50)');
CALL add_column_if_missing('workplace_types', 'name', 'VARCHAR(50)');
CALL add_column_if_missing('job_categories', 'name', 'VARCHAR(100)');
CALL add_column_if_missing('job_categories', 'description', 'VARCHAR(500)');
CALL add_column_if_missing('admin_roles', 'name', 'VARCHAR(255)');
CALL add_column_if_missing('admin_roles', 'description', 'VARCHAR(500)');
CALL add_column_if_missing('permissions', 'name', 'VARCHAR(100)');
CALL add_column_if_missing('permissions', 'description', 'VARCHAR(255)');
CALL add_column_if_missing('jobs', 'title', 'VARCHAR(150)');
CALL add_column_if_missing('jobs', 'company_id', 'INT');
CALL add_column_if_missing('jobs', 'location', 'VARCHAR(150)');
CALL add_column_if_missing('jobs', 'salary', 'VARCHAR(50)');
CALL add_column_if_missing('jobs', 'cloud_platform_id', 'INT');
CALL add_column_if_missing('jobs', 'employment_type_id', 'INT');
CALL add_column_if_missing('jobs', 'experience_level_id', 'INT');
CALL add_column_if_missing('jobs', 'workplace_type_id', 'INT');
CALL add_column_if_missing('jobs', 'job_category_id', 'INT');
CALL add_column_if_missing('jobs', 'description', 'TEXT');
CALL add_column_if_missing('jobs', 'responsibilities', 'TEXT');
CALL add_column_if_missing('jobs', 'required_qualifications', 'TEXT');
CALL add_column_if_missing('jobs', 'preferred_skills', 'TEXT');
CALL add_column_if_missing('jobs', 'education_requirements', 'VARCHAR(255)');
CALL add_column_if_missing('jobs', 'certification_requirements', 'VARCHAR(255)');
CALL add_column_if_missing('jobs', 'number_of_positions', 'INT');
CALL add_column_if_missing('jobs', 'application_deadline', 'DATE');
CALL add_column_if_missing('jobs', 'expected_start_date', 'DATE');
CALL add_column_if_missing('jobs', 'work_authorization', 'VARCHAR(150)');
CALL add_column_if_missing('jobs', 'sponsorship_available', 'BIT(1)');
CALL add_column_if_missing('jobs', 'security_clearance_required', 'VARCHAR(100)');
CALL add_column_if_missing('jobs', 'travel_requirements', 'VARCHAR(150)');
CALL add_column_if_missing('jobs', 'screening_questions', 'TEXT');
CALL add_column_if_missing('jobs', 'assigned_recruiter_id', 'INT');
CALL add_column_if_missing('jobs', 'status', 'VARCHAR(20)');
CALL add_column_if_missing('jobs', 'approval_status', 'VARCHAR(20)');
CALL add_column_if_missing('jobs', 'rejection_reason', 'VARCHAR(500)');
CALL add_column_if_missing('jobs', 'approved_at', 'DATETIME(6)');
CALL add_column_if_missing('jobs', 'approved_by_user_id', 'INT');
CALL add_column_if_missing('jobs', 'created_at', 'DATETIME(6)');
CALL add_column_if_missing('applications', 'job_id', 'INT');
CALL add_column_if_missing('applications', 'candidate_id', 'INT');
CALL add_column_if_missing('applications', 'status', 'VARCHAR(50)');
CALL add_column_if_missing('applications', 'applied_at', 'DATETIME(6)');
CALL add_column_if_missing('application_status_history', 'application_id', 'INT');
CALL add_column_if_missing('application_status_history', 'status', 'VARCHAR(50)');
CALL add_column_if_missing('application_status_history', 'changed_at', 'DATETIME(6)');
CALL add_column_if_missing('interviews', 'application_id', 'INT');
CALL add_column_if_missing('interviews', 'scheduled_at', 'DATETIME(6)');
CALL add_column_if_missing('interviews', 'interview_type', 'VARCHAR(50)');
CALL add_column_if_missing('interviews', 'status', 'VARCHAR(50)');
CALL add_column_if_missing('interviews', 'meeting_link', 'VARCHAR(500)');
CALL add_column_if_missing('interviews', 'notes', 'TEXT');
CALL add_column_if_missing('job_alerts', 'user_id', 'INT');
CALL add_column_if_missing('job_alerts', 'keyword', 'VARCHAR(150)');
CALL add_column_if_missing('job_alerts', 'location', 'VARCHAR(150)');
CALL add_column_if_missing('job_alerts', 'job_category_id', 'INT');
CALL add_column_if_missing('job_alerts', 'employment_type_id', 'INT');
CALL add_column_if_missing('job_alerts', 'experience_level_id', 'INT');
CALL add_column_if_missing('job_alerts', 'created_at', 'DATETIME(6)');
CALL add_column_if_missing('saved_jobs', 'user_id', 'INT');
CALL add_column_if_missing('saved_jobs', 'job_id', 'INT');
CALL add_column_if_missing('saved_jobs', 'saved_at', 'DATETIME(6)');
CALL add_column_if_missing('resumes', 'candidate_id', 'INT');
CALL add_column_if_missing('resumes', 'original_file_name', 'VARCHAR(255)');
CALL add_column_if_missing('resumes', 'storage_key', 'VARCHAR(500)');
CALL add_column_if_missing('resumes', 'content_type', 'VARCHAR(100)');
CALL add_column_if_missing('resumes', 'file_size', 'BIGINT');
CALL add_column_if_missing('resumes', 'uploaded_at', 'DATETIME(6)');
CALL add_column_if_missing('conversations', 'created_at', 'DATETIME(6)');
CALL add_column_if_missing('conversation_participants', 'conversation_id', 'INT');
CALL add_column_if_missing('conversation_participants', 'user_id', 'INT');
CALL add_column_if_missing('messages', 'conversation_id', 'INT');
CALL add_column_if_missing('messages', 'sender_id', 'INT');
CALL add_column_if_missing('messages', 'content', 'TEXT');
CALL add_column_if_missing('messages', 'sent_at', 'DATETIME(6)');
CALL add_column_if_missing('notifications', 'user_id', 'INT');
CALL add_column_if_missing('notifications', 'title', 'VARCHAR(200)');
CALL add_column_if_missing('notifications', 'message', 'TEXT');
CALL add_column_if_missing('notifications', 'is_read', 'BIT(1) NOT NULL DEFAULT 0');
CALL add_column_if_missing('notifications', 'created_at', 'DATETIME(6)');
CALL add_column_if_missing('user_admin_roles', 'user_id', 'INT');
CALL add_column_if_missing('user_admin_roles', 'admin_role_id', 'INT');
CALL add_column_if_missing('user_admin_roles', 'assigned_at', 'DATETIME(6)');

-- conversation_participants used to be a plain join table (no id). The entity needs an id.
CALL add_column_if_missing('conversation_participants', 'id', 'INT NOT NULL AUTO_INCREMENT UNIQUE');

DROP PROCEDURE add_column_if_missing;

-- Jobs that existed before the approval workflow: keep them visible.
UPDATE jobs SET status = 'ACTIVE' WHERE status IS NULL;
UPDATE jobs SET approval_status = 'APPROVED' WHERE approval_status IS NULL;
UPDATE users SET name = CONCAT_WS(' ', first_name, last_name) WHERE (name IS NULL OR name = '') AND first_name IS NOT NULL;
UPDATE candidate_profiles SET profile_visibility = 1 WHERE profile_visibility IS NULL;
UPDATE notifications SET is_read = 0 WHERE is_read IS NULL;

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
