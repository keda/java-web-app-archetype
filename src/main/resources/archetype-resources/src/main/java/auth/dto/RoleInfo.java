package ${package}.auth.dto;

import java.sql.Timestamp;

public class RoleInfo {
	private Long id;
	private String roleName;
	private String description;
	private Timestamp createTime;
	private char statusFlag;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public char getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(char statusFlag) {
		this.statusFlag = statusFlag;
	}
	
}
