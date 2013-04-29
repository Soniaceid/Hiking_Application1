<?php


error_reporting(E_ALL);
include 'config.php';





$var1=$_POST['var1'];

$var2=$_POST['var2'];

$var3=$_POST['var3'];

$var4=$_POST['var4'];
$var5=$_POST['var5'];

$var6=$_POST['var6'];
$var7=$_POST['var7'];

$var8=$_POST['var8'];
$var9=$_POST['var9'];
$mysqli = new mysqli($dbhost, $dbuser, $dbpass,$dbdb);
if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
		exit();
	}
$data=array();	
if ($stmt = $mysqli->prepare("SELECT * FROM route WHERE (latitude >= ? AND latitude <= ?) AND (longitude >= ? " +
			(" '$var9'" ? "OR" : "AND") + " Longitude <= ?) AND " +
			"acos(sin(?) * sin(Lat) + cos(?) * cos(Lat) * cos(Lon - ?)) <= ?")) {


//Set our params 


$stmt->bind_param('dddddddd',$var1, $var2, $var3, $var4, $var5, $var6,  $var7, $var8 );

$e2=$stmt->execute();
$stmt->bind_result($name1, $name2,  $name3 );
while ($stmt->fetch()) {
        printf ("%s (%s)\n", $name1, $name1, $name3);
    
	$data[] = (array('lati'=>$name1, 'longi'=>$name2,'trek_id_r'=>$name3));
	}


if ($e2){$trek2=1;}
$stmt->close();

}
echo json_encode($response);
$mysqli->close();


?>