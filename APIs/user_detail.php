<?php
//kode -> success,pesan -> message,ambil->object ,perintah->query,eksekusi->sql,cek->row,konek->con
require("connect.php");
 
    if($_SERVER['REQUEST_METHOD'] == 'POST') {

    $userid = $_POST['userid'];

$query = "SELECT * FROM users WHERE userid = '$userid'";
$sql = mysqli_query($conn, $query);
$row = mysqli_affected_rows($conn);

if($row > 0){
    $response["success"] = 1;
    $response["message"] = "Data Fetched.";
    $response["data"] = array();

    while($object = mysqli_fetch_object($sql)){
        $F["userid"] = $object->userid;
         $F["name"] = $object->name;
        $F["username"] = $object->username;
         $F["mobileno"] = $object->mobileno;
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
?>