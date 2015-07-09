package ${package}.auth.dto;

public class BasMemu {
	private Long id;
	private String pageUrl;
	private String dispName;
	private String description;
	private char menuLevel;
	private Long parentId;
	private char menuFlag;
	private char dispFlag;
	private Short sequeNum;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public String getDispName() {
		return dispName;
	}
	public void setDispName(String dispName) {
		this.dispName = dispName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public char getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(char menuLevel) {
		this.menuLevel = menuLevel;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public char getMenuFlag() {
		return menuFlag;
	}
	public void setMenuFlag(char menuFlag) {
		this.menuFlag = menuFlag;
	}
	public char getDispFlag() {
		return dispFlag;
	}
	public void setDispFlag(char dispFlag) {
		this.dispFlag = dispFlag;
	}
	public Short getSequeNum() {
		return sequeNum;
	}
	public void setSequeNum(Short sequeNum) {
		this.sequeNum = sequeNum;
	}
	
}
