<?php

error_reporting(E_ALL);
include 'config.php';

$mysqli = new mysqli($dbhost, $dbuser, $dbpass,$dbdb);

//$response = array("success1" => true, "success2" =>true ,"success3" => true);
if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
		exit();
	}
	
//insert some characteristic values of the uploading trek in the treks table
if ($stmt = $mysqli->prepare("INSERT INTO treks(activity,location,date,user_id_trek,start_lati,start_longi) values (?, ? , ? , ? ,?,?)")) {

//Set our params 
$acti = $_POST['activity_mysql'];
$loc = $_POST['location_mysql'];
$dat= $_POST['date_mysql'];
$user_id = $_POST['user_id_mysql']; 
$start_lat = $_POST['start_lati_mysql']; 
$start_long = $_POST['start_longi_mysql']; 
// Bind our params 
$stmt->bind_param('sssidd', $acti, $loc, $dat,$user_id,$start_lat,$start_long);


 //Execute the prepared Statement
$e1=$stmt->execute();
if ($e1){$trek1=1;}
$stmt->close();
}

//Find the max trek_id of the treks table in the rows which refer to the specific user-this trek_id is the trek id 
//of the previous insert 

$res = mysqli_query($mysqli, "SELECT MAX(trek_id)  AS id FROM treks WHERE user_id_trek='$user_id' ");
$max = mysqli_fetch_assoc($res);
//echo $max['id'];//this variable has the value of the max trek_id which we need


//Set our params 
 $find = $_POST['findings_mysql'];
$attra = $_POST['attractions_mysql'];


$dang= $_POST['dangers_mysql'];
$rate = $_POST['rate_mysql'];
$diff=$_POST['diff_mysql'];
if ( isset($find) || isset($attra)||isset($dang)||isset($rate) || isset($diff) ) {
//Insert the diary values in the description table for the uploading trek
if ($stmt = $mysqli->prepare("INSERT INTO description(findings,attractions,dangers,rate,difficulty,trek_id_d) VALUES (?, ?, ?, ?, ?, ?)")) {




$stmt->bind_param('sssiii',$find, $attra, $dang, $rate, $diff, $max['id'] );

$e2=$stmt->execute();

$stmt->close();

}
if ($e2){
$trek2=1;
}
else{ 
$trek2=0;
}
}
/*
if(get_magic_quotes_gpc()){
 $obj1 = stripslashes($_POST['Lati_Json']);
}else{
  $obj1= $_POST['Lati_Json'];
}
if(get_magic_quotes_gpc()){
 $obj2 = stripslashes($_POST['Longi_Json']);
}else{
  $obj2= $_POST['Longi_Json'];
}
*/ 
//insert distance and time of the uploading trek in the workout table
if ($stmt = $mysqli->prepare("INSERT INTO workout(distance,time,trek_id_w) values (?, ? ,?)")) {

//Set our params 
$dist = $_POST['distance_mysql'];
$time = $_POST['time_mysql'];

// Bind our params 
$stmt->bind_param('dsi', $dist, $time, $max['id']);


 //Execute the prepared Statement
$e4=$stmt->execute();
if ($e4){$trek4=1;}
$stmt->close();
}
//Insert the coordinates of the uploading trek into the route table
$obj1= $_POST['Lati_Json'];
 $obj2= $_POST['Longi_Json'];
$obj3 = json_decode($obj1,true);
$obj4 = json_decode($obj2,true);

      
/* set autocommit to off */
$mysqli->autocommit(FALSE);
if ($stmt = $mysqli->prepare("INSERT INTO route(latitude,longitude,trek_id_r) VALUES (?, ?, ?)")) {

for($i=0; $i<count($obj3);$i++)
{


$stmt->bind_param('ssi', $obj3[$i], $obj4[$i],$max['id'] );

$e3=$stmt->execute();
   
   
}

$stmt->close();
 $mysqli->commit();
}

if ($e3){$trek3=1;}

//$response =array( 'result1' => $trek1, 'result2' => $trek2, 'result3' => $trek3,'result4' => $trek4, 'trek_id' => $max['id']  );
$response =array( 'result1' => $trek1,'trek_id' => $max['id']  );
//echo json_encode($response);
 // $obj2 = json_decode($_POST['Longi_Json'],true);
//$response = array("error1" => $e1, "error2" =>$e2 ,"error3" => $e3);

   
echo json_encode($response);

 
// Create the prepared statement 
/*if ($stmt = $mysqli->prepare("INSERT INTO treks(activity,location,date,user_id_trek) values (?, ? , ? , ?)")) {
//Set our params 
$acti = $_POST['activity_mysql'];
 $loc = $_POST['location_mysql'];

$dat= $_POST['date_mysql'];
$user_id = $_POST['user_id_mysql']; 
// Bind our params 
$stmt->bind_param('sssi', $acti, $loc, $dat,$user_id);


 //Execute the prepared Statement
$stmt->execute();
$stmt->close();
}
/*
 
//if ($stmt = $mysqli->prepare("INSERT INTO route(latitude,longitude,trek_id_r) values (?, ? , ? )")) {
/* Set our params */

// $coordinates = array_combine($lati, $longi);

//$lati1 = implode(',', $lati);
//$longi1 = implode(',', $longi);

//$stmt->bind_param('ddi', $lati1, $longi1,$user_id);

 // $stmt->execute();

//$stmt->close();

  /*for($i=0; $i<count($obj1);$i++)
{
print_r($obj1[i]);}
for($i=0; $i<count($obj1);$i++)
{

printf("Prepared Statement Error:");


 $stmt->bind_param('ddi', $obj1[i], $obj2[i],$user_id);

  $stmt->execute();
   
   
   }

$stmt->close();/*
}
//}else{

//printf("Prepared Statement Error: %s\n", $mysqli->error);
//}

/* close our connection */

$mysqli->close();


?>