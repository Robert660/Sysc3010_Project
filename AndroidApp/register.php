<?php

$db_name = "projectdb";
$mysql_user="fooUser";
$mysql_pass ="1234";
$server_name="192.168.43.54:3306";
$username=$_POST["username"]; //username from the android application register field
$password =$_POST["user_password"];//password passed in from the android application field


$conn = new mysqli($server_name,$mysql_user,$mysql_pass,$db_name);



$sql = "INSERT INTO users(username,password) VALUES('$username','$password')";
//$sql = "INSERT INTO users(username,password) VALUES('RobertA','RobertA')";
//$sql = "INSERT INTO users(username,password) VALUES('Test','test')";

if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
	echo "retry";
    
}

?>

