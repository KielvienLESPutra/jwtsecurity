package kielvien.lourensius.ekasetiaputra.jwtsecurity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.User;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.UserRolePermission;

public interface UserRepository extends JpaRepository<User, Long> {
	public Optional<User> findByUsername(String username);

	public Optional<User> findByEmail(String email);

	@Query(value = "SELECT "
			+ " u.user_id AS userId, "
			+ " r.role_id AS roleId,  "
			+ " r.role_name AS roleName, "
			+ " p.permission_id AS permissionId, "
			+ " p.permission_name AS permissionName "
			+ " FROM `user` u "
			+ " JOIN `role_user` ru ON u.user_id = ru.user_id "
			+ " JOIN `role` r ON ru.role_id = r.role_id"
			+ " JOIN `role_permission` rp ON r.role_id = rp.role_id "
			+ " JOIN `permission` p ON rp.permission_id = p.permission_id "
			+ " WHERE u.user_id = :userId", nativeQuery = true)
	public Optional<List<UserRolePermission>> findUserWihRolesAndPermissions(@Param("userId") Long userId);
}
