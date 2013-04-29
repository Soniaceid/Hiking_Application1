<?php

include 'config.php';

$email = $_REQUEST['email'];
$password = $_REQUEST['password']; 
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");


$query = mysql_query("SELECT email,password,user_id FROM users WHERE email='$email' AND password='$password'");
$num = mysql_num_rows($query);

if($num == 1){

	while ($list=mysql_fetch_array($query)){

		$output=$list;
        echo json_encode($output);
        echo "user was found";
	}

   
	}
    mysql_close($conn);
?>