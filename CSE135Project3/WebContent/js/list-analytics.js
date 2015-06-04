$(document).ready(function(){
	$('#refresh').click(function(){
		
		//http://stackoverflow.com/questions/4614202/jquery-map-for-an-html-table-help-needed		
			
		var tableObject = $('#analytics-table tr').map(function(i) {
			  var row = {};
			  var rowHeaders;
			  var colHeaders;
			  var items;
				 
			  $(this).find('td').each(function(i) {
			    //var rowName = columns[i];
				 
			    var row = $(this).text();
			    
			    //if this.class == blank
			    
			    //if this.class == col_header
			    
			    //if this.class == row_header
			    
			    console.log("row: ", row);
			  });
			  return row;
		}).get();
		
		console.log("tableObject", tableObject);
			
		var request = $.ajax({
			url: "ajax_analytics.jsp",
			method: "POST",
			data: { rowHeads : rowHeaders,
					colHeads : colHeaders,
					items : items
			},
		});
		
		request.done(function(results){
			console.log(results);
		});
	});
})