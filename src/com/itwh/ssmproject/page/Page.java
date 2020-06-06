package com.itwh.ssmproject.page;

import org.springframework.stereotype.Component;

/*
 * 分页类分装
 */
@Component
public class Page {
	private int page;//当前页码
	
	private int rows;//每页显示数量
	
	private int offset;//偏移量

	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = (page-1)*rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	
	
}
