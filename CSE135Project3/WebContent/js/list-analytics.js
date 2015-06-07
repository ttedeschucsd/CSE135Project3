$(document).ready(function(){
	
	$('#refresh').click(function(){
		  var row = {};
		  var rowHeaders = [];
		  var colHeaders = [];
		  var items = [];
		  var table = {};
		  var category = 0;
		  category = $('#category').val();
		  console.log(category);
		
		//http://stackoverflow.com/questions/4614202/jquery-map-for-an-html-table-help-needed
		//http://encosia.com/use-jquery-to-extract-data-from-html-lists-and-tables/
		  
		  $('#table').each(function(){
			  $(this).find('td').each(function(i) {
			    if($(this).attr("class") == "blank"){
			    }
			    else if($(this).attr("class") == "col-header"){
			    	colHeaders.push({
			    		pid : $(this).data("pid"),
			    		colIndex : $(this).data("colid"),
			    		total : $(this).data("total")
			    	});
			    } else if($(this).attr("class") == "row-header"){
			    	rowHeaders.push({
			    			sid : $(this).data("sid"),
				    		rowIndex : $(this).data("rowid"),
				    		total : $(this).data("total")
				    });
			    	console.log("row headers: ", rowHeaders);
			    } else if($(this).attr("class") == "item"){
			    	var key = $(this).data("pid").toString() + "-" + $(this).data("sid").toString();
			    	items.push({
			    		pid : $(this).data("pid"),
			    		sid : $(this).data("sid"),
			    		total : $(this).text(),
			    	});
			    }
			 });
		});
		console.log("rowHeaders: ", rowHeaders);
		console.log("colHeaders: ", colHeaders);
		console.log("items: ", items);
		
		table = {
    			colHeads : colHeaders,
    			rowHeads : rowHeaders,
    			items : items
    	}
		
		var xmlHttp;
    	xmlHttp = new XMLHttpRequest();
    	var responseHandler = function(){
    		if(xmlHttp.readyState == 4){
    			if(xmlHttp.status === 200){
    				var obj = JSON.parse(xmlHttp.responseText);
    				for(i=0; i< obj.cols.length; i++){
    					var editCol = $(".col-header[data-pid='"+obj.cols[i].pid+"']");
    					editCol.css('color', 'red');
    					var name = editCol.data("name");
    					editCol.html(name + " (" + obj.cols[i].total + ")");
    				}
    				for(i=0; i<obj.rows.length; i++){
    					var editRow = $(".row-header[data-sid='"+obj.rows[i].sid+"']");
    					editRow.css('color', 'red');
    					var name = editRow.data("name");
    					editRow.html(name + " (" + obj.rows[i].total + ")");
    				}
    				for(i=0; i<obj.items.length; i++){
    					var editItem = $(".item[data-sid=" + obj.items[i].sid + "][data-pid=" + obj.items[i].pid + "]");
    					editItem.css('color', 'red');
    					editItem.html(obj.items[i].total);
    				}
    			}
    		}
    	}
    	
    	tableString = JSON.stringify(table);
    	console.log(tableString);
        xmlHttp.onreadystatechange=responseHandler;
        xmlHttp.open("POST","jsp/ajax_analytics.jsp?table=" + tableString,true);
        xmlHttp.send(null);
        
	});
})