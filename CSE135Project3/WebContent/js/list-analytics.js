$(document).ready(function(){
	$('#refresh').click(function(){
		  var row = {};
		  var rowHeaders = {};
		  var colHeaders = {};
		  var items = {};
		  var category = 0
		  category = $('#category').val();
		  console.log(category);
		
		//http://stackoverflow.com/questions/4614202/jquery-map-for-an-html-table-help-needed
		//http://encosia.com/use-jquery-to-extract-data-from-html-lists-and-tables/
		  
		  $('#table').each(function(){
			  $(this).find('td').each(function(i) {
			    if($(this).attr("class") == "blank"){
			    }
			    else if($(this).attr("class") == "col-header"){
			    	var key = $(this).data("pid");
			    	console.log(key);
			    	colHeaders[key] = {
			    		colIndex : $(this).data("colid"),
			    		total : $(this).data("total")
			    	}
			    	console.log("col headers: ", colHeaders);
			    } else if($(this).attr("class") == "row-header"){
			    	var key = $(this).data("sid");
			    	console.log(key);
			    	rowHeaders[key] = {
				    		rowIndex : $(this).data("rowid"),
				    		total : $(this).data("total")
				    }
			    	console.log("row headers: ", rowHeaders);
			    } else if($(this).attr("class") == "item"){
			    	var key = $(this).data("pid").toString() + "-" + $(this).data("sid").toString();
			    	items[key] = {
			    		pid : $(this).data("pid"),
			    		sid : $(this).data("sid"),
			    		total : $(this).text(),
			    	}
			    }
			 });
		});
		console.log("rowHeaders: ", rowHeaders);
		console.log("colHeaders: ", colHeaders);
		console.log("items: ", items);
		$.ajax({
			url: "jsp/ajax_analytics.jsp",
			method: "POST",
			data: { rowHeads : rowHeaders,
					colHeads : colHeaders,
					items : items,
					cat : category
            },
            //dataType : "json",
			success : function(msg){      
	            console.log("msg", msg);
			},
	        error : function(XMLHttpRequest, textStatus, errorThrown){
	        	console.log("request", XMLHttpRequest);
	        },
		});
	});
})