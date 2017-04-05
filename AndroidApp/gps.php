<?php

$db_name = "projectdb";
$mysql_user="fooUser";
$mysql_pass ="1234";
$server_name="192.168.43.54:3306";

$lat=$_POST["lat"]; //username from the android application register field
$long=$_POST["long"];//password passed in from the android application field
$username=$_POST["username"]; //username from the android application register field




$conn = new mysqli($server_name,$mysql_user,$mysql_pass,$db_name);

if($conn->connect_error){
	echo "retry";
	
}
//$sql = "UPDATE users SET lat = 50 WHERE username = 'TESTING'";
$sql = "UPDATE users SET latitude = '$lat',longitude = '$long' WHERE username = '$username'";




if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "retry";
}

?>

