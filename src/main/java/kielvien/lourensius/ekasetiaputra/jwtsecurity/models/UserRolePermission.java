package kielvien.lourensius.ekasetiaputra.jwtsecurity.models;

public class UserRolePermission {
	private Long userId;
	private Long roleId;
	private String roleName;
	private Long permissionId;
	private String permissionName;
	
	public UserRolePermission() {

	}

	public UserRolePermission(Long userId, Long roleId, String roleName, Long permissionId, String permissionName) {
		this.userId = userId;
		this.roleId = roleId;
		this.roleName = roleName;
		this.permissionId = permissionId;
		this.permissionName = permissionName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
}
