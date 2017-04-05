
<?php


$db_name = "projectdb";

$mysql_user="fooUser";

$mysql_pass ="1234";

$server_name="192.168.43.54:3306";

$conn = mysql_connect($server_name,$mysql_user,$mysql_pass,$db_name);
$selected = mysql_select_db($db_name,$conn);
$checkUserId = mysql_query("SELECT password FROM users WHERE username = ‘RobertA’");
$user = mysql_fetch_array($checkUserId);


if($user == null || $user == false || $user == ""){

 echo "false"; 

}

else{

 echo $user['password'];

}
?>
