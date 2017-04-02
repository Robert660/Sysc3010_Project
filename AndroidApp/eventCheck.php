<?php
//$user_id = "Robert";

$db_name = "sysc3010";
$mysql_user="root";
$mysql_pass ="abc";
$server_name="localhost";


$conn = mysql_connect($server_name,$mysql_user,$mysql_pass,$db_name);
$selected = mysql_select_db($db_name,$conn);


$eventType = mysql_query("SELECT event FROM Events");

$event = mysql_fetch_array($eventType);

echo $event['event'];

?>