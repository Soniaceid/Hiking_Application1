<?php
error_reporting(E_ALL);
include 'config.php';

$earth_radius = 3960.00; # in miles


$x =$_POST['current_x'];
$y =$_POST['current_y'];
$current_loc=$_POST['current_loc'];
$mysqli = new mysqli($dbhost, $dbuser, $dbpass,$dbdb);
$array=array();
$result_ids= array();
if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
		exit();
	}
	/* Create the prepared statement */
$result = mysqli_query($mysqli, "SELECT trek_id  FROM treks WHERE location='$current_loc' ");

while ($row = mysqli_fetch_row($result)){
 $result_ids[] = $row[0];

}

if (count($result_ids)>0){
for($i=0; $i<count($result_ids);$i++)
{

$result1 = mysqli_query($mysqli, "SELECT latitude,longitude FROM route WHERE trek_id_r='$result_ids[$i]' ");



 while ($row = mysqli_fetch_row($result1)) {
 
$hav_distance = distance_haversine($current_x,$current_y, $row[0],$row[1]);
$sum=$hav_distance;
if ($hav_distance<5000)
{
$array[]=$result_ids[$i];
break;
}


}
}
}

$data=array();
for($i=0; $i<count($result_ids);$i++)
{

$result1 = mysqli_query($mysqli, "SELECT * FROM route WHERE trek_id_r='$result_ids[$i]' ");



  while ($row = mysqli_fetch_row($result1)){
 $data[] = (array('route_id'=>$row[0], 'latitude'=>$row[1],'longitude'=>$row[2]));

}
}
echo json_encode(array('data'=>$data  ,'resulttt'=>$result_ids ));


function distance_haversine($lat1, $lon1, $lat2, $lon2) {
$earth_radius = 3960.00; # in miles

$delta_lat = $lat_2 - $lat_1 ;
$delta_lon = $lon_2 - $lon_1 ;
  $alpha    = $delta_lat/2;
  $beta     = $delta_lon/2;
  $a        = sin(deg2rad($alpha)) * sin(deg2rad($alpha)) + cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * sin(deg2rad($beta)) * sin(deg2rad($beta)) ;
  $c        = asin(min(1, sqrt($a)));
  $distance = 2*$earth_radius * $c;
  $distance = round($distance, 4);
 
  return $distance;
}
 




$mysqli->close();
?>