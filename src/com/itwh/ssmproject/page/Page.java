package com.itwh.ssmproject.page;

import org.springframework.stereotype.Component;

/*
 * ��ҳ���װ
 */
@Component
public class Page {
	private int page;//��ǰҳ��
	
	private int rows;//ÿҳ��ʾ����
	
	private int offset;//ƫ����

	
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
