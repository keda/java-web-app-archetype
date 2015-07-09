$.createQueryForm = function (div, obj){
	var qf = $("<form class='form-horizontal' role='form'></form>");
	qf.attr("action", obj.url);
	qf.attr("method", obj.mtype);
	
	var formGroup = $("<div class='form-group'></div>");
	$.each(obj.fieldModel, function(index, value){
		
		if(value.type == 'text') {
			formGroup.append($.field.text(value));
		}else if(value.type == 'selected') {
			formGroup.append($.field.selected(value));
		}
		
		formGroup.appendTo(qf);
	});
	qf.append(formGroup);
	qf.append($("<div class='form-actions center'>"
						+"<input type='submit' class='btn btn-sm btn-success'>"
					    +"</input>"
				+"</div>"));
	
	qf.appendTo(div);
}

$.field = {
	text: function(f) {
		var f_text = $( "<label class='col-sm-3 control-label no-padding-right' for='form-field-1'> " + f.label + " </label>"
				       + "<div class='col-sm-9'>"
				       + 	 "<input type='"+ f.type +"' id='form-field-1' class='col-xs-10 col-sm-5' name=" + f.name + "/>"
				       + "</div>");
		
		return f_text;
	},
	
	selected: function(f) {
		var d = $("<div></div>");
		var label = "<label class='col-sm-3 control-label no-padding-right' for='form-field-select-1'>" + f.label + "</label>";
		var selected = $("<select class='col-xs-10 col-sm-5' id='form-field-select-1' name=" + f.name + "></select>");
		selected.append($("<option value=''></option>"))
		$.each(f.options, function(index, opt){
			selected.append($("<option value='"+opt.key+"'>"+opt.value+"</option>"));
		});
		
		return d.append(label).append(selected);
	},
	
	checkbox: function(f) {
		
	}
}
