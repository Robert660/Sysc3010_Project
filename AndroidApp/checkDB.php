<?php
//$user_id = "Robert";
$db_name = "projectdb";
$mysql_user="fooUser";
$mysql_pass ="1234";
$server_name="192.168.43.54:3306";
$username=$_POST["username"]; //username from the android application register field



$conn = mysql_connect($server_name,$mysql_user,$mysql_pass,$db_name);


$selected = mysql_select_db($db_name,$conn);


$checkUserId = mysql_query("SELECT password FROM users WHERE username = '$username'");
//$checkUserId = mysql_query("SELECT password FROM users WHERE username = 'TESTING'");


$user = mysql_fetch_array($checkUserId);


if($user == null || $user == false || $user == ""){
	echo "false";	
}
else{
	echo $user['password'];
}

if(!$checkUserId){
	echo "retry";
	
}


?>

