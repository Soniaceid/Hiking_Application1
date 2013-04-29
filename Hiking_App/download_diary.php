<?php


error_reporting(E_ALL);
include 'config.php';





$key=$_POST['show_trek'];


$mysqli = new mysqli($dbhost, $dbuser, $dbpass,$dbdb);
if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
		exit();
	}
$data=array();	

$result1 = mysqli_query($mysqli, "SELECT  rate,findings,attractions,dangers,difficulty FROM description WHERE trek_id_d = '$key ' ");
 while ($row = mysqli_fetch_row($result1)){
 $data[] = (array('rate'=>$row[0], 'findings'=>$row[1], 'attractions'=>$row[2], 'dangers'=>$row[3], 'difficulty'=>$row[4] ));
 

}


 


if ($data!=null){

echo json_encode(array('data'=>$data,'response'=>1));}
else{
json_encode(array('response'=>0 ));}

$mysqli->close();


?>