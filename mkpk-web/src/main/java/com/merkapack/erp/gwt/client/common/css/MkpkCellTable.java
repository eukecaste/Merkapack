package com.merkapack.erp.gwt.client.common.css;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Style;

public interface MkpkCellTable extends CellTable.Resources {

	@Source({CellTable.Style.DEFAULT_CSS, "mkpkCellTable.css"})
	Style cellTableStyle();
	
}
