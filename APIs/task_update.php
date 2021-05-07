<?php
require("connect.php");

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $taskid = $_POST["taskid"];
     $task = $_POST["task"];
    
    $query = "UPDATE  todo_task SET task = '$task' WHERE taskid ='$taskid'";
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
    $response["message"] = "Please Select Task.";
}

echo json_encode($response);
mysqli_close($conn);
?>