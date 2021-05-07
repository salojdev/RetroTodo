<?php
require("connect.php");

$response = array();
//alamat->email,telepon->mobile
if($_SERVER["REQUEST_METHOD"] == "POST"){

      $task = $_POST["task"];

        $userid = $_POST["userid"];

      $query = "INSERT INTO todo_task(userid,task) VALUES ('$userid','$task')";

    $sql = mysqli_query($conn, $query);
    $row = mysqli_affected_rows($conn);

    if($row > 0){

        $response["success"] = 1;
        $response["message"] = "Data inserted.";

    }
    else{
        $response["success"] = 0;
        $response["message"] = "Data not insert.";
    }
}
else{
    $response["success"] = 0;
    $response["message"] = "Please fill data.";
}

echo json_encode($response);
mysqli_close($conn);

?>