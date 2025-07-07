package kielvien.lourensius.ekasetiaputra.jwtsecurity.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "role_permission")
@Entity
public class RolePermission extends AuditTrail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_permission_id")
	private Long rolePermissionId;
	@Column(name = "role_id")
	private Long roleId;
	@Column(name = "permission_id")
	private Long permissionId;

	public Long getRolePermissionId() {
		return rolePermissionId;
	}

	public void setRolePermissionId(Long rolePermissionId) {
		this.rolePermissionId = rolePermissionId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
}
