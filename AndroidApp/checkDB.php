<?php
//$user_id = "Robert";

$db_name = "sysc3010";
$mysql_user="root";
$mysql_pass ="abc";
$server_name="localhost";
$username=$_POST["username"]; //username from the android application register field

$conn = mysql_connect($server_name,$mysql_user,$mysql_pass,$db_name);
$selected = mysql_select_db($db_name,$conn);


$checkUserId = mysql_query("SELECT password FROM users WHERE username = '$username'");
//$checkUserId = mysql_query("SELECT password FROM users WHERE username = 'Robert'");
$user = mysql_fetch_array($checkUserId);


if($user == null || $user == false || $user == ""){
	echo "false";	
}
else{
	echo $user['password'];
}

if(!$checkUserId){
	echo "No entries";
	die("Query Failed for some unknown reason".mysql_error());
}


?>

