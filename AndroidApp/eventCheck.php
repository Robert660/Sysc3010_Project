<?php
//$user_id = "Robert";
$db_name = "projectdb";
$mysql_user="fooUser";
$mysql_pass ="1234";
$server_name="192.168.43.54:3306";
//echo "testing";

$conn = new mysqli($server_name,$mysql_user,$mysql_pass,$db_name);


//$selected = mysql_select_db($db_name,$conn);
//$selected = mysqli_select_db($conn,$db_name);


/*
$eventType = mysql_query("SELECT event FROM events");

$event = mysql_fetch_array($eventType);
if ($conn->query($sql) === FALSE) {
   echo "retry";
}

if($event == false){
	echo "False";
}



echo $event['event'];
*/

$selected = mysqli_select_db($conn,$db_name);

$eventType = mysqli_query($conn,"SELECT event FROM events");

$event = mysqli_fetch_array($eventType);
if($event == false){
	echo "False";
}



echo $event['event'];
?>