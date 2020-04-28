/**
 * This function initiates the application and loads the static parameters to be used
 */
var initialize= function(){ 
	$("#mainDiv")[0].style.display="block";
	$("#initDiv")[0].style.display="none";
	$.ajax({
        type: "POST",
        contentType: "application/json",
        url: "staticData",
        data: JSON.stringify(""), //no input needed here
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
        	var city=JSON.parse(data.city);
        	var state=JSON.parse(data.state);
        	var year=JSON.parse(data.year);
        	var category=JSON.parse(data.category);
        	setCity(city);
        	setState(state);
        	setYear(year);
        	setCategory(category);
        },
        error: function (e) {
        	console.log(e);
        }
    });
}

/**
 * This function retrieves the result as json for Q1 by passing categories as parameter
 */
var getData1= function(){ 
 $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "q1",
        data: JSON.stringify($("#category").val()), //category passed
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
          //$("#output1").text(JSON.stringify(data));
        	$("#output1").html(createTable(data,1));
        },
        error: function (e) {
        	console.log(e);
        }
    });
}

/**
 * This function retrieves the result as json for Q2 by passing state and year as parameter
 */
var getData2= function(){
	$.ajax({
        type: "POST",
        contentType: "application/json",
        url: "q2",
        data: JSON.stringify({state:$("#state").val(), year:$("#year").val()}), //state and year passed
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
         // $("#output2").text(JSON.stringify(data));
        	$("#output2").html(createTable(data,2));
        },
        error: function (e) {
        	console.log(e);
        }
    });
}

/**
 * This function retrieves the result as json for Q3 by passing city as parameter
 */
var getData3= function(){
	$.ajax({
        type: "POST",
        contentType: "application/json",
        url: "q3",
        data: JSON.stringify($("#city").val()), //city passed
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {/*
        	$("#output3").text(createTable(data));
          //$("#output3").text(JSON.stringify(data));
*/        	$("#output3").html(createTable(data,3));
        },
        error: function (e) {
        	console.log(e);
        }
    });
}


/**
 * sets static data for city
 */
var setCity= function(city){
	var a ="";
	for(var i=0;i<city.length;i++){
		a=a+"<option value='"+city[i]+"'>"+city[i]+"</option>";
	}
	$("#city")[0].innerHTML=a
}

/**
 * sets static data for state
 */
var setState= function(state){
	var a ="";
	for(var i=0;i<state.length;i++){
		a=a+"<option value='"+state[i]+"'>"+state[i]+"</option>";
	}
	$("#state")[0].innerHTML=a
}

/**
 * sets static data for year
 */
var setYear= function(year){
	var a ="";
	for(var i=0;i<year.length;i++){
		a=a+"<option value='"+year[i]+"'>"+year[i]+"</option>";
	}
	$("#year")[0].innerHTML=a
}

/**
 * sets static data for categories
 */
var setCategory= function(category){
	var a ="";
	for(var i=0;i<category.length;i++){
		a=a+"<option value='"+category[i]+"'>"+category[i]+"</option>";
	}
	$("#category")[0].innerHTML=a
}

/**
 * Creates table out of json
 */
var createTable = function(input,type){
	var html ="<table>";
	var head = "";
	if(type==1){
		head="<tr><th>Business Name</th><th>City</th></tr>";
	}else if(type==2){
		head="<tr><th>Reviewer ID</th><th>Reviews Count</th></tr>";
	}else if(type==3){
		head="<tr><th>Business Categories</th><th>Count</th></tr>";
	}
	html=html+head;
	for (var i= 0;i<Object.keys(input).length;i++){
		html+="<tr><td>"+Object.keys(input)[i]+"</td><td>"+Object.values(input)[i]+"</td></tr>";
	}
	html+="</table>";
	return html;
}