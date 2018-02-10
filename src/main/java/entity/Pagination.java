package entity;

import java.util.List;

public class Pagination {

	private Long total;
	private Integer page;
	private List rows;
	public Pagination() {
		super();
	}
	public Pagination(Long total, Integer page, List rows) {
		super();
		this.total = total;
		this.page = page;
		this.rows = rows;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
}
