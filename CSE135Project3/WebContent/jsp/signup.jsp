<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/html/head.html" />
<script src="http://code.jquery.com/jquery-latest.js"></script>

<script type="text/javascript">
        
        function verify(){ 	
        	var name = document.getElementById("name").value;
        	var role = document.getElementById("role").selectedIndex;
        	var age = document.getElementById("age").value;
        	var state = document.getElementById("state").selectedIndex;
        	var submitOK = true;
        	//var params = "name=" + name;
        	//params.push("age=" + age);
        	
        	if(name == null || name == ""){
        		document.getElementById("nameError").innerHTML = "<p>Name not provided</p>";
        		submitOK = false;
        	}
        	else {
        		document.getElementById("nameError").innerHTML = "";
        	}
        	if(age == null || age <= 0){
        		document.getElementById("ageError").innerHTML = "<p>Age not provided</p>";
        		submitOK = false;
        	}
        	else {
        		document.getElementById("ageError").innerHTML = "";
        	}
        	
        	if(name != null && name != "" && age >= 1){
        		duplicateUser();
        	}
        	return submitOK;
        }
       
        function duplicateUser(){
        	console.log("Entering duplicateUser()!");
        	var xmlHttp = new XMLHttpRequest();
        	var responseHandler = function(){
        		if(xmlHttp.readyState == 4){
        			document.getElementById("res").innerHTML = xmlHttp.responseText;
 					console.log(xmlHttp.responseText);	
 		
        		}
        	}
        	xmlHttp.onreadystatechange=responseHandler;
            xmlHttp.open("POST","jsp/verifyUser.jsp?name=" + document.getElementById("name").value, true);
            xmlHttp.send(null);   
        }
        
</script>
</head> 
<body class="page-index" data-spy="scroll" data-offset="60" data-target="#toc-scroll-target">

    <jsp:include page="/jsp/header.jsp" />
    <div class="container">
        <div class="row">
            <div class="span12">
                <div class="body-content">
                    <div class="section">
                        <div class="page-header">
                            <h4>Sign Up</h4>
                        </div>
                        <div class="row">
                        <span id="res" style="color:red"></span>
                            <!--jsp:include page="/html/signup-form.html" /-->
                        </div>
                        <div class="container">
                        <form name="f1" action="signup" method="post" onsubmit="duplicateUser()">
                        	<table align="center">
                        		<tr><td></td>
                        			<td><span id="nameError" style="color:red"></span></td>
                        		</tr>
                        		<tr>
                        			<td>Name</td>
                        			<td><input type="text" id="name" name="name"></td>
                        	
                        		</tr>
                        		<tr><td></td>
                        			<td><span id="roleError" style="color:red"></span></td>
                        		</tr>
                        		<tr>
                        			<td>Role:</td>
                					<td>
                						<select id="role" name="role">
                        					<option>owner</option>
                        					<option>customer</option>
                						</select>
                					</td>
                        		</tr>
                        		<tr><td></td>
                        			<td><span id="ageError" style="color:red"></span></td>
                        		</tr>
                        		<tr>
                					<td>Age:</td>
                					<td><input type="text" id="age" name="age"></td>
            					</tr>
            					<tr><td></td>
                        			<td><span id="stateError" style="color:red"></span></td>
                        		</tr>
                        		<tr>
                					<td>State:</td>
                					<td><select id="state" name="state">
                        				<option>Alabama</option>
                        				<option>Alaska</option>
                        				<option>Arizona</option>
                        				<option>Arkansas</option>
                        				<option>California</option>
                        				<option>Colorado</option>
                       	 				<option>Connecticut</option>
                        				<option>Delaware</option>
                        				<option>Florida</option>
                        				<option>Georgia</option>
                        				<option>Hawaii</option>
                        				<option>Idaho</option>
                        				<option>Illinois</option>
                        				<option>Indiana</option>
                        				<option>Iowa</option>
                        				<option>Kansas</option>
                        				<option>Kentucky</option>
                        				<option>Louisiana</option>
                        				<option>Maine</option>
                        				<option>Maryland</option>
                        				<option>Massachusetts</option>
                        				<option>Michigan</option>
                        				<option>Minnesota</option>
                       					<option>Mississippi</option>
                        				<option>Missouri</option>
                        				<option>Montana</option>
                        				<option>Nebraska</option>
                        				<option>Nevada</option>
                        				<option>New Hampshire</option>
                        				<option>New Jersey</option>
                        				<option>New Mexico</option>
                        				<option>New York</option>
                        				<option>North Carolina</option>
                        				<option>North Dakota</option>
                        				<option>Ohio</option>
                        				<option>Oklahoma</option>
                        				<option>Oregon</option>
                        				<option>Pennsylvania</option>
                        				<option>Rhode Island</option>
                        				<option>South Carolina</option>
                        				<option>South Dakota</option>
                        				<option>Tennessee</option>
                        				<option>Texas</option>
                        				<option>Utah</option>
                        				<option>Vermont</option>
                        				<option>Virginia</option>
                        				<option>Washington</option>
                        				<option>West Virginia</option>
                        				<option>Wisconsin</option>
                        				<option>Wyoming</option>
                						</select>
                					</td>
           		 				</tr>
           		 				
           		 				<tr>
                					<td><input onClick="verify();" type="button" value="Signup"></td>
            					</tr>
                        	</table>
                        </form>
                        </div>
                        <jsp:include page="/html/footer.html" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
