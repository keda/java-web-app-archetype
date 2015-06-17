$(document).ready(function(){
	//resize to fit page size
	$(window).on('resize.jqGrid', function () {
		$('#grid-table').jqGrid( 'setGridWidth', $(".page-content").width() );
    })
	
    $.createQueryForm($('#query-form'), {
    	action: '',
    	method: 'post',
    	fieldModel: [
    	     { label: 'Login ID', name: 'login_id', width: 150, type: 'text' },
    	     { label: 'User Name', name: 'user_name', type: 'text'},
    	     { label: '下拉列表', name: 'item_name', type: 'selected', options: [{key: '10001', value: '第一项'}, {key: '10002', value: '第二项'}]}
    	]
    });
    
	$('#grid-table').jqGrid({
		 jsonReader: {
			 root: "items",
			 page: "page",
		     total: "totalPages",
		     records: "totalCount",
		     id: "id"
		 },
		 url: '../users/alluser.json',
         mtype: 'GET',
         datatype: "json",
         colModel: [
             { label: 'ID', name: 'id', key: true, width: 100, formatter: eidtable },
             { label: '帐号', name: 'login_id', width: 150 },
             { label: '用户名', name: 'user_name', editable:true},
             { label: '手机号', name: 'phone', editable:true},
             { label:'创建时间', name: 'create_time', width: 100, sorttype:"date"}
         ],
		 viewrecords: true,
		 autowidth: true,
         height: 'auto',
         rowNum: 20,
         pager: "#grid-pager",
         multiselect: true,
         loadComplete : function() {
			var table = this;
			setTimeout(function(){
				//styleCheckbox(table);
				
				//updateActionIcons(table);
				updatePagerIcons(table);
				//enableTooltips(table);
			}, 0);
		},
	});
	
	function updatePagerIcons(table) {
		var replacement = 
		{
			'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
			'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
			'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
			'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
		};
		$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
			var icon = $(this);
			var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
			
			if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
		})
	}
	
	function eidtable(cellValue, options, rowObject) {
		
		//return '<a href=./' + cellValue + '/get.html>'+cellValue+'</a>';
		
		return '<a href="javascript:void(0)" onclick="eidtableWin();">'+cellValue+'</a>';
	}
	
});
function eidtableWin() {
	var gr = jQuery("#grid-table").jqGrid('getGridParam','selrow');
	if( gr != null ) {
		jQuery("#grid-table").jqGrid('editGridRow',gr
				,{editCaption: '编辑员工信息', height:'auto',reloadAfterSubmit:true,closeAfterEdit:true, url: '../users/update.html'});
	}else {
		alert("请选择一条记录！");
	}
}
