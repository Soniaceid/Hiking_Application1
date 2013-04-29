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

$result1 = mysqli_query($mysqli, "SELECT  user_id_trek,activity,date FROM treks WHERE trek_id = '$key ' ");
 while ($row = mysqli_fetch_row($result1)){
 $data[] = (array('activity'=>$row[1], 'date'=>$row[2]));
 $user=$row[0];

}

$result2 = mysqli_query($mysqli, "SELECT  username FROM users WHERE user_id = '$user' ");
$username = mysqli_fetch_assoc($result2);
 
$data1=array();	

$result1 = mysqli_query($mysqli, "SELECT  distance,time FROM workout WHERE trek_id_w = '$key ' ");
 while ($row = mysqli_fetch_row($result1)){
 $data1[] = (array('distance'=>$row[0], 'time'=>$row[1]));


}



echo json_encode(array('username'=>$username['username'],'info'=>$data,'info1'=>$data1 ));

$mysqli->close();


?>