<?php
require("connect.php");

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $taskid = $_POST["taskid"];
    
    $query = "DELETE FROM todo_task WHERE taskid ='$taskid'";
    $sql = mysqli_query($conn, $query);
    $row  = mysqli_affected_rows($conn);

    if($row > 0){
        $response["success"] = 1;
        $response["message"] = "Data Deleted";
    }
    else{
        $response["success"] = 0;
        $response["message"] = "Data not Deleted";

    }
}
else{
    $response["success"] = 0;
    $response["message"] = "Please Select taskid";
}

echo json_encode($response);
mysqli_close($conn);
?>