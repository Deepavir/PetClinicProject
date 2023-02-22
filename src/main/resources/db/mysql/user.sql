CREATE DATABASE IF NOT EXISTS petclinics;

ALTER DATABASE petclinics
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON petclinics.* TO 'petclinics'@'%' IDENTIFIED BY 'petclinics';
