<?php

$db_name = "sysc3010";
$mysql_user="root";
$mysql_pass ="abc";
$server_name="localhost";
$lat=$_POST["lat"]; //username from the android application register field
$long=$_POST["long"];//password passed in from the android application field
//$username=$_POST["username"]; //username from the android application register field




$conn = new mysqli($server_name,$mysql_user,$mysql_pass,$db_name);

if($conn->connect_error){
	die("Connection failed: " .$conn->connect_error);
	
}


//$sql = "UPDATE users SET lat = 50 WHERE username = 'Robert'";
$sql = "UPDATE users SET latitude = '$lat',longitude = '$long' WHERE username = 'Robert'";


if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

?>

