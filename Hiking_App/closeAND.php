<?php


error_reporting(E_ALL);
include 'config.php';




$trek2=0;
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

$result1 = mysqli_query($mysqli, "SELECT * FROM route WHERE (latitude >= '$var1' AND latitude <= '$var2') AND (longitude >= '$var3'  AND longitude <= '$var4') AND acos(sin('$var5') * sin(latitude) + cos('$var6') * cos(latitude) * cos(longitude - '$var7')) <= '$var8'");

//Set our params 


//$stmt->bind_param('dddddddd',$var1, $var2, $var3, $var4, $var5, $var6,  $var7, $var8 );

//$e2=$stmt->execute();
//$stmt->bind_result($name1, $name2,  $name3 );
//while ($stmt->fetch()) {
  //      printf ("%s (%s)\n", $name1, $name1, $name3);
    
	//$data[] = (array('lati'=>$name1, 'longi'=>$name2,'trek_id_r'=>$name3));
//	}
 while ($row = mysqli_fetch_row($result1)){
 $data[] = (array('latitude'=>$row[0], 'longitude'=>$row[1],'trek_id_r'=>$row[2]));
$ids[]=array('trek_id_r'=>$row[2]);
}


echo json_encode(array('data'=>$data  ,'ids'=>$ids));
$mysqli->close();


?>