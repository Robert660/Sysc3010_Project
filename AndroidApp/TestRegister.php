
<?php

$db_name = "projectdb";
$mysql_user="fooUser";
$mysql_pass ="1234";
$server_name="192.168.43.54:3306";
$conn = new mysqli($server_name,$mysql_user,$mysql_pass,$db_name);
$sql = "INSERT INTO users(username,password) VALUES('RobertA','RobertB')";

if ($conn->query($sql) === TRUE) {

    echo "New record created successfully";

} 

?>
