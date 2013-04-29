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

$result2 = mysqli_query($mysqli, "SELECT  latitude,longitude FROM route WHERE trek_id_r = '$key ' ");


 while ($row = mysqli_fetch_row($result2)){
 $data[] = (array('latitude'=>$row[0], 'longitude'=>$row[1]));

}




echo json_encode(array('trek_data'=>$data,'kleidi'=>$key ));

$mysqli->close();


?>