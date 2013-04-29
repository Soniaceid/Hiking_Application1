<?php


include 'config.php';



$mysqli = new mysqli($dbhost, $dbuser, $dbpass,$dbdb);
         
if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
		exit();
	}

/* Create the prepared statement */
if ($stmt1 = $mysqli->prepare("SELECT email FROM users WHERE email=?")) {
$email = $_POST['email'];

/* Bind results to variables */
$stmt1->bind_param('s', $email);
/* Execute the prepared Statement */
$stmt1->execute();
// store result of prepared statement
$stmt1->store_result();
 /* determine number of rows result set */
 $row_cnt = $stmt1->num_rows;

$stmt1->close();
}
if  ($row_cnt!=0){
//printf("User is already existed");
$response=0;

 echo 'user_exists';
 /* close our connection */
$mysqli->close();
}




else {


          
/* Create the prepared statement */
if ($stmt = $mysqli->prepare("INSERT INTO users (email,password,birth,username,gender) values (?, ? ,? , ?, ? )")) {
/* Set our params */
$nick = $_POST['nickname'];
 $pass = $_POST['password'];

$birth = $_POST['birth'];
$gender = $_POST['gender']; 
/* Bind our params */
$stmt->bind_param('sssss', $email, $pass, $birth,$nick,$gender);


/* Execute the prepared Statement */
$stmt->execute();

/* Echo results */
//printf("%d Row inserted.\n", $stmt->affected_rows);



echo 'register';
/* Close the statement */
$stmt->close();
}
else {
/* Error */
printf("Prepared Statement Error: %s\n", $mysqli->error);
}

/* close our connection */
$mysqli->close();
}

?>