<?php
 include 'config.php';

 
$key=$_POST['show_trek'];

$mysqli = new mysqli($dbhost, $dbuser, $dbpass,$dbdb);
$data=array();	
if (isset($key)) {

if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
		exit();
	}


$result1 = mysqli_query($mysqli, "SELECT  image_name FROM photos WHERE trek_id_p = '$key ' ");
 while ($row = mysqli_fetch_row($result1)){
 $data[] =$row[0] ;


}
}

$images=array();
for($i=0; $i<count($data);$i++){

$img_source = "C:\wamp\www\Hiking_App\images\post_images" . $data[$i]; // image path/name
$img_binary = fread(fopen($img_source, "r"), filesize($img_source));
$img_string = base64_encode($img_binary);
$images[]=(array('image'=>$img_string));
// binary, utf-8 bytes
 
header("Content-Type: bitmap; charset=utf-8");


 






//die($image_name);
 }
if ($images!=null){
echo json_encode(array('images'=>$images,'response' =>1));}
else{ json_encode(array('response' =>0));}
$mysqli->close();



?>