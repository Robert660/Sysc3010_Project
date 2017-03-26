<?php

$db_name = "sysc3010";
$mysql_user="root";
$mysql_pass ="abc";
$server_name="localhost";
$username=$_POST["username"]; //username from the android application register field
$password =$_POST["user_password"];//password passed in from the android application field



$conn = new mysqli($server_name,$mysql_user,$mysql_pass,$db_name);

if($conn->connect_error){
	die("Connection failed: " .$conn->connect_error);
	
}


$sql = "INSERT INTO users(username,password) VALUES('$username','$password')";
//$sql = "INSERT INTO users(username,password) VALUES('Test','test')";

if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

?>

