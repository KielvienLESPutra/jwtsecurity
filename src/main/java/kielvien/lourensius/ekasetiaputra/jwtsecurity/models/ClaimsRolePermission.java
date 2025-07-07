package kielvien.lourensius.ekasetiaputra.jwtsecurity.models;

import java.util.List;

public class ClaimsRolePermission {
	private Long userId;
	private List<String> roleName;
	private List<String> permissionName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<String> getRoleName() {
		return roleName;
	}

	public void setRoleName(List<String> roleName) {
		this.roleName = roleName;
	}

	public List<String> getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(List<String> permissionName) {
		this.permissionName = permissionName;
	}
}
