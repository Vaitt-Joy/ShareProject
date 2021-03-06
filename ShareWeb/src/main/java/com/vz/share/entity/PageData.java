package com.vz.share.entity;

import com.vz.share.entity.base.BaseEntity;

import java.rmi.RemoteException;
import java.util.List;

/**
 * page data
 * @author huang.jb
 *
 */
public class PageData extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	public static final String PAGE_INDEX_KEY = "pageIndex";
	public static final String PAGE_SIZE_KEY = "pageSize";
	
	private int pageSize = 20;
	private int total;
	private int currentPageNumber = 1;
	private List<?> items;
	private Object data;
	
	public PageData() {}
	public PageData(int pageSize, int total, int currentPageNumber,
					List<?> items) throws RemoteException {
		super();
		this.pageSize = pageSize;
		this.total = total;
		this.currentPageNumber = currentPageNumber;
		this.items = items;
	}
	
	public int getItemSize() {
		if (items != null) {
			return items.size();
		} else {
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getDataT() {
		if (data != null) {
			return (T) data;
		} else {
			return null;
		}
	}

	public int getTotalPage() {
		if (total % pageSize == 0) {
			return total / pageSize;
		} else {
			return total / pageSize + 1;
		}
	}

	public int getStartRow() {
		return (currentPageNumber - 1) * pageSize + 1;
	}

	public int getEndRow() {
		return currentPageNumber * pageSize;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}
	public List<?> getItems() {
		return items;
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> getItemsT() {
		return (List<T>) items;
	}
	public void setItems(List<?> items) {
		this.items = items;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}