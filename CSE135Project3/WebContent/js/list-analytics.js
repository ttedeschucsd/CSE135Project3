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
    			updateText();
    		}
    	}
    	
    	tableString = JSON.stringify(table);
    	console.log(tableString);
        xmlHttp.onreadystatechange=responseHandler;
        xmlHttp.open("POST","jsp/ajax_analytics.jsp?table=" + tableString,true);
        xmlHttp.send(null);
        
	});
	
	var updateText = function(){
//		var colToUpdate = $('.col-header').data("pid", 1);
//		colToUpdate.attr("style") = "text to red";
	}
})