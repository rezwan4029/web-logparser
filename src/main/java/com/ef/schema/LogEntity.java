package com.ef.schema;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "logparser")
public class LogEntity implements Serializable {
  private static final long serialVersionUID = 913906791104768399L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "ip_address")
  private String ipAddress;

  @Column(name = "start_date", columnDefinition = "DATETIME")
  private LocalDateTime startDate;

  private int status;
  
  @Column(name = "method_type")
  private String methodType;

  @Column(name = "user_agent")
  private String userAgent;
}
