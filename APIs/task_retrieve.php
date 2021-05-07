<?php
//kode -> success,pesan -> message,ambil->object ,perintah->query,eksekusi->sql,cek->row,konek->con
require("connect.php");
if($_SERVER["REQUEST_METHOD"] == "POST"){

    //  $task = $_POST["task"];
    $userid = $_POST["userid"];

//$query = "Select task , taskid , userid , created_at from todo_task WHERE userid = '$userid'  ";

$query = "SELECT * FROM todo_task WHERE userid = '$userid'";
$sql = mysqli_query($conn, $query);
$row = mysqli_affected_rows($conn);

if($row > 0){
    $response["success"] = 1;
    $response["message"] = "Data Fetched.";
    $response["data"] = array();

    while($object = mysqli_fetch_object($sql)){
      $F["taskid"] = $object->taskid;
      $F["userid"] = $object->userid;
     $F["task"] = $object->task;
      $F["created_at"] = $object->created_at;
        
        array_push($response["data"], $F);
    }
    
}
else{
    $response["success"] = 0;
    $response["message"] = "Data not Fetch.";
}
echo json_encode($response);
mysqli_close($conn);
}
//echo "Empty Input";
?>