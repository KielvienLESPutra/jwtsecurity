package kielvien.lourensius.ekasetiaputra.jwtsecurity.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import kielvien.lourensius.ekasetiaputra.jwtsecurity.constants.Constants;

@MappedSuperclass
public class AuditTrail {
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "update_by")
	private String updatedBy;
	@Column(name = "update_date")
	private Date updatedDate;
	
	@Column(name = "deleted_by")
	private String deletedBy;
	@Column(name = "deleted_date")
	private Date deletedDate;
	
	private Long version;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	@PrePersist
	public void onCreated() {
		createdDate = new Date();
		createdBy = getCurrentUser();
		incrementVersion();
	}
	
	@PreUpdate
	public void onUpdated() {
		updatedDate = new Date();
		updatedBy = getCurrentUser();
		incrementVersion();
	}
	
	public void onDeleted() {
		deletedDate = new Date();
		deletedBy = getCurrentUser();
		incrementVersion();
	}
	
	public String getCurrentUser() {
		return Constants.CREATED_BY_SYSTEM;
	}
	
	public void incrementVersion() {
		version = (this.version != null ? this.version + 1 : 1L);
	}
}
