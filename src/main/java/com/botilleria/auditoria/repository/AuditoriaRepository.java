package com.botilleria.auditoria.repository;

import com.botilleria.auditoria.model.RegistroAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<RegistroAuditoria, String> {
}
