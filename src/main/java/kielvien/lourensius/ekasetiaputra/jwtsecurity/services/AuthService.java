package kielvien.lourensius.ekasetiaputra.jwtsecurity.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import kielvien.lourensius.ekasetiaputra.jwtsecurity.entities.User;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.exceptions.FailLoginException;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.AuthLoginRequest;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.ClaimsRolePermission;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.models.UserRolePermission;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.utils.SecurityUtil;

@Service
public class AuthService {
	private UserServices userServices;
	private JWTServices jwtServices;
	private SecurityUtil securityUtil;

	public AuthService(UserServices userServices, JWTServices jwtServices, SecurityUtil securityUtil) {
		this.userServices = userServices;
		this.jwtServices = jwtServices;
		this.securityUtil = securityUtil;
	}

	public String login(AuthLoginRequest authLoginRequest) throws FailLoginException {
		User user = userServices.searchByIndetifier(authLoginRequest.getUsername());

		if (!user.isActive()) {
			throw new FailLoginException ("User is inactive with user : " + authLoginRequest.getUsername());
		}

		if (!securityUtil.checkPassword(authLoginRequest.getPassword(), user.getPassword())) {
			userServices.updateLastLogin(user, true);
			throw new FailLoginException ("Password not match with indentifier : " + authLoginRequest.getUsername());
		}
		userServices.updateLastLogin(user, false);

		Map<String, Object> claims = new HashMap<>();
		ClaimsRolePermission claimsRolePermission = new ClaimsRolePermission();
		claimsRolePermission.setUserId(user.getUserId());
		Set<String> setRoles = new HashSet<>();
		Set<String> setPermissions = new HashSet<>();

		Optional<List<UserRolePermission>> userRolePermission = userServices.searchUserRolePermission(user.getUserId());
		userRolePermission.ifPresent(list -> list.forEach(urp -> {
			setRoles.add(urp.getRoleName());
			setPermissions.add(urp.getPermissionName());
		}));
		claimsRolePermission.setRoleName(List.copyOf(setRoles));
		claimsRolePermission.setPermissionName(List.copyOf(setPermissions));

		claims.put("userKeys", claimsRolePermission);
		return jwtServices.generateJwt(claims, user.getUsername());
	}
}
