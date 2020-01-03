<?php
$host="localhost";
$database="id6056013_tabletest";
$username="id6056013_tabletest";
$password="chandan";

$con=mysqli_connect($host,$username,$password,$database);
if (mysqli_connect_errno())
 {
 echo "Failed to connect to MySQL: " . mysqli_connect_error();
 }
else
{
/*echo "Connection sucess.";*/
}

?>