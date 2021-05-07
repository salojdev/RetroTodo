<?php
require("connect.php");

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $userid = $_POST["userid"];
     $name = $_POST["name"];
        $username = $_POST["username"];
    
    $query = "UPDATE  users SET name = '$name' , username = '$username' WHERE userid ='$userid'";
    $sql = mysqli_query($conn, $query);
    $row  = mysqli_affected_rows($conn);

    if($row > 0){
        $response["success"] = 1;
        $response["message"] = "Data Updated.";
    }
    else{
        $response["success"] = 0;
        $response["message"] = "Data not Updated.";

    }
}
else{
    $response["success"] = 0;
    $response["message"] = "Please Select User.";
}

echo json_encode($response);
mysqli_close($conn);
?>