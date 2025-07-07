package kielvien.lourensius.ekasetiaputra.jwtsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.RolePermission;

public interface RolePermissionRespository extends JpaRepository<RolePermission, Long>{
	
}
