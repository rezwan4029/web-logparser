CREATE DATABASE `parser`;

CREATE TABLE IF NOT EXISTS `logparser` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `ip_address` VARCHAR(50),
  `start_date` DATETIME NOT NULL,
  `method_type` VARCHAR(50) NOT NULL,
  `status` INT NOT NULL,
  `user_agent` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
