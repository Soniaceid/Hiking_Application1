<?php
 include 'config.php';

 
$base1 =$_POST['Image_converted'];

$base=json_decode($base1,true);
$trek_id_photos=$_POST['trek_id_photos'];

if (isset($base1)) {

for($i=0; $i<count($base);$i++){
$suffix = createRandomID();
$image_name = "img_".$suffix."_".date("Y-m-d-H-m-s").".jpg";
 
// base64 encoded utf-8 string
$binary = base64_decode($base[$i]);
$path=$image_name;
// binary, utf-8 bytes
 
header("Content-Type: bitmap; charset=utf-8");

$file = fopen("C:\wamp\www\Hiking_App\images\post_images" . $image_name, "wb");

fwrite($file, $binary);
 
fclose($file);





//die($image_name);
 }


} else {
 
die("No POST");
}
 $mysqli = new mysqli($dbhost, $dbuser, $dbpass,$dbdb);
if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
		exit();
	}
	
//insert some characteristic values of the uploading trek in the treks table
if ($stmt = $mysqli->prepare("INSERT INTO photos(image_name,trek_id_p) values (?, ? )")) {

 

// Bind our params 
$stmt->bind_param('si', $path, $trek_id_photos);


 //Execute the prepared Statement
$e1=$stmt->execute();

$stmt->close();
}
if ($e1){$trek2=1;}
$response =array( 'imagename' => $path, 'result' => 1, 'imagename1' => $image_name );


   
echo json_encode($response);
$mysqli->close();
function createRandomID() {
 
$chars = "abcdefghijkmnopqrstuvwxyz0123456789?";
//srand((double) microtime() * 1000000);
 
$i = 0;
 
$pass = "";
 
while ($i <= 5) {
 
$num = rand() % 33;
 
$tmp = substr($chars, $num, 1);
 
$pass = $pass . $tmp;
 
$i++;
}
return $pass;
}
?>