package com.dvt.HubotService.business.main.dto;

import java.util.List;

public class SheetDTO {
	private int sheetNum;
	private List<RowDTO> rowList;
	private Integer copy2;
	private String copiedSheetName;
	public int getSheetNum() {
		return sheetNum;
	}
	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}
	public List<RowDTO> getRowList() {
		return rowList;
	}
	public void setRowList(List<RowDTO> rowList) {
		this.rowList = rowList;
	}
	public SheetDTO(int sheetNum, List<RowDTO> rowList) {
		super();
		this.sheetNum = sheetNum;
		this.rowList = rowList;
	}
	public SheetDTO(int sheetNum, Integer copy2, String copiedSheetName) {
		super();
		this.sheetNum = sheetNum;
		this.copy2 = copy2;
		this.copiedSheetName = copiedSheetName;
	}
	
	public Integer getCopy2() {
		return copy2;
	}
	public void setCopy2(Integer copy2) {
		this.copy2 = copy2;
	}
	public String getCopiedSheetName() {
		return copiedSheetName;
	}
	public void setCopiedSheetName(String copiedSheetName) {
		this.copiedSheetName = copiedSheetName;
	}
	
	
}
